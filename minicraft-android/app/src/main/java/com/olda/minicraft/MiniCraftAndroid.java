package com.olda.minicraft;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * MiniCraft Android v6.3.1
 * Ported from /var/home/olda/minicraft/src/MiniCraft.java
 * All game logic, world gen, physics, and block IDs match desktop exactly.
 */
public class MiniCraftAndroid {

    // ==================== EXACT CONSTANTS FROM DESKTOP ====================
    public static final int TILE = 32, W = 128, H = 64;
    public static final int BLOCK_COUNT = 150;
    public static final int AIR = 0, GRASS = 1, DIRT = 2, STONE = 3, COBBLESTONE = 4, BEDROCK = 5, SAND = 6, GRAVEL = 7;
    public static final int OAK_LOG = 8, SPRUCE_LOG = 9, BIRCH_LOG = 10, JUNGLE_LOG = 11, ACACIA_LOG = 12, DARK_OAK_LOG = 13;
    public static final int OAK_PLANKS = 14, SPRUCE_PLANKS = 15, BIRCH_PLANKS = 16, JUNGLE_PLANKS = 17, ACACIA_PLANKS = 18, DARK_OAK_PLANKS = 19;
    public static final int OAK_LEAVES = 20, SPRUCE_LEAVES = 21, BIRCH_LEAVES = 22, JUNGLE_LEAVES = 23, ACACIA_LEAVES = 24, DARK_OAK_LEAVES = 25;
    public static final int WATER = 26, LAVA = 27, ICE = 28, BRICKS = 29, STONE_BRICKS = 30, MOSSY_STONE_BRICKS = 31, CRACKED_STONE_BRICKS = 32;
    public static final int COAL_ORE = 33, IRON_ORE = 34, GOLD_ORE = 35, DIAMOND_ORE = 36, EMERALD_ORE = 37, REDSTONE_ORE = 38, LAPIS_ORE = 39, COPPER_ORE = 40;
    public static final int NETHERRACK = 41, SOUL_SAND = 42, GLOWSTONE = 43, OBSIDIAN = 44, CRYING_OBSIDIAN = 45;
    public static final int SMOOTH_STONE = 46, POLISHED_ANDESITE = 47, POLISHED_DIORITE = 48, POLISHED_GRANITE = 49;
    public static final int PRISMARINE = 50, DARK_PRISMARINE = 51, SEA_LANTERN = 52, END_STONE = 53, PURPUR = 54, MAGMA = 55, SLIME = 56, TNT = 57, FURNACE = 58, CRAFTING_TABLE = 59;
    public static final int RAW_BEEF = 60, COOKED_BEEF = 61, RAW_PORK = 62, COOKED_PORK = 63, WOOL = 64;
    public static final int IRON_INGOT = 65, GOLD_INGOT = 66, DIAMOND_GEM = 67, BED = 68, EXP_ORB = 69;
    public static final int SWORD = 70, PICKAXE = 71, AXE = 72, SHOVEL = 73, FURNACE_ITEM = 74, BOW = 75, ARROW_ITEM = 76, CHEST = 77, SPIDER_EYE = 78, BONE = 79;
    public static final int STICK = 80, FLINT_STEEL = 81, ROD = 82, RAW_FISH = 83, COOKED_FISH = 84, SADDLE = 85, LEATHER_ITEM = 86, TORCH_ITEM = 87, TOTEM = 88, ELYTRA = 89;
    public static final int FIRE = 90, SOUL_FIRE = 91, STRAW_BED = 92, CUSHION = 93, SHELF_MUSHROOM = 94, SULFUR_CUBE = 95;
    public static final int NETHERITE_BLOCK = 96, ANCIENT_DEBRIS = 97, BLACKSTONE = 98, GILDED_BLACKSTONE = 99, BASALT = 100, POLISHED_BASALT = 101;
    public static final int CRIMSON_STEM = 102, CRIMSON_PLANKS = 103, WARPED_STEM = 104, WARPED_PLANKS = 105, SHROOMLIGHT = 106, LODESTONE = 107;
    public static final int RESPAWN_ANCHOR = 108, SOUL_SOIL = 109, TARGET = 110, HONEY_BLOCK = 111, DEEPSLATE = 112, DEEPSLATE_DIAMOND_ORE = 113;
    public static final int AMETHYST_BLOCK = 114, BUDDING_AMETHYST = 115, SCULK = 116, MOSS_BLOCK = 117, AZALEA_LEAVES = 118, FLOWERING_AZALEA = 119;
    public static final int ROOTED_DIRT = 120, MUD = 121, MUD_BRICKS = 122, MANGROVE_LOG = 123, MANGROVE_LEAVES = 124, MANGROVE_PLANKS = 125;
    public static final int CHERRY_LOG = 126, CHERRY_LEAVES = 127, CHERRY_PLANKS = 128, BAMBOO_BLOCK = 129, BAMBOO_PLANKS = 130, BAMBOO_MOSAIC = 131;
    public static final int COPPER_BLOCK = 132, EXPOSED_COPPER = 133, WEATHERED_COPPER = 134, OXIDIZED_COPPER = 135, CUT_COPPER = 136, WAXED_COPPER = 137;
    public static final int RAW_IRON_BLOCK = 138, RAW_GOLD_BLOCK = 139, RAW_COPPER_BLOCK = 140, LANTERN = 141, SOUL_LANTERN = 142, CAMPFIRE = 143;
    public static final int SOUL_CAMPFIRE = 144, CHAIN = 145, SMITHING_TABLE = 146, BARREL = 147, SMOKER = 148, BLAST_FURNACE = 149;

