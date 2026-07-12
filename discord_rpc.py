#!/usr/bin/env python3
"""MiniCraft Discord RPC - handles connection to Discord IPC"""
import socket, json, os, sys, time

CLIENT_ID = "1512377902195540018"
PIPE = os.environ.get("XDG_RUNTIME_DIR", f"/run/user/{os.getuid()}") + "/discord-ipc-0"

def send_rpc(activity):
    """Send RPC update - called from Java with JSON argument"""
    try:
        sock = socket.socket(socket.AF_UNIX, socket.SOCK_STREAM)
        sock.settimeout(2)
        sock.connect(PIPE)
        # Handshake
        sock.send(json.dumps({"v": 1, "client_id": CLIENT_ID}).encode() + b'\n')
        resp = sock.recv(1024).decode()
        if "READY" not in resp:
            sock.close()
            return
        # Set activity
        payload = {
            "cmd": "SET_ACTIVITY",
            "args": {
                "pid": os.getpid(),
                "activity": json.loads(activity)
            },
            "nonce": str(int(time.time() * 1000))
        }
        sock.send(json.dumps(payload).encode() + b'\n')
        sock.recv(1024)
        sock.close()
        return True
    except:
        return False

if __name__ == "__main__":
    if len(sys.argv) > 1:
        send_rpc(sys.argv[1])
    else:
        # Subscribe mode: listen on stdin for JSON updates
        print("RPC ready", flush=True)
        for line in sys.stdin:
            line = line.strip()
            if not line: continue
            send_rpc(line)
