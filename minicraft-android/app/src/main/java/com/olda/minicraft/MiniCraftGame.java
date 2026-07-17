package com.olda.minicraft;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

/**
 * MiniCraft Android - Full Game Engine
 * Ported from desktop Swing with all core mechanics
 */
public class MiniCraftGame {

    public static final int TILE = 32;
    public static final int W = 128, H = 64;
    public static final int BLOCK_COUNT = 150;

    // All block IDs matching desktop exactly
    public static final int AIR=0,GRASS=1,DIRT=2,STONE=3,COBBLESTONE=4,BEDROCK=5,SAND=6,GRAVEL=7;
    public static final int OAK_LOG=8,SPRUCE_LOG=9,BIRCH_LOG=10,JUNGLE_LOG=11,ACACIA_LOG=12,DARK_OAK_LOG=13;
    public static final int OAK_PLANKS=14,SPRUCE_PLANKS=15,BIRCH_PLANKS=16,JUNGLE_PLANKS=17,ACACIA_PLANKS=18,DARK_OAK_PLANKS=19;
    public static final int OAK_LEAVES=20,SPRUCE_LEAVES=21,BIRCH_LEAVES=22,JUNGLE_LEAVES=23,ACACIA_LEAVES=24,DARK_OAK_LEAVES=25;
    public static final int WATER=26,LAVA=27,ICE=28,BRICKS=29,STONE_BRICKS=30,MOSSY_STONE_BRICKS=31,CRACKED_STONE_BRICKS=32;
    public static final int COAL_ORE=33,IRON_ORE=34,GOLD_ORE=35,DIAMOND_ORE=36,EMERALD_ORE=37,REDSTONE_ORE=38,LAPIS_ORE=39,COPPER_ORE=40;
    public static final int NETHERRACK=41,SOUL_SAND=42,GLOWSTONE=43,OBSIDIAN=44,CRYING_OBSIDIAN=45;
    public static final int SMOOTH_STONE=46,POLISHED_ANDESITE=47,POLISHED_DIORITE=48,POLISHED_GRANITE=49;
    public static final int PRISMARINE=50,DARK_PRISMARINE=51,SEA_LANTERN=52,END_STONE=53,PURPUR=54,MAGMA=55,SLIME=56,TNT=57,FURNACE=58,CRAFTING_TABLE=59;
    public static final int RAW_BEEF=60,COOKED_BEEF=61,RAW_PORK=62,COOKED_PORK=63,WOOL=64;
    public static final int IRON_INGOT=65,GOLD_INGOT=66,DIAMOND_GEM=67,BED=68,EXP_ORB=69;
    public static final int SWORD=70,PICKAXE=71,AXE=72,SHOVEL=73,FURNACE_ITEM=74,BOW=75,ARROW_ITEM=76,CHEST=77,SPIDER_EYE=78,BONE=79;
    public static final int STICK=80,FLINT_STEEL=81,ROD=82,RAW_FISH=83,COOKED_FISH=84,SADDLE=85,LEATHER_ITEM=86,TORCH_ITEM=87,TOTEM=88,ELYTRA=89;
    public static final int FIRE=90,SOUL_FIRE=91,STRAW_BED=92,CUSHION=93,SHELF_MUSHROOM=94,SULFUR_CUBE=95;
    public static final int NETHERITE_BLOCK=96,ANCIENT_DEBRIS=97,BLACKSTONE=98,GILDED_BLACKSTONE=99,BASALT=100,POLISHED_BASALT=101;
    public static final int CRIMSON_STEM=102,CRIMSON_PLANKS=103,WARPED_STEM=104,WARPED_PLANKS=105,SHROOMLIGHT=106,LODESTONE=107;
    public static final int RESPAWN_ANCHOR=108,SOUL_SOIL=109,TARGET=110,HONEY_BLOCK=111,DEEPSLATE=112,DEEPSLATE_DIAMOND_ORE=113;
    public static final int AMETHYST_BLOCK=114,BUDDING_AMETHYST=115,SCULK=116,MOSS_BLOCK=117,AZALEA_LEAVES=118,FLOWERING_AZALEA=119;
    public static final int ROOTED_DIRT=120,MUD=121,MUD_BRICKS=122,MANGROVE_LOG=123,MANGROVE_LEAVES=124,MANGROVE_PLANKS=125;
    public static final int CHERRY_LOG=126,CHERRY_LEAVES=127,CHERRY_PLANKS=128,BAMBOO_BLOCK=129,BAMBOO_PLANKS=130,BAMBOO_MOSAIC=131;
    public static final int COPPER_BLOCK=132,EXPOSED_COPPER=133,WEATHERED_COPPER=134,OXIDIZED_COPPER=135,CUT_COPPER=136,WAXED_COPPER=137;
    public static final int RAW_IRON_BLOCK=138,RAW_GOLD_BLOCK=139,RAW_COPPER_BLOCK=140,LANTERN=141,SOUL_LANTERN=142,CAMPFIRE=143;
    public static final int SOUL_CAMPFIRE=144,CHAIN=145,SMITHING_TABLE=146,BARREL=147,SMOKER=148,BLAST_FURNACE=149;