    public static final String[] BNAME = {"Air", "Grass", "Dirt", "Stone", "Cobblestone", "Bedrock", "Sand", "Gravel", "Oak Log", "Spruce Log", "Birch Log", "Jungle Log", "Acacia Log", "Dark Oak Log", "Oak Planks", "Spruce Planks", "Birch Planks", "Jungle Planks", "Acacia Planks", "Dark Oak Planks", "Oak Leaves", "Spruce Leaves", "Birch Leaves", "Jungle Leaves", "Acacia Leaves", "Dark Oak Leaves", "Water", "Lava", "Ice", "Bricks", "Stone Bricks", "Mossy Stone Bricks", "Cracked Stone Bricks", "Coal Ore", "Iron Ore", "Gold Ore", "Diamond Ore", "Emerald Ore", "Redstone Ore", "Lapis Ore", "Copper Ore", "Netherrack", "Soul Sand", "Glowstone", "Obsidian", "Crying Obsidian", "Smooth Stone", "Polished Andesite", "Polished Diorite", "Polished Granite", "Prismarine", "Dark Prismarine", "Sea Lantern", "End Stone", "Purpur Block", "Magma", "Slime Block", "TNT", "Furnace", "Crafting Table", "Raw Beef", "Cooked Beef", "Raw Pork", "Cooked Pork", "Wool", "Iron Ingot", "Gold Ingot", "Diamond", "Bed", "XP Orb", "Sword", "Pickaxe", "Axe", "Shovel", "Furnace Item", "Bow", "Arrow", "Chest", "Spider Eye", "Bone", "Stick", "FlintSteel", "FishRod", "RawFish", "CookFish", "Saddle", "Leather", "Torch", "Totem", "Elytra", "Fire", "Soul Fire", "Straw Bed", "Cushion", "Shelf Mushroom", "Sulfur Cube", "Netherite Block", "Ancient Debris", "Blackstone", "Gilded Blackstone", "Basalt", "Polished Basalt", "Crimson Stem", "Crimson Planks", "Warped Stem", "Warped Planks", "Shroomlight", "Lodestone", "Respawn Anchor", "Soul Soil", "Target", "Honey Block", "Deepslate", "Deepslate Diamond Ore", "Amethyst Block", "Budding Amethyst", "Sculk", "Moss Block", "Azalea Leaves", "Flowering Azalea", "Rooted Dirt", "Mud", "Mud Bricks", "Mangrove Log", "Mangrove Leaves", "Mangrove Planks", "Cherry Log", "Cherry Leaves", "Cherry Planks", "Bamboo Block", "Bamboo Planks", "Bamboo Mosaic", "Copper Block", "Exposed Copper", "Weathered Copper", "Oxidized Copper", "Cut Copper", "Waxed Copper", "Raw Iron Block", "Raw Gold Block", "Raw Copper Block", "Lantern", "Soul Lantern", "Campfire", "Soul Campfire", "Chain", "Smithing Table", "Barrel", "Smoker", "Blast Furnace"};

    // EXACT colors from desktop FB array
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

    // EXACT break times from desktop BT array
    private static final int[] BT = new int[BLOCK_COUNT];
    static {
        for (int i = 0; i < BLOCK_COUNT; i++) {
            if (i == BEDROCK) BT[i] = 99999;
            else if (i == WATER || i == FIRE || i == SOUL_FIRE) BT[i] = 1;
            else if (i >= COAL_ORE && i <= COPPER_ORE) BT[i] = 20;
            else if (i == DEEPSLATE_DIAMOND_ORE) BT[i] = 25;
            else if (i == OBSIDIAN || i == CRYING_OBSIDIAN || i == NETHERITE_BLOCK || i == ANCIENT_DEBRIS) BT[i] = 120;
            else if (i == DEEPSLATE || i == BLACKSTONE || i == BASALT || i == LODESTONE) BT[i] = 35;
            else if (i == AMETHYST_BLOCK || i == BUDDING_AMETHYST) BT[i] = 15;
            else if (i == SCULK) BT[i] = 8;
            else if (i == TARGET) BT[i] = 10;
            else if (i == HONEY_BLOCK) BT[i] = 5;
            else if (i == COPPER_BLOCK || i == EXPOSED_COPPER || i == WEATHERED_COPPER || i == OXIDIZED_COPPER || i == CUT_COPPER || i == WAXED_COPPER) BT[i] = 18;
            else if (i == RAW_IRON_BLOCK || i == RAW_GOLD_BLOCK || i == RAW_COPPER_BLOCK) BT[i] = 25;
            else if (i == CAMPFIRE || i == SOUL_CAMPFIRE || i == LANTERN || i == SOUL_LANTERN || i == CHAIN) BT[i] = 8;
            else BT[i] = Math.max(3, i * 2 % 15 + 2);
        }
    }

    // ==================== GAME STATE (copied from desktop) ====================
    private int[][] world, bgWorld;
    private double px, py;
    private String worldName = "", playerName = "Steve";
    private long worldSeed = 0;
    private int selBlock = GRASS;
    private boolean[] keys = new boolean[1024];
    private int camX, camY;
    private int mx = -999, my = -999;
    private boolean mouseIn = false;
    private int health = 20, hunger = 20, xp = 0, armor = 0, kills = 0;
    private boolean inNether = false;
    private boolean dead = false;
    private int fallDist = 0, breakTimer = 0, breakX = -1, breakY = -1, breakTime = 0;
    private boolean craftingOpen = false, survival = true;
    private int hungerTimer = 0;
    private int[] inv = new int[BLOCK_COUNT], invCount = new int[BLOCK_COUNT];
    private int[] craftGrid = new int[4], craftCount = new int[4];
    private int selInv = -1;
    private Random rand = new Random();

    class Particle {
        double x, y, vx, vy;
        int life, maxLife, block;
        Particle(double x, double y, int b) {
            this.x = x; this.y = y; block = b;
            life = maxLife = 8 + (int)(Math.random() * 12);
            vx = (Math.random() - 0.5) * 4;
            vy = -Math.random() * 5 - 2;
        }
    }

    class DropItem {
        double x, y, vy;
        int block, life;
        DropItem(double x, double y, int b) {
            this.x = x; this.y = y; block = b; life = 600; vy = -2;
        }
    }

    private ArrayList<Particle> particles = new ArrayList<>();
    private ArrayList<DropItem> drops = new ArrayList<>();
    private int bobFrame = 0;
    private boolean walking = false;
    private double playerVy = 0;
    private long worldTime = 12000;
    private int[] mountains;
    private int blocksBroken = 0, blocksPlaced = 0;
    private double camSmoothX = 0, camSmoothY = 0;
    private long sessionStart = 0;

    // ==================== NETWORKING ====================
    private boolean isHost = false;
    private int serverPort = 25565;
    private String serverIP = "localhost";
    private int clientPort = 0;
    private MiniServer server = null;
    private MiniClient client = null;
    private ArrayList<RemotePlayer> remotePlayers = new ArrayList<>();
    private int posTime = 0;

    // Android specific
    private int screenW, screenH;
    private Paint paint, textPaint;
    private boolean moveLeft, moveRight, moveUp, moveDown, btnJump, btnPlace, btnBreakMode;
    private float touchX = -1, touchY = -1;

    public MiniCraftAndroid(Context ctx) {
        paint = new Paint();
        paint.setAntiAlias(false);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
    }

    public void init(int w, int h) {
        screenW = w;
        screenH = h;
        genWorld(System.currentTimeMillis());
        sessionStart = System.currentTimeMillis();
    }

