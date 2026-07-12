#!/usr/bin/env python3
"""MiniCraft Discord RPC - proper IPC framing protocol"""
import socket, json, os, sys, time, struct

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
        if resp and resp[0] == 1 and "evt" in resp[1]:
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
        return recv_frame(sock) is not None
    except:
        return False

def main():
    sock = None
    last_activity = None
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
        
        if sock is not None:
            if not update_activity(sock, activity):
                sock.close()
                sock = connect()
                if sock:
                    update_activity(sock, activity)
        
        if sock is None:
            # No Discord running - silently skip
            pass

if __name__ == "__main__":
    main()