    public static final String[] BNAME={"Air","Grass","Dirt","Stone","Cobblestone","Bedrock","Sand","Gravel","Oak Log","Spruce Log","Birch Log","Jungle Log","Acacia Log","Dark Oak Log","Oak Planks","Spruce Planks","Birch Planks","Jungle Planks","Acacia Planks","Dark Oak Planks","Oak Leaves","Spruce Leaves","Birch Leaves","Jungle Leaves","Acacia Leaves","Dark Oak Leaves","Water","Lava","Ice","Bricks","Stone Bricks","Mossy Stone Bricks","Cracked Stone Bricks","Coal Ore","Iron Ore","Gold Ore","Diamond Ore","Emerald Ore","Redstone Ore","Lapis Ore","Copper Ore","Netherrack","Soul Sand","Glowstone","Obsidian","Crying Obsidian","Smooth Stone","Polished Andesite","Polished Diorite","Polished Granite","Prismarine","Dark Prismarine","Sea Lantern","End Stone","Purpur Block","Magma","Slime Block","TNT","Furnace","Crafting Table","Raw Beef","Cooked Beef","Raw Pork","Cooked Pork","Wool","Iron Ingot","Gold Ingot","Diamond","Bed","XP Orb","Sword","Pickaxe","Axe","Shovel","Furnace Item","Bow","Arrow","Chest","Spider Eye","Bone","Stick","FlintSteel","FishRod","RawFish","CookFish","Saddle","Leather","Torch","Totem","Elytra","Fire","Soul Fire","Straw Bed","Cushion","Shelf Mushroom","Sulfur Cube","Netherite Block","Ancient Debris","Blackstone","Gilded Blackstone","Basalt","Polished Basalt","Crimson Stem","Crimson Planks","Warped Stem","Warped Planks","Shroomlight","Lodestone","Respawn Anchor","Soul Soil","Target","Honey Block","Deepslate","Deepslate Diamond Ore","Amethyst Block","Budding Amethyst","Sculk","Moss Block","Azalea Leaves","Flowering Azalea","Rooted Dirt","Mud","Mud Bricks","Mangrove Log","Mangrove Leaves","Mangrove Planks","Cherry Log","Cherry Leaves","Cherry Planks","Bamboo Block","Bamboo Planks","Bamboo Mosaic","Copper Block","Exposed Copper","Weathered Copper","Oxidized Copper","Cut Copper","Waxed Copper","Raw Iron Block","Raw Gold Block","Raw Copper Block","Lantern","Soul Lantern","Campfire","Soul Campfire","Chain","Smithing Table","Barrel","Smoker","Blast Furnace"};

