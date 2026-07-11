#!/usr/bin/env python3
"""MiniCraft web server - shows server status via secure tunnel."""
import http.server
import json
import socket
import subprocess
import threading
import os
import zipfile
import io
import re

PORT = 25568
GAME_PORT = 25565
HTML = """<!DOCTYPE html>
<html>
<head>
    <title>MiniCraft Server</title>
    <meta charset="utf-8">
    <style>
        *{margin:0;padding:0;box-sizing:border-box}
        body{background:#2d2d2d;color:#eee;font-family:'Courier New',monospace;text-align:center;padding:40px}
        h1{color:#64c83c;font-size:48px;text-shadow:2px 2px 0 #3a3a3a;margin-bottom:10px}
        .status{display:inline-block;padding:20px 40px;margin:20px;border:2px solid #555;border-radius:8px;background:#3a3a3a;min-width:300px}
        .online{color:#64c83c;font-size:24px}
        .offline{color:#ff4444;font-size:24px}
        .info{font-size:16px;margin:8px 0;color:#aaa}
        .info span{color:#fff;font-weight:bold}
        .btn{display:inline-block;padding:12px 30px;margin:10px;background:#64c83c;color:#1a1a1a;text-decoration:none;font-size:18px;border-radius:6px;font-weight:bold}
        .btn:hover{background:#7de04e}
        .steps{text-align:left;max-width:600px;margin:30px auto;padding:20px;background:#333;border-radius:8px}
        .steps li{margin:10px 0;color:#ccc;font-size:14px}
        .footer{color:#666;margin-top:40px;font-size:12px}
    </style>
</head>
<body>
    <h1>MiniCraft</h1>
    <p style="color:#888;margin-bottom:20px">Multiplayer Server</p>
    <div class="status">
        <div id="statusText" class="offline">OFFLINE</div>
        <div class="info">Server Address: <span id="serverAddr">waiting...</span></div>
        <div class="info">Players: <span id="playerCount">0</span></div>
        <div class="info" style="color:#7de04e;font-size:12px">Your real IP is hidden (secure tunnel)</div>
    </div>
    <a class="btn" href="/download" download>Download for Windows</a>
    <a class="btn" href="/download_src" download>Download Source</a>
    <a class="btn" href="/guide" style="background:#555;color:#fff">Setup Guide</a>
    <div class="steps">
        <h3 style="color:#64c83c;margin-bottom:10px">How to connect:</h3>
        <ol>
            <li>Click "Download for Windows" and extract the zip</li>
            <li>Install <b>Java 21</b> from <a href="https://adoptium.net/" style="color:#7de04e">adoptium.net</a></li>
            <li>Run <b>run.bat</b> (Windows) or <b>run.sh</b> (Linux/Mac)</li>
            <li>Click <b>Multiplayer → Direct Connect</b></li>
            <li>Enter the <b>Server Address</b> shown above (bore.pub:port)</li>
            <li>Click <b>Connect</b> and you're in!</li>
        </ol>
        <p style="color:#888;font-size:12px;margin-top:10px">No port forwarding needed. Your IP stays hidden.</p>
    </div>
    <div class="footer">MiniCraft Server - Refresh page to update status</div>
    <script>
        function updateStatus(){
            fetch('/status').then(r=>r.json()).then(d=>{
                document.getElementById('statusText').textContent=d.online?'ONLINE':'OFFLINE';
                document.getElementById('statusText').className=d.online?'online':'offline';
                document.getElementById('serverAddr').textContent=d.address||'waiting...';
                document.getElementById('playerCount').textContent=d.players||'0';
            }).catch(()=>{});
        }
        updateStatus();
        setInterval(updateStatus,5000);
    </script>
</body>
</html>
"""