    // ==================== WORLD GEN (EXACT FROM DESKTOP) ====================
    public void genWorld(long seed) {
        world = new int[W][H];
        bgWorld = new int[W][H];
        Random r = new Random(seed);
        worldSeed = seed;

        mountains = new int[W];
        for (int x = 0; x < W; x++) {
            int s = H / 2 + r.nextInt(8) - 4;
            if (x > W / 3 && x < W * 2 / 3) s -= r.nextInt(6);
            mountains[x] = s;

            for (int y = s; y < H; y++) {
                int b;
                if (y == s) b = GRASS;
                else if (y < s + 3 + r.nextInt(3)) b = DIRT;
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
            if (r.nextInt(8) == 0 && x > 3 && x < W - 4 && world[x][s - 1] == AIR) {
                int log = OAK_LOG + r.nextInt(6);
                int th = 4 + r.nextInt(5);
                boolean wide = th > 6;
                for (int ty = s - 1; ty > s - th - 1 && ty >= 0; ty--) {
                    world[x][ty] = log;
                    if (bgWorld != null && r.nextInt(3) == 0) {
                        int sx = x + (r.nextBoolean() ? 1 : -1);
                        if (sx >= 0 && sx < W && bgWorld[sx][ty] == 0)
                            bgWorld[sx][ty] = SHELF_MUSHROOM;
                    }
                }
                if (wide && x + 1 < W)
                    for (int ty = s - 1; ty > s - th / 2 - 1 && ty >= 0; ty--) world[x + 1][ty] = log;

                // Leaves
                int crownY = s - th / 2;
                int crownR = th / 2 + 1;
                for (int lx = x - crownR - 1; lx <= x + crownR + (wide ? 2 : 1); lx++) {
                    for (int ly = crownY - crownR; ly <= s - 1; ly++) {
                        if (lx >= 0 && lx < W && ly >= 0 && ly < H && world[lx][ly] == AIR) {
                            double dist = Math.sqrt((lx - x) * (lx - x) + (ly - crownY) * (ly - crownY));
                            if (dist < crownR && r.nextInt(5) > 0)
                                world[lx][ly] = OAK_LEAVES + r.nextInt(6);
                        }
                    }
                }
            }

            // Caves
            if (r.nextInt(3) == 0) {
                int cx = x;
                int cy = s + 5 + r.nextInt(10);
                if (cy < H - 5) {
                    world[cx][cy] = AIR;
                    if (cx + 1 < W) world[cx + 1][cy] = AIR;
                    if (cy + 1 < H) world[cx][cy + 1] = AIR;
                }
            }
        }

        px = W * TILE / 2.0;
        py = getGround((int) (px / TILE)) * TILE - TILE;
        camSmoothX = px - screenW / 2.0;
        camSmoothY = py - screenH / 2.0;
    }

    private int getHeight(int x) {
        for (int y = 0; y < H; y++) if (world[x][y] > 0) return y;
        return H - 1;
    }

    private int getGround(int x) {
        if (x < 0 || x >= W) return H - 1;
        for (int y = 0; y < H; y++) if (world[x][y] > 0) return y;
        return H - 1;
    }

    // ==================== TICK (EXACT FROM DESKTOP) ====================
    public void tick() {
        if (dead) return;

        double speed = survival ? 3.0 : 5.5;
        if (moveDown && survival) speed *= 0.5;
        double dx = 0, dy = 0;

        if (moveLeft) dx -= speed;
        if (moveRight) dx += speed;
        if ((moveUp || btnJump)) {
            if (!survival) dy -= speed;
            else if (playerVy == 0) {
                boolean jg = false;
                for (int gx = -14; gx <= 14; gx += 8) {
                    int fx = (int) ((px + gx) / TILE), fy = (int) ((py + 16) / TILE);
                    if (isSolid(fx, fy)) { jg = true; break; }
                }
                if (jg) playerVy = -9;
            }
        }
        if (moveDown && !survival) dy += speed;

        boolean moving = dx != 0 || dy != 0;
        walking = moving;
        if (moving) {
            bobFrame++;
            if (!true) bobFrame += 2; // ultraFps check removed
        }
        if (moving) hungerTimer++;

        double nx = px + dx, ny = py + dy;
        if (!survival || noclip) {
            // Creative fly
            px = nx;
            py = ny;
        } else {
            // Survival physics
            int x1 = (int) ((nx - 10) / TILE), x2 = (int) ((nx + 10) / TILE);
            int yt = (int) ((py - 14) / TILE), yb = (int) ((py + 14) / TILE);
            boolean canX = true;
            for (int tx = x1; tx <= x2; tx++)
                for (int ty = yt; ty <= yb; ty++)
                    if (isSolid(tx, ty)) canX = false;
            if (canX) px = nx;

            int xl = (int) ((px - 10) / TILE), xr = (int) ((px + 10) / TILE);
            int y1 = (int) ((ny - 14) / TILE), y2 = (int) ((ny + 14) / TILE);
            boolean canY = true;
            for (int tx = xl; tx <= xr; tx++)
                for (int ty = y1; ty <= y2; ty++)
                    if (isSolid(tx, ty)) canY = false;
            if (canY) py = ny;

            // Gravity
            boolean og = false;
            for (int gx = -14; gx <= 14; gx += 8) {
                int fx = (int) ((px + gx) / TILE), fy = (int) ((py + 16) / TILE);
                if (isSolid(fx, fy)) { og = true; break; }
            }
            if (survival && !og && !noclip) {
                playerVy += 0.35;
                py += playerVy;
                fallDist++;
            } else if (survival && !noclip) {
                if (playerVy > 18) {
                    health -= (int) (playerVy - 18) / 10;
                    if (health <= 0) die();
                }
                playerVy = 0;
                fallDist = 0;
            }

            // Hunger
            if (survival && moving && hungerTimer % 30 == 0 && hunger > 0) hunger--;
            if (survival && hunger > 17 && health < 20 && hungerTimer % 10 == 0) health++;
        }

        px = Math.max(0, Math.min(W * TILE - TILE, px));
        py = Math.max(0, Math.min(H * TILE - TILE, py));

        // Camera
        int targetX = (int) px - screenW / 2;
        int targetY = (int) py - screenH / 2;
        camSmoothX += (targetX - camSmoothX) * 0.15;
        camSmoothY += (targetY - camSmoothY) * 0.15;
        camX = (int) camSmoothX;
        camY = (int) camSmoothY;

        // Particles
        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            p.x += p.vx;
            p.y += p.vy;
            p.vy += 0.25;
            p.life--;
            if (p.life <= 0) { particles.remove(i); i--; }
        }

        // Drops physics
        for (int i = 0; i < drops.size(); i++) {
            DropItem d = drops.get(i);
            d.y += d.vy;
            d.vy += 0.1;
            d.life--;
            if (Math.abs(d.x - px) < 24 && Math.abs(d.y - py) < 24) {
                if (d.block == EXP_ORB) xp++;
                else addToInv(d.block, 1);
                drops.remove(i);
                i--;
            } else if (d.life <= 0) {
                drops.remove(i);
                i--;
            }
        }

        // Block breaking (touch)
        if (btnBreakMode && touchX >= 0) {
            int tx = (int) ((touchX + camX) / TILE);
            int ty = (int) ((touchY + camY) / TILE);
            if (isIn(tx, ty) && world[tx][ty] > 0) {
                if (breakX != tx || breakY != ty) {
                    breakX = tx;
                    breakY = ty;
                    breakTimer = 0;
                    int spd = 1;
                    breakTime = Math.max(1, BT[Math.min(world[tx][ty], BT.length - 1)] / spd);
                }
                breakTimer++;
                if (breakTimer >= breakTime) {
                    int bk = world[tx][ty];
                    world[tx][ty] = AIR;
                    breakX = -1;
                    breakY = -1;
                    breakTimer = 0;
                    blocksBroken++;
                    addToInv(bk, 1);
                    spawnParticles(tx, ty, bk);
                }
            }
        } else if (breakX >= 0) {
            breakX = -1;
            breakY = -1;
            breakTimer = 0;
        }

        // Block placing
        if (btnPlace && touchX >= 0) {
            int tx = (int) ((touchX + camX) / TILE);
            int ty = (int) ((touchY + camY) / TILE);
            if (isIn(tx, ty) && selBlock >= 0) {
                if (survival && getInvCount(selBlock) > 0 && world[tx][ty] == AIR) {
                    if (selBlock <= CRAFTING_TABLE) {
                        world[tx][ty] = selBlock;
                        takeFromInv(selBlock, 1);
                        blocksPlaced++;
                        spawnParticles(tx, ty, selBlock);
                    }
                } else if (!survival && world[tx][ty] == AIR) {
                    world[tx][ty] = selBlock;
                    blocksPlaced++;
                    spawnParticles(tx, ty, selBlock);
                }
            }
            btnPlace = false;
        }

        // Fire
        if (rand.nextInt(5) == 0) {
            for (int fx = 0; fx < W; fx++) {
                for (int fy = 0; fy < H; fy++) {
                    if (world[fx][fy] == FIRE || world[fx][fy] == SOUL_FIRE) {
                        if (Math.abs(px - (fx * TILE + TILE / 2)) < 20 && Math.abs(py - (fy * TILE + TILE / 2)) < 20) {
                            if (survival) {
                                health -= 1;
                                if (health <= 0) die();
                            }
                        }
                        if (rand.nextInt(8) == 0) {
                            int fnx = fx + rand.nextInt(3) - 1;
                            int fny = fy + rand.nextInt(3) - 1;
                            if (isIn(fnx, fny) && world[fnx][fny] == AIR) {
                                int below = fny + 1 < H ? world[fnx][fny + 1] : 0;
                                if (below > 0 && below != BEDROCK && below != WATER) {
                                    boolean soul = world[fx][fy] == SOUL_FIRE || (below == SOUL_SAND || below == SOUL_SOIL);
                                    world[fnx][fny] = soul ? SOUL_FIRE : FIRE;
                                }
                            }
                        }
                        if (rand.nextInt(60) == 0 || (fy > 0 && world[fx][fy - 1] == WATER)) {
                            world[fx][fy] = AIR;
                        }
                    }
                }
            }
        }

        // Network position sync
        posTime++;
        if (posTime % 10 == 0) {
            if (client != null && client.isConnected()) {
                client.send("P " + (int) px + " " + (int) py);
            }
        }
    }