    // Block colors matching desktop exactly
    private static final int[] COLORS = {
        0x00000000,0xFF64B43C,0xFF8C643C,0xFF787878,0xFF646464,0xFF1E1E1E,0xFFE6D2A0,0xFF8C8278,
        0xFF645032,0xFF503C28,0xFFB4A064,0xFF786446,0xFFA0783C,0xFF3C2814,0xFFA08250,
        0xFF8C6E46,0xFFB49664,0xFFA0825A,0xFFB48C50,0xFF644628,0xFF328C32,0xFF287828,
        0xFF3C963C,0xFF32822D,0xFF3C8C32,0xFF286E1E,0xFF5050C8,0xFFFF7814,0xFFA0C8DC,
        0xFFB47850,0xFFA0A0A0,0xFF8C8C78,0xFF828282,0xFF3C3C3C,0xFFB48CA0,0xFFDCB428,
        0xFF50C8DC,0xFF3CB43C,0xFFC85050,0xFF3C50B4,0xFFA07850,0xFF642828,0xFF503C28,
        0xFFC8B450,0xFF1E1432,0xFF501E3C,0xFFB4B4B4,0xFF8C8C96,0xFFA0A0AA,0xFFA08C78,
        0xFF50A0A0,0xFF3C7878,0xFFC8C8A0,0xFFDCC8A0,0xFFA05078,0xFFC85028,0xFF50C850,
        0xFFB43C28,0xFF787878,0xFF8C643C,0xFFC83C3C,0xFFB4643C,0xFF969696,0xFFC89696,
        0xFFDCDCDC,0xFFB4B4B4,0xFFFFDC28,0xFFB4F0C8,0xFFC86464,0xFF50FF50,0xFFB4B4B4,
        0xFFC8C8C8,0xFFB4B4B4,0xFFC8C8C8,0xFFC8C8C8,0xFFC8C8C8,0xFFB4783C,0xFFC8B48C,
        0xFFA06428,0xFF8C3232,0xFFDCDCC8,0xFFB4A078,0xFF646464,0xFF5078A0,0xFF64A0B4,
        0xFFC89664,0xFF508C50,0xFFB4783C,0xFFFFC828,0xFFC8C864,0xFFC8B4A0,
        0xFFFF8C00,0xFF00B4FF,0xFFC8B450,0xFFF0C8C8,0xFFB4783C,0xFFC8C832,0xFF323232,0xFF503C28,
        0xFF282828,0xFF3C321E,0xFF50505A,0xFF64646E,0xFF782828,0xFFA03C3C,0xFF287878,
        0xFF3CA0A0,0xFFFFC864,0xFFA0A0AA,0xFF3C2864,0xFF3C3228,0xFFC8A078,0xFFFFC832,
        0xFF50505A,0xFF3C3C50,0xFFB48CDC,0xFFA078C8,0xFF14281E,0xFF508C3C,0xFF50A050,
        0xFFC864C8,0xFF8C7850,0xFF64503C,0xFF8C643C,0xFF785032,0xFF3C8C50,0xFF64A078,
        0xFFC896A0,0xFFFFB4C8,0xFFFFC8DC,0xFFC8DC64,0xFFDCF078,0xFFF0FA8C,0xFFB47850,
        0xFFA08C78,0xFF8C8264,0xFF78785A,0xFF8C7864,0xFFA09682,0xFF8C826E,0xFFB4A08C,
        0xFFC8B4A0,0xFFA08264,0xFF646464,0xFF00A0C8,0xFF0078A0,0xFFFF8C00,0xFF00C8FF,
        0xFFA0A0AA,0xFF646464,0xFF646464
    };

    // ==================== GAME STATE ====================
    private int[][] world, bgWorld;
    private double px = W*TILE/2.0, py = 0;
    private double playerVy = 0;
    private int camX = 0, camY = 0;
    private double camSmoothX = 0, camSmoothY = 0;
    private int selBlock = GRASS;
    private boolean survival = true;
    private int health = 20, hunger = 20;
    private int[] inv = new int[BLOCK_COUNT];
    private int[] invCount = new int[BLOCK_COUNT];
    private int breakX = -1, breakY = -1, breakTimer = 0, breakTime = 0;
    private int blocksBroken = 0, blocksPlaced = 0;
    private Random rand = new Random();
    private ArrayList<Particle> particles = new ArrayList<>();
    private boolean walking = false;
    private boolean dead = false;
    private int screenW, screenH;
    private int playerW = 20, playerH = 28;
    private int bobFrame = 0;

