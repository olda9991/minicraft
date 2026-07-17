#!/bin/bash
# serve.sh - Launch local web server for MiniCraft Web
cd "$(dirname "$0")"
echo "=== MiniCraft Web Server ==="
echo "Open http://localhost:8765 in your browser"
python3 -m http.server 8765