    private void die() {
        health = 0;
        dead = true;
        deathDrop();
    }

    private void deathDrop() {
        for (int i = 0; i < inv.length; i++) {
            if (inv[i] > 0) {
                drops.add(new DropItem(px, py, inv[i]));
                inv[i] = 0;
                invCount[i] = 0;
            }
        }
    }

    private boolean noclip = false;

    private void spawnParticles(int bx, int by, int block) {
        for (int i = 0; i < 4; i++)
            particles.add(new Particle(bx * TILE + TILE / 2 + Math.random() * TILE / 2 - TILE / 4,
                    by * TILE + TILE / 2 + Math.random() * TILE / 2 - TILE / 4, block));
        drops.add(new DropItem(bx * TILE + TILE / 2, by * TILE + TILE / 2, block));
    }

    private void addToInv(int block, int count) {
        if (block <= 0) return;
        for (int i = 0; i < inv.length; i++)
            if (inv[i] == block) { invCount[i] += count; return; }
        for (int i = 0; i < inv.length; i++)
            if (inv[i] == 0) { inv[i] = block; invCount[i] = count; return; }
    }

    private boolean takeFromInv(int block, int count) {
        for (int i = 0; i < inv.length; i++)
            if (inv[i] == block && invCount[i] >= count) {
                invCount[i] -= count;
                if (invCount[i] <= 0) inv[i] = 0;
                return true;
            }
        return false;
    }

    private int getInvCount(int block) {
        for (int i = 0; i < inv.length; i++) if (inv[i] == block) return invCount[i];
        return 0;
    }

    private boolean isSolid(int x, int y) {
        if (x < 0 || x >= W || y < 0 || y >= H) return true;
        return world[x][y] > 0 && world[x][y] != WATER && world[x][y] != LAVA;
    }

    private boolean isIn(int x, int y) {
        return x >= 0 && x < W && y >= 0 && y < H;
    }

