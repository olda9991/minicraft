# MiniCraft Bedrock Mobile

Android Studio project scaffold for porting MiniCraft to phones.

## Architecture

The desktop Swing codebase (`src/MiniCraft.java`) is ~2000 lines of single-file Java.
Porting to Android requires:

1. **Renderer replacement**: Swing `JPanel`/`Graphics2D` -> Android `SurfaceView` + `Canvas`
2. **Input replacement**: `KeyListener`/`MouseListener` -> Android `onTouchEvent` + virtual D-pad/jump buttons
3. **Audio replacement**: `javax.sound.sampled` -> Android `SoundPool` + `MediaPlayer`
4. **Networking**: Reuse TCP string protocol; add Android `DatagramSocket` for voice chat
5. **Storage**: `System.getProperty("user.dir")` -> Android `Context.getFilesDir()`

## Project Structure

```
minicraft-android/
├── app/
│   ├── src/main/java/com/olda/minicraft/
│   │   └── MainActivity.java      # Android entry point
│   ├── src/main/res/
│   │   └── layout/activity_main.xml # Touch controls overlay
│   └── build.gradle               # App-level dependencies
├── build.gradle                   # Project-level
└── settings.gradle
```

## Quick Start

1. Open this folder in Android Studio
2. Copy `src/MiniCraft.java` game logic into `app/src/main/java/com/olda/minicraft/`
3. Replace `JPanel` rendering with custom `SurfaceView` thread
4. Add virtual joystick + buttons in `res/layout/`
5. Build APK for Android 8.0+ (API 26+)

## Notes

- **LibGDX alternative**: For a faster port, use [LibGDX](https://libgdx.com/) which handles cross-platform rendering/input.
- **Texture strategy**: Export all `/textures/*.png` to `res/drawable-nodpi/`
- **Block count**: Current 150 blocks; arrays must match in mobile build
- **RPC bridge**: Python bridge won't run on Android; either embed IPC in Java or skip Discord RPC on mobile

## Voice Chat on Mobile

Use Android `AudioRecord` + `AudioTrack` instead of Java `TargetDataLine`.
UDP peer discovery via LAN broadcast on port +1000.

## License

Same as desktop MiniCraft.
