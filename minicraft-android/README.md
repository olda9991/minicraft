# MiniCraft Bedrock for Android

Full Android port of MiniCraft v6.3.0 with touch controls, 150 blocks, fire mechanics, and background layer.

## Features

- **150 Blocks** — Vanilla + Nether, Caves & Cliffs, Wild Update, Chaos Cubed
- **Fire & Soul Fire** — Spread mechanics, flint & steel ignition, player damage
- **Background Layer** (`bgWorld`) — Decorative parallax depth behind the world
- **Touch Controls** — Virtual D-pad, Jump, Break, and Place buttons
- **World Gen** — Procedural terrain with trees, caves, ores
- **Block Breaking** — Hold tap to break (survival timing)
- **Block Placing** — Tap Place button or double-tap
- **Hotbar** — Slots 1-9 visible at bottom
- **Debug HUD** — Coordinates, block count

## Project Structure

```
minicraft-android/
├── app/
│   ├── src/main/java/com/olda/minicraft/
│   │   ├── MainActivity.java      # Activity + touch button wiring
│   │   ├── GameView.java          # SurfaceView render thread
│   │   └── MiniCraftGame.java     # Core engine (ported from desktop)
│   ├── src/main/res/
│   │   ├── layout/activity_main.xml  # Virtual D-pad + action buttons
│   │   └── values/strings.xml
│   └── build.gradle
├── build-apk.sh                 # CLI build script
└── README.md
```

## Build APK

### Option 1: Android Studio (Recommended)

1. Open `minicraft-android/` folder in Android Studio
2. Wait for Gradle sync
3. **Build > Build Bundle(s) / APK(s) > Build APK(s)**
4. Find APK at `app/build/outputs/apk/debug/app-debug.apk`

### Option 2: Command Line

```bash
cd minicraft-android
export ANDROID_SDK_ROOT=/path/to/android-sdk
./build-apk.sh
```

### Install to Phone

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Controls

| Button | Action |
|--------|--------|
| D-Pad arrows | Move / Fly (creative) |
| JUMP | Jump (survival) / Fly up (creative) |
| BREAK | Hold + tap screen = break block |
| PLACE | Tap screen = place selected block |
| Hotbar 1-9 | Tap slot = select block |

## Textures

The game tries to load `assets/textures/<id>.png`. If missing, it generates colored placeholders using the `BLOCK_COLORS` array.

To add real textures:
1. Create `app/src/main/assets/textures/`
2. Copy all 150 block PNGs named `0.png` through `149.png`

## Desktop vs Mobile Differences

| Feature | Desktop | Mobile |
|---------|---------|--------|
| Renderer | Swing `JPanel` | Android `SurfaceView` |
| Input | Keyboard/Mouse | Touch + virtual buttons |
| Audio | `javax.sound.sampled` | TODO: Android `SoundPool` |
| Discord RPC | Python HTTP bridge | Not available |
| Voice Chat | `DatagramSocket` | `DatagramSocket` (same) |
| Save files | `System.getProperty("user.dir")` | `Context.getFilesDir()` |

## TODO for Full Parity

- [ ] Add sound effects (SoundPool)
- [ ] Add music looping
- [ ] Add inventory/crafting UI screen
- [ ] Add multiplayer client/server
- [ ] Add save/load worlds to Android storage
- [ ] Add settings menu (sensitivity, graphics)
- [ ] Add mobs (render + AI)
- [ ] Add particle effects polish
- [ ] Add night/day cycle shading
- [ ] Add world seed input

## License

Same as desktop MiniCraft.
