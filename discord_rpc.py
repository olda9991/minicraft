#!/usr/bin/env python3
"""MiniCraft Discord RPC - reads activity JSON from stdin, prints events to stdout"""
import socket, json, os, sys, time, struct, threading

CLIENT_ID = "1512377902195540018"
PIPES = [
    os.environ.get("XDG_RUNTIME_DIR", f"/run/user/{os.getuid()}") + "/discord-ipc-0",
    os.environ.get("XDG_RUNTIME_DIR", f"/run/user/{os.getuid()}") + "/app/com.discordapp.Discord/discord-ipc-0",
    os.environ.get("XDG_RUNTIME_DIR", f"/run/user/{os.getuid()}") + "/app/dev.vencord.Vesktop/discord-ipc-0",
]

def find_pipe():
    for pipe in PIPES:
        if os.path.exists(pipe):
            return pipe
    return None

def send_frame(sock, opcode, data):
    payload = json.dumps(data).encode('utf-8')
    header = struct.pack('<II', opcode, len(payload))
    sock.sendall(header + payload)

def recv_frame(sock):
    try:
        header = sock.recv(8)
        if len(header) < 8:
            return None
        opcode, length = struct.unpack('<II', header)
        payload = b''
        while len(payload) < length:
            chunk = sock.recv(length - len(payload))
            if not chunk:
                return None
            payload += chunk
        return opcode, json.loads(payload.decode('utf-8'))
    except:
        return None

def connect():
    pipe = find_pipe()
    if not pipe:
        return None
    try:
        sock = socket.socket(socket.AF_UNIX, socket.SOCK_STREAM)
        sock.settimeout(3)
        sock.connect(pipe)
        send_frame(sock, 0, {"v": 1, "client_id": CLIENT_ID})
        resp = recv_frame(sock)
        if resp and resp[0] == 1:
            return sock
        sock.close()
    except Exception as e:
        pass
    return None

def update_activity(sock, activity):
    try:
        send_frame(sock, 1, {
            "cmd": "SET_ACTIVITY",
            "args": {"pid": os.getpid(), "activity": activity},
            "nonce": str(int(time.time() * 1000))
        })
        return True
    except:
        return False

def event_loop(sock):
    """Read Discord events and print them to stdout for Java to consume."""
    while True:
        try:
            frame = recv_frame(sock)
            if frame is None:
                break
            opcode, payload = frame
            if opcode == 1 and "evt" in payload:
                evt = payload.get("evt", "")
                data = payload.get("data", {})
                if evt == "ACTIVITY_JOIN":
                    secret = data.get("secret", "")
                    if secret:
                        print(f"EVENT:JOIN:{secret}", flush=True)
                elif evt == "ACTIVITY_SPECTATE":
                    secret = data.get("secret", "")
                    if secret:
                        print(f"EVENT:SPECTATE:{secret}", flush=True)
                elif evt == "ACTIVITY_JOIN_REQUEST":
                    user = data.get("user", {})
                    uname = user.get("username", "?")
                    print(f"EVENT:REQUEST:{uname}", flush=True)
                # Silently ignore READY, ERROR, etc.
        except Exception as e:
            break

def main():
    sock = None
    event_thread = None
    print("RPC ready", flush=True)
    
    for line in sys.stdin:
        line = line.strip()
        if not line:
            continue
        try:
            activity = json.loads(line)
        except:
            continue
        
        if sock is None:
            sock = connect()
            if sock:
                event_thread = threading.Thread(target=event_loop, args=(sock,), daemon=True)
                event_thread.start()
        
        if sock is not None:
            if not update_activity(sock, activity):
                try: sock.close()
                except: pass
                sock = None
                event_thread = None
                sock = connect()
                if sock:
                    event_thread = threading.Thread(target=event_loop, args=(sock,), daemon=True)
                    event_thread.start()
                    update_activity(sock, activity)
        
        if sock is None:
            # No Discord running - silently skip
            pass

if __name__ == "__main__":
    main()
