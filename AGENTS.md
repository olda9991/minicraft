# MiniCraft Agent Notes

MiniCraft is a small Java Swing sandbox game. Keep changes simple, visible, and easy to explain to a young author.

## Build And Smoke Test
- Compile: `javac -d build src/MiniCraft.java`
- Run locally: `./run.sh` or `./run.sh --source`
- `run.sh` should prefer the current `src/MiniCraft.java` checkout over a stale `MiniCraft.jar`.
- Main game file: `src/MiniCraft.java`
- Do not edit generated jars or APK files unless the user asks for a release build.

## Code Map
- Constants, block IDs, names, textures: top of `MiniCraft.java`
- World state and player state: fields near `world`, `px`, `py`, `inv`
- World generation: `genWorld`
- Save/load: `saveWorld`, `loadWorld`
- Game loop: `actionPerformed`
- Movement/physics helpers: `movePlayer`, `isOnGround`, `updateDrops`, `repairWorldBounds`
- Drawing: `paintComponent`, `drawGame`, `drawHUD`, screen-specific `draw...` methods
- Input: `keyPressed`, `mousePressed`, `mouseWheelMoved`
- Multiplayer: `MiniServer`, `MiniClient`

## Design Rules
- Prefer one small playable improvement over many half-working features.
- Survival should teach this loop: get wood, craft planks/sticks, make tools, gather food/light, survive night, mine better ores.
- Creative gives unlimited blocks, but normal player collision still applies. Noclip is a separate debug cheat.
- If you add an item, update all needed places together: ID, `BNAME`, `TF`, fallback color, crafting/placement/use, save compatibility if it has state.
- Keep new systems data-driven when possible, but avoid a large refactor in one change.

## Save Compatibility
- New world saves start with `MINICRAFT_WORLD` and a version number.
- `loadWorld` must keep reading old saves that start directly with `worldName`.
- When adding persistent data, append it after the existing world arrays and bump the version.

## Review Priorities
- First fix bugs that make the game unfair or confusing.
- Keep survival movement solid before adding more content; debug noclip is a cheat only.
- Preserve the bedrock bottom via `repairWorldBounds` when changing generation or physics.
- Then improve onboarding and survival progression.
- Only then add large Minecraft-like systems such as redstone, villages, boats, or enchantments.
