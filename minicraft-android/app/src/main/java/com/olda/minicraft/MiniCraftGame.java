package com.olda.minicraft;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * MiniCraft Android Game Engine
 * Ported from desktop Swing to Android Canvas
 */
public class MiniCraftGame implements Runnable {

    // ==================== CONSTANTS ====================
    public static final int TILE = 32;
    public static final int W = 128, H = 64;
    public static final int BLOCK_COUNT = 150;

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

    public static final int[] BLOCK_COLORS = {
        0,0xFF64B43C,0xFF8C643C,0xFF787878,0xFF646464,0xFF1E1E1E,0xFFE6D2A0,0xFF8C8278,
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
        // New blocks 90+
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

    // ==================== STATE ====================
    private int[][] world, bgWorld;
    private double px, py, playerVy;
    private int camX, camY;
    private int selBlock = GRASS;
    private boolean survival = false;
    private int health = 20, hunger = 20;
    private long worldTime = 12000;
    private int[] inv = new int[BLOCK_COUNT], invCount = new int[BLOCK_COUNT];
    private int breakX = -1, breakY = -1, breakTimer = 0, breakTime = 0;
    private int blocksBroken = 0, blocksPlaced = 0;
    private Random rand = new Random();
    private ArrayList<Particle> particles = new ArrayList<>();
    private boolean walking = false;

    // Touch input state
    public boolean touchLeft, touchRight, touchUp, touchDown, touchBreak, touchPlace;
    public float touchBreakX, touchBreakY;

    private Context ctx;
    private Bitmap[] textures;
    private int viewW, viewH;

    public MiniCraftGame(Context c) { ctx = c; }

    public void init(int w, int h) {
        viewW = w; viewH = h;
        loadTextures();
        genWorld(System.currentTimeMillis());
        px = W * TILE / 2.0;
        py = getGround((int)(px / TILE)) * TILE - TILE;
    }

    private void loadTextures() {
        textures = new Bitmap[BLOCK_COUNT];
        for (int i = 0; i < BLOCK_COUNT; i++) {
            try {
                InputStream is = ctx.getAssets().open("textures/" + i + ".png");
                textures[i] = BitmapFactory.decodeStream(is);
                is.close();
            } catch (IOException e) {
                // Generate colored placeholder
                textures[i] = Bitmap.createBitmap(TILE, TILE, Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(textures[i]);
                Paint p = new Paint();
                p.setColor(BLOCK_COLORS[i]);
                c.drawRect(0, 0, TILE, TILE, p);
            }
        }
    }

    // ==================== WORLD GEN ====================
    public void genWorld(long seed) {
        world = new int[W][H];
        bgWorld = new int[W][H];
        Random r = new Random(seed);
        for (int x = 0; x < W; x++) {
            int s = H / 2 + r.nextInt(8) - 4;
            if (x > W / 3 && x < W * 2 / 3) s -= r.nextInt(6);
            for (int y = s; y < H; y++) {
                int b;
                if (y == s) b = GRASS;
                else if (y < s + 3 + r.nextInt(3)) b = DIRT;
                else if (y < H - 5) b = r.nextInt(10) < 2 ? STONE : (r.nextInt(20) == 0 ? COAL_ORE : STONE);
                else b = BEDROCK;
                world[x][y] = b;
            }
            if (r.nextInt(12) == 0 && x > 3 && x < W - 4 && world[x][s - 1] != WATER) {
                int log = OAK_LOG + r.nextInt(6);
                int th = 4 + r.nextInt(5);
                for (int ty = s - 1; ty > s - th - 1 && ty >= 0; ty--) {
                    world[x][ty] = log;
                    if (bgWorld != null && r.nextInt(3) == 0) {
                        int sx = x + (r.nextBoolean() ? 1 : -1);
                        if (sx >= 0 && sx < W && bgWorld[sx][ty] == 0) bgWorld[sx][ty] = SHELF_MUSHROOM;
                    }
                }
                int crownY = s - th / 2;
                int crownR = th / 2 + 1;
                for (int lx = x - crownR; lx <= x + crownR; lx++) {
                    for (int ly = crownY - crownR; ly <= s - 1; ly++) {
                        if (lx >= 0 && lx < W && ly >= 0 && ly < H && world[lx][ly] == AIR) {
                            double dist = Math.sqrt((lx - x) * (lx - x) + (ly - crownY) * (ly - crownY));
                            if (dist < crownR && r.nextInt(5) > 0) world[lx][ly] = OAK_LEAVES + r.nextInt(6);
                        }
                    }
                }
            }
        }
    }

    private int getGround(int x) {
        if (x < 0 || x >= W) return H - 1;
        for (int y = 0; y < H; y++) if (world[x][y] > 0) return y;
        return H - 1;
    }

    // ==================== TICK ====================
    public void tick() {
        double speed = survival ? 3 : 6;
        double dx = 0, dy = 0;
        if (touchLeft) dx -= speed;
        if (touchRight) dx += speed;
        if (touchUp) {
            if (!survival) dy -= speed;
            else if (playerVy == 0) {
                int gx = (int) (px / TILE);
                int gy = (int) ((py + TILE) / TILE);
                if (gy + 1 < H && world[gx][gy + 1] > 0) playerVy = -8;
            }
        }
        if (touchDown && !survival) dy += speed;

        walking = dx != 0 || dy != 0;

        double nx = px + dx, ny = py + dy;
        if (!survival || isSolid((int) (nx / TILE), (int) (py / TILE))) nx = px;
        if (!survival || isSolid((int) (px / TILE), (int) (ny / TILE))) ny = py;
        px = Math.max(0, Math.min(W * TILE - TILE, nx));
        py = Math.max(0, Math.min(H * TILE - TILE, ny));

        if (survival) {
            int gx = (int) (px / TILE);
            int gy = (int) ((py + TILE) / TILE);
            if (gy + 1 < H && world[gx][gy + 1] > 0) {
                playerVy = 0;
            } else {
                playerVy += 0.4;
                py += playerVy;
            }
        }

        // Camera follow
        int targetX = (int) px - viewW / 2;
        int targetY = (int) py - viewH / 2;
        camX += (targetX - camX) * 0.15;
        camY += (targetY - camY) * 0.15;

        // Block breaking
        if (touchBreak && touchBreakX > 0) {
            int tx = (int) ((touchBreakX + camX) / TILE);
            int ty = (int) ((touchBreakY + camY) / TILE);
            if (isIn(tx, ty) && world[tx][ty] > 0) {
                if (breakX != tx || breakY != ty) { breakX = tx; breakY = ty; breakTimer = 0; breakTime = Math.max(3, world[tx][ty] * 2 % 15 + 2); }
                breakTimer++;
                if (breakTimer >= breakTime) {
                    int bk = world[tx][ty];
                    world[tx][ty] = AIR;
                    breakX = -1; breakY = -1; breakTimer = 0;
                    blocksBroken++;
                    for (int i = 0; i < 4; i++) particles.add(new Particle(tx * TILE + TILE / 2, ty * TILE + TILE / 2, bk));
                }
            }
        } else { breakX = -1; breakY = -1; breakTimer = 0; }

        // Block placing
        if (touchPlace) {
            int tx = (int) ((touchBreakX + camX) / TILE);
            int ty = (int) ((touchBreakY + camY) / TILE);
            if (isIn(tx, ty) && world[tx][ty] == AIR) {
                world[tx][ty] = selBlock;
                blocksPlaced++;
                for (int i = 0; i < 3; i++) particles.add(new Particle(tx * TILE + TILE / 2, ty * TILE + TILE / 2, selBlock));
            }
            touchPlace = false;
        }

        // Particles
        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            p.x += p.vx; p.y += p.vy; p.vy += 0.2; p.life--;
            if (p.life <= 0) { particles.remove(i); i--; }
        }
    }

    // ==================== RENDER ====================
    public void render(Canvas c) {
        c.drawColor(0xFF87CEEB); // Sky blue

        int sx = Math.max(0, camX / TILE - 1);
        int sy = Math.max(0, camY / TILE - 1);
        int ex = Math.min(W, sx + viewW / TILE + 3);
        int ey = Math.min(H, sy + viewH / TILE + 3);

        Paint p = new Paint();
        // Background
        if (bgWorld != null) {
            int bgOffX = (int)(camX * 0.5), bgOffY = (int)(camY * 0.3);
            for (int x = sx; x < ex; x++) {
                for (int y = sy; y < ey; y++) {
                    if (bgWorld[x][y] > 0) {
                        p.setColor(BLOCK_COLORS[bgWorld[x][y]] & 0x00FFFFFF | 0x5A000000);
                        c.drawRect(x * TILE - bgOffX, y * TILE - bgOffY, x * TILE - bgOffX + TILE, y * TILE - bgOffY + TILE, p);
                    }
                }
            }
        }

        // World blocks
        for (int x = sx; x < ex; x++) {
            for (int y = sy; y < ey; y++) {
                int b = world[x][y];
                if (b > 0) {
                    int drawX = x * TILE - camX;
                    int drawY = y * TILE - camY;
                    if (textures[b] != null) {
                        c.drawBitmap(textures[b], null, new Rect(drawX, drawY, drawX + TILE, drawY + TILE), p);
                    } else {
                        p.setColor(BLOCK_COLORS[b]);
                        c.drawRect(drawX, drawY, drawX + TILE, drawY + TILE, p);
                    }
                }
            }
        }

        // Player
        p.setColor(0xFF00AAAA);
        c.drawRect((int) px - camX - 12, (int) py - camY - 24, (int) px - camX + 12, (int) py - camY, p);

        // Particles
        for (Particle pt : particles) {
            p.setColor(BLOCK_COLORS[pt.block]);
            c.drawRect((int) pt.x - camX, (int) pt.y - camY, (int) pt.x - camX + 4, (int) pt.y - camY + 4, p);
        }

        // Block name tooltip
        if (touchBreakX > 0) {
            int tx = (int) ((touchBreakX + camX) / TILE);
            int ty = (int) ((touchBreakY + camY) / TILE);
            if (isIn(tx, ty) && world[tx][ty] > 0) {
                p.setColor(0xCC000000);
                c.drawRect(touchBreakX - 40, touchBreakY - 30, touchBreakX + 80, touchBreakY - 10, p);
                p.setColor(0xFFFFFFFF);
                p.setTextSize(20);
                c.drawText(BNAME[world[tx][ty]], touchBreakX - 35, touchBreakY - 14, p);
            }
        }

        // Hotbar
        int slots = Math.min(9, BLOCK_COUNT);
        int hs = 48;
        int startX = viewW / 2 - slots * hs / 2;
        for (int i = 0; i < slots; i++) {
            int idx = i + 1;
            int sx2 = startX + i * hs;
            int sy2 = viewH - hs - 8;
            p.setColor(i == selBlock - 1 ? 0xFFFFFFFF : 0xAA000000);
            c.drawRect(sx2, sy2, sx2 + hs, sy2 + hs, p);
            p.setColor(0xFFFFFFFF);
            p.setTextSize(14);
            c.drawText(String.valueOf(i + 1), sx2 + 4, sy2 + 14, p);
            if (textures[idx] != null) {
                c.drawBitmap(textures[idx], null, new Rect(sx2 + 2, sy2 + 16, sx2 + hs - 2, sy2 + hs - 2), p);
            }
        }

        // Debug
        p.setColor(0xCC000000);
        c.drawRect(4, 4, 160, 80, p);
        p.setColor(0xFFFFFFFF);
        p.setTextSize(18);
        c.drawText("MiniCraft v6.3.0", 8, 22, p);
        c.drawText("X:" + (int)(px/TILE) + " Y:" + (int)(py/TILE), 8, 42, p);
        c.drawText("Blocks: " + blocksBroken + "/" + blocksPlaced, 8, 62, p);
    }

    private boolean isSolid(int x, int y) {
        if (x < 0 || x >= W || y < 0 || y >= H) return true;
        return world[x][y] > 0 && world[x][y] != WATER && world[x][y] != LAVA;
    }
    private boolean isIn(int x, int y) { return x >= 0 && x < W && y >= 0 && y < H; }

    public void setSelectedSlot(int slot) { selBlock = Math.min(BLOCK_COUNT - 1, Math.max(1, slot)); }
    public void cycleSlot(int delta) { selBlock = Math.min(BLOCK_COUNT - 1, Math.max(1, selBlock + delta)); }

    @Override public void run() {}

    static class Particle {
        double x, y, vx, vy; int life, block;
        Particle(double x, double y, int b) { this.x=x; this.y=y; block=b; life=8+(int)(Math.random()*12); vx=(Math.random()-0.5)*4; vy=-Math.random()*5-2; }
    }
}