    // ==================== RENDER (matching desktop style) ====================
    public void render(Canvas c) {
        // Sky
        c.drawColor(0xFF87CEEB);

        int sx = Math.max(0, camX / TILE - 1);
        int sy = Math.max(0, camY / TILE - 1);
        int ex = Math.min(W, sx + screenW / TILE + 3);
        int ey = Math.min(H, sy + screenH / TILE + 3);

        // Background
        if (bgWorld != null) {
            int bgOffX = (int) (camX * 0.5), bgOffY = (int) (camY * 0.3);
            int bsx = Math.max(0, (bgOffX) / TILE - 1), bex = Math.min(W, bsx + 25 + 3);
            int bsy = Math.max(0, (bgOffY) / TILE - 1), bey = Math.min(H, bsy + 18 + 3);
            for (int x = bsx; x < bex; x++) {
                for (int y = bsy; y < bey; y++) {
                    if (bgWorld[x][y] > 0) {
                        int alpha = 90;
                        int base = COLORS[Math.min(bgWorld[x][y], COLORS.length - 1)];
                        paint.setColor((base & 0x00FFFFFF) | (alpha << 24));
                        c.drawRect(x * TILE - bgOffX, y * TILE - bgOffY, x * TILE - bgOffX + TILE, y * TILE - bgOffY + TILE, paint);
                    }
                }
            }
        }

        // World
        for (int x = sx; x < ex; x++) {
            for (int y = sy; y < ey; y++) {
                int b = world[x][y];
                if (b > 0) {
                    int drawX = x * TILE - camX;
                    int drawY = y * TILE - camY;
                    paint.setColor(COLORS[Math.min(b, COLORS.length - 1)]);
                    c.drawRect(drawX, drawY, drawX + TILE, drawY + TILE, paint);

                    // Hover highlight
                    if (x == breakX && y == breakY && breakTimer > 0) {
                        float prog = (float) breakTimer / breakTime;
                        paint.setColor(0xAAFFFFFF);
                        c.drawRect(drawX, drawY, drawX + TILE * prog, drawY + 4, paint);
                        paint.setColor(0xAA000000);
                        c.drawRect(drawX + TILE * prog, drawY, drawX + TILE, drawY + 4, paint);
                    }
                }
            }
        }

        // Player
        int pxOff = (int) px - camX;
        int pyOff = (int) py - camY;
        int bob = walking ? (int) (Math.sin(bobFrame * 0.3) * 2) : 0;

        paint.setColor(0xFF00AAAA);
        c.drawRect(pxOff - 10, pyOff - 22 + bob, pxOff + 10, pyOff + bob, paint);
        paint.setColor(0xFFFFCC99);
        c.drawRect(pxOff - 8, pyOff - 30 + bob, pxOff + 8, pyOff - 22 + bob, paint);
        paint.setColor(0xFF000000);
        c.drawRect(pxOff - 4, pyOff - 26 + bob, pxOff - 2, pyOff - 24 + bob, paint);
        c.drawRect(pxOff + 2, pyOff - 26 + bob, pxOff + 4, pyOff - 24 + bob, paint);

        // Remote Players
        synchronized (remotePlayers) {
            for (RemotePlayer rp : remotePlayers) {
                rp.x += (rp.targetX - rp.x) * 0.25;
                rp.y += (rp.targetY - rp.y) * 0.25;
                int rpx = (int) rp.x - camX;
                int rpy = (int) rp.y - camY;
                if (rpx > -20 && rpx < screenW + 20 && rpy > -40 && rpy < screenH + 20) {
                    paint.setColor(0xFFFF5555);
                    c.drawRect(rpx - 8, rpy - 20, rpx + 8, rpy, paint);
                    paint.setColor(0xFFFFCC99);
                    c.drawRect(rpx - 6, rpy - 28, rpx + 6, rpy - 20, paint);
                    // Name tag
                    textPaint.setColor(0xDD000000);
                    int nw = (int) textPaint.measureText(rp.name);
                    c.drawRect(rpx - nw/2 - 2, rpy - 40, rpx + nw/2 + 2, rpy - 26, textPaint);
                    textPaint.setColor(0xFFFFFFFF);
                    textPaint.setTextSize(12);
                    c.drawText(rp.name, rpx - nw/2, rpy - 28, textPaint);
                }
            }
        }

        // Particles
        for (Particle p : particles) {
            paint.setColor(COLORS[Math.min(p.block, COLORS.length - 1)]);
            c.drawRect((int) p.x - camX - 2, (int) p.y - camY - 2, (int) p.x - camX + 2, (int) p.y - camY + 2, paint);
        }

        // Drops
        for (DropItem d : drops) {
            paint.setColor(COLORS[Math.min(d.block, COLORS.length - 1)]);
            c.drawRect((int) d.x - camX - 6, (int) d.y - camY - 6, (int) d.x - camX + 6, (int) d.y - camY + 6, paint);
        }

        // Crosshair
        int cx = screenW / 2, cy = screenH / 2;
        paint.setColor(0x44FFFFFF);
        c.drawRect(cx - 12, cy - 1, cx + 12, cy + 1, paint);
        c.drawRect(cx - 1, cy - 12, cx + 1, cy + 12, paint);

        // Hotbar
        drawHotbar(c);

        // Debug
        int netH = isHost || isConnected() ? 125 : 105;
        textPaint.setColor(0xDD000000);
        c.drawRect(4, 4, 240, netH, textPaint);
        textPaint.setColor(0xFFFFFFFF);
        textPaint.setTextSize(20);
        c.drawText("MiniCraft v6.3.2 Mobile", 8, 26, textPaint);
        textPaint.setTextSize(16);
        c.drawText("X:" + (int) (px / TILE) + " Y:" + (int) (py / TILE) + " " + (survival ? "SURVIVAL" : "CREATIVE"), 8, 48, textPaint);
        c.drawText("HP:" + health + (survival ? " Hunger:" + hunger : ""), 8, 68, textPaint);
        c.drawText("Blocks: " + blocksBroken + "/" + blocksPlaced, 8, 88, textPaint);
        if (isHost) {
            textPaint.setColor(0xFF55FF55);
            c.drawText("HOST Port:" + serverPort + " P:" + (server != null ? server.getPlayerCount() : 1), 8, 108, textPaint);
        } else if (isConnected()) {
            textPaint.setColor(0xFF55FFFF);
            c.drawText("CLIENT " + serverIP + ":" + serverPort + " R:" + remotePlayers.size(), 8, 108, textPaint);
        }

        // Block name at crosshair
        int ctx = (int) ((cx + camX) / TILE);
        int cty = (int) ((cy + camY) / TILE);
        if (isIn(ctx, cty) && world[ctx][cty] > 0) {
            String name = BNAME[Math.min(world[ctx][cty], BLOCK_COUNT - 1)];
            int tw = (int) textPaint.measureText(name);
            textPaint.setColor(0xDD000000);
            c.drawRect(cx - tw / 2 - 4, cy + 20, cx + tw / 2 + 4, cy + 44, textPaint);
            textPaint.setColor(0xFFFFFFFF);
            textPaint.setTextSize(18);
            c.drawText(name, cx - tw / 2, cy + 40, textPaint);
        }
    }

    private void drawHotbar(Canvas c) {
        int hs = 52, slots = Math.min(9, BLOCK_COUNT);
        int startX = screenW / 2 - slots * hs / 2;
        int hy = screenH - hs - 10;

        for (int i = 0; i < slots; i++) {
            int idx = i + 1;
            int hx = startX + i * hs;
            boolean selected = (i == selBlock - 1);

            paint.setColor(selected ? 0xDDFFFFFF : 0xAA000000);
            c.drawRect(hx, hy, hx + hs, hy + hs, paint);

            if (selected) {
                paint.setColor(0xFFFFFFFF);
                c.drawRect(hx, hy, hx + hs, hy + 3, paint);
            }

            if (idx < BLOCK_COUNT) {
                paint.setColor(COLORS[Math.min(idx, COLORS.length - 1)]);
                c.drawRect(hx + 4, hy + 14, hx + hs - 4, hy + hs - 4, paint);
            }

            textPaint.setColor(selected ? 0xFF000000 : 0xFFFFFFFF);
            textPaint.setTextSize(14);
            c.drawText(String.valueOf(i + 1), hx + 4, hy + 14, textPaint);

            if (survival && invCount[idx] > 1) {
                textPaint.setColor(0xFFFFFFFF);
                textPaint.setTextSize(12);
                c.drawText("" + invCount[idx], hx + hs - 24, hy + hs - 4, textPaint);
            }
        }

        // Selected block name
        String selName = BNAME[Math.min(selBlock, BLOCK_COUNT - 1)];
        int sw = (int) textPaint.measureText(selName);
        textPaint.setColor(0xDD000000);
        c.drawRect(screenW / 2 - sw / 2 - 4, hy - 24, screenW / 2 + sw / 2 + 4, hy - 4, textPaint);
        textPaint.setColor(0xFFFFFFFF);
        textPaint.setTextSize(16);
        c.drawText(selName, screenW / 2 - sw / 2, hy - 8, textPaint);
    }

