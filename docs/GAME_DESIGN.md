# MiniCraft Game Design

MiniCraft should feel like a tiny 2D Minecraft-inspired survival game, not just a block catalogue.

## Core Loop
1. Explore the surface and collect wood.
2. Craft planks, sticks, and a first tool.
3. Mine stone and coal.
4. Make torches, food, storage, and a small shelter.
5. Survive the night and fight or avoid hostile mobs.
6. Go deeper for better ores and craft better gear.

## First-Day Goals
- The player should understand what to do without reading external docs.
- A short HUD checklist is enough: wood, planks, sticks, pickaxe, light/shelter.
- The first night should be dangerous, but not a surprise instant death.
- Food and light must be reachable before the first night.

## Current V1 Systems
- Crafting uses a 2x2 grid. Items placed into the grid are already removed from inventory; crafting consumes the grid and adds the result.
- Chests are placeable storage blocks with 8 slots and are saved with the world.
- Furnaces are placeable utility blocks with input, fuel, and output slots. SPACE smelts one item when a valid recipe and fuel are present.
- The main menu has Continue for the newest saved world, New World, and Load World as separate choices.
- Survival inventory shows only collected items. Creative uses its own block palette.
- The world has a repaired bedrock bottom, and dropped items should land on terrain.
- Normal player collision applies in Survival and Creative. Noclip is a separate debug cheat.

## Good Next Features
- Better recipe book: show craftable recipes first and grey out missing ingredients.
- Movement polish: coyote time, variable jump height, and clearer one-block stepping.
- Real armor slots instead of using ingots directly as armor.
- Biomes that change surface blocks, tree types, mob types, and loot.
- Doors and fences, because they make shelters feel real.
- Simple quest/achievement chain for the first boss.

## Avoid For Now
- Big networking rewrites while survival basics are changing.
- Adding many new block IDs without a use.
- Redstone or minecarts before doors, chests, furnace, and inventory are stable.