class MiniCraftHTTP(http.server.BaseHTTPRequestHandler):
    def do_GET(self):
        if self.path == '/status':
            self.send_response(200)
            self.send_header('Content-Type', 'application/json')
            self.send_header('Access-Control-Allow-Origin', '*')
            self.end_headers()
            addr = self.get_bore_address()
            players = self.get_player_count()
            self.wfile.write(json.dumps({
                'online': True, 'address': addr,
                'players': players, 'world': get_world_name()
            }).encode())
        elif self.path == '/download':
            self.send_download_zip()
        elif self.path == '/download_src':
            self.send_source_zip()
        elif self.path == '/guide':
            self.send_response(200)
            self.send_header('Content-Type', 'text/html')
            self.end_headers()
            try:
                with open(os.path.join(os.path.dirname(__file__), 'WINDOWS_SETUP.md' if os.name != 'nt' else 'WINDOWS_SETUP.md'), 'r') as f:
                    content = f.read()
                html_content = f'<html><head><title>MiniCraft Setup Guide</title><meta charset="utf-8"><style>body{{background:#2d2d2d;color:#eee;font-family:"Courier New",monospace;padding:40px;max-width:800px;margin:auto}}h1{{color:#64c83c}}h2{{color:#7de04e;margin-top:30px}}code{{background:#444;padding:2px 6px;border-radius:3px}}a{{color:#7de04e}}</style></head><body><pre style="font-family:inherit;white-space:pre-wrap">{content}</pre></body></html>'
                self.wfile.write(html_content.encode())
            except:
                self.wfile.write(b'<h1>Guide not found</h1>')
        else:
            self.send_response(200)
            self.send_header('Content-Type', 'text/html')
            self.end_headers()
            self.wfile.write(HTML.encode())

    def get_bore_address(self):
        try:
            r = subprocess.run(['lsof', '-iTCP', '-sTCP:LISTEN', '-P', '-n'], capture_output=True, text=True, timeout=3)
            for line in r.stdout.split('\n'):
                if 'bore' in line.lower() and '25565' in line:
                    parts = line.strip().split()
                    for p in parts:
                        if '->' in p:
                            remote = p.split('->')[1]
                            if ':' in remote:
                                host, port = remote.rsplit(':', 1)
                                if 'bore.pub' in host or host.startswith('104.'):
                                    return f'bore.pub:{port}'
                    for p in parts:
                        if '*' in p and ':' in p:
                            port = p.split(':')[-1]
                            return f'bore.pub:{port}'
            return 'tunnel starting...'
        except:
            return 'tunnel starting...'

    def get_player_count(self):
        try:
            import subprocess
            r = subprocess.run(['ss', '-tlnp'], capture_output=True, text=True, timeout=2)
            if str(GAME_PORT) in r.stdout:
                return '1+ (check game)'
        except: pass
        return '?'

    def send_download_zip(self):
        buf = io.BytesIO()
        with zipfile.ZipFile(buf, 'w', zipfile.ZIP_DEFLATED) as z:
            base = '/var/home/olda/minicraft'
            for root, dirs, files in os.walk(base):
                for f in files:
                    path = os.path.join(root, f)
                    arcname = os.path.relpath(path, base)
                    if '.git' in arcname or '__pycache__' in arcname:
                        continue
                    z.write(path, arcname)
        buf.seek(0)
        self.send_response(200)
        self.send_header('Content-Type', 'application/zip')
        self.send_header('Content-Disposition', 'attachment; filename="MiniCraft.zip"')
        self.send_header('Content-Length', str(buf.getbuffer().nbytes))
        self.end_headers()
        self.wfile.write(buf.getvalue())

    def send_source_zip(self):
        buf = io.BytesIO()
        with zipfile.ZipFile(buf, 'w', zipfile.ZIP_DEFLATED) as z:
            z.write('/var/home/olda/minicraft/src/MiniCraft.java', 'MiniCraft.java')
        buf.seek(0)
        self.send_response(200)
        self.send_header('Content-Type', 'application/zip')
        self.send_header('Content-Disposition', 'attachment; filename="MiniCraft_src.zip"')
        self.send_header('Content-Length', str(buf.getbuffer().nbytes))
        self.end_headers()
        self.wfile.write(buf.getvalue())

    def log_message(self, format, *args):
        pass  # quiet

def get_world_name():
    try:
        worlds_dir = os.path.expanduser('~/minicraft/worlds')
        if os.path.isdir(worlds_dir):
            files = [f for f in os.listdir(worlds_dir) if f.endswith('.mcw')]
            if files:
                return files[0].replace('.mcw', '')
    except: pass
    return 'unknown'

if __name__ == '__main__':
    server = http.server.HTTPServer(('0.0.0.0', PORT), MiniCraftHTTP)
    print(f'MiniCraft web server running on http://0.0.0.0:{PORT}')
    print(f'Share your IP with friends so they can connect!')
    server.serve_forever()
