# MiniCraft - Linux Setup Guide

## Step 1: Install Java 21
```bash
# Ubuntu/Debian
sudo apt install openjdk-21-jdk

# Fedora/Bazzite
sudo dnf install java-21-openjdk-devel

# Arch
sudo pacman -S jdk21-openjdk

# Or use Homebrew
brew install openjdk@21
```

## Step 2: Download MiniCraft
1. Go to https://github.com/olda9991/minicraft
2. Click **Code → Download ZIP**
3. Extract to a folder (e.g., `~/minicraft`)
4. Or clone with git:
```bash
git clone https://github.com/olda9991/minicraft.git
```

## Step 3: Compile and Run
```bash
cd ~/minicraft
./update.sh    # download + compile latest
./run.sh       # start the game
```

## Step 4: Play!
- **WASD / Arrow keys** = Move
- **Left Click** = Break blocks
- **Right Click** = Place blocks  
- **Scroll wheel** = Change selected block
- **E** = Crafting
- **F** = Survival/Creative toggle
- **T** = Chat
- **G** = Noclip (fly through blocks)
- **M** = Toggle music
- **ESC** = Save & Exit

## Multiplayer
**Host a server:**
```
./host_server.sh
```
This starts the game + web status page + secure tunnel. Share the bore.pub address with friends.

**Connect to a server:**
- Multiplayer → wait for LAN server to appear, or
- Multiplayer → Direct Connect → enter IP:port

## Adding Minecraft Music
Place `.wav`, `.mp3`, `.mp4`, or `.ogg` files in the `music/` folder.
Minecraft music is included by default (calm1-3, creative1-2).

## Updating
```bash
./update.sh   # pull latest from GitHub + compile
```

## Troubleshooting

**"javac not found" →** Install Java JDK (not just JRE)

**Game won't open →** Install Java 21 from Step 1

**No sound →** Check system audio, press M in-game

**Font looks weird →** Install PixelPurl.ttf from the game folder

**Stuck in blocks →** Press G for noclip mode

## Need help?
Open an issue on GitHub: https://github.com/olda9991/minicraft/issues
