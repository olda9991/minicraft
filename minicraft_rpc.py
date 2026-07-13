#!/usr/bin/env python3
"""MiniCraft Discord RPC Bridge - HTTP server that forwards to Discord IPC"""
import socket, json, os, sys, time, struct, threading, queue
from http.server import HTTPServer, BaseHTTPRequestHandler

CLIENT_ID = "1512377902195540018"
PORT = 6464
PIPES = [
    os.environ.get("XDG_RUNTIME_DIR", f"/run/user/{os.getuid()}") + "/discord-ipc-0",
    os.environ.get("XDG_RUNTIME_DIR", f"/run/user/{os.getuid()}") + "/app/com.discordapp.Discord/discord-ipc-0",
    os.environ.get("XDG_RUNTIME_DIR", f"/run/user/{os.getuid()}") + "/app/dev.vencord.Vesktop/discord-ipc-0",
]

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
        return False
    try:
        s = socket.socket(socket.AF_UNIX, socket.SOCK_STREAM)
        s.settimeout(3)
        s.connect(pipe)
        send_frame(s, 0, {"v": 1, "client_id": CLIENT_ID})
        resp = recv_frame(s)
        if resp and resp[0] == 1:
            # Subscribe to events
            for evt in ["ACTIVITY_JOIN", "ACTIVITY_SPECTATE", "ACTIVITY_JOIN_REQUEST"]:
                send_frame(s, 1, {"cmd": "SUBSCRIBE", "evt": evt, "nonce": str(int(time.time()*1000))})
                try:
                    s.settimeout(0.5)
                    ack = recv_frame(s)
                except:
                    pass
            s.settimeout(None)
            with sock_lock:
                sock = s
            return True
        s.close()
    except Exception as e:
        print(f"[RPC-Bridge] Connect error: {e}", flush=True)
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
                with sock_lock:
                    sock = None
                time.sleep(1)
                continue
            opcode, payload = frame
            # Log ALL frames for debugging
            if "evt" in payload:
                evt_name = payload.get("evt", "")
                print(f"[RPC-Bridge] Event: {evt_name} | {json.dumps(payload)[:200]}", flush=True)
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
            with sock_lock:
                sock = None
            time.sleep(1)

def update_discord(activity):
    global sock
    with sock_lock:
        s = sock
    if s is None:
        if not connect_discord():
            return False
        with sock_lock:
            s = sock
    try:
        print(f"[RPC-Bridge] SET_ACTIVITY: {json.dumps(activity)[:300]}", flush=True)
        send_frame(s, 1, {
            "cmd": "SET_ACTIVITY",
            "args": {"pid": os.getpid(), "activity": activity},
            "nonce": str(int(time.time() * 1000))
        })
        return True
    except Exception as e:
        print(f"[RPC-Bridge] Update error: {e}", flush=True)
        with sock_lock:
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
        if self.path == "/update":
            length = int(self.headers.get('Content-Length', 0))
            body = self.rfile.read(length).decode('utf-8')
            try:
                activity = json.loads(body)
                ok = update_discord(activity)
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
    threading.Thread(target=event_reader, daemon=True).start()
    server = HTTPServer(("127.0.0.1", PORT), Handler)
    print(f"[RPC-Bridge] Listening on http://127.0.0.1:{PORT}", flush=True)
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        pass
    server.server_close()
    print("[RPC-Bridge] Exited", flush=True)

if __name__ == "__main__":
    main()
