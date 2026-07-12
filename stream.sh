#!/bin/bash
# MiniCraft + Discord Streaming Launcher
# Starts Vesktop (Discord) and MiniCraft together for streaming

cd "$(dirname "$0")"

# Start Vesktop if not already running
if ! pgrep -f "dev.vencord.Vesktop" > /dev/null 2>&1; then
    echo "Starting Vesktop (Discord)..."
    flatpak run --socket=wayland --socket=pulseaudio --device=all --socket=pipewire dev.vencord.Vesktop &
    sleep 3
fi

# Launch MiniCraft
echo "Starting MiniCraft..."
exec ./run.sh "$@"