    // Touch / Controls
    public boolean moveLeft, moveRight, moveUp, moveDown;
    public boolean btnBreak, btnPlace, btnJump;
    public float touchX = -1, touchY = -1;
    private boolean touchActive = false;
    private long breakStartTime = 0;
    private boolean isLongPress = false;

    private Paint paint, textPaint;

    public MiniCraftGame(Context ctx) {
        paint = new Paint();
        paint.setAntiAlias(false);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
    }

    public void init(int w, int h) {
        screenW = w;
        screenH = h;
        genWorld(System.currentTimeMillis());
        // Spawn on ground
        int gx = (int)(px / TILE);
        int ground = getGround(gx);
        py = (ground - 1) * TILE;
        camSmoothX = px - screenW/2;
        camSmoothY = py - screenH/2;
        // Give some starting blocks
        addToInv(DIRT, 64);
        addToInv(STONE, 32);
        addToInv(OAK_PLANKS, 32);
    }

    // ==================== WORLD GENERATION ====================
    public void genWorld(long seed) {
        world = new int[W][H];
        bgWorld = new int[W][H];
        Random r = new Random(seed);
        
        for (int x = 0; x < W; x++) {
            int surface = H/2 + r.nextInt(8) - 4;
            if (x > W/3 && x < W*2/3) surface -= r.nextInt(6);
            
            for (int y = surface; y < H; y++) {
                int b;
                if (y == surface) b = GRASS;
                else if (y < surface + 3 + r.nextInt(3)) b = DIRT;
                else if (y < H - 5) {
                    int rn = r.nextInt(20);
                    if (rn < 2) b = COAL_ORE;
                    else if (rn == 3) b = IRON_ORE;
                    else if (rn == 4) b = GOLD_ORE;
                    else b = STONE;
                }
                else b = BEDROCK;
                world[x][y] = b;
            }
            
            // Trees
            if (r.nextInt(8) == 0 && x > 3 && x < W-4 && world[x][surface-1] == AIR) {
                int log = OAK_LOG + r.nextInt(6);
                int th = 4 + r.nextInt(5);
                for (int ty = surface-1; ty > surface-th-1 && ty >= 0; ty--) {
                    world[x][ty] = log;
                    // Shelf mushrooms on bg
                    if (r.nextInt(3) == 0) {
                        int sx = x + (r.nextBoolean() ? 1 : -1);
                        if (sx >= 0 && sx < W && bgWorld[sx][ty] == 0)
                            bgWorld[sx][ty] = SHELF_MUSHROOM;
                    }
                }
                // Leaves canopy
                int crownY = surface - th/2;
                int crownR = th/2 + 1;
                for (int lx = x-crownR; lx <= x+crownR; lx++) {
                    for (int ly = crownY-crownR; ly <= surface-1; ly++) {
                        if (lx >= 0 && lx < W && ly >= 0 && ly < H && world[lx][ly] == AIR) {
                            double dist = Math.sqrt((lx-x)*(lx-x) + (ly-crownY)*(ly-crownY));
                            if (dist < crownR && r.nextInt(5) > 0)
                                world[lx][ly] = OAK_LEAVES + r.nextInt(6);
                        }
                    }
                }
            }
            
            // Caves
            if (r.nextInt(3) == 0) {
                int cx = x;
                int cy = surface + 5 + r.nextInt(10);
                if (cy < H-5) {
                    world[cx][cy] = AIR;
                    if (cx+1 < W) world[cx+1][cy] = AIR;
                    if (cy+1 < H) world[cx][cy+1] = AIR;
                }
            }
        }
    }