    // ==================== INPUT ====================
    public void handleTouch(float x, float y, int action) {
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            touchX = x;
            touchY = y;
            btnBreakMode = true;
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            touchX = -1;
            touchY = -1;
            btnBreakMode = false;
        }
    }

    public void setMoveLeft(boolean v) { moveLeft = v; }
    public void setMoveRight(boolean v) { moveRight = v; }
    public void setMoveUp(boolean v) { moveUp = v; }
    public void setMoveDown(boolean v) { moveDown = v; }
    public void setJump(boolean v) { btnJump = v; }
    public void setPlace(boolean v) {
        btnPlace = true;
        // Use crosshair position for placement
        touchX = screenW / 2f;
        touchY = screenH / 2f;
    }
    public void cycleSlot(int delta) {
        selBlock = Math.min(BLOCK_COUNT - 1, Math.max(1, selBlock + delta));
    }
    public void setSlot(int slot) { if (slot >= 1 && slot < BLOCK_COUNT) selBlock = slot; }
    public void toggleMode() { survival = !survival; }
    public void toggleNoclip() { noclip = !noclip; }

    // ==================== NETWORK METHODS ====================
    public void startServer(int port) {
        stopNetworking();
        serverPort = port;
        server = new MiniServer(port);
        isHost = true;
        server.start();
    }

    public void connectToServer(String ip, int port) {
        stopNetworking();
        serverIP = ip;
        serverPort = port;
        clientPort = port;
        client = new MiniClient(ip, port);
        if (client.connect()) {
            // Wait for world sync
        }
    }

    public void stopNetworking() {
        if (server != null) { server.stopServer(); server = null; isHost = false; }
        if (client != null) { client.disconnect(); client = null; }
        remotePlayers.clear();
    }

    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    public boolean isServerRunning() {
        return server != null && server.isRunning();
    }

    private void syncBlock(int x, int y, int block) {
        if (isHost && server != null) server.broadcast("B " + x + " " + y + " " + block);
        if (client != null && client.isConnected()) client.send("B " + x + " " + y + " " + block);
    }

    private void sendPosition() {
        if (client != null && client.isConnected()) {
            client.send("P " + (int)px + " " + (int)py);
        }
    }

    // ==================== REMOTE PLAYER ====================
    class RemotePlayer {
        int id;
        double x, y, targetX, targetY;
        String name;
        RemotePlayer(int i, String n, double sx, double sy) {
            id = i; name = n; x = targetX = sx; y = targetY = sy;
        }
    }

    // ==================== MINI SERVER ====================
    class MiniServer extends Thread {
        private ServerSocket ss;
        private boolean running = false;
        private ArrayList<ClientHandler> clients = new ArrayList<>();
        private int nextId = 1;

        MiniServer(int port) {
            try {
                ss = new ServerSocket(port);
                System.out.println("[Server] listening on port " + port);
            } catch (Exception e) {
                System.err.println("[Server] FAILED on port " + port + ": " + e.getMessage());
            }
        }

        public void run() {
            running = true;
            while (running) {
                try {
                    Socket clientSocket = ss.accept();
                    String clientIp = clientSocket.getInetAddress().getHostAddress();
                    // Add client as voice peer
                    if (voiceChat != null) {
                        voiceChat.addPeer(clientIp);
                    }
                    ClientHandler ch = new ClientHandler(clientSocket);
                    ch.start();
                    synchronized (clients) { clients.add(ch); }
                } catch (Exception e) {
                    if (running) {
                        try { Thread.sleep(100); } catch (Exception ex) {}
                    }
                    break;
                }
            }
        }

        void broadcast(String msg, int skipId) {
            ArrayList<ClientHandler> snapshot;
            synchronized (clients) { snapshot = new ArrayList<>(clients); }
            for (ClientHandler ch : snapshot) {
                if (ch.name != null && ch.id != skipId) {
                    try { ch.send(msg); } catch (Exception e) {}
                }
            }
        }

        void broadcast(String msg) {
            ArrayList<ClientHandler> snapshot;
            synchronized (clients) { snapshot = new ArrayList<>(clients); }
            for (ClientHandler ch : snapshot) {
                if (ch.name != null) {
                    try { ch.send(msg); } catch (Exception e) {}
                }
            }
        }

        int getPlayerCount() { return clients.size() + 1; }
        boolean isRunning() { return ss != null && running; }

        void stopServer() {
            running = false;
            try { if (ss != null) ss.close(); } catch (Exception e) {}
            synchronized (clients) {
                for (ClientHandler ch : clients) ch.interrupt();
                clients.clear();
            }
        }

        void removeClient(ClientHandler ch) {
            synchronized (clients) { clients.remove(ch); }
            broadcast("L " + ch.id + " " + ch.name);
            synchronized (remotePlayers) { 
                remotePlayers.removeIf(rp -> rp.id == ch.id); 
            }
            System.out.println("[Server] -Player id:" + ch.id + " " + ch.name);
        }

        class ClientHandler extends Thread {
            Socket s;
            PrintWriter out;
            BufferedReader in;
            String name = "";
            int x, y, id;

            ClientHandler(Socket s) { this.s = s; }

            public void run() {
                try {
                    out = new PrintWriter(s.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        String[] p = line.split(" ", 4);
                        if (p[0].equals("J")) {
                            id = nextId++;
                            name = p.length > 1 ? p[1] : "Player";
                            // Deduplicate name
                            ArrayList<ClientHandler> snap;
                            synchronized (clients) { snap = new ArrayList<>(clients); }
                            int cnt = 0;
                            for (ClientHandler ch : snap) if (ch != this && ch.name != null && ch.name.equals(name)) cnt++;
                            if (cnt > 0) {
                                String base = name.replaceAll("\\d+$", "");
                                int n = 2;
                                name = base + n;
                                while (true) {
                                    boolean taken = false;
                                    for (ClientHandler ch : snap) if (ch != this && ch.name != null && ch.name.equals(name)) { taken = true; break; }
                                    if (!taken) break;
                                    n++; name = base + n;
                                }
                            }
                            out.println("N " + id + " " + name);
                            
                            // Spawn position
                            int plrIdx = 0;
                            for (ClientHandler ch : snap) if (ch != this && ch.name != null) plrIdx++;
                            double spx = px + (plrIdx % 3 * 48) - 48;
                            double spy = py - (plrIdx * (plrIdx % 3 == 0 ? 64 : 32));
                            int gx = (int)(spx / TILE);
                            if (gx >= 0 && gx < W) {
                                int gy2 = getGround(gx);
                                spy = gy2 * TILE - 14;
                            } else { spx = px; }
                            
                            synchronized (remotePlayers) {
                                remotePlayers.add(new RemotePlayer(id, name, spx, spy));
                            }
                            
                            // Send world
                            out.println("W " + W + " " + H);
                            StringBuilder batch = new StringBuilder();
                            for (int wx = 0; wx < W; wx++) {
                                for (int wy = 0; wy < H; wy++) {
                                    batch.append("D ").append(wx).append(" ").append(wy).append(" ").append(world[wx][wy]).append("\n");
                                    if (batch.length() > 5000) {
                                        out.print(batch.toString());
                                        out.flush();
                                        batch.setLength(0);
                                    }
                                }
                            }
                            if (batch.length() > 0) { out.print(batch.toString()); out.flush(); }
                            out.println("WD");
                            out.flush();
                            out.println("S " + (int)spx + " " + (int)spy);
                            out.println("J 0 " + playerName + " " + (int)px + " " + (int)py);
                            for (ClientHandler ch : clients) {
                                if (ch != this && ch.name != null) {
                                    out.println("J " + ch.id + " " + ch.name + " " + ch.x + " " + ch.y);
                                }
                            }
                            broadcast("J " + id + " " + name + " " + (int)spx + " " + (int)spy, id);
                        }
                        else if (p[0].equals("P") && p.length >= 3) {
                            x = (int)Double.parseDouble(p[1]);
                            y = (int)Double.parseDouble(p[2]);
                            synchronized (remotePlayers) {
                                for (RemotePlayer rp : remotePlayers) {
                                    if (rp.id == id) { rp.targetX = Double.parseDouble(p[1]); rp.targetY = Double.parseDouble(p[2]); break; }
                                }
                            }
                            broadcast("P " + id + " " + p[1] + " " + p[2], id);
                        }
                        else if (p[0].equals("B") && p.length >= 4) {
                            final int bx = Integer.parseInt(p[1]), by = Integer.parseInt(p[2]), bl = Integer.parseInt(p[3]);
                            if (bx >= 0 && bx < W && by >= 0 && by < H) world[bx][by] = bl;
                            broadcast("B " + bx + " " + by + " " + bl, id);
                        }
                    }
                } catch (Exception e) {} finally {
                    try { s.close(); } catch (Exception e) {}
                    if (name != null) removeClient(this);
                }
            }

            void send(String m) {
                try {
                    if (out != null) {
                        out.println(m);
                        if (out.checkError()) {
                            out = null;
                            try { s.close(); } catch (Exception e2) {}
                            removeClient(this);
                        }
                    }
                } catch (Exception e) {
                    out = null;
                    try { s.close(); } catch (Exception e2) {}
                    removeClient(this);
                }
            }
        }
    }

    // ==================== MINI CLIENT ====================
    class MiniClient extends Thread {
        private Socket s;
        private PrintWriter out;
        private BufferedReader in;
        private boolean connected = false, running = false;
        private String host;
        private int port;
        private int[][] tempWorld = null;
        private boolean worldReady = false;
        private Handler uiHandler;

        MiniClient(String h, int p) {
            host = h; port = p;
            uiHandler = new Handler(Looper.getMainLooper());
        }

        boolean connect() {
            try {
                s = new Socket();
                s.connect(new InetSocketAddress(host, port), 4000);
                s.setSoTimeout(4000);
                out = new PrintWriter(s.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                connected = true;
                running = true;
                start();
                out.println("J " + playerName);
                // Add server as voice peer
                if (voiceChat != null) {
                    voiceChat.addPeer(host);
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public void run() {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    String[] p = line.split(" ", 4);
                    if (p[0].equals("W") && p.length >= 3) {
                        int w = Integer.parseInt(p[1]), h2 = Integer.parseInt(p[2]);
                        tempWorld = new int[w][h2];
                        worldReady = false;
                    }
                    else if (p[0].equals("D") && p.length >= 4 && tempWorld != null) {
                        int x = Integer.parseInt(p[1]), y = Integer.parseInt(p[2]), bl = Integer.parseInt(p[3]);
                        if (x >= 0 && x < tempWorld.length && y >= 0 && y < tempWorld[0].length) tempWorld[x][y] = bl;
                    }
                    else if (p[0].equals("S") && p.length >= 3) {
                        final double sx = Double.parseDouble(p[1]), sy = Double.parseDouble(p[2]);
                        uiHandler.post(() -> { px = sx; py = sy; });
                    }
                    else if (p[0].equals("WD") && tempWorld != null) {
                        final int[][] tw = tempWorld;
                        tempWorld = null;
                        worldReady = true;
                        uiHandler.post(() -> { world = tw; });
                    }
                    else if (p[0].equals("P") && p.length >= 4) {
                        final int pid = Integer.parseInt(p[1]);
                        final double sx = Double.parseDouble(p[2]), sy = Double.parseDouble(p[3]);
                        uiHandler.post(() -> {
                            synchronized (remotePlayers) {
                                for (RemotePlayer rp : remotePlayers) if (rp.id == pid) { rp.targetX = sx; rp.targetY = sy; return; }
                                remotePlayers.add(new RemotePlayer(pid, "?", sx, sy));
                            }
                        });
                    }
                    else if (p[0].equals("J") && p.length >= 5) {
                        final int jid = Integer.parseInt(p[1]);
                        final String jname = p[2];
                        final double jx = Double.parseDouble(p[3]), jy = Double.parseDouble(p[4]);
                        uiHandler.post(() -> {
                            synchronized (remotePlayers) {
                                for (RemotePlayer rp : remotePlayers) if (rp.id == jid) { rp.name = jname; rp.targetX = jx; rp.targetY = jy; return; }
                                remotePlayers.add(new RemotePlayer(jid, jname, jx, jy));
                            }
                        });
                    }
                    else if (p[0].equals("L") && p.length >= 2) {
                        final int lid = Integer.parseInt(p[1]);
                        uiHandler.post(() -> {
                            synchronized (remotePlayers) { remotePlayers.removeIf(rp -> rp.id == lid); }
                        });
                    }
                    else if (p[0].equals("B") && p.length >= 4) {
                        final int bx = Integer.parseInt(p[1]), by = Integer.parseInt(p[2]), bl = Integer.parseInt(p[3]);
                        uiHandler.post(() -> { if (world != null && bx >= 0 && bx < world.length && by >= 0 && by < world[0].length) world[bx][by] = bl; });
                    }
                    else if (p[0].equals("N") && p.length >= 3) {
                        if (p.length == 3) { playerName = p[2]; }
                        else if (p.length >= 4) {
                            final int nid = Integer.parseInt(p[1]);
                            final String newN = p[3];
                            uiHandler.post(() -> {
                                synchronized (remotePlayers) {
                                    for (RemotePlayer rp : remotePlayers) if (rp.id == nid) { rp.name = newN; break; }
                                }
                            });
                        }
                    }
                }
            } catch (Exception e) {} finally {
                connected = false;
                try { if (s != null) s.close(); } catch (Exception ex) {}
            }
        }

        void send(String m) {
            try {
                if (out != null) {
                    out.println(m);
                    if (out.checkError()) throw new Exception("broken");
                }
            } catch (Exception e) {
                out = null;
                connected = false;
            }
        }

        void disconnect() {
            running = false;
            try { if (s != null) s.close(); } catch (Exception e) {}
            connected = false;
            out = null;
        }

        boolean isConnected() { return connected; }
    }

    // ==================== VOICE CHAT ====================
    private VoiceChatThread voiceChat;

    public void toggleVoice() {
        if (voiceChat != null) {
            voiceChat.shutdown();
            voiceChat = null;
        } else {
            int vp = (serverPort > 0 ? serverPort : clientPort > 0 ? clientPort : 25565);
            voiceChat = new VoiceChatThread(vp);
            voiceChat.start();
        }
    }

    class VoiceChatThread extends Thread {
        private volatile boolean voiceRunning = false;
        private android.media.AudioRecord mic;
        private android.media.AudioTrack speaker;
        private java.net.DatagramSocket voiceSocket;
        private java.net.DatagramSocket broadcastSocket;
        private ArrayList<java.net.InetAddress> voicePeers = new ArrayList<>();
        private int voicePort = 0;
        private int sampleRate = 8000;
        private int frameSize = 320; // 20ms at 8kHz 16-bit mono = 320 bytes

        VoiceChatThread(int basePort) {
            voicePort = basePort + 1000;
            setDaemon(true);
        }

        public void run() {
            try {
                System.out.println("[Voice] Starting on port " + voicePort);

                // Initialize receive socket
                voiceSocket = new java.net.DatagramSocket(voicePort);
                voiceSocket.setSoTimeout(100);

                // Initialize broadcast socket for peer discovery
                broadcastSocket = new java.net.DatagramSocket();
                broadcastSocket.setBroadcast(true);

                // Initialize AudioRecord with proper buffer
                int minBuf = android.media.AudioRecord.getMinBufferSize(sampleRate,
                    android.media.AudioFormat.CHANNEL_IN_MONO,
                    android.media.AudioFormat.ENCODING_PCM_16BIT);
                if (minBuf < frameSize * 2) minBuf = frameSize * 2;

                mic = new android.media.AudioRecord(
                    android.media.MediaRecorder.AudioSource.VOICE_COMMUNICATION,
                    sampleRate,
                    android.media.AudioFormat.CHANNEL_IN_MONO,
                    android.media.AudioFormat.ENCODING_PCM_16BIT,
                    minBuf);

                if (mic.getState() != android.media.AudioRecord.STATE_INITIALIZED) {
                    System.err.println("[Voice] AudioRecord init FAILED");
                    return;
                }
                mic.startRecording();
                System.out.println("[Voice] Mic started");

                // Initialize AudioTrack
                int minBufOut = android.media.AudioTrack.getMinBufferSize(sampleRate,
                    android.media.AudioFormat.CHANNEL_OUT_MONO,
                    android.media.AudioFormat.ENCODING_PCM_16BIT);
                if (minBufOut < frameSize * 2) minBufOut = frameSize * 2;

                speaker = new android.media.AudioTrack(
                    android.media.AudioManager.STREAM_VOICE_CALL,
                    sampleRate,
                    android.media.AudioFormat.CHANNEL_OUT_MONO,
                    android.media.AudioFormat.ENCODING_PCM_16BIT,
                    minBufOut,
                    android.media.AudioTrack.MODE_STREAM);

                if (speaker.getState() != android.media.AudioTrack.STATE_INITIALIZED) {
                    System.err.println("[Voice] AudioTrack init FAILED");
                    return;
                }
                speaker.play();
                System.out.println("[Voice] Speaker started");

                voiceRunning = true;
                System.out.println("[Voice] Running! Peers: " + voicePeers.size());

                byte[] recvBuf = new byte[1024];
                byte[] sendBuf = new byte[frameSize];

                // Start sender thread
                Thread sender = new Thread(() -> {
                    while (voiceRunning) {
                        if (mic != null) {
                            int len = mic.read(sendBuf, 0, sendBuf.length);
                            if (len > 0) {
                                synchronized (voicePeers) {
                                    for (java.net.InetAddress peer : voicePeers) {
                                        try {
                                            java.net.DatagramPacket out = new java.net.DatagramPacket(sendBuf, len, peer, voicePort);
                                            voiceSocket.send(out);
                                        } catch (Exception ex) {}
                                    }
                                }
                            }
                        }
                    }
                });
                sender.setDaemon(true);
                sender.start();

                // Receiver loop
                while (voiceRunning) {
                    try {
                        java.net.DatagramPacket pkt = new java.net.DatagramPacket(recvBuf, recvBuf.length);
                        voiceSocket.receive(pkt);
                        // Auto-add any peer that sends us audio
                        synchronized (voicePeers) {
                            if (!voicePeers.contains(pkt.getAddress())) {
                                voicePeers.add(pkt.getAddress());
                                System.out.println("[Voice] Auto-added peer: " + pkt.getAddress().getHostAddress());
                            }
                        }
                        if (speaker != null && speaker.getState() == android.media.AudioTrack.STATE_INITIALIZED) {
                            speaker.write(pkt.getData(), pkt.getOffset(), pkt.getLength());
                        }
                    } catch (java.net.SocketTimeoutException e) {
                        // Timeout is normal, loop back
                    }
                }
            } catch (Exception e) {
                System.err.println("[Voice] Fatal error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                System.out.println("[Voice] Stopping...");
            }
        }

        void addPeer(String ip) {
            try {
                java.net.InetAddress addr = java.net.InetAddress.getByName(ip);
                synchronized (voicePeers) {
                    if (!voicePeers.contains(addr)) {
                        voicePeers.add(addr);
                        System.out.println("[Voice] Added peer: " + ip + " (total: " + voicePeers.size() + ")");
                    }
                }
            } catch (Exception e) {
                System.err.println("[Voice] Failed to add peer " + ip + ": " + e.getMessage());
            }
        }

        void removePeer(String ip) {
            try {
                java.net.InetAddress addr = java.net.InetAddress.getByName(ip);
                synchronized (voicePeers) {
                    voicePeers.remove(addr);
                }
            } catch (Exception e) {}
        }

        void shutdown() {
            voiceRunning = false;
            try { if (mic != null) { mic.stop(); mic.release(); } } catch (Exception e) {}
            try { if (speaker != null) { speaker.stop(); speaker.release(); } } catch (Exception e) {}
            try { if (voiceSocket != null) voiceSocket.close(); } catch (Exception e) {}
            try { if (broadcastSocket != null) broadcastSocket.close(); } catch (Exception e) {}
        }
    }
}
