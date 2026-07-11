#!/bin/bash
# MiniCraft Server Launcher - with secure tunnel (no IP exposed)
cd "$(dirname "$0")"

# Kill old instances
kill $(lsof -t -i:25568 2>/dev/null) 2>/dev/null
kill $(lsof -t -i:25566 2>/dev/null) 2>/dev/null
kill $(pgrep -f "bore local" 2>/dev/null) 2>/dev/null

# Check bore
BORE=$(which bore 2>/dev/null || echo /home/linuxbrew/.linuxbrew/bin/bore)
if [ ! -f "$BORE" ]; then
    echo "Installing bore..."
    brew install bore-cli 2>/dev/null || { echo "Failed to install bore!"; exit 1; }
    BORE=$(which bore)
fi

# Start bore tunnel (creates public URL, no real IP exposed)
echo "Creating secure tunnel..."
$BORE local 25565 --to bore.pub 2>&1 &
BORE_PID=$!
sleep 2

# Get bore URL
BORE_URL=""
for i in $(seq 1 5); do
    BORE_URL=$(lsof -p $BORE_PID 2>/dev/null | grep -oP 'bore\.pub:\K\d+' || echo "")
    [ -n "$BORE_URL" ] && break
    sleep 1
done

# Start web status page
echo "Starting web status page..."
python3 webserver.py &
WEB_PID=$!

echo ""
echo "============================================"
echo "  MiniCraft Server is RUNNING!"
echo ""
if [ -n "$BORE_URL" ]; then
    echo "  Share this with friends (no IP visible):"
    echo "  bore.pub:$BORE_URL"
else
    echo "  Waiting for tunnel... check bore.pub:port"
fi
echo ""
echo "  Web status:  http://localhost:25568"
echo "============================================"
echo ""

# Start game
export PATH="/home/linuxbrew/.linuxbrew/opt/openjdk@21/bin:$PATH"
export _JAVA_AWT_WM_NONREPARENTING=1
java -cp build MiniCraft

# Cleanup
echo "Shutting down..."
kill $BORE_PID $WEB_PID 2>/dev/null
echo "Server stopped."