    private int getGround(int x) {
        if (x < 0 || x >= W) return H-1;
        for (int y = 0; y < H; y++) if (world[x][y] > 0) return y;
        return H-1;
    }

    // ==================== TICK ====================
    public void tick() {
        if (dead) return;
        
        double speed = survival ? 3.0 : 5.5;
        double dx = 0;
        
        if (moveLeft) dx -= speed;
        if (moveRight) dx += speed;
        
        boolean jumping = moveUp || btnJump;
        
        walking = dx != 0;
        if (walking) bobFrame++;
        
        // Physics
        if (!survival) {
            // Creative - free fly
            double dy = 0;
            if (jumping) dy -= speed;
            if (moveDown) dy += speed;
            px += dx;
            py += dy;
        } else {
            // Survival with gravity
            px += dx;
            
            // Jump
            if (jumping && playerVy == 0) {
                int gx = (int)(px / TILE);
                int gy = (int)((py + playerH) / TILE);
                if (gy + 1 < H && isSolid(gx, gy + 1)) {
                    playerVy = -9;
                }
            }
            
            // Gravity
            playerVy += 0.35;
            double nextY = py + playerVy;
            int nextTileY = (int)(nextY / TILE);
            int pTileX = (int)(px / TILE);
            
            // Floor collision
            if (playerVy > 0 && nextTileY + 1 < H && isSolid(pTileX, nextTileY + 1)) {
                py = nextTileY * TILE;
                playerVy = 0;
            } else if (playerVy > 0 && nextTileY + 1 < H && pTileX + 1 < W && isSolid(pTileX + 1, nextTileY + 1) && (px % TILE) > TILE/2) {
                py = nextTileY * TILE;
                playerVy = 0;
            } else {
                py = nextY;
            }
            
            // Ceiling collision
            if (playerVy < 0 && nextTileY >= 0 && isSolid(pTileX, nextTileY)) {
                py = (nextTileY + 1) * TILE;
                playerVy = 0;
            }
            
            // Wall collision
            int nextTileX = (int)((px + dx) / TILE);
            if (dx > 0 && nextTileX + 1 < W && isSolid(nextTileX + 1, (int)(py/TILE))) {
                px = nextTileX * TILE;
            } else if (dx < 0 && nextTileX > 0 && isSolid(nextTileX, (int)(py/TILE))) {
                px = (nextTileX + 1) * TILE;
            }
        }
        
        // Bounds
        px = Math.max(0, Math.min(W*TILE - playerW, px));
        py = Math.max(0, Math.min(H*TILE - playerH, py));
        
        // Camera follow
        int targetX = (int)px - screenW/2;
        int targetY = (int)py - screenH/2;
        camSmoothX += (targetX - camSmoothX) * 0.15;
        camSmoothY += (targetY - camSmoothY) * 0.15;
        camX = (int)camSmoothX;
        camY = (int)camSmoothY;
        
        // Block breaking
        if (btnBreak && touchX >= 0) {
            int tx = (int)((touchX + camX) / TILE);
            int ty = (int)((touchY + camY) / TILE);
            if (isIn(tx, ty) && world[tx][ty] > 0) {
                if (breakX != tx || breakY != ty) {
                    breakX = tx; breakY = ty; breakTimer = 0;
                    breakTime = Math.max(3, (world[tx][ty] * 2) % 15 + 2);
                    if (survival) breakTime *= 3;
                }
                breakTimer++;
                if (breakTimer >= breakTime) {
                    int bk = world[tx][ty];
                    world[tx][ty] = AIR;
                    breakX = -1; breakY = -1; breakTimer = 0;
                    blocksBroken++;
                    addToInv(bk, 1);
                    for (int i = 0; i < 5; i++)
                        particles.add(new Particle(tx*TILE+TILE/2, ty*TILE+TILE/2, bk));
                }
            }
        } else {
            breakX = -1; breakY = -1; breakTimer = 0;
        }
        
        // Block placing
        if (btnPlace && touchX >= 0) {
            int tx = (int)((touchX + camX) / TILE);
            int ty = (int)((touchY + camY) / TILE);
            if (isIn(tx, ty) && world[tx][ty] == AIR && selBlock > 0) {
                if (survival && getInvCount(selBlock) > 0) {
                    world[tx][ty] = selBlock;
                    takeFromInv(selBlock, 1);
                    blocksPlaced++;
                    for (int i = 0; i < 3; i++)
                        particles.add(new Particle(tx*TILE+TILE/2, ty*TILE+TILE/2, selBlock));
                } else if (!survival) {
                    world[tx][ty] = selBlock;
                    blocksPlaced++;
                    for (int i = 0; i < 3; i++)
                        particles.add(new Particle(tx*TILE+TILE/2, ty*TILE+TILE/2, selBlock));
                }
            }
            btnPlace = false;
        }
        
        // Particles
        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            p.x += p.vx; p.y += p.vy; p.vy += 0.25; p.life--;
            if (p.life <= 0) { particles.remove(i); i--; }
        }
        
