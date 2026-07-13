#!/usr/bin/env python3
"""MiniCraft Discord RPC Bridge - HTTP server that forwards to Discord IPC"""
import socket, json, os, sys, time, struct, threading, queue, fcntl
from http.server import HTTPServer, BaseHTTPRequestHandler

# Use threaded HTTP server so Discord connect delays don't block other requests
try:
    from http.server import ThreadingHTTPServer
    ServerClass = ThreadingHTTPServer
except ImportError:
    import socketserver
    class ThreadedHTTPServer(socketserver.ThreadingMixIn, HTTPServer):
        daemon_threads = True
        allow_reuse_address = True
    ServerClass = ThreadedHTTPServer

CLIENT_ID = "1512377902195540018"
PORT = 6464
PIPES = [
    os.environ.get("XDG_RUNTIME_DIR", f"/run/user/{os.getuid()}") + "/discord-ipc-0",
    os.environ.get("XDG_RUNTIME_DIR", f"/run/user/{os.getuid()}") + "/app/com.discordapp.Discord/discord-ipc-0",
    os.environ.get("XDG_RUNTIME_DIR", f"/run/user/{os.getuid()}") + "/app/dev.vencord.Vesktop/discord-ipc-0",
]

LOCK_FILE = os.environ.get("XDG_RUNTIME_DIR", f"/run/user/{os.getuid()}") + "/minicraft_rpc.lock"

def acquire_bridge_lock():
    try:
        fd = os.open(LOCK_FILE, os.O_CREAT | os.O_RDWR)
        fcntl.flock(fd, fcntl.LOCK_EX | fcntl.LOCK_NB)
        os.ftruncate(fd, 0)
        os.write(fd, f"{os.getpid()}\n".encode())
        return fd
    except (IOError, OSError) as e:
        return None

events_queue = queue.Queue()
sock = None
sock_lock = threading.Lock()

def find_pipe():
    for pipe in PIPES:
        if os.path.exists(pipe):
            return pipe
    return None

def send_frame(s, opcode, data):
    payload = json.dumps(data).encode('utf-8')
    header = struct.pack('<II', opcode, len(payload))
    s.sendall(header + payload)

def recv_frame(s):
    try:
        header = s.recv(8)
        if len(header) < 8:
            return None
        opcode, length = struct.unpack('<II', header)
        payload = b''
        while len(payload) < length:
            chunk = s.recv(length - len(payload))
            if not chunk:
                return None
            payload += chunk
        return opcode, json.loads(payload.decode('utf-8'))
    except:
        return None

def connect_discord():
    global sock
    pipe = find_pipe()
    if not pipe:
        print("[RPC-Bridge] No Discord IPC pipe found", flush=True)
        return False
    s = None
    try:
        s = socket.socket(socket.AF_UNIX, socket.SOCK_STREAM)
        s.settimeout(3)
        s.connect(pipe)
        # Send handshake - DO NOT read response here (event_reader handles ALL reads)
        send_frame(s, 0, {"v": 1, "client_id": CLIENT_ID})
        # Send subscriptions - fire and forget
        for evt in ["ACTIVITY_JOIN", "ACTIVITY_SPECTATE", "ACTIVITY_JOIN_REQUEST"]:
            send_frame(s, 1, {"cmd": "SUBSCRIBE", "evt": evt, "nonce": str(int(time.time()*1000))})
        s.settimeout(None)
        with sock_lock:
            sock = s
        print(f"[RPC-Bridge] Connected to Discord IPC at {pipe}", flush=True)
        return True
    except Exception as e:
        print(f"[RPC-Bridge] Connect error: {e}", flush=True)
        if s is not None:
            try:
                s.close()
            except Exception:
                pass
    return False

