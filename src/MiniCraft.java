//sha:c8c9fbbe
//sha:ef94d81d
//sha:8eeefdea
//sha:dbae507a
//sha:48c38495
//sha:3de0e629
//sha:e9ead95f
//sha:2e5bf1b8
//sha:63641afe
//sha:7504f537
//sha:faf40073
//sha:1ba6abd0
//sha:9d113e53
//sha:12b070e4
//sha:dca32d86
//sha:9fec86eb
//sha:0ad507ca
//sha:a27a7a51
//sha:38836b38
//sha:ad9b24da
//sha:a503a4cf
//sha:6b77481f
//sha:fc6865f2
//sha:ca4a31c3
//sha:4ec7002e
//sha:845cfc00
//sha:6aa16497
//sha:daf003d6
//sha:c2888a02
//sha:5ff0b62d
//sha:d6bc9d04
//sha:6fda8454
//sha:2683f3ad
//sha:aba4c5bd
//sha:c93ef505
//sha:779471cc
//sha:88c66e73
//sha:4f7a25cb
//sha:ba527029
//sha:3bde94bb
//sha:90f0605e
//sha:1e9658fd
//sha:3a06463f
//sha:e84488e6
//sha:eb07462a
//sha:192d03dd
//sha:ff663c0b
//sha:a0d703c6
//sha:2e33f393
//sha:d4bf54aa
//sha:55264b8f
//sha:dea13c57
//sha:4dd8ee68
//sha:f03e8570
//sha:fe8aebdb
//sha:687d9e58
//sha:f9555418
//sha:2fc5bec4
//sha:4273ac79
//sha:d85ea93f
//sha:fe11266b
//sha:983eb6c2
//sha:be9b7f3f
//sha:0ba84a1b
//sha:957102da
//sha:e63cb844
//sha:605f5dba
//sha:903cdcfa
//sha:188fb1df
//sha:b1df95eb
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.ArrayList;
import javax.sound.sampled.*;