        // Fire
        if (rand.nextInt(10) == 0) {
            for (int fx = 0; fx < W; fx++) {
                for (int fy = 0; fy < H; fy++) {
                    if (world[fx][fy] == FIRE || world[fx][fy] == SOUL_FIRE) {
                        if (Math.abs(px - (fx*TILE+TILE/2)) < 20 && Math.abs(py - (fy*TILE+TILE/2)) < 20) {
                            if (survival) { health--; if (health <= 0) dead = true; }
                        }
                        if (rand.nextInt(12) == 0) {
                            int nx = fx + rand.nextInt(3) - 1;
                            int ny = fy + rand.nextInt(3) - 1;
                            if (isIn(nx, ny) && world[nx][ny] == AIR) {
                                int below = ny+1 < H ? world[nx][ny+1] : 0;
                                if (below > 0 && below != BEDROCK && below != WATER)
                                    world[nx][ny] = world[fx][fy];
                            }
                        }
                        if (rand.nextInt(80) == 0 || (fy > 0 && world[fx][fy-1] == WATER))
                            world[fx][fy] = AIR;
                    }
                }
            }
        }
    }

    // ==================== RENDER ====================
    public void render(Canvas c) {
        // Sky gradient
        c.drawColor(0xFF87CEEB);
        
        int sx = Math.max(0, camX/TILE - 1);
        int sy = Math.max(0, camY/TILE - 1);
        int ex = Math.min(W, sx + screenW/TILE + 3);
        int ey = Math.min(H, sy + screenH/TILE + 3);
        
        // Background parallax
        if (bgWorld != null) {
            int bgOffX = (int)(camX * 0.4);
            int bgOffY = (int)(camY * 0.2);
            for (int x = sx; x < ex; x++) {
                for (int y = sy; y < ey; y++) {
                    if (bgWorld[x][y] > 0) {
                        int col = COLORS[bgWorld[x][y]];
                        paint.setColor((col & 0x00FFFFFF) | 0x50000000);
                        int bx = x*TILE - bgOffX;
                        int by = y*TILE - bgOffY;
                        c.drawRect(bx, by, bx+TILE, by+TILE, paint);
                    }
                }
            }
        }
        
        // World blocks
        for (int x = sx; x < ex; x++) {
            for (int y = sy; y < ey; y++) {
                int b = world[x][y];
                if (b > 0) {
                    int drawX = x*TILE - camX;
                    int drawY = y*TILE - camY;
                    
                    // Main block
                    paint.setColor(COLORS[b]);
                    c.drawRect(drawX, drawY, drawX+TILE, drawY+TILE, paint);
                    
                    // Highlight top
                    paint.setColor(0x33FFFFFF);
                    c.drawRect(drawX, drawY, drawX+TILE, drawY+3, paint);
                    // Shadow bottom
                    paint.setColor(0x33000000);
                    c.drawRect(drawX, drawY+TILE-3, drawX+TILE, drawY+TILE, paint);
                }
            }
        }
        
        // Player
        int pxOff = (int)px - camX;
        int pyOff = (int)py - camY;
        int bob = walking ? (int)(Math.sin(bobFrame*0.3)*3) : 0;
        
        // Body
        paint.setColor(0xFF00AAAA);
        c.drawRect(pxOff-8, pyOff-20+bob, pxOff+8, pyOff+bob, paint);
        // Head
        paint.setColor(0xFFFFCC99);
        c.drawRect(pxOff-6, pyOff-28+bob, pxOff+6, pyOff-20+bob, paint);
        // Eyes
        paint.setColor(0xFF000000);
        c.drawRect(pxOff-4, pyOff-26+bob, pxOff-2, pyOff-24+bob, paint);
        c.drawRect(pxOff+2, pyOff-26+bob, pxOff+4, pyOff-24+bob, paint);
        
        // Particles
        for (Particle p : particles) {
            paint.setColor(COLORS[p.block]);
            int pxx = (int)p.x - camX;
            int pyy = (int)p.y - camY;
            c.drawRect(pxx-2, pyy-2, pxx+2, pyy+2, paint);
        }
        
        // Break progress
        if (breakX >= 0 && breakTimer > 0 && breakTime > 0) {
            int bx = breakX*TILE - camX;
            int by = breakY*TILE - camY;
            float prog = (float)breakTimer / breakTime;
            paint.setColor(0xAAFFFFFF);
            int crackW = (int)(TILE * prog);
            c.drawRect(bx, by, bx + crackW, by + 4, paint);
            paint.setColor(0xAA000000);
            c.drawRect(bx + crackW, by, bx + TILE, by + 4, paint);
        }
        
        // Crosshair
        int cx = screenW/2;
        int cy = screenH/2;
        paint.setColor(0x66FFFFFF);
        c.drawRect(cx-10, cy-1, cx+10, cy+1, paint);
        c.drawRect(cx-1, cy-10, cx+1, cy+10, paint);
        
        // Hotbar
        drawHotbar(c);
        
        // Debug
        textPaint.setColor(0xDD000000);
        c.drawRect(4, 4, 210, 100, textPaint);
        textPaint.setColor(0xFFFFFFFF);
        textPaint.setTextSize(20);
        c.drawText("MiniCraft v6.3.0 Mobile", 8, 26, textPaint);
        textPaint.setTextSize(16);
        c.drawText("X:"+(int)(px/TILE)+" Y:"+(int)(py/TILE)+" "+(survival?"SURVIVAL":"CREATIVE"), 8, 48, textPaint);
        c.drawText("HP:"+health+(survival?" Hunger:"+hunger:""), 8, 68, textPaint);
        c.drawText("Blocks: "+blocksBroken+"/"+blocksPlaced, 8, 88, textPaint);
        
        // Block name under crosshair
        int ctx = (int)((cx + camX) / TILE);
        int cty = (int)((cy + camY) / TILE);
        if (isIn(ctx, cty) && world[ctx][cty] > 0) {
            String name = BNAME[Math.min(world[ctx][cty], BLOCK_COUNT-1)];
            int tw = (int)textPaint.measureText(name);
            textPaint.setColor(0xDD000000);
            c.drawRect(cx-tw/2-4, cy+20, cx+tw/2+4, cy+42, textPaint);
            textPaint.setColor(0xFFFFFFFF);
            c.drawText(name, cx-tw/2, cy+38, textPaint);
        }
    }

    private void drawHotbar(Canvas c) {
        int slots = 9;
        int hs = 52;
        int startX = screenW/2 - slots*hs/2;
        int hy = screenH - hs - 8;
        
        for (int i = 0; i < slots; i++) {
            int idx = i + 1;
            int hx = startX + i*hs;
            boolean selected = (idx == selBlock);
            
            // Slot bg
            paint.setColor(selected ? 0xDDFFFFFF : 0xAA000000);
            c.drawRect(hx, hy, hx+hs, hy+hs, paint);
            
            // Selected border
            if (selected) {
                paint.setColor(0xFFFFFFFF);
                c.drawRect(hx, hy, hx+hs, hy+3, paint);
                c.drawRect(hx, hy+hs-3, hx+hs, hy+hs, paint);
            }
            
            // Block color
            if (idx < BLOCK_COUNT) {
                paint.setColor(COLORS[idx]);
                c.drawRect(hx+4, hy+12, hx+hs-4, hy+hs-4, paint);
            }
            
            // Number
            textPaint.setColor(selected ? 0xFF000000 : 0xFFFFFFFF);
            textPaint.setTextSize(14);
            c.drawText(String.valueOf(i+1), hx+4, hy+14, textPaint);
            
            // Count
            if (survival && invCount[idx] > 0) {
                textPaint.setColor(0xFFFFFFFF);
                textPaint.setTextSize(12);
                c.drawText(""+invCount[idx], hx+hs-20, hy+hs-4, textPaint);
            }
        }
        
        // Selected block name
        String selName = BNAME[Math.min(selBlock, BLOCK_COUNT-1)];
        int sw = (int)textPaint.measureText(selName);
        textPaint.setColor(0xDD000000);
        c.drawRect(screenW/2-sw/2-4, hy-22, screenW/2+sw/2+4, hy-4, textPaint);
        textPaint.setColor(0xFFFFFFFF);
        textPaint.setTextSize(16);
        c.drawText(selName, screenW/2-sw/2, hy-8, textPaint);
    }

    // ==================== INPUT ====================
    public void onTouch(MotionEvent e, int action) {
        float x = e.getX();
        float y = e.getY();
        
        // Check if in control zone (bottom 180px)
        boolean inControls = y > screenH - 180;
        
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            if (!inControls) {
                touchX = x;
                touchY = y;
                // Auto break on touch (like holding left click)
                if (action == MotionEvent.ACTION_DOWN) {
                    btnBreak = true;
                }
            }
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            touchX = -1;
            touchY = -1;
            btnBreak = false;
        }
    }

    public void cycleSlot(int delta) {
        selBlock = Math.max(1, Math.min(BLOCK_COUNT-1, selBlock + delta));
    }

    public void setSlot(int slot) {
        if (slot >= 1 && slot < BLOCK_COUNT) selBlock = slot;
    }

    public void toggleMode() {
        survival = !survival;
    }

    // Inventory helpers
    private void addToInv(int block, int count) {
        for (int i = 0; i < inv.length; i++) {
            if (inv[i] == block) { invCount[i] += count; return; }
        }
        for (int i = 0; i < inv.length; i++) {
            if (inv[i] == 0) { inv[i] = block; invCount[i] = count; return; }
        }
    }

    private boolean takeFromInv(int block, int count) {
        for (int i = 0; i < inv.length; i++) {
            if (inv[i] == block && invCount[i] >= count) {
                invCount[i] -= count;
                if (invCount[i] <= 0) inv[i] = 0;
                return true;
            }
        }
        return false;
    }

    private int getInvCount(int block) {
        for (int i = 0; i < inv.length; i++) if (inv[i] == block) return invCount[i];
        return 0;
    }

    private boolean isSolid(int x, int y) {
        if (x < 0 || x >= W || y < 0 || y >= H) return true;
        return world[x][y] > 0 && world[x][y] != WATER && world[x][y] != LAVA 
            && world[x][y] != FIRE && world[x][y] != SOUL_FIRE;
    }

    private boolean isIn(int x, int y) {
        return x >= 0 && x < W && y >= 0 && y < H;
    }

    static class Particle {
        double x, y, vx, vy;
        int life, block;
        Particle(double x, double y, int b) {
            this.x = x; this.y = y; block = b;
            life = 8 + (int)(Math.random()*12);
            vx = (Math.random()-0.5)*4;
            vy = -Math.random()*5-2;
        }
    }
}
