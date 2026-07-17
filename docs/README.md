# MiniCraft Web

Run the **original** MiniCraft Java JAR directly in your web browser.

## How It Works

This uses [CheerpJ](https://cheerpj.com/) — a Java-to-JavaScript runtime that executes
standard `.jar` files in the browser with **zero code changes**. The `MiniCraft.jar` here
is the exact same JAR built from `../src/MiniCraft.java`.

## Quick Start

```bash
cd web/
./serve.sh
# Open http://localhost:8765 in your browser
```

Or manually:
```bash
python3 -m http.server 8765
# Open http://localhost:8765
```

## Files

- `index.html` — Main page with CheerpJ launcher
- `cheerpj.html` — Minimal launcher (no UI chrome)
- `MiniCraft.jar` — The actual game JAR (copied from parent directory)
- `serve.sh` — Local HTTP server script

## Controls (same as desktop)

- **WASD** — Move
- **Space** — Jump
- **Mouse** — Look around (3D mode)
- **Click** — Break blocks
- **Right-click** — Place blocks
- **1-9** — Hotbar
- **F** — Survival/Creative toggle
- **F9** — 3D raycasting mode
- **F10** — VR SBS stereoscopic mode
- **Esc** — Menu

## Notes

- CheerpJ downloads the runtime on first load (~10-20 seconds)
- Audio and TCP multiplayer may have browser limitations
- For full features (Discord RPC, voice chat, file saves), use the desktop `../run.sh`
- World saves use browser localStorage when running via CheerpJ

## Alternative: TeaVM Build

For a fully ahead-of-time compiled version (no runtime interpreter), see:
https://teavm.org/docs/intro/overview.html

This requires setting up a TeaVM Gradle build that compiles `MiniCraft.java`
to optimized JavaScript. The CheerpJ approach above is recommended for immediate use.