public class MiniCraft extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
    private static final int TILE = 32, W = 128, H = 64, VW = 25, VH = 18;
    private static final String VERSION = "6.4.2";
    private static final String GITHUB_API = "https://api.github.com/repos/olda9991/minicraft/commits/main";
    private static final String GITHUB_RAW = "https://raw.githubusercontent.com/olda9991/minicraft/main/src/MiniCraft.java";
    private static final String DATA_DIR = System.getProperty("user.dir") + "/worlds/";
    private static final String TEX_DIR = System.getProperty("user.dir") + "/textures/";

    private BufferedImage[] tex, steveImg, heartImg, hungerImg;
    private BufferedImage logoImg;
    private BufferedImage[] logoFrames;
    private int logoFrame=0;
    private long logoFrameTime=0;
    private int playerW=28, playerH=28;

    private static final int AIR=0,GRASS=1,DIRT=2,STONE=3,COBBLESTONE=4,BEDROCK=5,SAND=6,GRAVEL=7;
    private static final int OAK_LOG=8,SPRUCE_LOG=9,BIRCH_LOG=10,JUNGLE_LOG=11,ACACIA_LOG=12,DARK_OAK_LOG=13;
    private static final int OAK_PLANKS=14,SPRUCE_PLANKS=15,BIRCH_PLANKS=16,JUNGLE_PLANKS=17,ACACIA_PLANKS=18,DARK_OAK_PLANKS=19;
    private static final int OAK_LEAVES=20,SPRUCE_LEAVES=21,BIRCH_LEAVES=22,JUNGLE_LEAVES=23,ACACIA_LEAVES=24,DARK_OAK_LEAVES=25;
    private static final int WATER=26,LAVA=27,ICE=28,BRICKS=29,STONE_BRICKS=30,MOSSY_STONE_BRICKS=31,CRACKED_STONE_BRICKS=32;
    private static final int COAL_ORE=33,IRON_ORE=34,GOLD_ORE=35,DIAMOND_ORE=36,EMERALD_ORE=37,REDSTONE_ORE=38,LAPIS_ORE=39,COPPER_ORE=40;
    private static final int NETHERRACK=41,SOUL_SAND=42,GLOWSTONE=43,OBSIDIAN=44,CRYING_OBSIDIAN=45;
    private static final int SMOOTH_STONE=46,POLISHED_ANDESITE=47,POLISHED_DIORITE=48,POLISHED_GRANITE=49;
    private static final int PRISMARINE=50,DARK_PRISMARINE=51,SEA_LANTERN=52,END_STONE=53,PURPUR=54,MAGMA=55,SLIME=56,TNT=57,FURNACE=58,CRAFTING_TABLE=59;
    private static final int BLOCK_COUNT=150,STICK=80;
    private static final int RAW_BEEF=60,COOKED_BEEF=61,RAW_PORK=62,COOKED_PORK=63,WOOL=64;
    private static final int IRON_INGOT=65,GOLD_INGOT=66,DIAMOND_GEM=67,BED=68,EXP_ORB=69;
    private static final int SWORD=70,PICKAXE=71,AXE=72,SHOVEL=73,FURNACE_ITEM=74,BOW=75,ARROW_ITEM=76,CHEST=77,SPIDER_EYE=78,BONE=79;
    private static final int FLINT_STEEL=81,ROD=82,RAW_FISH=83,COOKED_FISH=84,SADDLE=85,LEATHER_ITEM=86,TORCH_ITEM=87,TOTEM=88,ELYTRA=89,FIRE=90,SOUL_FIRE=91,STRAW_BED=92,CUSHION=93,SHELF_MUSHROOM=94,SULFUR_CUBE=95,NETHERITE_BLOCK=96,ANCIENT_DEBRIS=97,BLACKSTONE=98,GILDED_BLACKSTONE=99,BASALT=100,POLISHED_BASALT=101,CRIMSON_STEM=102,CRIMSON_PLANKS=103,WARPED_STEM=104,WARPED_PLANKS=105,SHROOMLIGHT=106,LODESTONE=107,RESPAWN_ANCHOR=108,SOUL_SOIL=109,TARGET=110,HONEY_BLOCK=111,DEEPSLATE=112,DEEPSLATE_DIAMOND_ORE=113,AMETHYST_BLOCK=114,BUDDING_AMETHYST=115,SCULK=116,MOSS_BLOCK=117,AZALEA_LEAVES=118,FLOWERING_AZALEA=119,ROOTED_DIRT=120,MUD=121,MUD_BRICKS=122,MANGROVE_LOG=123,MANGROVE_LEAVES=124,MANGROVE_PLANKS=125,CHERRY_LOG=126,CHERRY_LEAVES=127,CHERRY_PLANKS=128,BAMBOO_BLOCK=129,BAMBOO_PLANKS=130,BAMBOO_MOSAIC=131,COPPER_BLOCK=132,EXPOSED_COPPER=133,WEATHERED_COPPER=134,OXIDIZED_COPPER=135,CUT_COPPER=136,WAXED_COPPER=137,RAW_IRON_BLOCK=138,RAW_GOLD_BLOCK=139,RAW_COPPER_BLOCK=140,LANTERN=141,SOUL_LANTERN=142,CAMPFIRE=143,SOUL_CAMPFIRE=144,CHAIN=145,SMITHING_TABLE=146,BARREL=147,SMOKER=148,BLAST_FURNACE=149;
    private static final String[] BNAME={"Air","Grass","Dirt","Stone","Cobblestone","Bedrock","Sand","Gravel","Oak Log","Spruce Log","Birch Log","Jungle Log","Acacia Log","Dark Oak Log","Oak Planks","Spruce Planks","Birch Planks","Jungle Planks","Acacia Planks","Dark Oak Planks","Oak Leaves","Spruce Leaves","Birch Leaves","Jungle Leaves","Acacia Leaves","Dark Oak Leaves","Water","Lava","Ice","Bricks","Stone Bricks","Mossy Stone Bricks","Cracked Stone Bricks","Coal Ore","Iron Ore","Gold Ore","Diamond Ore","Emerald Ore","Redstone Ore","Lapis Ore","Copper Ore","Netherrack","Soul Sand","Glowstone","Obsidian","Crying Obsidian","Smooth Stone","Polished Andesite","Polished Diorite","Polished Granite","Prismarine","Dark Prismarine","Sea Lantern","End Stone","Purpur Block","Magma","Slime Block","TNT","Furnace","Crafting Table","Raw Beef","Cooked Beef","Raw Pork","Cooked Pork","Wool","Iron Ingot","Gold Ingot","Diamond","Bed","XP Orb","Sword","Pickaxe","Axe","Shovel","Furnace Item","Bow","Arrow","Chest","Spider Eye","Bone","Stick","FlintSteel","FishRod","RawFish","CookFish","Saddle","Leather","Torch","Totem","Elytra","Fire","Soul Fire","Straw Bed","Cushion","Shelf Mushroom","Sulfur Cube","Netherite Block","Ancient Debris","Blackstone","Gilded Blackstone","Basalt","Polished Basalt","Crimson Stem","Crimson Planks","Warped Stem","Warped Planks","Shroomlight","Lodestone","Respawn Anchor","Soul Soil","Target","Honey Block","Deepslate","Deepslate Diamond Ore","Amethyst Block","Budding Amethyst","Sculk","Moss Block","Azalea Leaves","Flowering Azalea","Rooted Dirt","Mud","Mud Bricks","Mangrove Log","Mangrove Leaves","Mangrove Planks","Cherry Log","Cherry Leaves","Cherry Planks","Bamboo Block","Bamboo Planks","Bamboo Mosaic","Copper Block","Exposed Copper","Weathered Copper","Oxidized Copper","Cut Copper","Waxed Copper","Raw Iron Block","Raw Gold Block","Raw Copper Block","Lantern","Soul Lantern","Campfire","Soul Campfire","Chain","Smithing Table","Barrel","Smoker","Blast Furnace"};
    private static final String[] TF={"air","grass","dirt","stone","cobblestone","bedrock","sand","gravel","oak_log","spruce_log","birch_log","jungle_log","acacia_log","dark_oak_log","oak_planks","spruce_planks","birch_planks","jungle_planks","acacia_planks","dark_oak_planks","oak_leaves","spruce_leaves","birch_leaves","jungle_leaves","acacia_leaves","dark_oak_leaves","water","snow","ice","bricks","stone_bricks","mossy_stone_bricks","cracked_stone_bricks","coal_ore","iron_ore","gold_ore","diamond_ore","emerald_ore","redstone_ore","lapis_ore","copper_ore","netherrack","soul_sand","glowstone","obsidian","crying_obsidian","smooth_stone","polished_andesite","polished_diorite","polished_granite","prismarine","dark_prismarine","sea_lantern","end_stone","purpur_block","magma","slime_block","tnt_side","furnace_front","crafting_table_front","beef","cbeef","pork","cpork","wool","iron_ingot","gold_ingot","diamond","bed","xp","sword","pickaxe","axe","shovel","furnace","bow","arrow","chest","eye","bone","stick","flint","fishrod","rawfish","cookfish","saddle","leather","torch","totem","elytra","fire","soul_fire","straw_bed","cushion","shelf_mushroom","sulfur_cube","netherite_block","ancient_debris","blackstone","gilded_blackstone","basalt","polished_basalt","crimson_stem","crimson_planks","warped_stem","warped_planks","shroomlight","lodestone","respawn_anchor","soul_soil","target","honey_block","deepslate","deepslate_diamond_ore","amethyst_block","budding_amethyst","sculk","moss_block","azalea_leaves","flowering_azalea","rooted_dirt","mud","mud_bricks","mangrove_log","mangrove_leaves","mangrove_planks","cherry_log","cherry_leaves","cherry_planks","bamboo_block","bamboo_planks","bamboo_mosaic","copper_block","exposed_copper","weathered_copper","oxidized_copper","cut_copper","waxed_copper","raw_iron_block","raw_gold_block","raw_copper_block","lantern","soul_lantern","campfire","soul_campfire","chain","smithing_table","barrel","smoker","blast_furnace"};
    private static final Color[] FB={new Color(0,0,0,0),new Color(100,180,60),new Color(140,100,60),new Color(120,120,120),new Color(100,100,100),new Color(30,30,30),new Color(230,210,160),new Color(140,130,120),new Color(100,80,50),new Color(80,60,40),new Color(180,160,100),new Color(120,100,70),new Color(160,120,60),new Color(60,40,20),new Color(160,130,80),new Color(140,110,70),new Color(180,150,100),new Color(160,130,90),new Color(180,140,80),new Color(100,70,40),new Color(50,140,50),new Color(40,120,40),new Color(60,150,60),new Color(50,130,50),new Color(60,140,50),new Color(40,110,30),new Color(80,80,200),new Color(255,120,20),new Color(160,200,220),new Color(180,120,80),new Color(160,160,160),new Color(140,140,120),new Color(130,130,130),new Color(60,60,60),new Color(180,140,160),new Color(220,180,40),new Color(80,200,220),new Color(60,180,60),new Color(200,80,80),new Color(60,80,180),new Color(160,120,80),new Color(100,40,40),new Color(80,60,40),new Color(200,180,80),new Color(30,20,50),new Color(80,30,60),new Color(180,180,180),new Color(140,140,150),new Color(160,160,170),new Color(160,140,120),new Color(80,160,160),new Color(60,120,120),new Color(200,200,160),new Color(220,200,160),new Color(160,80,120),new Color(200,80,40),new Color(80,200,80),new Color(180,60,40),new Color(120,120,120),new Color(140,100,60),new Color(200,60,60),new Color(180,100,60),new Color(255,150,150),new Color(200,150,150),new Color(220,220,220),new Color(180,180,180),new Color(255,220,40),new Color(180,240,200),new Color(200,100,100),new Color(80,255,80),new Color(180,180,180),new Color(200,200,200),new Color(180,180,180),new Color(200,200,200),new Color(200,200,200),new Color(180,120,60),new Color(200,180,140),new Color(160,100,40),new Color(140,50,50),new Color(220,220,200),new Color(180,160,120),new Color(100,100,100),new Color(80,120,160),new Color(100,160,180),new Color(200,150,100),new Color(80,140,80),new Color(180,120,60),new Color(255,200,40),new Color(200,200,100),new Color(200,180,160),new Color(255,140,0),new Color(0,180,255),new Color(200,180,80),new Color(240,200,200),new Color(180,120,60),new Color(200,200,50),new Color(50,50,50),new Color(80,60,40),new Color(40,40,40),new Color(60,50,30),new Color(80,80,90),new Color(100,100,110),new Color(120,40,40),new Color(160,60,60),new Color(40,120,120),new Color(60,160,160),new Color(255,200,100),new Color(160,160,170),new Color(60,40,100),new Color(60,50,40),new Color(200,160,120),new Color(255,200,50),new Color(80,80,90),new Color(60,60,80),new Color(180,140,220),new Color(160,120,200),new Color(20,40,30),new Color(80,140,60),new Color(80,160,80),new Color(200,100,200),new Color(140,120,80),new Color(100,80,60),new Color(140,100,60),new Color(120,80,50),new Color(60,140,80),new Color(100,160,120),new Color(200,150,160),new Color(255,180,200),new Color(255,200,220),new Color(200,220,100),new Color(220,240,120),new Color(240,250,140),new Color(180,120,80),new Color(160,140,120),new Color(140,130,100),new Color(120,120,90),new Color(140,120,100),new Color(160,150,130),new Color(140,130,110),new Color(180,160,140),new Color(200,180,160),new Color(160,130,100),new Color(100,100,100),new Color(0,160,200),new Color(0,120,160),new Color(255,140,0),new Color(0,200,255),new Color(160,160,170),new Color(100,100,100),new Color(100,100,100)};
    private static final int[] BT = new int[BLOCK_COUNT];
    static { for(int i=0;i<BLOCK_COUNT;i++){
        if(i==BEDROCK)BT[i]=99999;
        else if(i==WATER||i==FIRE||i==SOUL_FIRE)BT[i]=1;
        else if(i>=COAL_ORE&&i<=COPPER_ORE)BT[i]=20;
        else if(i==DEEPSLATE_DIAMOND_ORE)BT[i]=25;
        else if(i==OBSIDIAN||i==CRYING_OBSIDIAN||i==NETHERITE_BLOCK||i==ANCIENT_DEBRIS)BT[i]=120;
        else if(i==DEEPSLATE||i==BLACKSTONE||i==BASALT||i==LODESTONE)BT[i]=35;
        else if(i==AMETHYST_BLOCK||i==BUDDING_AMETHYST)BT[i]=15;
        else if(i==SCULK)BT[i]=8;
        else if(i==TARGET)BT[i]=10;
        else if(i==HONEY_BLOCK)BT[i]=5;
        else if(i==COPPER_BLOCK||i==EXPOSED_COPPER||i==WEATHERED_COPPER||i==OXIDIZED_COPPER||i==CUT_COPPER||i==WAXED_COPPER)BT[i]=18;
        else if(i==RAW_IRON_BLOCK||i==RAW_GOLD_BLOCK||i==RAW_COPPER_BLOCK)BT[i]=25;
        else if(i==CAMPFIRE||i==SOUL_CAMPFIRE||i==LANTERN||i==SOUL_LANTERN||i==CHAIN)BT[i]=8;
        else BT[i]=Math.max(3,i*2%15+2);
    } }

    private int[][] world;
    private int[][] bgWorld;
    private double px,py;
    private String worldName="", playerName="Steve";
    private long worldSeed=0;
    private int selBlock=1, creativeOffset=0;
    private boolean[] keys=new boolean[1024];
    private javax.swing.Timer timer;
    private int camX,camY,mx=-999,my=-999;
    private boolean mouseIn=false;
    private int health=20,hunger=20,xp=0,armor=0,kills=0,elytraFly=0;
    private boolean inNether=false;
    private boolean dead=false;
    private int fallDist=0,breakTimer=0,breakX=-1,breakY=-1,breakTime=0;
    private boolean craftingOpen=false,survival=true;
    private int hungerTimer=0;
    private int[] inv=new int[BLOCK_COUNT],invCount=new int[BLOCK_COUNT],craftGrid=new int[4],craftCount=new int[4];
    private int selInv=-1;
    private Random rand=new Random();

    private enum Screen{MENU,WORLD_LIST,CREATE_WORLD,PLAY,CRAFTING,DEATH,MULTIPLAYER,CONNECT,HOST,SETTINGS,CONNECTING,PAUSE,HELP};
    private Screen screen=Screen.HELP;
    private ArrayList<String> worldList=new ArrayList<>();
    private String typing="";
    private int selectedWorld=-1;
    private int menuHover=-1;
    private boolean showFps=false, showCoords=true, showDebug=false, noclip=false, fullscreen=false, ultraFps=false, rtxMode=false, rtxWater=false, physicsOn=true, superflat=false, bedrockEdition=false, threeDMode=false;
    private int shaderMode=0;
    private int physicsLevel=2;
    private int gameFov=25, settingSel=-1;
    private long sessionStart=0;
    private int blocksBroken=0,blocksPlaced=0;
    private boolean nameEditing=false;
    private boolean bgEdit=false;
    private boolean updateAvailable=false;
    private String updateVersion="";
    private int fps=0, fpsCount=0;
    private long fpsTimer=0;
    private java.util.ArrayList<Particle> particles=new java.util.ArrayList<>();
    private int bobFrame=0;
    private double camSmoothX=0, camSmoothY=0;
    private double playerDir=0; // 3D mode: facing angle in radians (0=right/+X)
    private java.awt.Robot robot;
    private boolean robotOk=false;
    private boolean walking=false;
    private double playerVy=0;
    private long worldTime=12000;
    private int[] mountains;

    class Particle{double x,y,vx,vy;int life,maxLife;int block;
        Particle(double x,double y,int b){this.x=x;this.y=y;block=b;life=maxLife=8+(int)(Math.random()*12);vx=(Math.random()-0.5)*4;vy=-Math.random()*5-2;}
    }
    class DropItem{double x,y,vy;int block,life;
        DropItem(double x,double y,int b){this.x=x;this.y=y;block=b;life=600;vy=-2;}
    }
    class Mob{double x,y,vy;int health=6;int maxHealth=6;int type;int aiT;int hurtT;
        Mob(double x,double y,int t){this.x=x;this.y=y;type=t;maxHealth=t==2?10:t==4?8:t==5?12:6;health=maxHealth;aiT=(int)(Math.random()*60);}
    }
    class DmgNum{double x,y;int val,life;DmgNum(double x,double y,int v){this.x=x;this.y=y;val=v;life=40;}}
    class Arrow{double x,y,vx,vy;int life;Arrow(double x,double y,double vx,double vy){this.x=x;this.y=y;this.vx=vx;this.vy=vy;life=120;}}
    class PrimedTnt{double x,y;int timer;PrimedTnt(double x,double y){this.x=x;this.y=y;timer=180;}}
    class Achieve{String msg;int life;Achieve(String m){msg=m;life=120;}}
    private java.util.ArrayList<DropItem> drops=new java.util.ArrayList<>();
    private java.util.ArrayList<Mob> mobs=new java.util.ArrayList<>();
    private java.util.ArrayList<DmgNum> dmgNums=new java.util.ArrayList<>();
    private java.util.ArrayList<Arrow> arrows=new java.util.ArrayList<>();
    private java.util.ArrayList<PrimedTnt> tntList=new java.util.ArrayList<>();
    private java.util.ArrayList<Achieve> achieves=new java.util.ArrayList<>();
    private BufferedImage discIcon,ghIcon;
    private boolean chatOpen=false;
    private String chatText="";
    private ArrayList<String> chatMessages=new ArrayList<>();
    private int chatTimer=0;
    private boolean musicOn=true;
    private javax.sound.sampled.Clip musicClip;
    private java.util.ArrayList<String> musicFiles=new java.util.ArrayList<>();
    private java.util.ArrayList<String> sfxFiles=new java.util.ArrayList<>();
    private int walkSoundTimer=0;
    private ArrayList<DiscoveredServer> discoveredServers=new ArrayList<>();
    private int selectedServer=-1;
    private DiscoveryThread discovery;
    private DiscoveryResponder responder;

    class DiscoveredServer{String ip;int port;String name;String world;int players;int maxPlayers;long lastSeen;
        DiscoveredServer(String i,int p,String n,String w,int pl,int mx){ip=i;port=p;name=n;world=w;players=pl;maxPlayers=mx;lastSeen=System.currentTimeMillis();}
    }

    class DiscoveryThread extends Thread{
        private DatagramSocket socket;private boolean running=true;private static final int DISC_PORT=25566;
        public void run(){
            try{socket=new DatagramSocket();socket.setBroadcast(true);socket.setSoTimeout(2000);
                byte[] disc="MINICRAFT_DISCOVER".getBytes();
                DatagramPacket dp=new DatagramPacket(disc,disc.length,InetAddress.getByName("255.255.255.255"),DISC_PORT);
                while(running){
                    try{socket.send(dp);}catch(Exception e){}
                    long start=System.currentTimeMillis();
                    while(System.currentTimeMillis()-start<1500&&running){
                        try{
                            byte[] buf=new byte[512];DatagramPacket pkt=new DatagramPacket(buf,buf.length);socket.setSoTimeout(500);
                            socket.receive(pkt);String data=new String(pkt.getData(),0,pkt.getLength());
                            String[] p=data.split(" ",4);
                            if(p[0].equals("MINICRAFT_SERVER")&&p.length>=4){
                                String ip=pkt.getAddress().getHostAddress();int port=Integer.parseInt(p[1]);
                                boolean found=false;
                                for(DiscoveredServer ds:discoveredServers)
                                    if(ds.ip.equals(ip)&&ds.port==port){ds.players=Integer.parseInt(p[3]);ds.lastSeen=System.currentTimeMillis();found=true;break;}
                                if(!found)discoveredServers.add(new DiscoveredServer(ip,port,p[2],p[2],Integer.parseInt(p[3]),8));
                            }
                        }catch(SocketTimeoutException e){}catch(Exception e){break;}
                    }
                    long now=System.currentTimeMillis();
                    discoveredServers.removeIf(ds->now-ds.lastSeen>15000);
                    try{Thread.sleep(3000);}catch(Exception e){}
                }
            }catch(Exception e){}finally{if(socket!=null)socket.close();}
        }
        void stopDisc(){running=false;if(socket!=null)socket.close();}
    }

    class DiscoveryResponder extends Thread{
        private DatagramSocket socket;private boolean running=true;private static final int DISC_PORT=25566;
        public void run(){
            try{socket=new DatagramSocket(DISC_PORT);socket.setBroadcast(true);socket.setSoTimeout(1000);
                while(running){
                    byte[] buf=new byte[512];DatagramPacket pkt=new DatagramPacket(buf,buf.length);
                    try{socket.receive(pkt);String data=new String(pkt.getData(),0,pkt.getLength());
                        if(data.equals("MINICRAFT_DISCOVER")){
                            int pc=isHost&&server!=null?server.getPlayerCount():0;
                            String resp="MINICRAFT_SERVER 25565 "+worldName+" "+pc;
                            byte[] rbuf=resp.getBytes();
                            DatagramPacket rpkt=new DatagramPacket(rbuf,rbuf.length,pkt.getAddress(),pkt.getPort());
                            socket.send(rpkt);
                        }
                    }catch(SocketTimeoutException e){}catch(Exception e){break;}
                }
            }catch(Exception e){}finally{if(socket!=null)socket.close();}
        }
        void stopDisc(){running=false;if(socket!=null)socket.close();}
    }

    private MiniServer server;
    private MiniClient client;
    private ArrayList<RemotePlayer> remotePlayers=new ArrayList<>();
    private boolean isHost=false;
    private int serverPort=25565;
    private int clientPort=0;
    private String serverIP="localhost";
    private Process webProcess, boreProcess;
    private DiscordRPC discordRPC;

    // ==================== VOICE CHAT ====================
    class VoiceChatThread extends Thread{
        private boolean voiceRunning=false;
        private javax.sound.sampled.TargetDataLine micLine;
        private javax.sound.sampled.SourceDataLine speakerLine;
        private java.net.DatagramSocket voiceSocket;
        private java.util.ArrayList<java.net.InetAddress> voicePeers=new java.util.ArrayList<>();
        private int voicePort=0;
        private int sampleRate=8000;
        VoiceChatThread(int basePort){
            voicePort=basePort+1000;
            setDaemon(true);
        }
        public void run(){
            try{
                javax.sound.sampled.AudioFormat fmt=new javax.sound.sampled.AudioFormat(sampleRate,16,1,true,true);
                micLine=javax.sound.sampled.AudioSystem.getTargetDataLine(fmt);
                micLine.open(fmt,1024);
                micLine.start();
                speakerLine=javax.sound.sampled.AudioSystem.getSourceDataLine(fmt);
                speakerLine.open(fmt,1024);
                speakerLine.start();
                voiceSocket=new java.net.DatagramSocket(voicePort);
                voiceSocket.setSoTimeout(500);
                voiceRunning=true;
                System.out.println("[Voice] Initialized on port "+voicePort);
                byte[] buf=new byte[512];
                while(voiceRunning){
                    try{
                        java.net.DatagramPacket pkt=new java.net.DatagramPacket(buf,buf.length);
                        voiceSocket.receive(pkt);
                        if(speakerLine!=null&&speakerLine.isOpen())speakerLine.write(pkt.getData(),pkt.getOffset(),pkt.getLength());
                    }catch(java.net.SocketTimeoutException e){}
                    if(micLine!=null&&micLine.available()>0){
                        int len=micLine.read(buf,0,Math.min(buf.length,micLine.available()));
                        for(java.net.InetAddress peer:voicePeers){
                            try{
                                java.net.DatagramPacket out=new java.net.DatagramPacket(buf,len,peer,voicePort);
                                voiceSocket.send(out);
                            }catch(Exception ex){}
                        }
                    }
                }
            }catch(Exception e){System.out.println("[Voice] Error: "+e.getMessage());}
        }
        void addPeer(String ip){
            try{
                java.net.InetAddress addr=java.net.InetAddress.getByName(ip);
                if(!voicePeers.contains(addr))voicePeers.add(addr);
            }catch(Exception e){}
        }
        void removePeer(String ip){
            try{
                java.net.InetAddress addr=java.net.InetAddress.getByName(ip);
                voicePeers.remove(addr);
            }catch(Exception e){}
        }
        void shutdown(){
            voiceRunning=false;
            try{micLine.close();}catch(Exception e){}
            try{speakerLine.close();}catch(Exception e){}
            try{voiceSocket.close();}catch(Exception e){}
        }
    }
    private VoiceChatThread voiceChat;

    class DiscordRPC extends Thread{
        private Process bridgeProcess;
        private boolean running=true;
        private long startTime=0;private String lastState="";private int lastPartySize=-1;
        String currentJson="";String currentSecret="";
        private static final String BRIDGE_URL="http://127.0.0.1:6464";
        public void run(){
            startBridge();
            // Wait for bridge to come up
            for(int i=0;i<20&&running;i++){if(isBridgeReady())break;try{Thread.sleep(250);}catch(Exception e){break;}}
            if(!isBridgeReady()){System.out.println("[RPC] Bridge failed to start");return;}
            System.out.println("[RPC] Bridge ready");
            startTime=System.currentTimeMillis();
            updatePresence();
            while(running){
                try{Thread.sleep(1000);updatePresence();pollEvents();}
                catch(Exception e){break;}
            }
        }
        private void startBridge(){
            // Check if bridge already running on port before spawning
            if(isBridgeReady()){
                System.out.println("[RPC] Bridge already running on port, reusing");
                bridgeProcess=null;
                return;
            }
            try{
                String dir=System.getProperty("user.dir");
                ProcessBuilder pb=new ProcessBuilder("python3",dir+"/minicraft_rpc.py");
                pb.redirectErrorStream(true);
                bridgeProcess=pb.start();
                System.out.println("[RPC] Bridge PID="+bridgeProcess.pid());
            }catch(Exception e){System.out.println("[RPC] Bridge start error: "+e.getMessage());}
        }
        private boolean isBridgeReady(){
            try{
                java.net.URL url=new java.net.URI(BRIDGE_URL+"/events").toURL();
                java.net.HttpURLConnection c=(java.net.HttpURLConnection)url.openConnection();
                c.setUseCaches(false);c.setDefaultUseCaches(false);
                c.setConnectTimeout(1000);c.setReadTimeout(1000);c.setRequestMethod("GET");
                c.getResponseCode();c.disconnect();return true;
            }catch(Exception e){return false;}
        }
        private void httpPost(String path,String json){
            try{
                java.net.URL url=new java.net.URI(BRIDGE_URL+path).toURL();
                java.net.HttpURLConnection c=(java.net.HttpURLConnection)url.openConnection();
                c.setDoOutput(true);c.setRequestMethod("POST");
                c.setRequestProperty("Content-Type","application/json");
                c.setUseCaches(false);c.setDefaultUseCaches(false);
                c.setConnectTimeout(2000);c.setReadTimeout(3000);
                try(java.io.OutputStream os=c.getOutputStream()){os.write(json.getBytes("UTF-8"));}
                int code=c.getResponseCode();
                if(code!=200)System.out.println("[RPC] HTTP "+code);
                c.disconnect();
            }catch(Exception e){System.out.println("[RPC] POST error: "+e.getMessage());}
        }
        private String httpGet(String path){
            try{
                java.net.URL url=new java.net.URI(BRIDGE_URL+path).toURL();
                java.net.HttpURLConnection c=(java.net.HttpURLConnection)url.openConnection();
                c.setUseCaches(false);c.setDefaultUseCaches(false);
                c.setConnectTimeout(2000);c.setReadTimeout(3000);c.setRequestMethod("GET");
                int code=c.getResponseCode();
                if(code==200){try(java.io.BufferedReader r=new java.io.BufferedReader(new java.io.InputStreamReader(c.getInputStream()))){StringBuilder sb=new StringBuilder();String l;while((l=r.readLine())!=null)sb.append(l);return sb.toString();}}
                if(code!=200)System.out.println("[RPC] GET HTTP "+code);
                c.disconnect();
            }catch(Exception e){System.out.println("[RPC] GET error: "+e.getMessage());}
            return "";
        }
        private void pollEvents(){
            try{
                String resp=httpGet("/events");
                if(resp.isEmpty())return;
                // Simple JSON array parsing: [{"type":"JOIN","secret":"..."},...]
                String inner=resp.trim();
                if(inner.startsWith("["))inner=inner.substring(1);
                if(inner.endsWith("]"))inner=inner.substring(0,inner.length()-1);
                if(inner.isEmpty())return;
                // Split objects
                int depth=0;StringBuilder cur=new StringBuilder();
                java.util.ArrayList<String> objs=new java.util.ArrayList<>();
                for(int i=0;i<inner.length();i++){
                    char ch=inner.charAt(i);
                    if(ch=='{'){depth++;cur.append(ch);}
                    else if(ch=='}'){depth--;cur.append(ch);if(depth==0){objs.add(cur.toString());cur.setLength(0);}}
                    else if(depth>0)cur.append(ch);
                }
                for(String ev:objs){
                    String type=parseJsonField(ev,"\"type\":\"");
                    if(type.equals("JOIN")){
                        String sec=parseJsonField(ev,"\"secret\":\"");
                        if(!sec.isEmpty())SwingUtilities.invokeLater(()->{addChat("Discord","Join accepted!");handleJoinSecret(sec);});
                    }else if(type.equals("REQUEST")){
                        String user=parseJsonField(ev,"\"user\":\"");
                        SwingUtilities.invokeLater(()->{
                            if(isHost&&server!=null&&server.isRunning()){
                                addChat("Discord",user+" wants to join! Code: "+serverPort);
                            }else{
                                addChat("Discord",user+" wants to join!");
                            }
                        });
                    }
                }
            }catch(Exception e){}
        }
        private String parseJsonField(String json,String key){
            int i=json.indexOf(key);if(i<0)return"";
            i+=key.length();
            int j=json.indexOf("\"",i);if(j<0)return"";
            return json.substring(i,j);
        }
        private void handleJoinSecret(String sec){
            try{
                if(isHost&&server!=null&&server.isRunning()){
                    addChat("Discord","Friend wants to join! Tell them code: "+serverPort);return;
                }
                if(screen==Screen.PLAY){addChat("Discord","Already in a game!");return;}
                if(client!=null&&client.isConnected()){addChat("Discord","Already connected!");return;}
                if(sec.startsWith("mc:")){
                    String rest=sec.substring(3);
                    if(rest.contains(":")){String[] p=rest.split(":");connectToServer(p[0],Integer.parseInt(p[1]));}
                    else{int pr=Integer.parseInt(rest);connectToServer("bore.pub",pr);}
                    addChat("Discord","Auto-connecting to server...");
                }
            }catch(Exception e){System.out.println("[RPC] Join error: "+e.getMessage());}
        }
        void updatePresence(){
            try{
                if(bridgeProcess!=null&&!bridgeProcess.isAlive()){
                    System.out.println("[RPC] Bridge died, restarting...");
                    startBridge();
                    for(int i=0;i<20&&running;i++){if(isBridgeReady())break;try{Thread.sleep(250);}catch(Exception e){break;}}
                }
                String state=screen==Screen.PLAY?(survival?"Survival":"Creative"):"In Menu";
                int partySize=1,partyMax=8;
                if(isHost&&server!=null&&server.isRunning()){partySize=server.getPlayerCount();}
                else if(client!=null&&client.isConnected()){partySize=remotePlayers.size()+1;}
                if(state.equals(lastState)&&partySize==lastPartySize)return;
                lastState=state;lastPartySize=partySize;
                String partyId="mc_"+worldName.hashCode()+"_"+serverPort;
                String joinSec="";String spectateSec="";String matchSec="";
                if(isHost&&server!=null&&server.isRunning()){
                    joinSec="mc:"+serverPort;
                    spectateSec="mc:spec:"+serverPort;
                    matchSec="mc:match:"+serverPort;
                }else if(client!=null&&client.isConnected()){
                    joinSec="mc:client:"+serverIP+":"+serverPort;
                }
                currentSecret=joinSec;
                StringBuilder sb=new StringBuilder();
                sb.append("{\"details\":\"MiniCraft v").append(VERSION).append("\",\"state\":\"").append(state).append("\",\"timestamps\":{\"start\":").append(startTime).append("},\"assets\":{\"large_image\":\"minecraft\",\"large_text\":\"MiniCraft\"},\"party\":{\"id\":\"").append(partyId).append("\",\"size\":[").append(partySize).append(",").append(partyMax).append("]}");
                if(!joinSec.isEmpty()){
                    sb.append(",\"secrets\":{\"join\":\"").append(joinSec).append("\",\"spectate\":\"").append(spectateSec).append("\",\"match\":\"").append(matchSec).append("\"}");
                }
                sb.append("}");
                currentJson=sb.toString();
                // Send pid + activity in wrapper object
                String wrapper="{\"pid\":"+ProcessHandle.current().pid()+",\"activity\":"+sb.toString()+"}";
                httpPost("/update",wrapper);
            }catch(Exception e){System.out.println("[RPC] Update error: "+e.getMessage());}
        }
        void stopRPC(){
            running=false;
            try{if(bridgeProcess!=null){bridgeProcess.destroyForcibly();bridgeProcess.waitFor(2,java.util.concurrent.TimeUnit.SECONDS);}}catch(Exception e){}
            this.interrupt();
        }
    }

    class RPCVisualizer extends JDialog{
        private JLabel statusLbl,partyLbl,timeLbl,detailsLbl,stateLbl,secLbl,codeLbl,inviteStatusLbl;
        private JTextArea jsonArea;
        private javax.swing.Timer timer;
        private JButton askBtn;
        RPCVisualizer(){
            super((JFrame)SwingUtilities.getWindowAncestor(MiniCraft.this),"Rich Presence Visualizer");
            setSize(460,620);setLocationRelativeTo(null);setLayout(null);getContentPane().setBackground(new Color(24,25,28));
            JLabel title=new JLabel("Discord Rich Presence Preview");title.setBounds(20,10,420,24);
            title.setFont(new Font("PixelPurl",Font.BOLD,16));title.setForeground(new Color(220,220,220));add(title);

            // Discord profile card
            JPanel card=new JPanel(null);card.setBounds(20,40,420,180);
            card.setBackground(new Color(48,49,54));card.setBorder(BorderFactory.createLineBorder(new Color(32,34,37)));
            JPanel img=new JPanel(){public void paintComponent(Graphics g){super.paintComponent(g);g.setColor(new Color(60,180,60));g.fillRect(0,0,60,60);g.setColor(Color.WHITE);g.setFont(new Font("Arial",Font.BOLD,20));g.drawString("MC",12,38);}};
            img.setBounds(12,12,60,60);img.setBackground(new Color(48,49,54));card.add(img);
            detailsLbl=new JLabel("MiniCraft v"+VERSION);detailsLbl.setBounds(82,12,320,20);
            detailsLbl.setFont(new Font("PixelPurl",Font.BOLD,14));detailsLbl.setForeground(Color.WHITE);card.add(detailsLbl);
            stateLbl=new JLabel("In Menu");stateLbl.setBounds(82,34,320,18);
            stateLbl.setFont(new Font("PixelPurl",Font.PLAIN,13));stateLbl.setForeground(new Color(180,180,180));card.add(stateLbl);
            partyLbl=new JLabel("1 of 8");partyLbl.setBounds(82,54,320,18);
            partyLbl.setFont(new Font("PixelPurl",Font.PLAIN,12));partyLbl.setForeground(new Color(140,140,140));card.add(partyLbl);
            timeLbl=new JLabel("00:00 elapsed");timeLbl.setBounds(82,74,320,18);
            timeLbl.setFont(new Font("PixelPurl",Font.PLAIN,12));timeLbl.setForeground(new Color(140,140,140));card.add(timeLbl);
            askBtn=new JButton("Ask to Join");askBtn.setBounds(82,100,110,24);
            askBtn.setFont(new Font("PixelPurl",Font.BOLD,10));askBtn.setForeground(new Color(100,200,100));
            askBtn.setBackground(new Color(48,49,54));askBtn.setFocusPainted(false);
            askBtn.setBorder(BorderFactory.createLineBorder(new Color(100,200,100)));
            askBtn.setEnabled(false);
            askBtn.addActionListener(e->{
                if(discordRPC!=null&&!discordRPC.currentSecret.isEmpty()){
                    SwingUtilities.invokeLater(()->{addChat("Discord","Join request received!");discordRPC.handleJoinSecret(discordRPC.currentSecret);});
                }
            });card.add(askBtn);
            JButton btn2=new JButton("Spectate");btn2.setBounds(200,100,90,24);
            btn2.setFont(new Font("PixelPurl",Font.BOLD,10));btn2.setForeground(new Color(100,200,100));
            btn2.setBackground(new Color(48,49,54));btn2.setFocusPainted(false);
            btn2.setBorder(BorderFactory.createLineBorder(new Color(100,200,100)));
            btn2.setEnabled(false);
            btn2.addActionListener(e->{addChat("Discord","Spectate not implemented yet");});card.add(btn2);
            add(card);

            // Invite section
            JLabel invTitle=new JLabel("Invite Friends");invTitle.setBounds(20,228,200,20);
            invTitle.setFont(new Font("PixelPurl",Font.BOLD,13));invTitle.setForeground(new Color(180,180,180));add(invTitle);
            inviteStatusLbl=new JLabel("Start a server to enable invites");inviteStatusLbl.setBounds(20,250,420,18);
            inviteStatusLbl.setFont(new Font("PixelPurl",Font.PLAIN,12));inviteStatusLbl.setForeground(new Color(140,140,140));add(inviteStatusLbl);
            codeLbl=new JLabel("Server Code: --");codeLbl.setBounds(20,272,300,18);
            codeLbl.setFont(new Font("Monospaced",Font.PLAIN,12));codeLbl.setForeground(new Color(100,200,100));add(codeLbl);
            secLbl=new JLabel("Secret: --");secLbl.setBounds(20,294,300,18);
            secLbl.setFont(new Font("Monospaced",Font.PLAIN,11));secLbl.setForeground(new Color(100,200,100));add(secLbl);
            JButton copyCodeBtn=new JButton("Copy Code");copyCodeBtn.setBounds(340,250,90,24);
            copyCodeBtn.setFont(new Font("PixelPurl",Font.PLAIN,10));copyCodeBtn.setBackground(new Color(60,60,70));
            copyCodeBtn.setForeground(Color.WHITE);copyCodeBtn.setFocusPainted(false);
            copyCodeBtn.addActionListener(e->{
                if(isHost&&server!=null&&server.isRunning()){
                    String invite="Join my MiniCraft server! Code: "+serverPort;
                    java.awt.datatransfer.StringSelection sel=new java.awt.datatransfer.StringSelection(invite);
                    java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel,null);
                    copyCodeBtn.setText("Copied!");new Thread(()->{try{Thread.sleep(1500);SwingUtilities.invokeLater(()->copyCodeBtn.setText("Copy Code"));}catch(Exception ex){}}).start();
                }
            });add(copyCodeBtn);
            JButton copyBtn=new JButton("Copy Secret");copyBtn.setBounds(340,280,90,24);
            copyBtn.setFont(new Font("PixelPurl",Font.PLAIN,10));copyBtn.setBackground(new Color(60,60,70));
            copyBtn.setForeground(Color.WHITE);copyBtn.setFocusPainted(false);
            copyBtn.addActionListener(e->{
                if(discordRPC!=null&&!discordRPC.currentSecret.isEmpty()){
                    java.awt.datatransfer.StringSelection sel=new java.awt.datatransfer.StringSelection(discordRPC.currentSecret);
                    java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel,null);
                    copyBtn.setText("Copied!");new Thread(()->{try{Thread.sleep(1500);SwingUtilities.invokeLater(()->copyBtn.setText("Copy Secret"));}catch(Exception ex){}}).start();
                }
            });add(copyBtn);

            // JSON viewer
            JLabel jsonTitle=new JLabel("Raw JSON Payload");jsonTitle.setBounds(20,324,200,20);
            jsonTitle.setFont(new Font("PixelPurl",Font.BOLD,13));jsonTitle.setForeground(new Color(180,180,180));add(jsonTitle);
            jsonArea=new JTextArea();jsonArea.setEditable(false);jsonArea.setLineWrap(true);jsonArea.setWrapStyleWord(true);
            jsonArea.setFont(new Font("Monospaced",Font.PLAIN,10));jsonArea.setBackground(new Color(32,34,37));
            jsonArea.setForeground(new Color(180,180,180));jsonArea.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            JScrollPane sp=new JScrollPane(jsonArea);sp.setBounds(20,346,420,180);add(sp);

            // Status bar
            statusLbl=new JLabel("Status: Checking...");statusLbl.setBounds(20,536,420,20);
            statusLbl.setFont(new Font("PixelPurl",Font.PLAIN,12));statusLbl.setForeground(new Color(140,140,140));add(statusLbl);
            JButton closeBtn=new JButton("Close");closeBtn.setBounds(180,560,100,28);
            closeBtn.setBackground(new Color(60,60,70));closeBtn.setForeground(Color.WHITE);closeBtn.setFocusPainted(false);
            closeBtn.addActionListener(e->dispose());add(closeBtn);
            timer=new javax.swing.Timer(500,e->refresh());
            timer.start();refresh();
            addWindowListener(new java.awt.event.WindowAdapter(){public void windowClosing(java.awt.event.WindowEvent e){timer.stop();}});
        }
        void refresh(){
            if(discordRPC!=null){
                statusLbl.setText("Status: Connected | PID "+ProcessHandle.current().pid());
                String j=discordRPC.currentJson;if(j==null)j="";
                jsonArea.setText(j.isEmpty()?"(waiting for update...)":j);
                
                boolean canInvite=isHost&&server!=null&&server.isRunning();
                askBtn.setEnabled(canInvite);
                if(canInvite){
                    inviteStatusLbl.setText("Hosting! Give friends the code below");
                    inviteStatusLbl.setForeground(new Color(100,200,100));
                    codeLbl.setText("Server Code: "+serverPort);
                }else{
                    inviteStatusLbl.setText("Host a world to get a server code");
                    inviteStatusLbl.setForeground(new Color(140,140,140));
                    codeLbl.setText("Server Code: --");askBtn.setEnabled(false);
                }
                try{
                    if(j.contains("\"state\":\"")){int i1=j.indexOf("\"state\":\"")+9;int i2=j.indexOf("\"",i1);stateLbl.setText(j.substring(i1,i2));}
                    if(j.contains("\"details\":\"")){int i1=j.indexOf("\"details\":\"")+11;int i2=j.indexOf("\"",i1);detailsLbl.setText(j.substring(i1,i2));}
                    if(j.contains("\"size\":[")){int i1=j.indexOf("\"size\":[")+8;int i2=j.indexOf("]",i1);partyLbl.setText(j.substring(i1,i2).replace(","," of "));
                    partyLbl.setText(partyLbl.getText()+" players");}
                }catch(Exception ex){}
            }else{
                statusLbl.setText("Status: Not connected. Use /rpc to start.");
                jsonArea.setText("");stateLbl.setText("In Menu");partyLbl.setText("1 of 1");secLbl.setText("Secret: --");
                codeLbl.setText("Server Code: --");inviteStatusLbl.setText("Start a server to enable invites");askBtn.setEnabled(false);
            }
            long elapsed=(discordRPC!=null&&discordRPC.startTime>0)?(System.currentTimeMillis()-discordRPC.startTime)/1000:0;
            timeLbl.setText(String.format("%02d:%02d elapsed",elapsed/60,elapsed%60));
        }
    }

    public MiniCraft(){
        setPreferredSize(new Dimension(VW*TILE,VH*TILE));
        setFocusable(true);
        addKeyListener(this);addMouseListener(this);addMouseMotionListener(this);addMouseWheelListener(this);
        loadTex();
        steveImg=new BufferedImage[1];
        try{steveImg[0]=javax.imageio.ImageIO.read(new File(System.getProperty("user.dir")+"/steve.png"));playerW=steveImg[0].getWidth();playerH=steveImg[0].getHeight();}catch(Exception ex){
        steveImg[0]=makeSteve();}
        heartImg=new BufferedImage[1];heartImg[0]=makeIcon(new Color(200,0,0),9);
        hungerImg=new BufferedImage[1];hungerImg[0]=makeIcon(new Color(180,120,40),9);
        try{
            File gifFile=new File(System.getProperty("user.dir")+"/MINICRAFT.gif");
            if(gifFile.exists()){
                javax.imageio.stream.ImageInputStream iis=javax.imageio.ImageIO.createImageInputStream(gifFile);
                javax.imageio.ImageReader reader=javax.imageio.ImageIO.getImageReadersByFormatName("gif").next();
                reader.setInput(iis);int n=reader.getNumImages(true);
                logoFrames=new BufferedImage[n];
                for(int i=0;i<n;i++)logoFrames[i]=reader.read(i);
                reader.dispose();iis.close();
            }
        }catch(Exception e){
            try{logoImg=javax.imageio.ImageIO.read(new File(System.getProperty("user.dir")+"/MINICRAFT.png"));}catch(Exception e2){logoImg=null;}
        }
        try{discIcon=javax.imageio.ImageIO.read(new File(System.getProperty("user.dir")+"/discord.png"));}catch(Exception e){discIcon=null;}
        try{ghIcon=javax.imageio.ImageIO.read(new File(System.getProperty("user.dir")+"/github.png"));}catch(Exception e){ghIcon=null;}
        new File(DATA_DIR).mkdirs();
        try{GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(Font.createFont(Font.TRUETYPE_FONT,new File(System.getProperty("user.dir")+"/PixelPurl.ttf")));}catch(Exception e){}
        refreshWorldList();
        loadSettings();
        try{robot=new java.awt.Robot();robotOk=true;}catch(Exception e){robotOk=false;}
        timer=new javax.swing.Timer(16,this);timer.start();
        new Thread(()->checkUpdate()).start();
        new Thread(()->{
            try{Thread.sleep(500);}
            catch(Exception e){}
            try{loadMusic();}catch(Exception e){System.out.println("[Auto] Music error: "+e.getMessage());}
            try{loadSFX();}catch(Exception e){System.out.println("[Auto] SFX error: "+e.getMessage());}
            try{
                discordRPC=new DiscordRPC();
                discordRPC.setDaemon(true);
                discordRPC.start();
                System.out.println("[Auto] RPC started");
            }catch(Exception e){System.out.println("[Auto] RPC error: "+e.getMessage());}
            try{loadMods();}catch(Exception e){System.out.println("[Auto] Mods error: "+e.getMessage());}
        }).start();
    }

    private BufferedImage makeIcon(Color c,int s){BufferedImage img=new BufferedImage(s,s,BufferedImage.TYPE_INT_ARGB);Graphics2D g=img.createGraphics();g.setColor(c);g.fillOval(1,1,s-2,s-2);g.setColor(c.brighter());g.fillOval(2,1,s-4,s-3);g.dispose();return img;}
    private BufferedImage makeSteve(){
        BufferedImage img=new BufferedImage(TILE-4,TILE-4,BufferedImage.TYPE_INT_ARGB);Graphics2D g=img.createGraphics();
        int[][] p={{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0},{0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0},{0,0,0,0,1,1,1,1,1,1,5,5,1,1,1,1,5,5,1,1,1,1,1,0,0,0,0,0},{0,0,0,0,1,1,1,1,5,5,6,6,5,5,5,5,6,6,5,5,1,1,1,1,0,0,0,0},{0,0,0,0,1,1,1,5,6,6,6,6,5,5,5,5,6,6,6,6,5,1,1,1,0,0,0,0},{0,0,0,0,1,1,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,1,1,0,0,0,0},{0,0,0,0,0,1,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,1,0,0,0,0,0},{0,0,0,0,0,0,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,0,0,0,0,0,0},{0,0,0,0,0,0,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,0,0,0,0,0,0},{0,0,0,0,0,0,4,4,4,4,4,4,3,3,3,3,4,4,4,4,4,4,0,0,0,0,0,0},{0,0,0,0,0,0,4,4,4,4,4,3,3,3,3,3,3,4,4,4,4,4,0,0,0,0,0,0},{0,0,0,0,0,0,4,4,4,4,3,3,7,7,7,7,3,3,4,4,4,4,0,0,0,0,0,0},{0,0,0,0,0,0,4,4,4,3,3,7,7,7,7,7,7,3,3,4,4,4,0,0,0,0,0,0},{0,0,0,0,0,0,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,0,0,0,0,0,0},{0,0,0,0,0,0,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,0,0,0,0,0,0},{0,0,0,0,0,0,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,0,0,0,0,0,0},{0,0,0,0,0,0,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,0,0,0,0,0,0},{0,0,0,0,0,0,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,0,0,0,0,0,0},{0,0,0,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0},{0,0,0,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0},{0,0,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0},{0,0,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0},{0,0,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0},{0,0,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
        int[] c={0,0x8B5E3C,0x3B7A9E,0xF0E8D8,0x1A1A1A,0x6B3A1F,0xFFFFFF,0x000000};
        for(int yy=0;yy<p.length;yy++)for(int xx=0;xx<p[yy].length;xx++){int v=p[yy][xx];if(v==0)continue;g.setColor(new Color(c[v]));g.fillRect(xx*(TILE-4)/28,yy*(TILE-4)/28,(TILE-4)/28+1,(TILE-4)/28+1);}
        g.dispose();return img;
    }

    private void checkUpdate(){
        try{
            java.net.URL url=java.net.URI.create(GITHUB_API).toURL();
            java.net.HttpURLConnection conn=(java.net.HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);conn.setReadTimeout(5000);
            BufferedReader br=new BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
            String line;StringBuilder sb=new StringBuilder();
            while((line=br.readLine())!=null)sb.append(line);
            br.close();
            String json=sb.toString();
            int ci=json.indexOf("\"sha\":\"");if(ci<0)return;
            String latestSha=json.substring(ci+7,ci+15);
            String localSha="";
            File sf=new File(System.getProperty("user.dir")+"/src/MiniCraft.java");
            if(sf.exists()){
                BufferedReader lr=new BufferedReader(new FileReader(sf));
                String fl;while((fl=lr.readLine())!=null){if(fl.contains("//sha:")){localSha=fl.replaceAll(".*//sha:","").trim();break;}}
                lr.close();
            }
            if(!localSha.isEmpty()&&localSha.equals(latestSha))return;
            updateAvailable=true;updateVersion=latestSha.substring(0,8);
        }catch(Exception e){}
    }

    private void doUpdate(){
        new Thread(()->{
        try{
            java.net.URL url=java.net.URI.create(GITHUB_RAW).toURL();
            java.net.HttpURLConnection conn=(java.net.HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(10000);conn.setReadTimeout(10000);
            if(conn.getResponseCode()!=200){showPopup("Download failed (HTTP "+conn.getResponseCode()+")");return;}
            BufferedReader br=new BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
            StringBuilder sb=new StringBuilder();
            String line;while((line=br.readLine())!=null)sb.append(line).append("\n");
            br.close();
            String code="//sha:"+updateVersion+"\n"+sb.toString();
            FileWriter fw=new FileWriter(System.getProperty("user.dir")+"/src/MiniCraft.java");
            fw.write(code);fw.close();
            showPopup("Downloaded. Compiling...");
            String dir=System.getProperty("user.dir");
            String javaHome=System.getProperty("java.home");
            String javac=javaHome.replace("jre","")+"/bin/javac";
            if(File.separatorChar=='\\')javac=javaHome.replace("jre","")+"\\bin\\javac.exe";
            ProcessBuilder pb=new ProcessBuilder(javac,"-d",dir+"/build",dir+"/src/MiniCraft.java");
            pb.directory(new File(dir));pb.redirectErrorStream(true);
            Process p=pb.start();
            BufferedReader perr=new BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
            String el,errors="";while((el=perr.readLine())!=null)errors+=el+"\n";
            p.waitFor();
            if(p.exitValue()!=0){showPopup("Compile failed:\n"+errors);return;}
            showPopup("Done! Restarting...");
            String java=javaHome.replace("jre","")+"/bin/java";
            if(File.separatorChar=='\\')java+=".exe";
            try{new ProcessBuilder(java,"-cp",dir+"/build","MiniCraft").directory(new File(dir)).start();}catch(Exception e2){}
            Thread.sleep(800);
            System.exit(0);
        }catch(Exception e){showPopup("Error: "+e.getMessage());}
        }).start();
    }
    private void showPopup(String msg){
        SwingUtilities.invokeLater(()->JOptionPane.showMessageDialog(this,msg,"MiniCraft Update",JOptionPane.INFORMATION_MESSAGE));
    }

    private void loadTex(){tex=new BufferedImage[BLOCK_COUNT];for(int i=0;i<BLOCK_COUNT;i++){try{tex[i]=javax.imageio.ImageIO.read(new File(TEX_DIR+TF[i]+".png"));
        if(i==WATER||i==LAVA){BufferedImage wt=new BufferedImage(TILE,TILE,BufferedImage.TYPE_INT_ARGB);Graphics2D g=wt.createGraphics();g.drawImage(tex[i],0,0,null);g.setColor(i==LAVA?new Color(255,100,20,150):new Color(60,60,200,120));g.fillRect(0,0,TILE,TILE);g.dispose();tex[i]=wt;}
        if(i>=COAL_ORE&&i<=COPPER_ORE){BufferedImage ot=new BufferedImage(TILE,TILE,BufferedImage.TYPE_INT_ARGB);Graphics2D g=ot.createGraphics();g.drawImage(tex[i],0,0,null);Color oc=FB[i];g.setColor(new Color(oc.getRed(),oc.getGreen(),oc.getBlue(),80));g.fillRect(0,0,TILE,TILE);g.dispose();tex[i]=ot;}
    }catch(Exception e){tex[i]=new BufferedImage(TILE,TILE,BufferedImage.TYPE_INT_ARGB);Graphics2D g=tex[i].createGraphics();g.setColor(FB[i]);g.fillRect(0,0,TILE,TILE);g.dispose();}}}
    private void refreshWorldList(){worldList.clear();File[] f=new File(DATA_DIR).listFiles((d,n)->n.endsWith(".mcw"));if(f!=null)for(File x:f)worldList.add(x.getName().replace(".mcw",""));}

    private void saveSettings(){
        try{PrintWriter pw=new PrintWriter(new FileWriter(System.getProperty("user.dir")+"/settings.txt"));
            pw.println("showFps="+showFps);pw.println("showCoords="+showCoords);pw.println("showDebug="+showDebug);pw.println("noclip="+noclip);
            pw.println("fullscreen="+fullscreen);pw.println("fov="+gameFov);pw.println("name="+playerName);            pw.println("ultraFps="+ultraFps);            pw.println("rtxMode="+rtxMode);            pw.println("rtxWater="+rtxWater);            pw.println("physicsLevel="+physicsLevel);            pw.println("shaderMode="+shaderMode);
            pw.close();
        }catch(Exception e){}
    }

    private void loadSettings(){
        try{BufferedReader br=new BufferedReader(new FileReader(System.getProperty("user.dir")+"/settings.txt"));
            String line;
            while((line=br.readLine())!=null){
                String[] p=line.split("=",2);if(p.length<2)continue;
                if(p[0].equals("showFps"))showFps=Boolean.parseBoolean(p[1]);
                if(p[0].equals("showCoords"))showCoords=Boolean.parseBoolean(p[1]);
                if(p[0].equals("showDebug"))showDebug=Boolean.parseBoolean(p[1]);
                if(p[0].equals("noclip"))noclip=Boolean.parseBoolean(p[1]);
                if(p[0].equals("fullscreen"))fullscreen=Boolean.parseBoolean(p[1]);
                if(p[0].equals("fov"))gameFov=Integer.parseInt(p[1]);
                if(p[0].equals("ultraFps"))ultraFps=Boolean.parseBoolean(p[1]);
                if(p[0].equals("rtxMode"))rtxMode=Boolean.parseBoolean(p[1]);
                if(p[0].equals("rtxWater"))rtxWater=Boolean.parseBoolean(p[1]);
                if(p[0].equals("physicsLevel")){physicsLevel=Integer.parseInt(p[1]);physicsOn=physicsLevel>0;}
                if(p[0].equals("shaderMode"))shaderMode=Integer.parseInt(p[1]);
                if(p[0].equals("bedrockEdition"))bedrockEdition=Boolean.parseBoolean(p[1]);
                if(p[0].equals("name"))playerName=p[1];
            }
            br.close();
        }catch(Exception e){}
    }

    private void startWebServer(){
        try{String dir=System.getProperty("user.dir");
            webProcess=new ProcessBuilder("python3",dir+"/webserver.py").redirectErrorStream(true).start();
            boreProcess=new ProcessBuilder("bore","local","25565","--to","bore.pub").redirectErrorStream(true).start();
            new Thread(()->{
                try{java.io.BufferedReader r=new java.io.BufferedReader(new java.io.InputStreamReader(boreProcess.getInputStream()));
                    String l;while((l=r.readLine())!=null){if(l.contains("listening at")){serverPort=Integer.parseInt(l.replaceAll(".*:","").trim());break;}}
                }catch(Exception e){}
            }).start();
            try{new ProcessBuilder("bore","local","25568","--to","bore.pub").start();}catch(Exception e){}
        }catch(Exception e){}
    }

    private void loadMusic(){
        try{
            File mdir=new File(System.getProperty("user.dir")+"/music");
            if(!mdir.exists())return;
            File[] files=mdir.listFiles((d,n)->n.endsWith(".wav")||n.endsWith(".mp3")||n.endsWith(".mp4")||n.endsWith(".ogg"));
            if(files==null||files.length==0){System.out.println("No music files found");return;}
            musicFiles.clear();
            for(File f:files){musicFiles.add(f.getAbsolutePath());System.out.println("Music: "+f.getName());}
            playNext();
        }catch(Exception e){e.printStackTrace();}
    }

    private void playNext(){
        if(!musicOn||musicFiles.isEmpty())return;
        new Thread(()->{
            try{
                String path=musicFiles.get((int)(Math.random()*musicFiles.size()));
                System.out.println("Playing: "+path);
                if(!path.endsWith(".wav")){
                    File tmp=File.createTempFile("mc_music",".wav");
                    Process p=Runtime.getRuntime().exec(new String[]{"ffmpeg","-i",path,"-acodec","pcm_s16le","-ar","11025","-ac","1","-y",tmp.getAbsolutePath()});
                    p.waitFor();
                    if(!tmp.exists()||tmp.length()<100){playNext();return;}
                    path=tmp.getAbsolutePath();
                    tmp.deleteOnExit();
                }
                javax.sound.sampled.AudioInputStream ais=javax.sound.sampled.AudioSystem.getAudioInputStream(new File(path));
                if(musicClip!=null){musicClip.stop();musicClip.close();}
                musicClip=javax.sound.sampled.AudioSystem.getClip();
                musicClip.open(ais);
                musicClip.start();
                musicClip.addLineListener(ev->{if(ev.getType()==javax.sound.sampled.LineEvent.Type.STOP)playNext();});
            }catch(Exception e){e.printStackTrace();playNext();}
        }).start();
    }

    private void toggleMusic(){
        musicOn=!musicOn;
        if(musicOn)playNext();
        else if(musicClip!=null){musicClip.stop();musicClip.close();}
    }

    private void toggleFullscreen(){
        fullscreen=!fullscreen;
        JFrame f=(JFrame)SwingUtilities.getWindowAncestor(this);
        if(f==null)return;
        try{
            new ProcessBuilder("hyprctl","dispatch","fullscreen",fullscreen?"1":"0").start();
            return;
        }catch(Exception e1){}
        try{
            f.setVisible(false);f.dispose();
            f.setUndecorated(fullscreen);
            f.setVisible(true);
            if(fullscreen){
                java.awt.GraphicsDevice gd=java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                if(gd.isFullScreenSupported())gd.setFullScreenWindow(f);
                else f.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }else{
                java.awt.GraphicsDevice gd=java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                if(gd!=null)gd.setFullScreenWindow(null);
                f.setSize(VW*TILE,VH*TILE);f.setLocationRelativeTo(null);
            }
            f.requestFocus();
        }catch(Exception e2){}
    }

    private void loadSFX(){
        try{
            File sdir=new File(System.getProperty("user.dir")+"/sounds");
            if(!sdir.exists())return;
            File[] files=sdir.listFiles((d,n)->n.endsWith(".wav"));
            if(files==null||files.length==0)return;
            sfxFiles.clear();
            for(File f:files)sfxFiles.add(f.getAbsolutePath());
        }catch(Exception e){}
    }

    private void playSFX(String type){
        new Thread(()->{
            try{
                java.util.ArrayList<String> matches=new java.util.ArrayList<>();
                for(String f:sfxFiles)if(f.contains(type))matches.add(f);
                if(matches.isEmpty())return;
                String f=matches.get((int)(Math.random()*matches.size()));
                javax.sound.sampled.Clip c=javax.sound.sampled.AudioSystem.getClip();
                c.open(javax.sound.sampled.AudioSystem.getAudioInputStream(new File(f)));
                c.start();
                c.addLineListener(ev->{if(ev.getType()==javax.sound.sampled.LineEvent.Type.STOP)c.close();});
            }catch(Exception e){}
        }).start();
    }

    private void playSound(String exact){
        new Thread(()->{
            try{
                String f=null;
                for(String sf:sfxFiles)if(sf.contains(exact)){f=sf;break;}
                if(f==null)return;
                javax.sound.sampled.Clip c=javax.sound.sampled.AudioSystem.getClip();
                c.open(javax.sound.sampled.AudioSystem.getAudioInputStream(new File(f)));
                c.start();
                c.addLineListener(ev->{if(ev.getType()==javax.sound.sampled.LineEvent.Type.STOP)c.close();});
            }catch(Exception e){}
        }).start();
    }

    private void stopWebServer(){
        if(boreProcess!=null){boreProcess.destroy();boreProcess=null;}
        if(webProcess!=null){webProcess.destroy();webProcess=null;}
    }

    private void achieve(String m){achieves.add(new Achieve(m));}

    private void deathDrop(){
        for(int i=0;i<inv.length;i++)while(inv[i]>0){drops.add(new DropItem(px+Math.random()*64-32,py+Math.random()*32-16,inv[i]));invCount[i]--;if(invCount[i]<=0)inv[i]=0;}
    }

    private void loadMods(){
        try{
            File mdir=new File(System.getProperty("user.dir")+"/mods");
            if(!mdir.exists()){mdir.mkdir();return;}
            File[] files=mdir.listFiles((d,n)->n.endsWith(".class"));
            if(files==null||files.length==0)return;
            java.net.URL[] urls={mdir.toURI().toURL()};
            java.net.URLClassLoader cl=new java.net.URLClassLoader(urls);
            for(File f:files){
                try{String n=f.getName().replace(".class","");cl.loadClass(n);System.out.println("[MiniCraft] Mod loaded: "+n);}catch(Exception e){}
            }
        }catch(Exception e){}
    }

    private void genWorld(long seed){
        world=new int[W][H];bgWorld=new int[W][H];Random r=new Random(seed);
        if(superflat){
            for(int x=0;x<W;x++)for(int y=0;y<H;y++)world[x][y]=0;
            for(int x=0;x<W;x++){world[x][H-1]=BEDROCK;world[x][H-2]=DIRT;world[x][H-3]=DIRT;world[x][H-4]=GRASS;}
        }else if(inNether){
            for(int x=0;x<W;x++)for(int y=0;y<H;y++){
                if(y<H/4)world[x][y]=0;
                else if(y==H/4&&r.nextInt(3)==0)world[x][y]=GLOWSTONE;
                else if(y>H-4)world[x][y]=LAVA;
                else world[x][y]=r.nextInt(6)==0?SOUL_SAND:NETHERRACK;
            }
        }else{
        int[] hm=new int[W];
        hm[0]=H/3+r.nextInt(H/3);
        for(int x=1;x<W;x++){
            int ch=r.nextInt(5)-2;
            if(r.nextInt(8)==0)ch+=r.nextInt(5)-2;
            hm[x]=Math.max(H/6,Math.min(H*2/3,hm[x-1]+ch));
        }
        for(int x=0;x<W;x++){
            int s=hm[x];int bedrock=H-r.nextInt(3);
            for(int y=0;y<H;y++){
                if(y==s)world[x][y]=GRASS;
                else if(y>s&&y<=s+4)world[x][y]=DIRT;
                else if(y>s+4&&y<bedrock)world[x][y]=r.nextInt(8)==0?COBBLESTONE:STONE;
                if(y==bedrock-1&&r.nextInt(3)==0)world[x][y]=LAVA;
                else if(y>=bedrock)world[x][y]=BEDROCK;
            }
            if(x>2&&x<W-3&&s>hm[x-1]+2&&s>hm[x+1]+2){
                int wl=Math.min(hm[x-1],hm[x+1])+1;
                for(int y=wl;y<s;y++)world[x][y]=WATER;
            }
            int beach=r.nextInt(30);
            if(beach<2&&s<H/2-2){
                for(int by=s;by<=s+2;by++)if(by>=0&&by<H)world[x][by]=SAND;
            }
            if(s<H-6&&r.nextInt(12)==0&&x>3&&x<W-4&&world[x][s-1]!=WATER){
                int tt=r.nextInt(6),lg=OAK_LOG+tt,lf=OAK_LEAVES+tt;
                int th=4+r.nextInt(5);
                boolean wide=th>6;
                // Trunk
                for(int ty=s-1;ty>s-th-1&&ty>=0;ty--){world[x][ty]=lg;if(bgWorld!=null&&r.nextInt(3)==0){int sx=x+(r.nextBoolean()?1:-1);if(sx>=0&&sx<W&&bgWorld[sx][ty]==0)bgWorld[sx][ty]=SHELF_MUSHROOM;}}
                if(wide&&x+1<W)for(int ty=s-1;ty>s-th/2-1&&ty>=0;ty--)world[x+1][ty]=lg;
                // Leaves - rounded canopy
                int crownY=s-th/2;
                int crownR=th/2+1;
                for(int lx=x-crownR-1;lx<=x+crownR+(wide?2:1);lx++){
                    for(int ly=crownY-crownR;ly<=s-1;ly++){
                        if(lx>=0&&lx<W&&ly>=0&&ly<H&&world[lx][ly]==0){
                            int dx=lx-(x+(wide?1:0)),dy=ly-crownY;
                            double dist=Math.sqrt(dx*dx+dy*dy*0.8);
                            if(dist<=crownR+0.5)world[lx][ly]=lf;
                        }
                    }
                }
            }
        }
        for(int x=3;x<W-3;x++)for(int y=H/2;y<H-3;y++){
            if(world[x][y]==STONE||world[x][y]==COBBLESTONE){
                int v=r.nextInt(80);
                if(v<1&&y>H*3/4)world[x][y]=DIAMOND_ORE;
                else if(v<2&&y>H*3/4)world[x][y]=EMERALD_ORE;
                else if(v<5&&y>H/2+8)world[x][y]=GOLD_ORE;
                else if(v<9&&y>H/2+3)world[x][y]=REDSTONE_ORE;
                else if(v<12&&y>H/2+3)world[x][y]=LAPIS_ORE;
                else if(v<22)world[x][y]=IRON_ORE;
                else if(v<30)world[x][y]=COPPER_ORE;
                else if(v<45)world[x][y]=COAL_ORE;
            }
        }
        for(int x=2;x<W-2;x++)for(int y=getHeight(x)+10;y<H-3;y++){
            if((world[x][y]==STONE||world[x][y]==COBBLESTONE)&&r.nextInt(50)==0){
                int sz=2+r.nextInt(4);
                for(int cx=x;cx<x+sz&&cx<W;cx++)for(int cy=y;cy<y+sz/2&&cy<H;cy++)if(cx>=0&&cx<W&&cy>=0&&cy<H)world[cx][cy]=0;
            }
        }
        for(int i=0;i<3;i++){
            int dx=10+r.nextInt(W-20),dy=H/2+r.nextInt(H/3);
            if(world[dx][dy]==STONE){
                for(int x=dx-2;x<=dx+2;x++)for(int y=dy-2;y<=dy+2;y++)
                    if(x>=0&&x<W&&y>=0&&y<H)world[x][y]=0;
                if(dy+2<H)world[dx][dy+2]=CHEST;
                world[dx-2][dy]=COBBLESTONE;world[dx+2][dy]=COBBLESTONE;
                world[dx][dy-2]=COBBLESTONE;world[dx][dy+2]=COBBLESTONE;
                for(int x=dx-1;x<=dx+1;x++)for(int y=dy-1;y<=dy+1;y++)
                    if(x>=0&&x<W&&y>=0&&y<H)world[x][y]=0;
                if(dy<H-1)world[dx][dy]=COAL_ORE;
            }
        }
        }
        px=W/2.0*TILE;py=getGround(W/2)*TILE-playerH/2;health=20;hunger=20;dead=false;fallDist=0;playerVy=0;survival=true;
        for(int i=0;i<inv.length;i++){inv[i]=0;invCount[i]=0;}
        mobs.clear();
        for(int i=0;i<8;i++){
            int mx=10+(int)(Math.random()*(W-20)),my=getGround(mx);
            if(world[mx][my]==GRASS)mobs.add(new Mob(mx*TILE,my*TILE-playerH/2,(int)(Math.random()*4)));
        }
        for(int i=0;i<3;i++){
            int mx=10+(int)(Math.random()*(W-20)),my=getGround(mx);
            if(world[mx][my]==GRASS)mobs.add(new Mob(mx*TILE,my*TILE-playerH/2,5));
        }
    }

    private int getGround(int x){
        for(int y=0;y<H;y++){
            int b=world[x][y];
            if(b>0&&b!=OAK_LOG&&b!=SPRUCE_LOG&&b!=BIRCH_LOG&&b!=JUNGLE_LOG&&b!=ACACIA_LOG&&b!=DARK_OAK_LOG&&b!=OAK_LEAVES&&b!=SPRUCE_LEAVES&&b!=BIRCH_LEAVES&&b!=JUNGLE_LEAVES&&b!=ACACIA_LEAVES&&b!=DARK_OAK_LEAVES&&b!=WATER)return y;
        }
        return H-1;
    }

    private int getHeight(int x){for(int y=0;y<H;y++)if(world[x][y]>0)return y;return H-1;}
    private boolean isFlammable(int b){
        return b==OAK_LOG||b==SPRUCE_LOG||b==BIRCH_LOG||b==JUNGLE_LOG||b==ACACIA_LOG||b==DARK_OAK_LOG
            ||b==OAK_PLANKS||b==SPRUCE_PLANKS||b==BIRCH_PLANKS||b==JUNGLE_PLANKS||b==ACACIA_PLANKS||b==DARK_OAK_PLANKS
            ||b==OAK_LEAVES||b==SPRUCE_LEAVES||b==BIRCH_LEAVES||b==JUNGLE_LEAVES||b==ACACIA_LEAVES||b==DARK_OAK_LEAVES
            ||b==WOOL||b==STRAW_BED||b==CUSHION||b==MANGROVE_LOG||b==MANGROVE_PLANKS||b==MANGROVE_LEAVES
            ||b==CHERRY_LOG||b==CHERRY_PLANKS||b==CHERRY_LEAVES||b==BAMBOO_BLOCK||b==BAMBOO_PLANKS||b==BAMBOO_MOSAIC
            ||b==CRIMSON_STEM||b==CRIMSON_PLANKS||b==WARPED_STEM||b==WARPED_PLANKS
            ||b==SHELF_MUSHROOM||b==AZALEA_LEAVES||b==FLOWERING_AZALEA;
    }
    private boolean isSolid(int x,int y){if(x<0||x>=W||y<0||y>=H)return true;return world[x][y]>0&&world[x][y]!=WATER&&world[x][y]!=LAVA;}
    private boolean isIn(int x,int y){return x>=0&&x<W&&y>=0&&y<H;}

    private void saveWorld(String name){
        try{DataOutputStream d=new DataOutputStream(new FileOutputStream(DATA_DIR+name+".mcw"));
            d.writeUTF(name);d.writeUTF(playerName);d.writeLong(worldSeed);
            d.writeInt((int)px);d.writeInt((int)py);d.writeInt(health);d.writeInt(hunger);d.writeInt(survival?1:0);
            for(int i=0;i<inv.length;i++){d.writeInt(inv[i]);d.writeInt(invCount[i]);}
            for(int x=0;x<W;x++)for(int y=0;y<H;y++){d.writeInt(world[x][y]);d.writeInt(bgWorld[x][y]);}
            d.close();worldName=name;
        }catch(Exception e){e.printStackTrace();}
    }

    private boolean loadWorld(String name){
        try{DataInputStream d=new DataInputStream(new FileInputStream(DATA_DIR+name+".mcw"));
            worldName=d.readUTF();playerName=d.readUTF();worldSeed=d.readLong();
            px=d.readInt();py=d.readInt();health=d.readInt();hunger=d.readInt();survival=d.readInt()==1;
            for(int i=0;i<inv.length;i++){inv[i]=d.readInt();invCount[i]=d.readInt();}
            world=new int[W][H];bgWorld=new int[W][H];for(int x=0;x<W;x++)for(int y=0;y<H;y++){world[x][y]=d.readInt();bgWorld[x][y]=d.readInt();}
            d.close();dead=false;fallDist=0;craftingOpen=false;screen=Screen.PLAY;return true;
        }catch(Exception e){return false;}
    }

    private void deleteWorld(String name){new File(DATA_DIR+name+".mcw").delete();refreshWorldList();}

    private String lastMsg="";
    private int msgTimer=0;

    @Override
    public void actionPerformed(ActionEvent e){
        if(screen==Screen.MENU||screen==Screen.WORLD_LIST||screen==Screen.CREATE_WORLD||screen==Screen.MULTIPLAYER||screen==Screen.CONNECT||screen==Screen.HOST||screen==Screen.CONNECTING||screen==Screen.SETTINGS||screen==Screen.PAUSE||screen==Screen.HELP){repaint();return;}
        if(screen==Screen.DEATH||screen==Screen.CRAFTING){repaint();return;}
        if(screen!=Screen.PLAY)return;
        double speed=survival&&hunger<=0?1.5:3.0;
        if(keys[KeyEvent.VK_SHIFT])speed*=1.6;
        int pfoot=(int)((py+playerH/2)/TILE);
        if(pfoot>=0&&pfoot<H&&(world[(int)(px/TILE)][pfoot]==WATER||world[(int)(px/TILE)][pfoot]==LAVA)){speed*=0.4;if(world[(int)(px/TILE)][pfoot]==LAVA&&hungerTimer%30==0){health--;if(health<=0){dead=true;deathDrop();screen=Screen.DEATH;}}}
        double dx=0,dy=0;
        if(threeDMode){
            if(keys[KeyEvent.VK_W]||keys[KeyEvent.VK_UP]){dx+=Math.cos(playerDir)*speed;dy+=Math.sin(playerDir)*speed;}
            if(keys[KeyEvent.VK_S]||keys[KeyEvent.VK_DOWN]){dx-=Math.cos(playerDir)*speed;dy-=Math.sin(playerDir)*speed;}
            if(keys[KeyEvent.VK_A]||keys[KeyEvent.VK_LEFT]){dx+=Math.cos(playerDir-Math.PI/2)*speed;dy+=Math.sin(playerDir-Math.PI/2)*speed;}
            if(keys[KeyEvent.VK_D]||keys[KeyEvent.VK_RIGHT]){dx+=Math.cos(playerDir+Math.PI/2)*speed;dy+=Math.sin(playerDir+Math.PI/2)*speed;}
        }else{
            if(keys[KeyEvent.VK_A]||keys[KeyEvent.VK_LEFT])dx-=speed;
            if(keys[KeyEvent.VK_D]||keys[KeyEvent.VK_RIGHT])dx+=speed;
            if(keys[KeyEvent.VK_S]||keys[KeyEvent.VK_DOWN]){if(!survival||noclip)dy+=speed;else speed*=0.5;}
        }
        if((keys[KeyEvent.VK_W]||keys[KeyEvent.VK_UP]||keys[KeyEvent.VK_SPACE])&&!threeDMode){if(!survival||noclip)dy-=speed;else if(playerVy==0){boolean jg=false;for(int gx=-playerW/2;gx<=playerW/2;gx+=8){int fx=(int)((px+gx)/TILE),fy=(int)((py+playerH/2+2)/TILE);if(isSolid(fx,fy)){jg=true;break;}}if(jg)playerVy=-8;}}
        // Fire spread & damage
        if(rand.nextInt(5)==0){
            for(int fx=0;fx<W;fx++){
                for(int fy=0;fy<H;fy++){
                    if(world[fx][fy]==FIRE||world[fx][fy]==SOUL_FIRE){
                        if(Math.abs(px-(fx*TILE+TILE/2))<20&&Math.abs(py-(fy*TILE+TILE/2))<20){
                            if(survival){health-=1;if(health<=0){dead=true;deathDrop();screen=Screen.DEATH;}}}
                        if(rand.nextInt(8)==0){
                            int nx=fx+rand.nextInt(3)-1;
                            int ny=fy+rand.nextInt(3)-1;
                            if(isIn(nx,ny)&&world[nx][ny]==AIR){
                                int below=ny+1<H?world[nx][ny+1]:0;
                                if(below>0&&below!=BEDROCK&&below!=WATER){
                                    boolean soul=world[fx][fy]==SOUL_FIRE||(below==SOUL_SAND||below==SOUL_SOIL);
                                    world[nx][ny]=soul?SOUL_FIRE:FIRE;
                                    syncBlock(nx,ny,world[nx][ny]);
                                }
                            }
                        }
                        if(rand.nextInt(60)==0||world[fx][Math.max(0,fy-1)]==WATER){
                            world[fx][fy]=AIR;syncBlock(fx,fy,0);
                        }
                    }
                }
            }
        }
        boolean moving=dx!=0||dy!=0;walking=moving;
        if(keys[KeyEvent.VK_SHIFT]&&moving&&!ultraFps&&Math.random()<0.3)particles.add(new Particle(px-playerW/2-Math.random()*playerW,py+playerH/2,COBBLESTONE));
        if(moving){walkSoundTimer++;if(walkSoundTimer>20){walkSoundTimer=0;playSFX("grass");}}
        if(moving)hungerTimer++;
        double nx=px+dx,ny=py+dy;
        boolean canX=true,canY=true;
        if(!noclip){
            int hw=playerW/2, hh=playerH/2;
            int x1=(int)((nx-hw)/TILE),x2=(int)((nx+hw)/TILE),yt=(int)((py-hh)/TILE),yb=(int)((py+hh)/TILE);
            for(int tx=x1;tx<=x2;tx++)for(int ty=yt;ty<=yb;ty++)if(isSolid(tx,ty))canX=false;
            int y1=(int)((ny-hh)/TILE),y2=(int)((ny+hh)/TILE),xl=(int)((px-hw)/TILE),xr=(int)((px+hw)/TILE);
            for(int tx=xl;tx<=xr;tx++)for(int ty=y1;ty<=y2;ty++)if(isSolid(tx,ty))canY=false;
        }
        if(canX)px=nx;if(canY)py=ny;
        px=Math.max(0,Math.min(W*TILE-TILE,px));py=Math.max(0,Math.min(H*TILE-TILE,py));
        if(survival){if(moving&&hungerTimer%30==0&&hunger>0)hunger--;if(hunger>17&&health<20&&hungerTimer%10==0)health++;}
        boolean og=false;
        for(int gx=-playerW/2;gx<=playerW/2;gx+=8){
            int fx=(int)((px+gx)/TILE),fy=(int)((py+playerH/2)/TILE);
            if(isSolid(fx,fy)){og=true;break;}
        }
        if(elytraFly>0){playerVy*=.8;elytraFly--;}
        if(survival&&!og&&!noclip&&physicsOn){playerVy+=0.2;py+=playerVy;fallDist++;}else if(survival&&!noclip){if(playerVy>18){health-=(int)(playerVy-18)/10;if(health<=0){dead=true;deathDrop();screen=Screen.DEATH;}}playerVy=0;fallDist=0;}
        int targetX=Math.max(0,Math.min(W*TILE-VW*TILE,(int)(px-VW*TILE/2)));
        int targetY=Math.max(0,Math.min(H*TILE-VH*TILE,(int)(py-VH*TILE/2)));
        if(ultraFps){camX=targetX;camY=targetY;}else{if(camSmoothX==0){camSmoothX=targetX;camSmoothY=targetY;}camSmoothX+=(targetX-camSmoothX)*0.15;camSmoothY+=(targetY-camSmoothY)*0.15;camX=(int)camSmoothX;camY=(int)camSmoothY;}
        if(!ultraFps)for(int i=0;i<particles.size();i++){Particle pt=particles.get(i);pt.x+=pt.vx;pt.y+=pt.vy;pt.vy+=0.2;pt.life--;if(pt.life<=0){particles.remove(i);i--;}}
        if(!ultraFps&&physicsOn)for(int i=0;i<drops.size();i++){DropItem d=drops.get(i);d.y+=d.vy;d.vy+=0.1;d.life--;if(Math.abs(d.x-px)<24&&Math.abs(d.y-py)<24){if(d.block==EXP_ORB){xp++;}else addToInv(d.block,1);drops.remove(i);i--;}else if(d.life<=0){drops.remove(i);i--;}}
        for(int i=0;i<dmgNums.size();i++){DmgNum dn=dmgNums.get(i);dn.y-=1.5;dn.life--;if(dn.life<=0){dmgNums.remove(i);i--;}}
        for(int i=0;i<arrows.size();i++){Arrow a=arrows.get(i);a.x+=a.vx;a.y+=a.vy;a.life--;if(Math.abs(a.x-px)<16&&Math.abs(a.y-py)<16){health-=Math.max(1,3-armor);if(health<=0){dead=true;deathDrop();screen=Screen.DEATH;}arrows.remove(i);i--;}else if(a.life<=0||isSolid((int)(a.x/TILE),(int)(a.y/TILE))){arrows.remove(i);i--;}}
        for(int i=0;i<tntList.size();i++){PrimedTnt pt=tntList.get(i);pt.timer--;if(pt.timer%10==0){particles.add(new Particle(pt.x,pt.y,COAL_ORE));}if(pt.timer<=0){explode((int)(pt.x/TILE),(int)(pt.y/TILE));tntList.remove(i);i--;}}
        if(!ultraFps)for(Mob m:mobs){
            m.aiT++;if(m.hurtT>0)m.hurtT--;
            double ndark=Math.abs(worldTime-12000)/12000.0;
            if(m.type==5&&ndark>0.3){double ddx=px-m.x,ddy=py-m.y,dist=Math.sqrt(ddx*ddx+ddy*ddy);m.x+=ddx/dist*2;if(m.aiT>30){m.aiT=0;m.vy=-5;}}
            else if(m.type==2&&ndark>0.3){double ddx=px-m.x,ddy=py-m.y,dist=Math.sqrt(ddx*ddx+ddy*ddy);if(dist>8){m.x+=ddx/dist*1.5;m.y+=ddy/dist*0.5;}}
            else if(m.type==4&&ndark>0.3){double ddx=px-m.x,ddy=py-m.y,dist=Math.sqrt(ddx*ddx+ddy*ddy);if(dist>60){m.x+=ddx/dist*2;}else if(m.aiT>60){m.aiT=0;if(dist<100){arrows.add(new Arrow(m.x,m.y,(px-m.x)/dist*6,(py-m.y)/dist*6));}}}
            else if(m.aiT>100){m.aiT=0;if(Math.random()<0.3)m.x+=(Math.random()-0.5)*TILE;}
            if(isSolid((int)(m.x/TILE),(int)((m.y+20)/TILE))){m.aiT=-10;}else if(physicsOn)m.y+=1.5;
            if(m.type==2&&ndark>0.3&&Math.abs(m.x-px)<24&&Math.abs(m.y-py)<24&&m.hurtT<=0){health-=Math.max(1,3-armor);m.hurtT=40;if(health<=0){dead=true;deathDrop();screen=Screen.DEATH;}}
        }
        // Night mob spawning
        double ndark=Math.abs(worldTime-12000)/12000.0;
        if(survival&&ndark>0.6&&mobs.size()<12&&Math.random()<0.02){
            int sx2=(int)(px/TILE)+(int)((Math.random()-0.5)*20);
            int sy2=getGround(sx2);
            if(sx2>5&&sx2<W-5&&world[sx2][sy2-1]==0){
                int mt=Math.random()<0.3?2:Math.random()<0.5?4:Math.random()<0.7?5:0;
                mobs.add(new Mob(sx2*TILE,sy2*TILE-playerH/2,mt));
            }
        }
        bobFrame++;if(walking&&!ultraFps)bobFrame+=2;
        for(int i=0;i<achieves.size();i++){achieves.get(i).life--;if(achieves.get(i).life<=0){achieves.remove(i);i--;}}
        for(int i=0;i<10;i++)if(keys[KeyEvent.VK_1+i]){
            if(survival)selBlock=Math.min(i+1,BLOCK_COUNT-1);
            else{selBlock=Math.min(i+1+creativeOffset,BLOCK_COUNT-1);}
        }
        if(breakX>=0&&breakY>=0&&isIn(breakX,breakY)&&world[breakX][breakY]>0){breakTimer++;if(breakTimer>=breakTime){int bk=world[breakX][breakY];syncBlock(breakX,breakY,0);addToInv(bk,1);spawnParticles(breakX,breakY,bk);blocksBroken++;String st="stone";if(bk==SAND||bk==GRAVEL)st="sand";else if(bk>=OAK_LOG&&bk<=DARK_OAK_LOG)st="wood";else if(bk<=GRASS||bk==DIRT)st="grass";playSFX(st);world[breakX][breakY]=0;breakX=-1;breakY=-1;breakTimer=0;}}else if(breakX>=0){breakX=-1;breakY=-1;breakTimer=0;}
        if(msgTimer>0)msgTimer--;
        if(chatTimer>0)chatTimer--;
        long posTime=System.currentTimeMillis();
        if(isHost&&server!=null&&posTime%100<80){
            server.broadcast("P 0 "+(int)px+" "+(int)py);
        }
        if(!isHost&&client!=null&&client.isConnected()&&posTime%100<80){
            client.send("P "+(int)px+" "+(int)py);
        }
        synchronized(remotePlayers){for(RemotePlayer rp:remotePlayers){rp.x+=(rp.targetX-rp.x)*0.25;rp.y+=(rp.targetY-rp.y)*0.25;}}
        if(ultraFps||posTime%3!=0||!physicsOn){}else{
            boolean bossAlive=false;for(Mob m:mobs)if(m.type==6)bossAlive=true;
            if(!bossAlive&&worldTime>17500&&worldTime<18500&&Math.random()<0.005){int bx=(int)(10+Math.random()*(W-20));mobs.add(new Mob(bx*TILE,getGround(bx)*TILE-playerH/2,6));}
            for(int y=H-2;y>=0;y--)for(int x=W-1;x>=0;x--){
                if(world[x][y]==SAND||world[x][y]==GRAVEL){if(y+1<H&&world[x][y+1]==0){world[x][y+1]=world[x][y];world[x][y]=0;}}
                else if(world[x][y]==WATER||world[x][y]==LAVA){
                    int fl=world[x][y];
                    if(y+1<H&&world[x][y+1]==0){world[x][y+1]=fl;world[x][y]=0;}
                    else if(isSolid(x,y+1)){
                        for(int dir=-1;dir<=1;dir+=2)if(x+dir>=0&&x+dir<W&&world[x+dir][y]==0){world[x+dir][y]=fl;if(physicsLevel<2||fl==LAVA)break;}
                    }
                }
            }
            if(posTime%30==0)for(int y=H-2;y>=0;y--)for(int x=1;x<W-1;x++)if(world[x][y]==LAVA){
                for(int fx=-1;fx<=1;fx++)for(int fy=-1;fy<=1;fy++){
                    int b=world[x+fx][y+fy];
                    if(b==OAK_LOG||b==SPRUCE_LOG||b==BIRCH_LOG||b==JUNGLE_LOG||(b>=OAK_PLANKS&&b<=DARK_OAK_PLANKS)||(b>=OAK_LEAVES&&b<=DARK_OAK_LEAVES)||b==WOOL)world[x+fx][y+fy]=LAVA;
                }
            }
        }
        repaint();
    }

    private void syncName(){
        if(client!=null&&client.isConnected())client.send("N "+playerName);
    }

    private void syncBlock(int x,int y,int block){
        String msg="B "+x+" "+y+" "+block;
        if(isHost&&server!=null)server.broadcast(msg);
        if(client!=null&&client.isConnected())client.send(msg);
    }

    private void syncChat(String msg){
        String full="C "+playerName+" "+msg;
        if(isHost&&server!=null)server.broadcast(full);
        if(client!=null&&client.isConnected())client.send(full);
        addChat(playerName,msg);
    }

    private void runCommand(String cmd){
        String[] parts=cmd.substring(1).split(" ");
        addChat("CMD","/"+String.join(" ",parts));
        switch(parts[0]){
            case "time":if(parts.length>1){if(parts[1].equals("day"))worldTime=6000;else if(parts[1].equals("night"))worldTime=18000;addChat("Time","set to "+parts[1]);}break;
            case "tp":if(parts.length>2){try{px=Integer.parseInt(parts[1])*TILE;py=Integer.parseInt(parts[2])*TILE;addChat("TP","teleported");}catch(Exception e){addChat("TP","invalid coords");}}break;
            case "heal":health=20;hunger=20;addChat("Heal","restored");break;
            case "creative":survival=false;addChat("Mode","creative");break;
            case "survival":survival=true;addChat("Mode","survival");break;
            case "give":if(parts.length>1){try{int b=Integer.parseInt(parts[1]),c=parts.length>2?Integer.parseInt(parts[2]):1;addToInv(b,c);addChat("Give",""+c+"x "+BNAME[Math.min(b,BLOCK_COUNT-1)]);}catch(Exception e){addChat("Give","usage: /give <id> [count]");}}break;
            case "kill":health=0;dead=true;deathDrop();screen=Screen.DEATH;break;
            case "help":addChat("Cmds","time day/night, tp x y, heal, creative, survival, give id, kill, nether, rpc, rpcviz, rpcdebug, voice, bg");break;
            case "rpcdebug":if(discordRPC!=null){addChat("RPC-State",discordRPC.lastState);addChat("RPC-Secret",discordRPC.currentSecret.isEmpty()?"(none)":discordRPC.currentSecret);addChat("RPC-JSON",discordRPC.currentJson);}else{addChat("RPC","Not running. Use /rpc to start.");}break;
            case "voice":if(voiceChat!=null){voiceChat.shutdown();voiceChat=null;addChat("Voice","stopped");}else{int vp=(serverPort>0?serverPort:clientPort>0?clientPort:0);voiceChat=new VoiceChatThread(vp);voiceChat.start();addChat("Voice","started on port "+(vp+1000));}break;
            case "bg":bgEdit=!bgEdit;addChat("BG","background edit mode: "+bgEdit);break;
            case "rpc":if(discordRPC!=null){discordRPC.stopRPC();discordRPC=null;addChat("RPC","stopped");}else{discordRPC=new DiscordRPC();discordRPC.setDaemon(true);discordRPC.start();addChat("RPC","started");}break;
            case "rpcviz":SwingUtilities.invokeLater(()->new RPCVisualizer().setVisible(true));break;
            case "nether":inNether=!inNether;genWorld(worldSeed);addChat("Nether",inNether?"Entered!":"Overworld!");break;
            default:addChat("CMD","unknown: /"+parts[0]+"  use /help");
        }
    }

    private void addChat(String who,String msg){
        chatMessages.add("<"+who+"> "+msg);
        while(chatMessages.size()>20)chatMessages.remove(0);
    }

    private void addToInv(int block,int count){if(block<=0)return;for(int i=0;i<inv.length;i++)if(inv[i]==block){invCount[i]+=count;return;}for(int i=0;i<inv.length;i++)if(inv[i]==0){inv[i]=block;invCount[i]=count;return;}}
    private void spawnParticles(int bx,int by,int block){
        if(ultraFps)return;
        if(block==WATER){removeConnectedWater(bx,by);return;}
        int count=physicsLevel==2?20:8;
        for(int i=0;i<count;i++)particles.add(new Particle(bx*TILE+TILE/2+Math.random()*TILE/2-TILE/4,by*TILE+TILE/2+Math.random()*TILE/2-TILE/4,block));
        drops.add(new DropItem(bx*TILE+TILE/2,by*TILE+TILE/2,block));
    }

    private void removeConnectedWater(int x,int y){
        if(x<0||x>=W||y<0||y>=H||world[x][y]!=WATER)return;
        java.util.LinkedList<int[]> q=new java.util.LinkedList<>();
        q.add(new int[]{x,y});int count=0;
        while(!q.isEmpty()){
            int[] p=q.poll();int cx=p[0],cy=p[1];
            if(cx<0||cx>=W||cy<0||cy>=H||world[cx][cy]!=WATER)continue;
            world[cx][cy]=0;count++;
            q.add(new int[]{cx-1,cy});q.add(new int[]{cx+1,cy});
            q.add(new int[]{cx,cy-1});q.add(new int[]{cx,cy+1});
        }
        if(count>0)addChat("Water","Cleared "+count+" blocks");
    }

    private void explode(int bx,int by){
        for(int x=bx-3;x<=bx+3;x++)for(int y=by-3;y<=by+3;y++){
            if(x>=0&&x<W&&y>=0&&y<H&&world[x][y]!=BEDROCK&&Math.abs(x-bx)+Math.abs(y-by)<=4){
                if(world[x][y]==OAK_LOG||world[x][y]==SPRUCE_LOG||world[x][y]==BIRCH_LOG||world[x][y]==JUNGLE_LOG||world[x][y]==ACACIA_LOG||world[x][y]==DARK_OAK_LOG||world[x][y]==OAK_LEAVES||world[x][y]==SPRUCE_LEAVES||world[x][y]==BIRCH_LEAVES||world[x][y]==JUNGLE_LEAVES||world[x][y]==ACACIA_LEAVES||world[x][y]==DARK_OAK_LEAVES||world[x][y]==WOOL||world[x][y]>=OAK_PLANKS&&world[x][y]<=DARK_OAK_PLANKS)world[x][y]=LAVA;
                else spawnParticles(x,y,world[x][y]);world[x][y]=0;
            }
        }
        for(int i=0;i<40;i++)particles.add(new Particle(bx*TILE,by*TILE,COAL_ORE));
        if(Math.abs(bx*TILE-px)<100&&Math.abs(by*TILE-py)<100){health-=5;if(health<=0){dead=true;deathDrop();screen=Screen.DEATH;}}
    }

    private boolean takeFromInv(int block,int count){for(int i=0;i<inv.length;i++)if(inv[i]==block&&invCount[i]>=count){invCount[i]-=count;if(invCount[i]<=0)inv[i]=0;return true;}return false;}
    private int getInvCount(int block){for(int i=0;i<inv.length;i++)if(inv[i]==block)return invCount[i];return 0;}

    private int[] getCraft(){
        int nz=0,f=0;for(int i=0;i<4;i++)if(craftGrid[i]>0){nz++;if(f==0)f=craftGrid[i];}
        if(nz==1&&f>=OAK_LOG&&f<=DARK_OAK_LOG)return new int[]{f-OAK_LOG+OAK_PLANKS,4};
        if(nz==1&&f==STONE)return new int[]{COBBLESTONE,1};if(nz==1&&f==COBBLESTONE)return new int[]{STONE,1};
        if(nz==1&&f==SAND)return new int[]{LAVA,1};if(nz==1&&f==NETHERRACK)return new int[]{SOUL_SAND,1};
        if(nz==2&&((craftGrid[0]==OAK_PLANKS&&craftGrid[1]==OAK_PLANKS)||(craftGrid[0]==OAK_PLANKS&&craftGrid[2]==OAK_PLANKS)))return new int[]{4,4};
        if(nz==4&&craftGrid[0]==OAK_PLANKS&&craftGrid[1]==OAK_PLANKS&&craftGrid[2]==OAK_PLANKS&&craftGrid[3]==OAK_PLANKS)return new int[]{CRAFTING_TABLE,1};
        if(nz==4&&craftGrid[0]==WOOL&&craftGrid[1]==WOOL&&craftGrid[2]==OAK_PLANKS&&craftGrid[3]==OAK_PLANKS)return new int[]{BED,1};
        if(nz==4&&craftGrid[0]==STONE&&craftGrid[1]==STONE&&craftGrid[2]==STONE&&craftGrid[3]==STONE)return new int[]{FURNACE_ITEM,1};
        if(nz==4&&craftGrid[0]==OAK_PLANKS&&craftGrid[1]==OAK_PLANKS&&craftGrid[2]==STONE&&craftGrid[3]==STONE)return new int[]{SWORD,1};
        if(nz==4&&craftGrid[0]==OAK_PLANKS&&craftGrid[2]==STONE&&craftGrid[3]==STONE)return new int[]{PICKAXE,1};
        return new int[]{0,0};
    }

    @Override
    protected void paintComponent(Graphics g){super.paintComponent(g);Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
        switch(screen){case MENU:drawMenu(g2);break;case WORLD_LIST:drawWorldList(g2);break;case CREATE_WORLD:drawCreateWorld(g2);break;case MULTIPLAYER:drawMultiplayer(g2);break;case CONNECT:drawConnect(g2);break;case HOST:drawHost(g2);break;case SETTINGS:drawSettings(g2);break;case CONNECTING:drawConnecting(g2);break;case PAUSE:drawPause(g2);break;case HELP:drawHelp(g2);break;case DEATH:drawDeath(g2);break;case CRAFTING:drawCrafting(g2);break;case PLAY:drawGame(g2);break;}
    }

    private void drawDirtBG(Graphics2D g2,int w,int h){for(int x=0;x<w;x+=TILE)for(int y=0;y<h;y+=TILE)g2.drawImage(tex[bedrockEdition?STONE:DIRT],x,y,null);}
    private void drawBtn(Graphics2D g2,String t,int x,int y,int w,int hh,boolean hov){g2.setColor(hov?new Color(bedrockEdition?100:120,bedrockEdition?140:120,bedrockEdition?220:120,200):new Color(80,80,80,200));g2.fillRect(x,y,w,hh);g2.setColor(hov?Color.WHITE:new Color(180,180,180));g2.drawRect(x,y,w,hh);g2.setFont(new Font("PixelPurl",Font.BOLD,18));g2.drawString(t,x+(w-g2.getFontMetrics().stringWidth(t))/2,y+hh-12);}
    private boolean inBtn(int mx,int my,int x,int y,int w,int h){return mx>=x&&mx<x+w&&my>=y&&my<y+h;}

    private void drawMenu(Graphics2D g2){int w=getWidth(),h=getHeight();drawDirtBG(g2,w,h);
        if(logoFrames!=null&&logoFrames.length>0){
            long now=System.currentTimeMillis();if(now-logoFrameTime>80){logoFrame=(logoFrame+1)%logoFrames.length;logoFrameTime=now;}
            BufferedImage f=logoFrames[logoFrame];
            int lw=320,lh=(int)(320.0/f.getWidth()*f.getHeight());
            g2.drawImage(f,w/2-lw/2,10,lw,lh,null);
        }else if(logoImg!=null){
            int lw=320,lh=84;
            g2.drawImage(logoImg,w/2-lw/2,20,lw,lh,null);
        }else{
            g2.setFont(new Font("PixelPurl",Font.PLAIN,36));g2.setColor(new Color(255,220,60));
            String t1="MiniCraft";g2.drawString(t1,w/2-120,68);
        }
        String t2=(bedrockEdition?"Bedrock":"Java")+" Edition v"+VERSION;g2.setFont(new Font("PixelPurl",Font.PLAIN,14));g2.setColor(new Color(180,180,180));g2.drawString(t2,w/2+20,108);
        if(updateAvailable){g2.setFont(new Font("PixelPurl",Font.PLAIN,14));g2.setColor(Color.YELLOW);g2.drawString("Update v"+updateVersion+" available! (update.sh or update.bat)",w/2-200,130);}
        drawBtn(g2,"Edition: "+(bedrockEdition?"Bedrock":"Java"),w/2-100,140,200,36,menuHover==7);
        drawBtn(g2,"Singleplayer",w/2-100,186,200,40,menuHover==0);drawBtn(g2,"Multiplayer",w/2-100,236,200,40,menuHover==1);
        drawBtn(g2,"Options",w/2-100,286,200,40,menuHover==2);drawBtn(g2,"Mods",w/2-100,336,200,40,menuHover==3);
        drawBtn(g2,"Invite (RPC)",w/2-100,386,200,40,menuHover==80);
        drawBtn(g2,"Quit",w/2-100,436,200,40,menuHover==4);
        drawBtn(g2," Discord",w/2-100,486,95,32,menuHover==5);drawBtn(g2," GitHub",w/2+5,486,95,32,menuHover==6);
        if(discIcon!=null)g2.drawImage(discIcon,w/2-92,488,24,24,null);
        if(ghIcon!=null)g2.drawImage(ghIcon,w/2+13,488,24,24,null);
    }

    private void drawWorldList(Graphics2D g2){int w=getWidth(),h=getHeight();drawDirtBG(g2,w,h);
        g2.setFont(new Font("PixelPurl",Font.BOLD,28));g2.setColor(new Color(100,200,60));g2.drawString("Select World",w/2-100,60);
        int yy=100;
        for(int i=0;i<worldList.size();i++){g2.setColor(i==selectedWorld?new Color(120,120,120,200):new Color(60,60,60,200));g2.fillRect(w/2-150,yy,300,36);g2.setColor(i==selectedWorld?Color.WHITE:new Color(200,200,200));g2.drawRect(w/2-150,yy,300,36);g2.setFont(new Font("PixelPurl",Font.BOLD,14));g2.drawString(worldList.get(i),w/2-140,yy+25);yy+=42;}
        if(worldList.isEmpty()){g2.setFont(new Font("PixelPurl",Font.PLAIN,18));g2.setColor(new Color(200,200,200));g2.drawString("No worlds yet!",w/2-80,yy+10);yy+=30;}
        yy=Math.max(yy+10,350);
        drawBtn(g2,"Create New World",w/2-120,yy,240,36,menuHover==10);
        if(selectedWorld>=0){drawBtn(g2,"Delete World",w/2-120,yy+46,240,36,menuHover==11);drawBtn(g2,"Play",w/2-60,yy+92,120,36,menuHover==12);}
        drawBtn(g2,"Back",w/2-60,yy+138,120,36,menuHover==13);
    }

    private void drawCreateWorld(Graphics2D g2){int w=getWidth(),h=getHeight();drawDirtBG(g2,w,h);
        g2.setFont(new Font("PixelPurl",Font.BOLD,28));g2.setColor(new Color(100,200,60));g2.drawString("New World",w/2-80,60);
        g2.setFont(new Font("PixelPurl",Font.PLAIN,18));g2.setColor(Color.WHITE);g2.drawString("Player Name:",w/2-150,100);
        g2.setColor(new Color(40,40,40));g2.fillRect(w/2-150,110,300,35);g2.setColor(Color.WHITE);g2.drawRect(w/2-150,110,300,35);
        g2.setFont(new Font("PixelPurl",Font.PLAIN,16));g2.drawString(playerName+(System.currentTimeMillis()/500%2==0?"_":""),w/2-140,135);
        g2.setColor(Color.WHITE);g2.drawString("World Name:",w/2-150,190);
        g2.setColor(new Color(40,40,40));g2.fillRect(w/2-150,205,300,35);g2.setColor(Color.WHITE);g2.drawRect(w/2-150,205,300,35);
        g2.setFont(new Font("PixelPurl",Font.PLAIN,16));g2.drawString(typing+(System.currentTimeMillis()/500%2==0?"_":""),w/2-140,230);
        drawBtn(g2,superflat?"World: Superflat":"World: Normal",w/2-140,255,280,32,menuHover==22);
        drawBtn(g2,"Create",w/2-60,300,120,36,menuHover==20);drawBtn(g2,"Back",w/2-60,350,120,36,menuHover==21);
    }

    private void drawMultiplayer(Graphics2D g2){int w=getWidth(),h=getHeight();drawDirtBG(g2,w,h);
        g2.setFont(new Font("PixelPurl",Font.BOLD,28));g2.setColor(new Color(100,200,60));g2.drawString("Multiplayer",w/2-80,60);
        g2.setFont(new Font("PixelPurl",Font.PLAIN,14));g2.setColor(new Color(200,200,200));
        int yy=100;
        if(discoveredServers.isEmpty()){
            g2.drawString("Scanning for servers...",w/2-100,yy+20);
        }else{
            for(int i=0;i<discoveredServers.size();i++){
                DiscoveredServer ds=discoveredServers.get(i);
                g2.setColor(i==selectedServer?new Color(120,120,120,200):new Color(60,60,60,200));
                g2.fillRect(w/2-220,yy,440,36);
                g2.setColor(i==selectedServer?Color.WHITE:new Color(200,200,200));
                g2.drawRect(w/2-220,yy,440,36);
                g2.setFont(new Font("PixelPurl",Font.BOLD,12));
                g2.drawString(ds.name,w/2-210,yy+15);
                g2.setFont(new Font("PixelPurl",Font.PLAIN,13));
                g2.drawString(ds.players+" players  "+ds.world+"  Code:"+ds.port,w/2-210,yy+30);
                yy+=42;
            }
        }
        yy=Math.max(yy+10,300);
        drawBtn(g2,"Host Server",w/2-100,yy,200,36,menuHover==30);
        drawBtn(g2,"Join by Code",w/2-100,yy+46,200,36,menuHover==31);
        drawBtn(g2,"Refresh",w/2-100,yy+92,200,36,menuHover==33);
        drawBtn(g2,"Back",w/2-100,yy+138,200,36,menuHover==32);
    }

    private void drawPause(Graphics2D g2){int w=getWidth(),h=getHeight();
        g2.setColor(new Color(0,0,0,180));g2.fillRect(0,0,w,h);
        g2.setFont(new Font("PixelPurl",Font.BOLD,36));g2.setColor(Color.WHITE);
        String t="Game Menu";g2.drawString(t,(w-g2.getFontMetrics().stringWidth(t))/2,h/2-60);
        drawBtn(g2,"Back to Game",w/2-100,h/2-10,200,36,menuHover==70);
        drawBtn(g2,"Options",w/2-100,h/2+40,200,36,menuHover==71);
        drawBtn(g2,"Invite (RPC)",w/2-100,h/2+90,200,36,menuHover==73);
        drawBtn(g2,"Save and Quit to Title",w/2-100,h/2+140,200,36,menuHover==72);
    }

    private void drawConnect(Graphics2D g2){int w=getWidth(),h=getHeight();drawDirtBG(g2,w,h);
        g2.setFont(new Font("PixelPurl",Font.BOLD,28));g2.setColor(new Color(100,200,60));g2.drawString("Connect",w/2-60,60);
        g2.setFont(new Font("PixelPurl",Font.PLAIN,18));g2.setColor(Color.WHITE);        g2.drawString("Join Server",w/2-150,130);
        g2.setColor(new Color(40,40,40));g2.fillRect(w/2-150,145,300,35);g2.setColor(Color.WHITE);g2.drawRect(w/2-150,145,300,35);
        g2.setFont(new Font("PixelPurl",Font.PLAIN,16));g2.drawString(typing+(System.currentTimeMillis()/500%2==0?"_":""),w/2-140,170);
        drawBtn(g2,"Connect",w/2-60,220,120,36,menuHover==40);drawBtn(g2,"Back",w/2-60,270,120,36,menuHover==41);
        if(!lastMsg.isEmpty()){g2.setFont(new Font("PixelPurl",Font.PLAIN,14));g2.setColor(Color.YELLOW);g2.drawString(lastMsg,w/2-150,330);}
    }

    private void drawSettings(Graphics2D g2){int w=getWidth(),h=getHeight();drawDirtBG(g2,w,h);
        g2.setFont(new Font("PixelPurl",Font.BOLD,28));g2.setColor(new Color(100,200,60));g2.drawString("Options  v"+VERSION,w/2-90,60);
        g2.setFont(new Font("PixelPurl",Font.PLAIN,16));g2.setColor(Color.WHITE);
        String[][] opts={
            {"[F1] FPS:"+(showFps?"ON":"OFF"),"[F2] Coords:"+(showCoords?"ON":"OFF"),"[F3] Debug:"+(showDebug?"ON":"OFF")},
            {"[F4] UltraFPS:"+(ultraFps?"ON":"OFF"),"[F5] RTX:"+(rtxMode?"ON":"OFF"),"[F6] RTX H2O:"+(rtxWater?"ON":"OFF")},
            {"[F7] Phys:"+(physicsLevel==0?"OFF":physicsLevel==1?"Basic":"RTX"),"[F8] Shader:"+(shaderMode==0?"OFF":shaderMode==1?"Nost":"Solas"),"[M] Music:"+(musicOn?"ON":"OFF")},
            {"[F] "+(survival?"Survival":"Creative"),"[F11] Fullscreen:"+(fullscreen?"ON":"OFF"),"[< >] FOV:"+gameFov},
            {"[N] Name: "+playerName+(nameEditing?(System.currentTimeMillis()/500%2==0?"_":""):""),"[U] "+(updateAvailable?"UPDATE!":"Check"),""},
        };
        int y=100;
        for(String[] row:opts){
            g2.setColor(Color.WHITE);g2.drawString(row[0],20,y);
            g2.drawString(row[1],w/2-100,y);
            if(!row[2].isEmpty())g2.drawString(row[2],w-260,y);
            y+=34;
        }
        drawBtn(g2,"Back",w/2-60,h-70,120,36,menuHover==60);
    }

    private void drawConnecting(Graphics2D g2){int w=getWidth(),h=getHeight();drawDirtBG(g2,w,h);
        g2.setFont(new Font("PixelPurl",Font.BOLD,28));g2.setColor(new Color(100,200,60));
        g2.drawString("Connecting...",w/2-100,120);
        g2.setFont(new Font("PixelPurl",Font.PLAIN,18));g2.setColor(Color.WHITE);
        g2.drawString("Receiving world...",w/2-100,180);
    }

    private void drawHelp(Graphics2D g2){int w=getWidth(),h=getHeight();drawDirtBG(g2,w,h);
        g2.setFont(new Font("PixelPurl",Font.BOLD,36));g2.setColor(new Color(100,200,60));
        g2.drawString("Welcome to MiniCraft!",w/2-200,60);
        g2.setFont(new Font("PixelPurl",Font.BOLD,14));g2.setColor(Color.WHITE);
        String[] lines={"[WASD] Move","[Click] Break/Place blocks","[Scroll] Change block","[E] Crafting","[T] Chat","[F] Survival/Creative","[G] Noclip","[M] Music","[ESC] Pause","[F1-F7] Settings","[ENTER] Continue..."};
        int yy=110;
        for(String l:lines){g2.drawString(l,w/2-120,yy*13);yy+=28;}
    }

    private void drawHost(Graphics2D g2){int w=getWidth(),h=getHeight();drawDirtBG(g2,w,h);
        g2.setFont(new Font("PixelPurl",Font.BOLD,28));g2.setColor(new Color(100,200,60));g2.drawString("Host Server",w/2-80,60);
        g2.setFont(new Font("PixelPurl",Font.PLAIN,18));g2.setColor(Color.WHITE);
        g2.drawString("Port: "+serverPort,w/2-60,130);
        g2.drawString("IP: [hidden]",w/2-100,170);
        if(server!=null&&server.isRunning()){
            g2.setColor(Color.GREEN);g2.drawString("SERVER RUNNING",w/2-80,220);
            g2.drawString("Players: "+server.getPlayerCount(),w/2-80,250);
            drawBtn(g2,"Stop Server",w/2-80,300,160,36,menuHover==50);
        }else{
            drawBtn(g2,"Start Server",w/2-80,300,160,36,menuHover==50);
        }
        drawBtn(g2,"Back",w/2-60,360,120,36,menuHover==51);
    }

    private String getLocalIP(){try{return InetAddress.getLocalHost().getHostAddress();}catch(Exception e){return "127.0.0.1";}}

    private void drawGame(Graphics2D g2){int w=getWidth(),h=getHeight();
        worldTime=(worldTime+1)%24000;
        double night=Math.abs(worldTime-12000)/12000.0;
        int skyR=(int)(150*(1-night)+10*night),skyG=(int)(200*(1-night)+20*night),skyB=(int)(255*(1-night)+40*night);
        g2.setColor(new Color(skyR,skyG,skyB));g2.fillRect(0,0,w,h);
        g2.setColor(new Color((int)(50*(1-night)+5*night),(int)(160*(1-night)+10*night),(int)(255*(1-night)+20*night)));g2.fillRect(0,0,w,h/4);
        g2.setColor(new Color((int)(30*(1-night)+3*night),(int)(120*(1-night)+8*night),(int)(230*(1-night)+15*night)));g2.fillRect(0,0,w,h/8);
        int na=(int)(120*night);
        if(mountains==null){mountains=new int[w];Random r=new Random(42);mountains[0]=h/4;for(int i=1;i<w;i++){int ch=r.nextInt(3)-1;mountains[i]=Math.max(h/6,Math.min(h/3,mountains[i-1]+ch));}}
        g2.setColor(new Color(40,60,40,150-na));for(int x=0;x<w;x++){int mx=h-mountains[x]-20;g2.fillRect(x,mx,1,mountains[x]+20);}
        g2.setColor(new Color(30,45,30,180-na));for(int x=0;x<w;x++){int mx2=h-mountains[x];g2.fillRect(x,mx2,1,mountains[x]);}
        if(night>0.5&&!ultraFps){g2.setColor(new Color(255,255,255,(int)(night*200-100)));for(int i=0;i<40;i++){int sx=(i*73+500)%w,sy=(i*47+200)%(h/2);g2.fillOval(sx,sy,2,2);}}
        double sunAngle=(worldTime*Math.PI/12000);int sunX=(int)(w/2+Math.cos(sunAngle-Math.PI/2)*w*0.4);int sunY=(int)(h/4-Math.sin(sunAngle-Math.PI/2)*h*0.3);
        if(night<0.5){g2.setColor(new Color(255,255,150,(int)(200*(1-night*2))));g2.fillOval(sunX-15,sunY-15,30,30);if(rtxMode){g2.setColor(new Color(255,255,200,40));g2.fillOval(sunX-35,sunY-35,70,70);}}else{g2.setColor(new Color(200,200,220,(int)(200*(night*2-1))));g2.fillOval(sunX-12,sunY-12,24,24);}
        int frame=bobFrame%240;
        for(int i=0;i<4;i++){
            if(ultraFps)break;
            g2.setColor(new Color(255,255,255,160));
            int cx=((500+i*300+frame*2)%(w+600))-300;
            g2.fillOval(cx,30-i*10,80+i*15,20+i*8);g2.fillOval(cx+40,34-i*8,50+i*10,14+i*5);
        }
        if(worldTime>3000&&worldTime<9000&&!ultraFps){
            g2.setColor(new Color(100,140,200,60));
            for(int i=0;i<60;i++){int rx=(i*117+frame*3)%w,ry=(i*83+frame*5)%h;g2.drawLine(rx,ry,rx,ry+8);}
        }
        int fov=gameFov;if(keys[KeyEvent.VK_SHIFT]&&walking&&!ultraFps)fov+=3;
        int sx=camX/TILE,sy=camY/TILE,ex=Math.min(W,sx+fov+2),ey=Math.min(H,sy+fov*18/25+2);
        // Background blocks (depth layer)
        if(bgWorld!=null){
            int bgOffX=(int)(camX*0.5),bgOffY=(int)(camY*0.3);
            int bsx=Math.max(0,(bgOffX)/TILE-1),bex=Math.min(W,bsx+VW+3);
            int bsy=Math.max(0,(bgOffY)/TILE-1),bey=Math.min(H,bsy+VH+3);
            for(int x=bsx;x<bex;x++)for(int y=bsy;y<bey;y++){
                if(bgWorld[x][y]>0){
                    int alpha=90;
                    Color base=FB[Math.min(bgWorld[x][y],FB.length-1)];
                    g2.setColor(new Color(base.getRed(),base.getGreen(),base.getBlue(),alpha));
                    g2.fillRect(x*TILE-bgOffX,y*TILE-bgOffY,TILE,TILE);
                }
            }
        }
        if(threeDMode&&screen==Screen.PLAY){
            render3D(g2);
        }else{
            for(int x=sx;x<ex;x++)for(int y=sy;y<ey;y++)if(world[x][y]>0){g2.drawImage(tex[Math.min(world[x][y],BLOCK_COUNT-1)],x*TILE-camX,y*TILE-camY,null);if(rtxWater&&world[x][y]==WATER){g2.setColor(new Color(60,120,255,40));g2.fillRect(x*TILE-camX-2,y*TILE-camY-2,TILE+4,TILE+4);g2.setColor(new Color(100,180,255,20+Math.abs(bobFrame%40-20)));g2.fillRect(x*TILE-camX,y*TILE-camY,TILE,TILE);}if(world[x][y]==TORCH_ITEM){g2.setColor(new Color(255,200,50,40));g2.fillOval(x*TILE-camX-16,y*TILE-camY-16,64,64);g2.setColor(new Color(255,240,100,20));g2.fillOval(x*TILE-camX-24,y*TILE-camY-24,80,80);}}
        }
        if(!threeDMode){
        int pxOff=(int)(px-camX),pyOff=(int)(py-camY);
        int bob=(int)(Math.sin(frame*0.3)*2);
        g2.drawImage(steveImg[0],pxOff-playerW/2,pyOff-playerH/2+bob,null);
        drawNameTag(g2,pxOff,pyOff+bob,playerName,new Color(255,255,255));
        g2.setColor(new Color(0,0,0,30));g2.fillOval(pxOff-8,pyOff+TILE-6,16,4);

        synchronized(remotePlayers){
            Color[] tagColors={new Color(150,200,255),new Color(255,150,150),new Color(150,255,150),new Color(255,255,100),new Color(255,180,80),new Color(200,150,255)};
            int idx=0;
            for(RemotePlayer rp:remotePlayers){
                if(rp.name==null)continue;
                int rx=(int)(rp.x-camX)+(idx%3-1)*8,ry=(int)(rp.y-camY)+(idx/3)*4;
                g2.drawImage(steveImg[0],rx-playerW/2,ry-playerH/2,null);
                drawNameTag(g2,rx,ry,rp.name+" ["+(idx+1)+"]",tagColors[idx%tagColors.length]);
                idx++;
            }
        }

        for(Particle pt:particles){
            int alpha=pt.life*255/pt.maxLife;
            g2.setColor(new Color(FB[Math.min(pt.block,FB.length-1)].getRed(),FB[Math.min(pt.block,FB.length-1)].getGreen(),FB[Math.min(pt.block,FB.length-1)].getBlue(),alpha));
            g2.fillRect((int)(pt.x-camX),(int)(pt.y-camY),2+pt.life/5,2+pt.life/5);
        }
        for(DropItem d:drops){
            int dx=(int)(d.x-camX)-6,dy=(int)(d.y-camY)-6+(int)(Math.sin(d.life*0.1)*2);
            if(dx>-20&&dx<getWidth()+20&&dy>-20&&dy<getHeight()+20)
                g2.drawImage(tex[Math.min(d.block,BLOCK_COUNT-1)],dx,dy,null);
        }
        for(DmgNum dn:dmgNums){
            g2.setColor(new Color(255,0,0,dn.life>30?255:dn.life*8));g2.setFont(new Font("PixelPurl",Font.BOLD,14));
            g2.drawString(""+dn.val,(int)(dn.x-camX)-8,(int)(dn.y-camY));
        }
        for(Arrow a:arrows){g2.setColor(Color.WHITE);g2.drawLine((int)(a.x-camX),(int)(a.y-camY),(int)(a.x-a.vx*2-camX),(int)(a.y-a.vy*2-camY));}
        for(Mob m:mobs){
            int mx=(int)(m.x-camX),my=(int)(m.y-camY);
            // Cull off-screen mobs
            if(mx<-40||mx>getWidth()+40||my<-40||my>getHeight()+40)continue;
            Color mobColor=m.type==0?new Color(100,80,60):m.type==1?new Color(255,180,180):m.type==2?new Color(60,100,60):m.type==3?new Color(200,200,200):m.type==4?new Color(180,180,160):m.type==5?new Color(40,40,40):new Color(255,220,100);
            g2.setColor(mobColor);g2.fillRect(mx-10,my-14,20,24);
            // Eyes
            g2.setColor(m.type==2||m.type==5?new Color(255,0,0):Color.BLACK);
            g2.fillRect(mx-6,my-10,3,3);g2.fillRect(mx+3,my-10,3,3);
            g2.setColor(Color.WHITE);g2.setFont(new Font("PixelPurl",Font.PLAIN,10));
            g2.drawString(m.type==0?"Cow":m.type==1?"Pig":m.type==2?"Zomb":m.type==3?"Sheep":m.type==4?"Skele":m.type==5?"Spider":"Chick",mx-10,my-20);
            g2.setColor(new Color(0,0,0,100));g2.fillRect(mx-12,my-26,24,3);
            g2.setColor(new Color(255,0,0));g2.fillRect(mx-12,my-26,m.health*24/m.maxHealth,3);
            if(m.hurtT>0){g2.setColor(new Color(255,0,0,100));g2.fillRect(mx-10,my-14,20,24);}
        }

        if(mouseIn&&breakX<0){int hx=(mx+camX)/TILE,hy=(my+camY)/TILE;if(isIn(hx,hy)){g2.setColor(new Color(255,255,255,100));g2.drawRect(hx*TILE-camX,hy*TILE-camY,TILE,TILE);if(world[hx][hy]>0){g2.setFont(new Font("PixelPurl",Font.PLAIN,11));g2.setColor(Color.WHITE);g2.drawString(BNAME[Math.min(world[hx][hy],BLOCK_COUNT-1)],hx*TILE-camX+2,hy*TILE-camY-4);}}}
        if(breakX>=0){
            int bx=breakX*TILE-camX,by=breakY*TILE-camY;
            g2.setColor(new Color(255,255,255,100));g2.drawRect(bx,by,TILE,TILE);
            float pct=Math.min(1f,(float)breakTimer/(float)Math.max(1,breakTime));
            // Draw crack stages
            int stage=(int)(pct*7);
            if(stage>0){
                g2.setColor(new Color(0,0,0,40+stage*25));
                for(int i=0;i<stage*3;i++){
                    int cx=bx+4+(i*17+breakX*3+breakY*7)%24;
                    int cy=by+4+(i*13+breakX*5+breakY*11)%24;
                    g2.fillRect(cx,cy,3+(i%4),2+(i%3));
                }
            }
            g2.setColor(new Color(0,0,0,80));g2.fillRect(bx,by,(int)(TILE*pct),3);
        }
        }
        drawHUD(g2);
        if(shaderMode>0&&!ultraFps){
            int vw=getWidth(),vh=getHeight();
            if(shaderMode==1){
                g2.setColor(new Color(200,170,100,40));g2.fillRect(0,0,vw,vh);
                for(int i=0;i<25;i++){int a=90-i*3;if(a>0){g2.setColor(new Color(0,0,0,a));g2.drawRect(i,i,vw-i*2,vh-i*2);}}
                for(int i=0;i<200;i++){int rx=(int)(Math.random()*vw),ry=(int)(Math.random()*vh);g2.setColor(new Color(100,80,60,15));g2.fillRect(rx,ry,1,1);}
            }else{
                g2.setColor(new Color(255,255,200,20));g2.fillRect(0,0,vw,vh);
                for(int i=0;i<15;i++){int a=40-i*2;if(a>0){g2.setColor(new Color(100,150,255,a));g2.drawRect(i,i,vw-i*2,vh-i*2);}}
                if(night<0.3){g2.setColor(new Color(255,255,200,25));g2.fillOval(sunX-50,sunY-50,100,100);}
            }
        }
        drawChat(g2);
        for(Achieve a:achieves){
            g2.setColor(new Color(0,0,0,150));g2.fillRect(getWidth()/2-100,60,200,30);
            g2.setColor(new Color(255,255,100,a.life>30?255:a.life*8));
            g2.setFont(new Font("PixelPurl",Font.BOLD,14));g2.drawString(a.msg,getWidth()/2-90,82);
        }
        // Crosshair
        if(screen==Screen.PLAY){
            int cw=getWidth()/2,ch=getHeight()/2;
            g2.setColor(new Color(255,255,255,180));
            g2.drawLine(cw-4,ch,cw+4,ch);g2.drawLine(cw,ch-4,cw,ch+4);
        }
        if(showFps){g2.setColor(new Color(0,0,0,150));g2.fillRect(getWidth()-70,10,60,16);g2.setColor(Color.YELLOW);g2.setFont(new Font("PixelPurl",Font.PLAIN,13));g2.drawString(fps+" FPS",getWidth()-65,22);fpsCount++;long now=System.currentTimeMillis();if(now-fpsTimer>1000){fps=fpsCount;fpsCount=0;fpsTimer=now;}}
        if(showDebug&&screen==Screen.PLAY){
            g2.setColor(new Color(0,0,0,180));g2.fillRect(5,5,220,130);
            g2.setColor(Color.WHITE);g2.setFont(new Font("Monospaced",Font.PLAIN,11));
            long played=(System.currentTimeMillis()-sessionStart)/1000;
            String[] db={"MiniCraft v"+VERSION,"FPS: "+fps,"X: "+(int)(px/TILE)+" Y: "+(int)(py/TILE),"Seed: "+worldSeed,"Time: "+(worldTime/1000)+"h","Mobs: "+mobs.size()+" Drops: "+drops.size(),"Blocks: "+blocksBroken+"/"+blocksPlaced,"Mem: "+(Runtime.getRuntime().totalMemory()/1024/1024)+"MB"};
            for(int i=0;i<db.length;i++)g2.drawString(db[i],10,20+i*14);
        }
        if(isHost&&server!=null){g2.setFont(new Font("PixelPurl",Font.BOLD,13));g2.setColor(server.isRunning()?Color.GREEN:Color.RED);g2.drawString(server.isRunning()?"SERVER "+server.getPlayerCount()+" players  Code: "+serverPort:"SERVER FAILED - Check port "+serverPort,10,35);}
        if(client!=null&&client.isConnected()){g2.setFont(new Font("PixelPurl",Font.BOLD,13));g2.setColor(Color.CYAN);g2.drawString("CONNECTED  R:"+remotePlayers.size(),10,45);}
    }

    // ==================== 3D RAYCASTING MODE ====================
    private void render3D(Graphics2D g2){
        int w=getWidth(),h=getHeight();
        // Floor
        g2.setColor(new Color(60,100,60));
        g2.fillRect(0,h/2,w,h/2);
        // Sky
        g2.setColor(new Color(135,206,235));
        g2.fillRect(0,0,w,h/2);
        // Raycast walls
        double dirX=Math.cos(playerDir),dirY=Math.sin(playerDir);
        double planeX=-Math.sin(playerDir)*0.66,planeY=Math.cos(playerDir)*0.66;
        double pTileX=px/TILE,pTileY=py/TILE;
        for(int x=0;x<w;x+=2){
            double cameraX=2.0*x/w-1.0;
            double rayDirX=dirX+planeX*cameraX;
            double rayDirY=dirY+planeY*cameraX;
            int mapX=(int)pTileX,mapY=(int)pTileY;
            double sideDistX,sideDistY;
            double deltaDistX=Math.abs(1.0/rayDirX);
            double deltaDistY=Math.abs(1.0/rayDirY);
            double perpWallDist;
            int stepX,stepY,hit=0,side=0;
            if(rayDirX<0){stepX=-1;sideDistX=(pTileX-mapX)*deltaDistX;}
            else{stepX=1;sideDistX=(mapX+1.0-pTileX)*deltaDistX;}
            if(rayDirY<0){stepY=-1;sideDistY=(pTileY-mapY)*deltaDistY;}
            else{stepY=1;sideDistY=(mapY+1.0-pTileY)*deltaDistY;}
            for(int i=0;i<W+H&&hit==0;i++){
                if(sideDistX<sideDistY){sideDistX+=deltaDistX;mapX+=stepX;side=0;}
                else{sideDistY+=deltaDistY;mapY+=stepY;side=1;}
                if(mapX<0||mapX>=W||mapY<0||mapY>=H||world[mapX][mapY]>0)hit=1;
            }
            if(hit==0)continue;
            if(mapX<0)mapX=0;if(mapX>=W)mapX=W-1;
            if(mapY<0)mapY=0;if(mapY>=H)mapY=H-1;
            int block=world[mapX][mapY];
            if(block<=0)continue;
            if(side==0)perpWallDist=(mapX-pTileX+(1-stepX)/2)/rayDirX;
            else perpWallDist=(mapY-pTileY+(1-stepY)/2)/rayDirY;
            if(perpWallDist<=0)perpWallDist=0.01;
            int lineHeight=(int)(h/perpWallDist);
            int drawStart=-lineHeight/2+h/2;if(drawStart<0)drawStart=0;
            int drawEnd=lineHeight/2+h/2;if(drawEnd>=h)drawEnd=h-1;
            Color c=FB[Math.min(block,FB.length-1)];
            if(side==1)c=new Color(Math.max(0,c.getRed()-40),Math.max(0,c.getGreen()-40),Math.max(0,c.getBlue()-40));
            g2.setColor(c);
            g2.fillRect(x,drawStart,2,drawEnd-drawStart+1);
        }
        // Mini-map overlay
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(w-130,10,120,120);
        int ms=2;
        for(int mx2=0;mx2<W;mx2++)for(int my2=0;my2<H;my2++){
            if(world[mx2][my2]>0){
                Color mc=FB[Math.min(world[mx2][my2],FB.length-1)];
                g2.setColor(mc);
                g2.fillRect(w-130+mx2*ms,10+my2*ms,ms,ms);
            }
        }
        // Player dot on mini-map
        g2.setColor(Color.RED);
        g2.fillRect(w-130+(int)(px/TILE)*ms,10+(int)(py/TILE)*ms,ms,ms);
        // Direction line
        g2.setColor(Color.YELLOW);
        int mpx=w-130+(int)(px/TILE)*ms+1,mpy=10+(int)(py/TILE)*ms+1;
        g2.drawLine(mpx,mpy,(int)(mpx+Math.cos(playerDir)*8),(int)(mpy+Math.sin(playerDir)*8));
    }

    private void drawNameTag(Graphics2D g2,int x,int y,String name,Color c){
        g2.setFont(new Font("PixelPurl",Font.BOLD,12));
        int tw=g2.getFontMetrics().stringWidth(name);
        g2.setColor(new Color(0,0,0,120));g2.fillRect(x-tw/2-2,y-playerH/2-16,tw+4,14);
        g2.setColor(c);g2.drawString(name,x-tw/2,y-playerH/2-5);
    }

    private void drawHUD(Graphics2D g2){int bh=getHeight();
        int sw=getWidth();
        if(survival){
            for(int i=0;i<10;i++){
                g2.drawImage(heartImg[0],8+i*16,bh-90,null);
                if(i>=(health+1)/2){g2.setColor(new Color(0,0,0,120));g2.fillRect(8+i*16,bh-90,9,9);}
            }
            for(int i=0;i<10;i++){
                g2.drawImage(hungerImg[0],8+i*16,bh-70,null);
                if(i>=(hunger+1)/2){g2.setColor(new Color(0,0,0,120));g2.fillRect(8+i*16,bh-70,9,9);}
            }
            if(xp>0){
                g2.setColor(new Color(0,0,0,100));g2.fillRect(8,bh-60,160,5);
                g2.setColor(new Color(80,255,80));g2.fillRect(8,bh-60,xp*160/(xp+20),5);
                g2.setFont(new Font("PixelPurl",Font.PLAIN,12));g2.setColor(Color.WHITE);
                g2.drawString("XP:"+xp,8,bh-62);
            }
            if(armor>0){
                for(int i=0;i<armor;i++){g2.setColor(new Color(80,80,80));g2.fillRect(8+i*12,bh-56,10,10);g2.setColor(Color.BLUE);g2.drawRect(8+i*12,bh-56,10,10);}
            }
        }
        int hs=24,slots=Math.min(9,BLOCK_COUNT);
        int hotbarW=slots*hs+2,hbX=(sw-hotbarW)/2,hbY=bh-hs-4;
        g2.setColor(new Color(80,80,80,200));g2.fillRect(hbX-2,hbY-2,hotbarW+4,hs+4);
        g2.setColor(new Color(50,50,50,200));g2.fillRect(hbX,hbY,hotbarW,hs);
        for(int i=0;i<slots;i++){
            int idx=survival?i+1:i+1+creativeOffset;
            if(idx>=BLOCK_COUNT)break;
            int sx=hbX+i*hs,sy=hbY;
            g2.setColor(i==(selBlock-1-creativeOffset)||(!survival&&i==selBlock-1)?new Color(255,255,255,100):new Color(30,30,30,100));
            g2.fillRect(sx,sy,hs,hs);
            g2.setColor(i==(selBlock-1-creativeOffset)||(!survival&&i==selBlock-1)?Color.WHITE:new Color(100,100,100));
            g2.drawRect(sx,sy,hs,hs);
            g2.drawImage(tex[Math.min(idx,BLOCK_COUNT-1)],sx+2,sy+2,null);
            g2.setFont(new Font("PixelPurl",Font.PLAIN,9));g2.setColor(new Color(180,180,180));
            g2.drawString(""+(i+1),sx+2,sy+10);
            if(survival){int cnt=getInvCount(idx);if(cnt>0){g2.setFont(new Font("PixelPurl",Font.PLAIN,12));g2.setColor(Color.WHITE);g2.drawString(""+cnt,sx+hs-10,sy+hs-3);}}
        }
        if(!survival){
            g2.setFont(new Font("PixelPurl",Font.PLAIN,13));
            g2.setColor(new Color(0,0,0,180));g2.fillRect(sw/2-60,2,120,16);
            g2.setColor(Color.WHITE);g2.drawString(BNAME[Math.min(selBlock,BLOCK_COUNT-1)],sw/2-55,14);
        }
        g2.setColor(new Color(0,0,0,150));g2.fillRect(10,10,280,24);
        g2.setColor(Color.WHITE);g2.setFont(new Font("PixelPurl",Font.BOLD,12));
        g2.drawString(worldName+(!worldName.isEmpty()?" | ":"")+playerName+(noclip?" NOCLIP":"")+(inNether?" NETHER":"")+(physicsLevel==0?" NO PHY":physicsLevel==2?" RTX PHY":"")+(survival?" S":" C")+(kills>0?" K:"+kills:""),15,25);
        if(showCoords){g2.setColor(new Color(0,0,0,150));g2.fillRect(10,36,140,14);g2.setColor(new Color(200,200,200));g2.setFont(new Font("PixelPurl",Font.PLAIN,12));g2.drawString("X:"+(int)(px/TILE)+" Y:"+(int)(py/TILE),15,46);}
    }

    private void drawDeath(Graphics2D g2){g2.setColor(new Color(100,0,0,200));g2.fillRect(0,0,getWidth(),getHeight());g2.setFont(new Font("PixelPurl",Font.BOLD,48));g2.setColor(Color.RED);String t="YOU DIED";g2.drawString(t,(getWidth()-g2.getFontMetrics().stringWidth(t))/2,getHeight()/2-20);g2.setFont(new Font("PixelPurl",Font.BOLD,20));g2.setColor(Color.WHITE);String r="Press ENTER to respawn";g2.drawString(r,(getWidth()-g2.getFontMetrics().stringWidth(r))/2,getHeight()/2+30);}

    private void drawChat(Graphics2D g2){
        int y=getHeight()-130;
        g2.setFont(new Font("PixelPurl",Font.PLAIN,13));
        for(int i=Math.max(0,chatMessages.size()-5);i<chatMessages.size();i++){
            g2.setColor(new Color(0,0,0,100));g2.fillRect(5,y-2,400,14);
            g2.setColor(Color.WHITE);g2.drawString(chatMessages.get(i),8,y+2);y+=16;
        }
        if(chatOpen){
            g2.setColor(new Color(0,0,0,180));g2.fillRect(5,getHeight()-25,410,22);
            g2.setColor(Color.WHITE);g2.setFont(new Font("PixelPurl",Font.PLAIN,12));
            g2.drawString((chatText.startsWith("/")?"":"")+"> "+chatText+(System.currentTimeMillis()/500%2==0?"_":""),8,getHeight()-9);
        }
    }
    private void drawCrafting(Graphics2D g2){g2.setColor(new Color(60,60,60));g2.fillRect(0,0,getWidth(),getHeight());g2.setColor(new Color(80,80,80));g2.fillRect(20,20,140,140);g2.fillRect(180,20,600,140);g2.fillRect(20,180,760,160);g2.setColor(Color.WHITE);g2.drawRect(20,20,140,140);g2.drawRect(180,20,600,140);g2.drawRect(20,180,760,160);g2.setFont(new Font("PixelPurl",Font.BOLD,16));g2.setColor(Color.WHITE);g2.drawString("Crafting",25,40);g2.drawString("Inventory",25,200);g2.setFont(new Font("PixelPurl",Font.PLAIN,12));g2.setColor(new Color(200,200,100));g2.drawString("Click item -> click slot | SPACE to craft",25,160);
        for(int i=0;i<4;i++){int cx=30+(i%2)*60,cy=50+(i/2)*60;g2.setColor(new Color(50,50,50));g2.fillRect(cx,cy,50,50);g2.setColor(Color.GRAY);g2.drawRect(cx,cy,50,50);if(craftGrid[i]>0)g2.drawImage(tex[craftGrid[i]],cx+1,cy+1,null);}
        int[] res=getCraft();g2.setColor(new Color(50,50,50));g2.fillRect(130,85,50,50);g2.setColor(Color.YELLOW);g2.drawRect(130,85,50,50);if(res[0]>0)g2.drawImage(tex[Math.min(res[0],BLOCK_COUNT-1)],131,86,null);
        for(int i=0;i<inv.length;i++){int ix=30+(i%8)*60,iy=220+(i/8)*60;if(iy>getHeight()-60)break;g2.setColor(new Color(50,50,50));g2.fillRect(ix,iy,50,50);g2.setColor(Color.GRAY);g2.drawRect(ix,iy,50,50);if(inv[i]>0){g2.drawImage(tex[Math.min(inv[i],BLOCK_COUNT-1)],ix+1,iy+1,null);if(invCount[i]>1){g2.setFont(new Font("PixelPurl",Font.BOLD,13));g2.setColor(Color.WHITE);g2.drawString(""+invCount[i],ix+38,iy+46);}}}
        g2.setFont(new Font("PixelPurl",Font.PLAIN,14));g2.setColor(new Color(200,200,200));g2.drawString("Recipes:",500,40);g2.drawString("Log -> 4 Planks",500,60);g2.drawString("Stone -> Cobblestone",500,80);g2.drawString("2 Planks -> 4 Sticks",500,100);g2.drawString("4 Planks -> Crafting Table",500,120);
    }

    @Override public void keyPressed(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_ENTER&&screen==Screen.HELP){screen=Screen.MENU;return;}
        if(!chatOpen){
            if(e.getKeyCode()==KeyEvent.VK_F1){showFps=!showFps;return;}
            if(e.getKeyCode()==KeyEvent.VK_F2){showCoords=!showCoords;return;}
            if(e.getKeyCode()==KeyEvent.VK_F3){showDebug=!showDebug;return;}
            if(e.getKeyCode()==KeyEvent.VK_F4){ultraFps=!ultraFps;return;}
            if(e.getKeyCode()==KeyEvent.VK_F5){rtxMode=!rtxMode;return;}
            if(e.getKeyCode()==KeyEvent.VK_F6){rtxWater=!rtxWater;return;}
            if(e.getKeyCode()==KeyEvent.VK_F7){physicsLevel=(physicsLevel+1)%3;physicsOn=physicsLevel>0;return;}
            if(e.getKeyCode()==KeyEvent.VK_F8){shaderMode=(shaderMode+1)%3;return;}
            if(e.getKeyCode()==KeyEvent.VK_F9){threeDMode=!threeDMode;addChat("3D","Mode: "+(threeDMode?"ON":"OFF"));return;}
            if(e.getKeyCode()==KeyEvent.VK_M){toggleMusic();return;}
            if(e.getKeyCode()==KeyEvent.VK_F11&&(screen==Screen.PLAY||screen==Screen.SETTINGS)){
                toggleFullscreen();
                return;
            }
        }
        if(e.getKeyCode()==KeyEvent.VK_G&&(screen==Screen.PLAY||screen==Screen.SETTINGS)&&!chatOpen){noclip=!noclip;return;}
        if(screen==Screen.SETTINGS){
            if(nameEditing){
                if(e.getKeyCode()==KeyEvent.VK_ENTER||e.getKeyCode()==KeyEvent.VK_ESCAPE){nameEditing=false;syncName();}
                else if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE&&playerName.length()>0)playerName=playerName.substring(0,playerName.length()-1);
                else{char c=e.getKeyChar();if(c>=' '&&c<='~'&&playerName.length()<16)playerName+=c;}
                return;
            }
            if(e.getKeyCode()==KeyEvent.VK_N){settingSel=5;nameEditing=true;return;}
            if(e.getKeyCode()==KeyEvent.VK_COMMA||e.getKeyCode()==KeyEvent.VK_LEFT){gameFov=Math.max(10,gameFov-1);return;}
            if(e.getKeyCode()==KeyEvent.VK_PERIOD||e.getKeyCode()==KeyEvent.VK_RIGHT){gameFov=Math.min(50,gameFov+1);return;}
            if(e.getKeyCode()==KeyEvent.VK_U){if(updateAvailable)doUpdate();else{new Thread(()->checkUpdate()).start();}}
            return;
        }
        if(chatOpen){
            if(e.getKeyCode()==KeyEvent.VK_ENTER&&!chatText.isEmpty()){
                if(chatText.startsWith("/")){runCommand(chatText);chatText="";chatOpen=false;}
                else{syncChat(chatText);chatText="";chatOpen=false;}
            }
            else if(e.getKeyCode()==KeyEvent.VK_ESCAPE){chatText="";chatOpen=false;}
            else if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE&&chatText.length()>0)chatText=chatText.substring(0,chatText.length()-1);
            else{char c=e.getKeyChar();if(c>=' '&&c<='~'&&chatText.length()<60)chatText+=c;}
            return;
        }
        if(e.getKeyCode()>=0&&e.getKeyCode()<keys.length)keys[e.getKeyCode()]=true;
        if(e.getKeyCode()==KeyEvent.VK_T&&screen==Screen.PLAY){chatOpen=true;chatText="";return;}
        if(screen==Screen.CREATE_WORLD){
            if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE&&typing.length()>0)typing=typing.substring(0,typing.length()-1);
            else if(e.getKeyCode()==KeyEvent.VK_ENTER&&!typing.isEmpty()){genWorld(System.currentTimeMillis());sessionStart=System.currentTimeMillis();blocksBroken=0;blocksPlaced=0;screen=Screen.PLAY;}
            else{char c=e.getKeyChar();if(c>=' '&&c<='~'&&typing.length()<20)typing+=c;}
            return;
        }
        if(screen==Screen.CONNECT){
            if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE&&typing.length()>0)typing=typing.substring(0,typing.length()-1);
            else if(e.getKeyCode()==KeyEvent.VK_ENTER&&!typing.isEmpty())tryConnect();
            else{char c=e.getKeyChar();if(c>=' '&&c<='~'&&typing.length()<40)typing+=c;}
            return;
        }
        if(screen==Screen.PLAY&&e.getKeyCode()==KeyEvent.VK_E){craftingOpen=!craftingOpen;screen=craftingOpen?Screen.CRAFTING:Screen.PLAY;return;}
        if(screen==Screen.PLAY&&e.getKeyCode()==KeyEvent.VK_F){survival=!survival;return;}
        if(screen==Screen.PLAY&&e.getKeyCode()==KeyEvent.VK_V){if(voiceChat!=null){voiceChat.shutdown();voiceChat=null;addChat("Voice","stopped");}else{int vp=(serverPort>0?serverPort:clientPort>0?clientPort:0);voiceChat=new VoiceChatThread(vp);voiceChat.start();addChat("Voice","started on port "+(vp+1000));}return;}
        if(screen==Screen.PLAY&&e.getKeyCode()==KeyEvent.VK_ESCAPE){screen=Screen.PAUSE;return;}
        if(screen==Screen.PAUSE&&e.getKeyCode()==KeyEvent.VK_ESCAPE){screen=Screen.PLAY;return;}
        if(screen==Screen.DEATH&&e.getKeyCode()==KeyEvent.VK_ENTER){px=W/2.0*TILE;py=getGround(W/2)*TILE-playerH/2;health=20;hunger=20;dead=false;fallDist=0;playerVy=0;screen=Screen.PLAY;}
        if(screen==Screen.CRAFTING&&e.getKeyCode()==KeyEvent.VK_E){craftingOpen=false;screen=Screen.PLAY;return;}
        if(screen==Screen.CRAFTING&&e.getKeyCode()==KeyEvent.VK_SPACE){int[] r=getCraft();if(r[0]>0){boolean h=true;for(int i=0;i<4;i++)if(craftGrid[i]>0&&!takeFromInv(craftGrid[i],craftCount[i]))h=false;if(h){addToInv(r[0],r[1]);for(int i=0;i<4;i++){craftGrid[i]=0;craftCount[i]=0;}}}}
        if(screen==Screen.PLAY&&e.getKeyCode()==KeyEvent.VK_SPACE){if(survival&&selBlock==ELYTRA){boolean onG=false;for(int gx=-playerW/2;gx<=playerW/2;gx+=8){int fx=(int)((px+gx)/TILE),fy=(int)((py+playerH/2+2)/TILE);if(isSolid(fx,fy)){onG=true;break;}}if(!onG)elytraFly=20;}else if(!survival){int tx=(int)((px)/TILE),ty=(int)((py+playerH/2)/TILE);for(int dy=ty;dy<H;dy++)if(isSolid(tx,dy)){if(dy>0)py=(dy-1)*TILE;playerVy=0;break;}}}
    }
    @Override public void keyReleased(KeyEvent e){if(e.getKeyCode()>=0&&e.getKeyCode()<keys.length)keys[e.getKeyCode()]=false;}
    @Override public void keyTyped(KeyEvent e){}

    private void stopNetworking(){
        stopWebServer();
        if(isHost&&server!=null){server.stopServer();server=null;isHost=false;}
        if(responder!=null){responder.stopDisc();responder=null;}
        if(client!=null){client.disconnect();client=null;}
        if(discovery!=null){discovery.stopDisc();discovery=null;}
        remotePlayers.clear();
    }

    private void connectToServer(String ip,int port){
        if(screen==Screen.PLAY)return;
        screen=Screen.CONNECTING;lastMsg="";
        new Thread(()->{
            try{
                serverIP=ip;serverPort=port;clientPort=port;
                client=new MiniClient(ip,port);
                if(client.connect()){
                    new Thread(()->{try{Thread.sleep(8000);SwingUtilities.invokeLater(()->{if(screen==Screen.CONNECTING){screen=Screen.CONNECT;lastMsg="Timeout! Server not responding.";}});}catch(Exception e){}}).start();}
                else{SwingUtilities.invokeLater(()->{screen=Screen.CONNECT;lastMsg="Failed: connection refused";});}
            }catch(Exception e){SwingUtilities.invokeLater(()->{screen=Screen.CONNECT;lastMsg="Error: "+e.getMessage();});}
        }).start();
    }
    private void tryConnect(){
        String a=typing.trim();if(a.matches("\\d{4,6}"))a="bore.pub:"+a;
        String[] parts=a.split(":");String ip=parts[0];int port=parts.length>1?Integer.parseInt(parts[1]):25565;
        connectToServer(ip,port);
    }

    private boolean loadUnconnectedWorld(){
        File[] f=new File(DATA_DIR).listFiles((d,n)->n.endsWith(".mcw"));
        if(f!=null&&f.length>0){return loadWorld(f[0].getName().replace(".mcw",""));}
        return false;
    }

    @Override public void mouseMoved(MouseEvent e){mx=e.getX();my=e.getY();menuHover=-1;
        if(threeDMode&&screen==Screen.PLAY){
            int cx=getWidth()/2;
            playerDir+=(mx-cx)*0.002;
            if(robotOk){try{robot.mouseMove(cx,my);}catch(Exception ex){robotOk=false;}}
        }
        int w=getWidth()/2;
        if(screen==Screen.MENU){if(inBtn(mx,my,w-100,140,200,36))menuHover=7;else if(inBtn(mx,my,w-100,186,200,40))menuHover=0;else if(inBtn(mx,my,w-100,236,200,40))menuHover=1;else if(inBtn(mx,my,w-100,286,200,40))menuHover=2;else if(inBtn(mx,my,w-100,336,200,40))menuHover=3;else if(inBtn(mx,my,w-100,386,200,40))menuHover=80;else if(inBtn(mx,my,w-100,436,200,40))menuHover=4;else if(inBtn(mx,my,w-100,486,95,32))menuHover=5;else if(inBtn(mx,my,w+5,486,95,32))menuHover=6;}
        if(screen==Screen.WORLD_LIST){int yy=Math.max(worldList.isEmpty()?130:100+worldList.size()*42+10,350);if(inBtn(mx,my,w-120,yy,240,36))menuHover=10;else if(selectedWorld>=0&&inBtn(mx,my,w-120,yy+46,240,36))menuHover=11;else if(selectedWorld>=0&&inBtn(mx,my,w-60,yy+92,120,36))menuHover=12;else if(inBtn(mx,my,w-60,yy+138,120,36))menuHover=13;}
        if(screen==Screen.CREATE_WORLD){if(inBtn(mx,my,w-60,300,120,36))menuHover=20;else if(inBtn(mx,my,w-60,350,120,36))menuHover=21;else if(inBtn(mx,my,w-140,255,280,32))menuHover=22;}
        if(screen==Screen.MULTIPLAYER){
            int yy=100+Math.min(discoveredServers.size(),5)*42;
            yy=Math.max(yy+10,300);
            if(inBtn(mx,my,w-100,yy,200,36))menuHover=30;
            else if(inBtn(mx,my,w-100,yy+46,200,36))menuHover=31;
            else if(inBtn(mx,my,w-100,yy+92,200,36))menuHover=33;
            else if(inBtn(mx,my,w-100,yy+138,200,36))menuHover=32;
            else{selectedServer=-1;for(int i=0;i<discoveredServers.size();i++){if(inBtn(mx,my,w-220,100+i*42,440,36)){selectedServer=i;break;}}}
        }
        if(screen==Screen.CONNECT){if(inBtn(mx,my,w-60,220,120,36))menuHover=40;else if(inBtn(mx,my,w-60,270,120,36))menuHover=41;}
        if(screen==Screen.SETTINGS){if(inBtn(mx,my,w-60,getHeight()-80,120,36))menuHover=60;}
        if(screen==Screen.HOST){if(inBtn(mx,my,w-80,300,160,36))menuHover=50;else if(inBtn(mx,my,w-60,360,120,36))menuHover=51;}
        if(screen==Screen.PAUSE){int bw=getWidth()/2,cy=getHeight()/2;if(inBtn(mx,my,bw-100,cy-10,200,36))menuHover=70;else if(inBtn(mx,my,bw-100,cy+40,200,36))menuHover=71;else if(inBtn(mx,my,bw-100,cy+90,200,36))menuHover=73;else if(inBtn(mx,my,bw-100,cy+140,200,36))menuHover=72;}
    }

    @Override public void mousePressed(MouseEvent e){int wx=e.getX(),wy=e.getY(),w=getWidth()/2;
        if(screen==Screen.HELP){screen=Screen.MENU;return;}
        if(screen==Screen.MENU){
            if(inBtn(wx,wy,w-100,140,200,36)){bedrockEdition=!bedrockEdition;return;}
            if(inBtn(wx,wy,w-100,186,200,40)){playSound("click");refreshWorldList();screen=worldList.isEmpty()?Screen.CREATE_WORLD:Screen.WORLD_LIST;}
            else if(inBtn(wx,wy,w-100,236,200,40)){playSound("click");
                discoveredServers.clear();selectedServer=-1;
                if(discovery!=null)discovery.stopDisc();discovery=new DiscoveryThread();discovery.start();
                screen=Screen.MULTIPLAYER;
            }
            else if(inBtn(wx,wy,w-100,286,200,40)){playSound("click");screen=Screen.SETTINGS;}
            else if(inBtn(wx,wy,w-100,336,200,40)){playSound("click");try{Runtime.getRuntime().exec(new String[]{"flatpak","run","org.prismlauncher.PrismLauncher"});}catch(Exception ex){try{Runtime.getRuntime().exec(new String[]{"/var/home/olda/PrismLauncher-Linux-x86_64.AppImage"});}catch(Exception ex2){}}}
            else if(inBtn(wx,wy,w-100,386,200,40)){playSound("click");SwingUtilities.invokeLater(()->new RPCVisualizer().setVisible(true));}
            else if(inBtn(wx,wy,w-100,436,200,40)){playSound("click");System.exit(0);}
            else if(inBtn(wx,wy,w-100,486,95,32)){try{java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://discord.gg/wAWrPCHR5z"));}catch(Exception ex){}}
            else if(inBtn(wx,wy,w+5,486,95,32)){try{java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://github.com/olda9991/minicraft"));}catch(Exception ex){}}
            return;
        }
        if(screen==Screen.WORLD_LIST){int yy=100;for(int i=0;i<worldList.size();i++){if(inBtn(wx,wy,w-150,yy,300,36)){selectedWorld=i;return;}yy+=42;}if(worldList.isEmpty())yy+=30;yy=Math.max(yy+10,350);
            if(inBtn(wx,wy,w-120,yy,240,36)){typing="My World";screen=Screen.CREATE_WORLD;}
            else if(selectedWorld>=0&&inBtn(wx,wy,w-120,yy+46,240,36)){deleteWorld(worldList.get(selectedWorld));selectedWorld=-1;}
            else if(selectedWorld>=0&&inBtn(wx,wy,w-60,yy+92,120,36)){loadWorld(worldList.get(selectedWorld));}
            else if(inBtn(wx,wy,w-60,yy+138,120,36)){stopNetworking();screen=Screen.MENU;}return;
        }
        if(screen==Screen.CREATE_WORLD){
            if(inBtn(wx,wy,w-140,255,280,32)){superflat=!superflat;return;}
            if(inBtn(wx,wy,w-60,300,120,36)&&!typing.isEmpty()){worldName=typing;genWorld(System.currentTimeMillis());screen=Screen.PLAY;}
            else if(inBtn(wx,wy,w-60,350,120,36))screen=Screen.WORLD_LIST;return;
        }
        if(screen==Screen.MULTIPLAYER){
            int yy=100+Math.min(discoveredServers.size(),5)*42;
            yy=Math.max(yy+10,300);
            if(inBtn(wx,wy,w-100,yy,200,36)){stopNetworking();loadUnconnectedWorld();screen=Screen.HOST;}
            else if(inBtn(wx,wy,w-100,yy+46,200,36)){typing="";screen=Screen.CONNECT;}
            else if(inBtn(wx,wy,w-100,yy+92,200,36)){discoveredServers.clear();if(discovery!=null)discovery.stopDisc();discovery=new DiscoveryThread();discovery.start();}
            else if(inBtn(wx,wy,w-100,yy+138,200,36)){if(discovery!=null)discovery.stopDisc();screen=Screen.MENU;}
            else if(selectedServer>=0&&selectedServer<discoveredServers.size()){
                DiscoveredServer ds=discoveredServers.get(selectedServer);
                serverIP=ds.ip;serverPort=ds.port;
                typing=ds.ip+":"+ds.port;
                tryConnect();
            }
            return;
        }
        if(screen==Screen.CONNECT){
            if(inBtn(wx,wy,w-60,220,120,36)&&!typing.isEmpty())tryConnect();
            else if(inBtn(wx,wy,w-60,270,120,36))screen=Screen.MULTIPLAYER;return;
        }
        if(screen==Screen.SETTINGS){
            if(inBtn(wx,wy,w-60,getHeight()-80,120,36))screen=Screen.MENU;return;
        }
        if(screen==Screen.PAUSE){
            int bw=getWidth()/2;
            if(inBtn(wx,wy,bw-100,getHeight()/2-10,200,36)){screen=Screen.PLAY;}
            else if(inBtn(wx,wy,bw-100,getHeight()/2+40,200,36)){screen=Screen.SETTINGS;}
            else if(inBtn(wx,wy,bw-100,getHeight()/2+90,200,36)){SwingUtilities.invokeLater(()->new RPCVisualizer().setVisible(true));}
            else if(inBtn(wx,wy,bw-100,getHeight()/2+140,200,36)){
                saveWorld(worldName.isEmpty()?"world_"+System.currentTimeMillis():worldName);saveSettings();
                refreshWorldList();stopNetworking();screen=Screen.WORLD_LIST;
            }
            return;
        }
        if(screen==Screen.HOST){
            if(inBtn(wx,wy,w-80,300,160,36)){
                if(server!=null&&server.isRunning()){server.stopServer();server=null;isHost=false;if(responder!=null){responder.stopDisc();responder=null;}
                    if(client!=null){client.disconnect();client=null;}remotePlayers.clear();screen=Screen.MULTIPLAYER;
                }else{
                    server=new MiniServer(serverPort);isHost=true;server.start();
                    addChat("Server","Running on port "+serverPort+". Invite friends: give them this code.");
                    if(responder!=null)responder.stopDisc();responder=new DiscoveryResponder();responder.start();
                    if(!loadUnconnectedWorld()){genWorld(System.currentTimeMillis());worldName="host_"+System.currentTimeMillis();}
                    startWebServer();
                    sessionStart=System.currentTimeMillis();blocksBroken=0;blocksPlaced=0;screen=Screen.PLAY;
                }
            }else if(inBtn(wx,wy,w-60,360,120,36)){stopNetworking();screen=Screen.MULTIPLAYER;}return;
        }
        if(screen==Screen.DEATH)return;
        if(screen==Screen.CRAFTING){
            int cx=(wx-30)/60,cy=(wy-50)/60,idx=cx+cy*2;
            if(cx>=0&&cx<2&&cy>=0&&cy<2&&idx>=0&&idx<4){
                if(craftGrid[idx]>0){addToInv(craftGrid[idx],craftCount[idx]);craftGrid[idx]=0;craftCount[idx]=0;selInv=-1;}
                else if(selInv>=0&&selInv<inv.length&&inv[selInv]>0){craftGrid[idx]=inv[selInv];craftCount[idx]=1;invCount[selInv]--;if(invCount[selInv]<=0)inv[selInv]=0;selInv=-1;}
                return;
            }
            for(int i=0;i<inv.length;i++){int ix=30+(i%8)*60,iy=220+(i/8)*60;if(wx>=ix&&wx<ix+50&&wy>=iy&&wy<iy+50&&inv[i]>0){selInv=i;return;}}
            return;
        }
        if(screen==Screen.PLAY){int tx=(mx+camX)/TILE,ty=(my+camY)/TILE;if(!isIn(tx,ty))return;int pt=(int)(px/TILE),pyt=(int)(py/TILE);if(Math.abs(tx-pt)+Math.abs(ty-pyt)<2)return;
            if(e.getButton()==MouseEvent.BUTTON1){
                boolean hitMob=false;int dmg=selBlock==SWORD?7:selBlock==AXE?5:selBlock==PICKAXE?4:3;
                for(Mob m:mobs){if(Math.abs((mx+camX)-m.x)<24&&Math.abs((my+camY)-m.y)<24){int critDmg=dmg;boolean onG=false;int fx=(int)((px+14)/TILE),fy=(int)((py+TILE-4)/TILE);if(isSolid(fx,fy))onG=true;if(!onG){critDmg*=2;for(int i=0;i<5;i++)particles.add(new Particle(m.x,m.y-20,GOLD_ORE));}m.health-=critDmg;m.hurtT=10;dmgNums.add(new DmgNum(m.x,m.y-20,critDmg));m.x+=(m.x>px?8:-8);if(m.health<=0){if(m.type==6){drops.add(new DropItem(m.x,m.y,DIAMOND_GEM));drops.add(new DropItem(m.x-10,m.y,DIAMOND_GEM));for(int i=0;i<10;i++)drops.add(new DropItem(m.x+Math.random()*40-20,m.y+Math.random()*20-10,EXP_ORB));achieve("BOSS DEFEATED!");}else{drops.add(new DropItem(m.x,m.y,m.type==0?RAW_BEEF:m.type==1?RAW_PORK:m.type==3?WOOL:m.type==4?COOKED_BEEF:COOKED_BEEF));drops.add(new DropItem(m.x-10,m.y-10,EXP_ORB));}mobs.remove(m);kills++;if(kills==1)achieve("First Blood!");if(kills==10)achieve("Monster Hunter!");if(kills==50)achieve("Slayer!");}hitMob=true;break;}}
                if(!hitMob){if(survival&&world[tx][ty]>0){breakX=tx;breakY=ty;breakTimer=0;int spd=1;if(selBlock==PICKAXE)spd=4;if(selBlock==AXE)spd=4;if(selBlock==SHOVEL)spd=4;breakTime=Math.max(1,BT[Math.min(world[tx][ty],BT.length-1)]/spd);}else if(!survival){world[tx][ty]=0;syncBlock(tx,ty,0);}}
            }
            else if(e.getButton()==MouseEvent.BUTTON3&&selBlock==FLINT_STEEL){
                int ftx=(mx+camX)/TILE,fty=(my+camY)/TILE;
                if(isIn(ftx,fty)&&world[ftx][fty]==AIR){
                    int fbelow=fty+1<H?world[ftx][fty+1]:0;
                    if(fbelow>0&&fbelow!=BEDROCK&&fbelow!=WATER){
                        boolean soul=fbelow==SOUL_SAND||fbelow==SOUL_SOIL;
                        world[ftx][fty]=soul?SOUL_FIRE:FIRE;
                        syncBlock(ftx,fty,world[ftx][fty]);playSound("flint");return;
                    }
                }
            }
            else if(e.getButton()==MouseEvent.BUTTON3&&selBlock>=0&&(!survival||getInvCount(selBlock)>0)){
                if(survival&&world[tx][ty]==TNT&&selBlock==FLINT_STEEL){tntList.add(new PrimedTnt(tx*TILE+TILE/2,ty*TILE+TILE/2));world[tx][ty]=0;return;}
                if(survival&&selBlock==TORCH_ITEM){world[tx][ty]=selBlock;takeFromInv(selBlock,1);return;}
                if(survival&&selBlock==ROD&&world[tx][ty]==WATER){addToInv(RAW_FISH,1);playSound("splash");return;}
                if(survival&&selBlock==BOW&&getInvCount(ARROW_ITEM)>=1){takeFromInv(ARROW_ITEM,1);arrows.add(new Arrow(px,py,(mx+camX-px)*0.3,(my+camY-py)*0.3));playSound("bow");return;}
                if(survival&&(selBlock==RAW_BEEF||selBlock==COOKED_BEEF||selBlock==RAW_PORK||selBlock==COOKED_PORK||selBlock==RAW_FISH||selBlock==COOKED_FISH)){hunger=Math.min(20,hunger+(selBlock==COOKED_BEEF||selBlock==COOKED_PORK||selBlock==COOKED_FISH?8:4));takeFromInv(selBlock,1);playSound("eat");return;}
                if(survival&&selBlock==BED){worldTime=6000;addChat("Bed","Good morning!");return;}
                if(survival&&selBlock==FURNACE_ITEM&&getInvCount(IRON_ORE)>=1&&getInvCount(COAL_ORE)>=1){takeFromInv(IRON_ORE,1);takeFromInv(COAL_ORE,1);addToInv(IRON_INGOT,1);return;}
                if(survival&&selBlock==FURNACE_ITEM&&getInvCount(GOLD_ORE)>=1&&getInvCount(COAL_ORE)>=1){takeFromInv(GOLD_ORE,1);takeFromInv(COAL_ORE,1);addToInv(GOLD_INGOT,1);return;}
                if(survival&&selBlock==TOTEM){takeFromInv(TOTEM,1);armor=Math.max(armor,5);playSound("totem");return;}
                if(survival&&selBlock==IRON_INGOT){takeFromInv(IRON_INGOT,1);armor=Math.max(armor,2);return;}
                if(survival&&selBlock==DIAMOND_GEM){takeFromInv(DIAMOND_GEM,1);armor=Math.max(armor,3);return;}
                if(survival&&world[tx][ty]==CHEST){for(int i=0;i<8;i++)if(inv[i]>0){addToInv(inv[i],invCount[i]);inv[i]=0;invCount[i]=0;}addChat("Chest","Opened!");}
                else if(selBlock<=CRAFTING_TABLE){
                    if(bgEdit&&bgWorld!=null){bgWorld[tx][ty]=selBlock;if(survival)takeFromInv(selBlock,1);}
                    else{world[tx][ty]=selBlock;if(survival)takeFromInv(selBlock,1);syncBlock(tx,ty,selBlock);blocksPlaced++;for(int i=0;i<4;i++)particles.add(new Particle(tx*TILE+TILE/2,ty*TILE+TILE/2,selBlock));playSFX("place");}
                }
            }
        }
    }

    @Override public void mouseReleased(MouseEvent e){breakX=-1;breakY=-1;breakTimer=0;}
    @Override public void mouseEntered(MouseEvent e){mouseIn=true;}
    @Override public void mouseExited(MouseEvent e){mouseIn=false;breakX=-1;breakY=-1;breakTimer=0;}
    @Override public void mouseDragged(MouseEvent e){mx=e.getX();my=e.getY();
        if(threeDMode&&screen==Screen.PLAY){
            int cx=getWidth()/2;
            playerDir+=(mx-cx)*0.002;
            if(robotOk){try{robot.mouseMove(cx,my);}catch(Exception ex){robotOk=false;}}
        }
    }
    @Override public void mouseClicked(MouseEvent e){}

    @Override public void mouseWheelMoved(MouseWheelEvent e){
        if(screen==Screen.PLAY){
            if(!survival){creativeOffset+=e.getWheelRotation();if(creativeOffset<0)creativeOffset=0;if(creativeOffset>BLOCK_COUNT-10)creativeOffset=BLOCK_COUNT-10;}
            selBlock+=e.getWheelRotation();if(selBlock<1)selBlock=BLOCK_COUNT-1;if(selBlock>=BLOCK_COUNT)selBlock=1;
        }
    }

    class RemotePlayer{int id;double x,y,targetX,targetY;String name;RemotePlayer(int i,String n,double sx,double sy){id=i;name=n;x=targetX=sx;y=targetY=sy;}}

    class MiniServer extends Thread{
        private ServerSocket ss;private boolean running=false;
        private ArrayList<ClientHandler> clients=new ArrayList<>();
        private int nextId=1;
        MiniServer(int port){try{ss=new ServerSocket(port);System.out.println("Server listening on port "+port);}catch(Exception e){System.err.println("Server FAILED on port "+port+": "+e.getMessage());}}
        public void run(){
            running=true;
            while(running){try{ClientHandler ch=new ClientHandler(ss.accept());ch.start();synchronized(clients){clients.add(ch);}}catch(Exception e){if(running)try{Thread.sleep(100);}catch(Exception ex){}break;}}
        }
        void broadcast(String msg,int skipId){
            ArrayList<ClientHandler> snapshot;
            synchronized(clients){snapshot=new ArrayList<>(clients);}
            for(ClientHandler ch:snapshot)if(ch.name!=null&&ch.id!=skipId)try{ch.send(msg);}catch(Exception e){}
        }
        void broadcast(String msg){
            ArrayList<ClientHandler> snapshot;
            synchronized(clients){snapshot=new ArrayList<>(clients);}
            for(ClientHandler ch:snapshot)if(ch.name!=null)try{ch.send(msg);}catch(Exception e){}
        }
        int getPlayerCount(){return clients.size()+1;}
        boolean isRunning(){return ss!=null&&running;}
        void stopServer(){running=false;try{if(ss!=null)ss.close();}catch(Exception e){}synchronized(clients){for(ClientHandler ch:clients)ch.interrupt();clients.clear();}}
        void removeClient(ClientHandler ch){
            synchronized(clients){clients.remove(ch);}
            broadcast("L "+ch.id+" "+ch.name);
            synchronized(remotePlayers){remotePlayers.removeIf(rp->rp.id==ch.id);}
            System.out.println("[Server] -Player id:"+ch.id+" "+ch.name);
        }

        class ClientHandler extends Thread{
            Socket s;PrintWriter out;BufferedReader in;String name="";int x,y,id;
            ClientHandler(Socket s){this.s=s;}
            public void run(){
                try{out=new PrintWriter(s.getOutputStream(),true);in=new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String line;
                    while((line=in.readLine())!=null){
                        String[] p=line.split(" ",4);
                        if(p[0].equals("J")){
                            id=nextId++;
                            name=p.length>1?p[1]:"Player";
                            ArrayList<ClientHandler> snap;
                            synchronized(clients){snap=new ArrayList<>(clients);}
                            int cnt=0;for(ClientHandler ch:snap)if(ch!=this&&ch.name!=null&&ch.name.equals(name))cnt++;
                            if(cnt>0){String base=name.replaceAll("\\d+$","");int n=2;name=base+n;while(true){boolean taken=false;for(ClientHandler ch:snap)if(ch!=this&&ch.name!=null&&ch.name.equals(name)){taken=true;break;}if(!taken)break;n++;name=base+n;}}
                            out.println("N "+id+" "+name);
                            int plrIdx=0;for(ClientHandler ch:snap)if(ch!=this&&ch.name!=null)plrIdx++;
                            double spx=px+(plrIdx%3*48)-48;
                            double spy=py-(plrIdx*(plrIdx%3==0?64:32));
                            int gx=(int)(spx/TILE);
                            if(gx>=0&&gx<W){int gy2=getGround(gx);spy=gy2*TILE-playerH/2;}else{spx=px;}
                            synchronized(remotePlayers){remotePlayers.add(new RemotePlayer(id,name,spx,spy));System.out.println("[Server] +Player id:"+id+" "+name+" total:"+remotePlayers.size());}
                            out.println("W "+W+" "+H);
                            StringBuilder batch=new StringBuilder();
                            for(int x=0;x<W;x++)for(int y=0;y<H;y++){
                                batch.append("D ").append(x).append(" ").append(y).append(" ").append(world[x][y]).append("\n");
                                if(batch.length()>5000){out.print(batch.toString());out.flush();batch.setLength(0);}
                            }
                            if(batch.length()>0){out.print(batch.toString());out.flush();}
                            out.println("WD");out.flush();
                            out.println("S "+(int)spx+" "+(int)spy);
                            out.println("J 0 "+playerName+" "+(int)px+" "+(int)py);
                            for(ClientHandler ch:clients)if(ch!=this&&ch.name!=null){
                                out.println("J "+ch.id+" "+ch.name+" "+ch.x+" "+ch.y);
                            }
                            broadcast("J "+id+" "+name+" "+spx+" "+spy,id);
                            for(int hc=Math.max(0,chatMessages.size()-5);hc<chatMessages.size();hc++){
                                String cm=chatMessages.get(hc);
                                int ci=cm.indexOf('>');
                                if(ci>0)out.println("C "+cm.substring(1,ci)+" "+cm.substring(ci+2));
                            }
                        }
                        else if(p[0].equals("P")&&p.length>=3){
                            x=(int)Double.parseDouble(p[1]);y=(int)Double.parseDouble(p[2]);
                            synchronized(remotePlayers){
                                for(RemotePlayer rp:remotePlayers)if(rp.id==id){rp.targetX=Double.parseDouble(p[1]);rp.targetY=Double.parseDouble(p[2]);break;}
                            }
                            broadcast("P "+id+" "+p[1]+" "+p[2],id);
                        }
                        else if(p[0].equals("B")&&p.length>=4){
                            final int bx=Integer.parseInt(p[1]),by=Integer.parseInt(p[2]),bl=Integer.parseInt(p[3]);
                            SwingUtilities.invokeLater(()->{if(bx>=0&&bx<W&&by>=0&&by<H)world[bx][by]=bl;});
                            broadcast("B "+bx+" "+by+" "+bl,id);
                        }
                        else if(p[0].equals("C")&&p.length>=3){
                            String msg=p[2];addChat(name,msg);
                            broadcast("C "+name+" "+msg,id);
                        }
                        else if(p[0].equals("N")&&p.length>=2){
                            String oldName=name;name=p[1];
                            synchronized(remotePlayers){remotePlayers.removeIf(rp->rp.id==id);remotePlayers.add(new RemotePlayer(id,name,(int)px,(int)py));}
                            broadcast("N "+id+" "+oldName+" "+name,0);
                        }
                    }
                }catch(Exception e){}finally{try{s.close();}catch(Exception e){}if(name!=null)removeClient(this);}
            }
            void send(String m){try{if(out!=null){out.println(m);if(out.checkError()){out=null;try{s.close();}catch(Exception e2){}removeClient(this);}}}catch(Exception e){out=null;try{s.close();}catch(Exception e2){}removeClient(this);}}
        }
    }

    class MiniClient extends Thread{
        private Socket s;private PrintWriter out;private BufferedReader in;private boolean connected=false,running=false;
        private String host;private int port;
        private int[][] tempWorld=null;
        private boolean worldReady=false;
        MiniClient(String h,int p){host=h;port=p;}
        boolean connect(){
            try{s=new Socket();s.connect(new java.net.InetSocketAddress(host,port),4000);s.setSoTimeout(4000);out=new PrintWriter(s.getOutputStream(),true);in=new BufferedReader(new InputStreamReader(s.getInputStream()));connected=true;running=true;start();out.println("J "+playerName);return true;}
            catch(Exception e){return false;}
        }
        public void run(){
            try{
                String line;
                while((line=in.readLine())!=null){
                    String[] p=line.split(" ",4);
                    if(p[0].equals("W")&&p.length>=3){
                        int w=Integer.parseInt(p[1]),h2=Integer.parseInt(p[2]);
                        tempWorld=new int[w][h2];worldReady=false;
                    }
                    else if(p[0].equals("D")&&p.length>=4&&tempWorld!=null){
                        int x=Integer.parseInt(p[1]),y=Integer.parseInt(p[2]),bl=Integer.parseInt(p[3]);
                        if(x>=0&&x<tempWorld.length&&y>=0&&y<tempWorld[0].length)tempWorld[x][y]=bl;
                    }
                    else if(p[0].equals("S")&&p.length>=3){
                        final double sx=Double.parseDouble(p[1]),sy=Double.parseDouble(p[2]);
                        SwingUtilities.invokeLater(()->{px=sx;py=sy;});
                    }
                    else if(p[0].equals("WD")&&tempWorld!=null){
                        final int[][] tw=tempWorld;tempWorld=null;worldReady=true;
                        SwingUtilities.invokeLater(()->{world=tw;screen=Screen.PLAY;});
                    }
                    else if(p[0].equals("P")&&p.length>=4){
                        final int pid=Integer.parseInt(p[1]);
                        final double sx=Double.parseDouble(p[2]),sy=Double.parseDouble(p[3]);
                        SwingUtilities.invokeLater(()->{
                            synchronized(remotePlayers){
                                for(RemotePlayer rp:remotePlayers)if(rp.id==pid){rp.targetX=sx;rp.targetY=sy;return;}
                                remotePlayers.add(new RemotePlayer(pid,"?",sx,sy));
                                System.out.println("[Client] P-ADD id:"+pid+" total:"+remotePlayers.size());
                            }
                        });
                    }
                    else if(p[0].equals("J")&&p.length>=5){
                        final int jid=Integer.parseInt(p[1]);
                        final String jname=p[2];
                        final double jx=Double.parseDouble(p[3]),jy=Double.parseDouble(p[4]);
                        SwingUtilities.invokeLater(()->{
                            synchronized(remotePlayers){
                                for(RemotePlayer rp:remotePlayers)if(rp.id==jid){rp.name=jname;rp.targetX=jx;rp.targetY=jy;return;}
                                remotePlayers.add(new RemotePlayer(jid,jname,jx,jy));
                                System.out.println("[Client] J-ADD id:"+jid+" "+jname+" total:"+remotePlayers.size());
                            }
                        });
                    }
                    else if(p[0].equals("L")&&p.length>=2){
                        final int lid=Integer.parseInt(p[1]);
                        SwingUtilities.invokeLater(()->{synchronized(remotePlayers){remotePlayers.removeIf(rp->rp.id==lid);System.out.println("[Client] L-REMOVE id:"+lid+" total:"+remotePlayers.size());}});
                    }
                    else if(p[0].equals("B")&&p.length>=4){
                        final int bx=Integer.parseInt(p[1]),by=Integer.parseInt(p[2]),bl=Integer.parseInt(p[3]);
                        SwingUtilities.invokeLater(()->{if(world!=null&&bx>=0&&bx<world.length&&by>=0&&by<world[0].length)world[bx][by]=bl;});
                    }
                    else if(p[0].equals("C")&&p.length>=3){addChat(p[1],line.substring(line.indexOf(' ',line.indexOf(' ')+1)+1));}
                    else if(p[0].equals("N")&&p.length>=3){
                        final int nid=Integer.parseInt(p[1]);
                        if(p.length==3){playerName=p[2];}
                        else if(p.length>=4){
                            final String newN=p[3];
                            SwingUtilities.invokeLater(()->{
                                synchronized(remotePlayers){
                                    for(RemotePlayer rp:remotePlayers)if(rp.id==nid){rp.name=newN;break;}
                                }
                            });
                        }
                    }
                }
            }catch(Exception e){}finally{connected=false;try{if(s!=null)s.close();}catch(Exception ex){}}
        }
        void send(String m){try{if(out!=null){out.println(m);if(out.checkError())throw new Exception("broken");}}catch(Exception e){out=null;connected=false;if(screen==Screen.PLAY)SwingUtilities.invokeLater(()->lastMsg="Connection lost!");}}
        void disconnect(){running=false;try{if(s!=null)s.close();}catch(Exception e){}connected=false;out=null;}
        boolean isConnected(){return connected;}
    }

    public static void main(String[] args){JFrame f=new JFrame("MiniCraft");MiniCraft g=new MiniCraft();f.add(g);f.pack();f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);f.setLocationRelativeTo(null);f.setResizable(false);f.setVisible(true);
        Runtime.getRuntime().addShutdownHook(new Thread(()->{if(g.discordRPC!=null)g.discordRPC.stopRPC();}));
        f.addWindowListener(new java.awt.event.WindowAdapter(){public void windowClosing(java.awt.event.WindowEvent e){g.saveSettings();g.stopNetworking();if(g.discordRPC!=null)g.discordRPC.stopRPC();System.exit(0);}});}
}