def event_reader():
    global sock
    while True:
        with sock_lock:
            s = sock
        if s is None:
            time.sleep(1)
            continue
        try:
            frame = recv_frame(s)
            if frame is None:
                print("[RPC-Bridge] Discord closed connection", flush=True)
                with sock_lock:
                    if sock:
                        try:
                            sock.close()
                        except Exception:
                            pass
                    sock = None
                time.sleep(1)
                continue
            opcode, payload = frame
            # Log ALL frames for debugging
            evt_name = payload.get("evt", payload.get("cmd", "unknown"))
            if payload.get("cmd") == "SET_ACTIVITY":
                print(f"[RPC-Bridge] Response: {json.dumps(payload)[:300]}", flush=True)
            if "evt" in payload:
                print(f"[RPC-Bridge] Event: {evt_name} | {json.dumps(payload)[:200]}", flush=True)
            # Log ERROR frames explicitly
            if payload.get("evt") == "ERROR" or "error" in json.dumps(payload).lower():
                print(f"[RPC-Bridge] ERROR frame: {json.dumps(payload)}", flush=True)
            if opcode == 1:
                evt = payload.get("evt", "")
                data = payload.get("data", {})
                if evt == "ACTIVITY_JOIN":
                    secret = data.get("secret", "")
                    if secret:
                        events_queue.put({"type": "JOIN", "secret": secret})
                        print(f"[RPC-Bridge] JOIN event: {secret}", flush=True)
                elif evt == "ACTIVITY_JOIN_REQUEST":
                    user = data.get("user", {})
                    uname = user.get("username", "?")
                    events_queue.put({"type": "REQUEST", "user": uname})
                    print(f"[RPC-Bridge] REQUEST event: {uname}", flush=True)
        except Exception as e:
            print(f"[RPC-Bridge] Event reader error: {e}", flush=True)
            with sock_lock:
                if sock:
                    try:
                        sock.close()
                    except Exception:
                        pass
                sock = None
            time.sleep(1)

game_pid = os.getpid()

def update_discord(activity, pid=None):
    global sock, game_pid
    if pid is not None:
        game_pid = pid
    t0 = time.time()
    with sock_lock:
        s = sock
    if s is None:
        print("[RPC-Bridge] No Discord socket, attempting connect...", flush=True)
        if not connect_discord():
            print("[RPC-Bridge] Connect failed, skipping update", flush=True)
            return False
        with sock_lock:
            s = sock
    try:
        nonce = str(int(time.time() * 1000))
        payload = {
            "cmd": "SET_ACTIVITY",
            "args": {"pid": game_pid, "activity": activity},
            "nonce": nonce
        }
        elapsed = time.time() - t0
        print(f"[RPC-Bridge] SET_ACTIVITY nonce={nonce} (connect took {elapsed:.2f}s)", flush=True)
        send_frame(s, 1, payload)
        # Do NOT read response here - event_reader handles all socket reads
        return True
    except Exception as e:
        print(f"[RPC-Bridge] Update error: {e}", flush=True)
        with sock_lock:
            if sock:
                try:
                    sock.close()
                except Exception:
                    pass
            sock = None
        return False

class Handler(BaseHTTPRequestHandler):
    def log_message(self, format, *args):
        pass  # Suppress HTTP logs

    def do_GET(self):
        if self.path == "/events":
            pending = []
            try:
                while True:
                    pending.append(events_queue.get_nowait())
            except queue.Empty:
                pass
            self.send_response(200)
            self.send_header("Content-Type", "application/json")
            self.end_headers()
            self.wfile.write(json.dumps(pending).encode())
        else:
            self.send_response(404)
            self.end_headers()

    def do_POST(self):
        t0 = time.time()
        if self.path == "/update":
            length = int(self.headers.get('Content-Length', 0))
            body = self.rfile.read(length).decode('utf-8')
            try:
                data = json.loads(body)
                # Support both {activity: {...}} and raw activity object
                if "activity" in data and "pid" in data:
                    pid = data.get("pid", os.getpid())
                    activity = data["activity"]
                else:
                    pid = os.getpid()
                    activity = data
                ok = update_discord(activity, pid)
                dt = time.time() - t0
                print(f"[RPC-Bridge] /update handled in {dt:.3f}s -> {'OK' if ok else 'FAIL'}", flush=True)
                self.send_response(200 if ok else 503)
                self.send_header("Content-Type", "text/plain")
                self.end_headers()
                self.wfile.write(b"OK" if ok else b"DISCORD_NOT_CONNECTED")
            except Exception as e:
                self.send_response(400)
                self.end_headers()
                self.wfile.write(str(e).encode())
        else:
            self.send_response(404)
            self.end_headers()

def main():
    lock_fd = acquire_bridge_lock()
    if lock_fd is None:
        print("[RPC-Bridge] Another bridge is already running. Exiting.", flush=True)
        sys.exit(0)
    threading.Thread(target=event_reader, daemon=True).start()
    server = ServerClass(("127.0.0.1", PORT), Handler)
    print(f"[RPC-Bridge] Listening on http://127.0.0.1:{PORT}", flush=True)
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        pass
    server.server_close()
    try:
        os.close(lock_fd)
        os.unlink(LOCK_FILE)
    except Exception:
        pass
    print("[RPC-Bridge] Exited", flush=True)

if __name__ == "__main__":
    main()
