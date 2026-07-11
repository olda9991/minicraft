//sha:38d1732a
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
    private static final String VERSION = "4.0";
    private static final String GITHUB_API = "https://api.github.com/repos/olda9991/minicraft/commits/main";
    private static final String GITHUB_RAW = "https://raw.githubusercontent.com/olda9991/minicraft/main/src/MiniCraft.java";
    private static final String DATA_DIR = System.getProperty("user.dir") + "/worlds/";
    private static final String TEX_DIR = System.getProperty("user.dir") + "/textures/";

    private BufferedImage[] tex, steveImg, heartImg, hungerImg;
    private BufferedImage logoImg;
    private int playerW=28, playerH=28;

    private static final int AIR=0,GRASS=1,DIRT=2,STONE=3,COBBLESTONE=4,BEDROCK=5,SAND=6,GRAVEL=7;
    private static final int OAK_LOG=8,SPRUCE_LOG=9,BIRCH_LOG=10,JUNGLE_LOG=11,ACACIA_LOG=12,DARK_OAK_LOG=13;
    private static final int OAK_PLANKS=14,SPRUCE_PLANKS=15,BIRCH_PLANKS=16,JUNGLE_PLANKS=17,ACACIA_PLANKS=18,DARK_OAK_PLANKS=19;
    private static final int OAK_LEAVES=20,SPRUCE_LEAVES=21,BIRCH_LEAVES=22,JUNGLE_LEAVES=23,ACACIA_LEAVES=24,DARK_OAK_LEAVES=25;
    private static final int WATER=26,SNOW=27,ICE=28,BRICKS=29,STONE_BRICKS=30,MOSSY_STONE_BRICKS=31,CRACKED_STONE_BRICKS=32;
    private static final int COAL_ORE=33,IRON_ORE=34,GOLD_ORE=35,DIAMOND_ORE=36,EMERALD_ORE=37,REDSTONE_ORE=38,LAPIS_ORE=39,COPPER_ORE=40;
    private static final int NETHERRACK=41,SOUL_SAND=42,GLOWSTONE=43,OBSIDIAN=44,CRYING_OBSIDIAN=45;
    private static final int SMOOTH_STONE=46,POLISHED_ANDESITE=47,POLISHED_DIORITE=48,POLISHED_GRANITE=49;
    private static final int PRISMARINE=50,DARK_PRISMARINE=51,SEA_LANTERN=52,END_STONE=53,PURPUR=54,MAGMA=55,SLIME=56,TNT=57,FURNACE=58,CRAFTING_TABLE=59;
    private static final int BLOCK_COUNT=75,STICK=99;
    private static final int RAW_BEEF=60,COOKED_BEEF=61,RAW_PORK=62,COOKED_PORK=63,WOOL=64;
    private static final int IRON_INGOT=65,GOLD_INGOT=66,DIAMOND_GEM=67,BED=68,EXP_ORB=69;
    private static final int SWORD=70,PICKAXE=71,AXE=72,SHOVEL=73,FURNACE_ITEM=74;
    private static final String[] BNAME={"Air","Grass","Dirt","Stone","Cobblestone","Bedrock","Sand","Gravel","Oak Log","Spruce Log","Birch Log","Jungle Log","Acacia Log","Dark Oak Log","Oak Planks","Spruce Planks","Birch Planks","Jungle Planks","Acacia Planks","Dark Oak Planks","Oak Leaves","Spruce Leaves","Birch Leaves","Jungle Leaves","Acacia Leaves","Dark Oak Leaves","Water","Snow","Ice","Bricks","Stone Bricks","Mossy Stone Bricks","Cracked Stone Bricks","Coal Ore","Iron Ore","Gold Ore","Diamond Ore","Emerald Ore","Redstone Ore","Lapis Ore","Copper Ore","Netherrack","Soul Sand","Glowstone","Obsidian","Crying Obsidian","Smooth Stone","Polished Andesite","Polished Diorite","Polished Granite","Prismarine","Dark Prismarine","Sea Lantern","End Stone","Purpur Block","Magma","Slime Block","TNT","Furnace","Crafting Table","Raw Beef","Cooked Beef","Raw Pork","Cooked Pork","Wool","Iron Ingot","Gold Ingot","Diamond","Bed","XP Orb","Sword","Pickaxe","Axe","Shovel","Furnace Item"};
    private static final String[] TF={"air","grass","dirt","stone","cobblestone","bedrock","sand","gravel","oak_log","spruce_log","birch_log","jungle_log","acacia_log","dark_oak_log","oak_planks","spruce_planks","birch_planks","jungle_planks","acacia_planks","dark_oak_planks","oak_leaves","spruce_leaves","birch_leaves","jungle_leaves","acacia_leaves","dark_oak_leaves","water","snow","ice","bricks","stone_bricks","mossy_stone_bricks","cracked_stone_bricks","coal_ore","iron_ore","gold_ore","diamond_ore","emerald_ore","redstone_ore","lapis_ore","copper_ore","netherrack","soul_sand","glowstone","obsidian","crying_obsidian","smooth_stone","polished_andesite","polished_diorite","polished_granite","prismarine","dark_prismarine","sea_lantern","end_stone","purpur_block","magma","slime_block","tnt_side","furnace_front","crafting_table_front","beef","cbeef","pork","cpork","wool","iron_ingot","gold_ingot","diamond","bed","xp","sword","pickaxe","axe","shovel","furnace"};
    private static final Color[] FB={new Color(0,0,0,0),new Color(100,180,60),new Color(140,100,60),new Color(120,120,120),new Color(100,100,100),new Color(30,30,30),new Color(230,210,160),new Color(140,130,120),new Color(100,80,50),new Color(80,60,40),new Color(180,160,100),new Color(120,100,70),new Color(160,120,60),new Color(60,40,20),new Color(160,130,80),new Color(140,110,70),new Color(180,150,100),new Color(160,130,90),new Color(180,140,80),new Color(100,70,40),new Color(50,140,50),new Color(40,120,40),new Color(60,150,60),new Color(50,130,50),new Color(60,140,50),new Color(40,110,30),new Color(80,80,200),new Color(220,220,230),new Color(160,200,220),new Color(180,120,80),new Color(160,160,160),new Color(140,140,120),new Color(130,130,130),new Color(60,60,60),new Color(180,140,160),new Color(220,180,40),new Color(80,200,220),new Color(60,180,60),new Color(200,80,80),new Color(60,80,180),new Color(160,120,80),new Color(100,40,40),new Color(80,60,40),new Color(200,180,80),new Color(30,20,50),new Color(80,30,60),new Color(180,180,180),new Color(140,140,150),new Color(160,160,170),new Color(160,140,120),new Color(80,160,160),new Color(60,120,120),new Color(200,200,160),new Color(220,200,160),new Color(160,80,120),new Color(200,80,40),new Color(80,200,80),new Color(180,60,40),new Color(120,120,120),new Color(140,100,60),new Color(200,60,60),new Color(180,100,60),new Color(255,150,150),new Color(200,150,150),new Color(220,220,220),new Color(180,180,180),new Color(255,220,40),new Color(180,240,200),new Color(200,100,100),new Color(80,255,80),new Color(180,180,180),new Color(200,200,200),new Color(180,180,180),new Color(200,200,200),new Color(120,120,120)};
    private static final int[] BT = new int[BLOCK_COUNT];
    static { for(int i=0;i<BLOCK_COUNT;i++){if(i==BEDROCK)BT[i]=99999;else if(i>=COAL_ORE&&i<=COPPER_ORE)BT[i]=20;else if(i==OBSIDIAN||i==CRYING_OBSIDIAN)BT[i]=120;else BT[i]=Math.max(3,i*2%15+2);} }

    private int[][] world;
    private double px,py;
    private String worldName="", playerName="Steve";
    private long worldSeed=0;
    private int selBlock=1, creativeOffset=0;
    private boolean[] keys=new boolean[1024];
    private javax.swing.Timer timer;
    private int camX,camY,mx=-999,my=-999;
    private boolean mouseIn=false;
    private int health=20,hunger=20;
    private boolean dead=false;
    private int fallDist=0,breakTimer=0,breakX=-1,breakY=-1,breakTime=0;
    private boolean craftingOpen=false,survival=true;
    private int hungerTimer=0;
    private int[] inv=new int[BLOCK_COUNT],invCount=new int[BLOCK_COUNT],craftGrid=new int[4],craftCount=new int[4];
    private Random rand=new Random();

    private enum Screen{MENU,WORLD_LIST,CREATE_WORLD,PLAY,CRAFTING,DEATH,MULTIPLAYER,CONNECT,HOST,SETTINGS,CONNECTING,PAUSE};
    private Screen screen=Screen.MENU;
    private ArrayList<String> worldList=new ArrayList<>();
    private String typing="";
    private int selectedWorld=-1;
    private int menuHover=-1;
    private boolean showFps=false, showCoords=true, noclip=false, fullscreen=false, ultraFps=false;
    private int gameFov=25, settingSel=-1;
    private boolean nameEditing=false;
    private boolean updateAvailable=false;
    private String updateVersion="";
    private int fps=0, fpsCount=0;
    private long fpsTimer=0;
    private java.util.ArrayList<Particle> particles=new java.util.ArrayList<>();
    private int bobFrame=0;
    private double camSmoothX=0, camSmoothY=0;
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
    class Mob{double x,y;int health=6;int maxHealth=6;int type;int aiT;int hurtT;
        Mob(double x,double y,int t){this.x=x;this.y=y;type=t;maxHealth=t==2?10:6;health=maxHealth;aiT=(int)(Math.random()*60);}
    }
    private java.util.ArrayList<DropItem> drops=new java.util.ArrayList<>();
    private java.util.ArrayList<Mob> mobs=new java.util.ArrayList<>();
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
                            String resp="MINICRAFT_SERVER "+serverPort+" "+worldName+" "+pc;
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
    private String serverIP="localhost";
    private Process webProcess, boreProcess;

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
        try{logoImg=javax.imageio.ImageIO.read(new File(System.getProperty("user.dir")+"/MINICRAFT.png"));}catch(Exception e){logoImg=null;}
        try{discIcon=javax.imageio.ImageIO.read(new File(System.getProperty("user.dir")+"/discord.png"));}catch(Exception e){discIcon=null;}
        try{ghIcon=javax.imageio.ImageIO.read(new File(System.getProperty("user.dir")+"/github.png"));}catch(Exception e){ghIcon=null;}
        new File(DATA_DIR).mkdirs();
        try{GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(Font.createFont(Font.TRUETYPE_FONT,new File(System.getProperty("user.dir")+"/PixelPurl.ttf")));}catch(Exception e){}
        refreshWorldList();
        loadSettings();
        timer=new javax.swing.Timer(16,this);timer.start();
        new Thread(()->checkUpdate()).start();
        new Thread(()->{try{Thread.sleep(1000);loadMusic();loadSFX();}catch(Exception e){}}).start();
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
            String latestSha=json.substring(ci+7,ci+47);
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
        if(i==WATER){BufferedImage wt=new BufferedImage(TILE,TILE,BufferedImage.TYPE_INT_ARGB);Graphics2D g=wt.createGraphics();g.drawImage(tex[i],0,0,null);g.setColor(new Color(60,60,200,120));g.fillRect(0,0,TILE,TILE);g.dispose();tex[i]=wt;}
        if(i>=COAL_ORE&&i<=COPPER_ORE){BufferedImage ot=new BufferedImage(TILE,TILE,BufferedImage.TYPE_INT_ARGB);Graphics2D g=ot.createGraphics();g.drawImage(tex[i],0,0,null);Color oc=FB[i];g.setColor(new Color(oc.getRed(),oc.getGreen(),oc.getBlue(),80));g.fillRect(0,0,TILE,TILE);g.dispose();tex[i]=ot;}
    }catch(Exception e){tex[i]=new BufferedImage(TILE,TILE,BufferedImage.TYPE_INT_ARGB);Graphics2D g=tex[i].createGraphics();g.setColor(FB[i]);g.fillRect(0,0,TILE,TILE);g.dispose();}}}
    private void refreshWorldList(){worldList.clear();File[] f=new File(DATA_DIR).listFiles((d,n)->n.endsWith(".mcw"));if(f!=null)for(File x:f)worldList.add(x.getName().replace(".mcw",""));}

    private void saveSettings(){
        try{PrintWriter pw=new PrintWriter(new FileWriter(System.getProperty("user.dir")+"/settings.txt"));
            pw.println("showFps="+showFps);pw.println("showCoords="+showCoords);pw.println("noclip="+noclip);
            pw.println("fullscreen="+fullscreen);pw.println("fov="+gameFov);pw.println("name="+playerName);pw.println("ultraFps="+ultraFps);
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
                if(p[0].equals("noclip"))noclip=Boolean.parseBoolean(p[1]);
                if(p[0].equals("fullscreen"))fullscreen=Boolean.parseBoolean(p[1]);
                if(p[0].equals("fov"))gameFov=Integer.parseInt(p[1]);
                if(p[0].equals("ultraFps"))ultraFps=Boolean.parseBoolean(p[1]);
                if(p[0].equals("name"))playerName=p[1];
            }
            br.close();
        }catch(Exception e){}
    }

    private void startWebServer(){
        try{String dir=System.getProperty("user.dir");
            webProcess=new ProcessBuilder("python3",dir+"/webserver.py").redirectErrorStream(true).start();
            boreProcess=new ProcessBuilder("bore","local","25565","--to","bore.pub").redirectErrorStream(true).start();
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

    private void stopWebServer(){
        if(boreProcess!=null){boreProcess.destroy();boreProcess=null;}
        if(webProcess!=null){webProcess.destroy();webProcess=null;}
    }

    private void genWorld(long seed){
        world=new int[W][H];Random r=new Random(seed);
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
            if(s<H-6&&r.nextInt(14)==0&&x>3&&x<W-4&&world[x][s-1]!=WATER){
                int tt=r.nextInt(6),lg=OAK_LOG+tt,lf=OAK_LEAVES+tt;
                int th=4+r.nextInt(4);
                for(int ty=s-1;ty>s-th-1&&ty>=0;ty--)world[x][ty]=lg;
                int ls=th+2;
                for(int lx=x-2;lx<=x+2;lx++)for(int ly=s-ls;ly<=s-2;ly++)
                    if(lx>=0&&lx<W&&ly>=0&&ly<H&&Math.abs(lx-x)+Math.abs(ly-(s-3))<=3&&world[lx][ly]==0)world[lx][ly]=lf;
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
        px=W/2.0*TILE;py=getGround(W/2)*TILE-playerH/2;health=20;hunger=20;dead=false;fallDist=0;survival=true;
        for(int i=0;i<inv.length;i++){inv[i]=0;invCount[i]=0;}
        mobs.clear();
        for(int i=0;i<8;i++){
            int mx=10+(int)(Math.random()*(W-20)),my=getGround(mx);
            if(world[mx][my]==GRASS)mobs.add(new Mob(mx*TILE,my*TILE-playerH/2,(int)(Math.random()*4)));
        }
        for(int i=0;i<3;i++){
            int mx=10+(int)(Math.random()*(W-20)),my=getGround(mx);
            if(world[mx][my]==GRASS)mobs.add(new Mob(mx*TILE,my*TILE-playerH/2,4));
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
    private boolean isSolid(int x,int y){if(x<0||x>=W||y<0||y>=H)return true;return world[x][y]>0&&world[x][y]!=WATER;}
    private boolean isIn(int x,int y){return x>=0&&x<W&&y>=0&&y<H;}

    private void saveWorld(String name){
        try{DataOutputStream d=new DataOutputStream(new FileOutputStream(DATA_DIR+name+".mcw"));
            d.writeUTF(name);d.writeUTF(playerName);d.writeLong(worldSeed);
            d.writeInt((int)px);d.writeInt((int)py);d.writeInt(health);d.writeInt(hunger);d.writeInt(survival?1:0);
            for(int i=0;i<inv.length;i++){d.writeInt(inv[i]);d.writeInt(invCount[i]);}
            for(int x=0;x<W;x++)for(int y=0;y<H;y++)d.writeInt(world[x][y]);
            d.close();worldName=name;
        }catch(Exception e){e.printStackTrace();}
    }

    private boolean loadWorld(String name){
        try{DataInputStream d=new DataInputStream(new FileInputStream(DATA_DIR+name+".mcw"));
            worldName=d.readUTF();playerName=d.readUTF();worldSeed=d.readLong();
            px=d.readInt();py=d.readInt();health=d.readInt();hunger=d.readInt();survival=d.readInt()==1;
            for(int i=0;i<inv.length;i++){inv[i]=d.readInt();invCount[i]=d.readInt();}
            world=new int[W][H];for(int x=0;x<W;x++)for(int y=0;y<H;y++)world[x][y]=d.readInt();
            d.close();dead=false;fallDist=0;craftingOpen=false;screen=Screen.PLAY;return true;
        }catch(Exception e){return false;}
    }

    private void deleteWorld(String name){new File(DATA_DIR+name+".mcw").delete();refreshWorldList();}

    private String lastMsg="";
    private int msgTimer=0;

    @Override
    public void actionPerformed(ActionEvent e){
        if(screen==Screen.MENU||screen==Screen.WORLD_LIST||screen==Screen.CREATE_WORLD||screen==Screen.MULTIPLAYER||screen==Screen.CONNECT||screen==Screen.HOST||screen==Screen.CONNECTING||screen==Screen.SETTINGS||screen==Screen.PAUSE){repaint();return;}
        if(screen==Screen.DEATH||screen==Screen.CRAFTING){repaint();return;}
        if(screen!=Screen.PLAY)return;
        double speed=survival&&hunger<=0?1.5:3.0;
        int pfoot=(int)((py+playerH/2)/TILE);
        if(pfoot>=0&&pfoot<H&&world[(int)(px/TILE)][pfoot]==WATER)speed*=0.4;
        double dx=0,dy=0;
        if(keys[KeyEvent.VK_A]||keys[KeyEvent.VK_LEFT])dx-=speed;
        if(keys[KeyEvent.VK_D]||keys[KeyEvent.VK_RIGHT])dx+=speed;
        if(keys[KeyEvent.VK_W]||keys[KeyEvent.VK_UP])dy-=speed;
        if(keys[KeyEvent.VK_S]||keys[KeyEvent.VK_DOWN])dy+=speed;
        boolean moving=dx!=0||dy!=0;walking=moving;
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
        if(survival&&!og&&!noclip){playerVy+=0.4;py+=playerVy;fallDist++;}else if(survival&&!noclip){if(playerVy>6){health-=(int)(playerVy-6)/3;if(health<=0){dead=true;screen=Screen.DEATH;}}playerVy=0;fallDist=0;}
        int targetX=Math.max(0,Math.min(W*TILE-VW*TILE,(int)(px-VW*TILE/2)));
        int targetY=Math.max(0,Math.min(H*TILE-VH*TILE,(int)(py-VH*TILE/2)));
        if(ultraFps){camX=targetX;camY=targetY;}else{if(camSmoothX==0){camSmoothX=targetX;camSmoothY=targetY;}camSmoothX+=(targetX-camSmoothX)*0.15;camSmoothY+=(targetY-camSmoothY)*0.15;camX=(int)camSmoothX;camY=(int)camSmoothY;}
        if(!ultraFps)for(int i=0;i<particles.size();i++){Particle pt=particles.get(i);pt.x+=pt.vx;pt.y+=pt.vy;pt.vy+=0.2;pt.life--;if(pt.life<=0){particles.remove(i);i--;}}
        if(!ultraFps)for(int i=0;i<drops.size();i++){DropItem d=drops.get(i);d.y+=d.vy;d.vy+=0.1;d.life--;if(Math.abs(d.x-px)<24&&Math.abs(d.y-py)<24){if(d.block==EXP_ORB){addChat("XP","+"+1);}else addToInv(d.block,1);drops.remove(i);i--;}else if(d.life<=0){drops.remove(i);i--;}}
        if(!ultraFps)for(Mob m:mobs){
            m.aiT++;if(m.hurtT>0)m.hurtT--;
            double ndark=Math.abs(worldTime-12000)/12000.0;
            if(m.type==2&&ndark>0.3){double ddx=px-m.x,ddy=py-m.y,dist=Math.sqrt(ddx*ddx+ddy*ddy);if(dist>8){m.x+=ddx/dist*1.5;m.y+=ddy/dist*0.5;}}
            else if(m.type==4&&ndark>0.3){double ddx=px-m.x,ddy=py-m.y,dist=Math.sqrt(ddx*ddx+ddy*ddy);if(dist>60){m.x+=ddx/dist*2;}else if(m.aiT>40){m.aiT=0;if(dist<80){health--;if(health<=0){dead=true;screen=Screen.DEATH;}}}}
            else if(m.aiT>100){m.aiT=0;if(Math.random()<0.3)m.x+=(Math.random()-0.5)*TILE;}
            if(isSolid((int)(m.x/TILE),(int)((m.y+20)/TILE))){m.aiT=-10;}else m.y+=1.5;
            if(m.type==2&&ndark>0.3&&Math.abs(m.x-px)<24&&Math.abs(m.y-py)<24&&m.hurtT<=0){health--;m.hurtT=40;if(health<=0){dead=true;screen=Screen.DEATH;}}
        }
        bobFrame++;if(walking&&!ultraFps)bobFrame+=2;
        for(int i=0;i<10;i++)if(keys[KeyEvent.VK_1+i]){
            if(survival)selBlock=Math.min(i+1,BLOCK_COUNT-1);
            else{selBlock=Math.min(i+1+creativeOffset,BLOCK_COUNT-1);}
        }
        if(breakX>=0&&breakY>=0&&isIn(breakX,breakY)&&world[breakX][breakY]>0){breakTimer++;if(breakTimer>=breakTime){syncBlock(breakX,breakY,0);addToInv(world[breakX][breakY],1);spawnParticles(breakX,breakY,world[breakX][breakY]);playSFX(world[breakX][breakY]<=GRASS||world[breakX][breakY]==SAND?"grass":"stone");world[breakX][breakY]=0;breakX=-1;breakY=-1;breakTimer=0;}}else if(breakX>=0){breakX=-1;breakY=-1;breakTimer=0;}
        if(msgTimer>0)msgTimer--;
        if(chatTimer>0)chatTimer--;
        long posTime=System.currentTimeMillis();
        if(isHost&&server!=null&&posTime%200<16){
            server.broadcast("P "+playerName+" "+(int)px+" "+(int)py);
        }
        if(!isHost&&client!=null&&client.isConnected()&&posTime%100<16){
            client.send("P "+(int)px+" "+(int)py);
        }
        repaint();
    }

    private void syncName(){
        String msg="N "+playerName;
        if(isHost&&server!=null){server.broadcast(msg);}
        if(client!=null&&client.isConnected())client.send(msg);
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
            case "kill":health=0;dead=true;screen=Screen.DEATH;break;
            case "help":addChat("Cmds","time day/night, tp x y, heal, creative, survival, give id, kill");break;
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
        for(int i=0;i<8;i++)particles.add(new Particle(bx*TILE+TILE/2+Math.random()*TILE/2-TILE/4,by*TILE+TILE/2+Math.random()*TILE/2-TILE/4,block));
        drops.add(new DropItem(bx*TILE+TILE/2,by*TILE+TILE/2,block));
    }
    private boolean takeFromInv(int block,int count){for(int i=0;i<inv.length;i++)if(inv[i]==block&&invCount[i]>=count){invCount[i]-=count;if(invCount[i]<=0)inv[i]=0;return true;}return false;}
    private int getInvCount(int block){for(int i=0;i<inv.length;i++)if(inv[i]==block)return invCount[i];return 0;}

    private int[] getCraft(){
        int nz=0,f=0;for(int i=0;i<4;i++)if(craftGrid[i]>0){nz++;if(f==0)f=craftGrid[i];}
        if(nz==1&&f>=OAK_LOG&&f<=DARK_OAK_LOG)return new int[]{f-OAK_LOG+OAK_PLANKS,4};
        if(nz==1&&f==STONE)return new int[]{COBBLESTONE,1};if(nz==1&&f==COBBLESTONE)return new int[]{STONE,1};
        if(nz==1&&f==SAND)return new int[]{SNOW,1};if(nz==1&&f==NETHERRACK)return new int[]{SOUL_SAND,1};
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
        switch(screen){case MENU:drawMenu(g2);break;case WORLD_LIST:drawWorldList(g2);break;case CREATE_WORLD:drawCreateWorld(g2);break;case MULTIPLAYER:drawMultiplayer(g2);break;case CONNECT:drawConnect(g2);break;case HOST:drawHost(g2);break;case SETTINGS:drawSettings(g2);break;case CONNECTING:drawConnecting(g2);break;case PAUSE:drawPause(g2);break;case DEATH:drawDeath(g2);break;case CRAFTING:drawCrafting(g2);break;case PLAY:drawGame(g2);break;}
    }

    private void drawDirtBG(Graphics2D g2,int w,int h){for(int x=0;x<w;x+=TILE)for(int y=0;y<h;y+=TILE)g2.drawImage(tex[DIRT],x,y,null);}
    private void drawBtn(Graphics2D g2,String t,int x,int y,int w,int hh,boolean hov){g2.setColor(hov?new Color(120,120,120,200):new Color(80,80,80,200));g2.fillRect(x,y,w,hh);g2.setColor(hov?Color.WHITE:new Color(180,180,180));g2.drawRect(x,y,w,hh);g2.setFont(new Font("PixelPurl",Font.BOLD,18));g2.drawString(t,x+(w-g2.getFontMetrics().stringWidth(t))/2,y+hh-12);}
    private boolean inBtn(int mx,int my,int x,int y,int w,int h){return mx>=x&&mx<x+w&&my>=y&&my<y+h;}

    private void drawMenu(Graphics2D g2){int w=getWidth(),h=getHeight();drawDirtBG(g2,w,h);
        if(logoImg!=null){
            int lw=320,lh=84;
            g2.drawImage(logoImg,w/2-lw/2,20,lw,lh,null);
        }else{
            g2.setFont(new Font("PixelPurl",Font.PLAIN,36));g2.setColor(new Color(255,220,60));
            String t1="MiniCraft";g2.drawString(t1,w/2-120,68);
        }
        String t2="v"+VERSION;g2.setFont(new Font("PixelPurl",Font.PLAIN,14));g2.setColor(new Color(180,180,180));g2.drawString(t2,w/2+80,108);
        if(updateAvailable){g2.setFont(new Font("PixelPurl",Font.PLAIN,14));g2.setColor(Color.YELLOW);g2.drawString("Update v"+updateVersion+" available! (update.sh or update.bat)",w/2-200,130);}
        drawBtn(g2,"Singleplayer",w/2-100,140,200,40,menuHover==0);drawBtn(g2,"Multiplayer",w/2-100,190,200,40,menuHover==1);
        drawBtn(g2,"Options",w/2-100,240,200,40,menuHover==2);drawBtn(g2,"Mods",w/2-100,290,200,40,menuHover==3);
        drawBtn(g2,"Quit",w/2-100,340,200,40,menuHover==4);
        drawBtn(g2," Discord",w/2-100,390,95,32,menuHover==5);drawBtn(g2," GitHub",w/2+5,390,95,32,menuHover==6);
        if(discIcon!=null)g2.drawImage(discIcon,w/2-92,392,24,24,null);
        if(ghIcon!=null)g2.drawImage(ghIcon,w/2+13,392,24,24,null);
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
        drawBtn(g2,"Create",w/2-60,280,120,36,menuHover==20);drawBtn(g2,"Back",w/2-60,330,120,36,menuHover==21);
    }

    private void drawMultiplayer(Graphics2D g2){int w=getWidth(),h=getHeight();drawDirtBG(g2,w,h);
        g2.setFont(new Font("PixelPurl",Font.BOLD,28));g2.setColor(new Color(100,200,60));g2.drawString("Multiplayer",w/2-80,60);
        g2.setFont(new Font("PixelPurl",Font.PLAIN,14));g2.setColor(new Color(200,200,200));
        int yy=100;
        if(discoveredServers.isEmpty()){
            g2.drawString("Scanning for LAN servers...",w/2-100,yy+20);
        }else{
            for(int i=0;i<discoveredServers.size();i++){
                DiscoveredServer ds=discoveredServers.get(i);
                g2.setColor(i==selectedServer?new Color(120,120,120,200):new Color(60,60,60,200));
                g2.fillRect(w/2-220,yy,440,36);
                g2.setColor(i==selectedServer?Color.WHITE:new Color(200,200,200));
                g2.drawRect(w/2-220,yy,440,36);
                g2.setFont(new Font("PixelPurl",Font.BOLD,12));
                g2.drawString(ds.name,w/2-210,yy+15);
                g2.setFont(new Font("PixelPurl",Font.PLAIN,10));
                g2.drawString(ds.players+" players  "+ds.world,w/2-210,yy+30);
                yy+=42;
            }
        }
        yy=Math.max(yy+10,300);
        drawBtn(g2,"Host Server",w/2-100,yy,200,36,menuHover==30);
        drawBtn(g2,"Direct Connect",w/2-100,yy+46,200,36,menuHover==31);
        drawBtn(g2,"Refresh",w/2-100,yy+92,200,36,menuHover==33);
        drawBtn(g2,"Back",w/2-100,yy+138,200,36,menuHover==32);
    }

    private void drawPause(Graphics2D g2){int w=getWidth(),h=getHeight();
        g2.setColor(new Color(0,0,0,150));g2.fillRect(0,0,w,h);
        g2.setFont(new Font("PixelPurl",Font.BOLD,36));g2.setColor(Color.WHITE);
        String t="Game Paused";g2.drawString(t,(w-g2.getFontMetrics().stringWidth(t))/2,100);
        drawBtn(g2,"Resume",w/2-80,170,160,36,menuHover==70);
        drawBtn(g2,"Save & Quit",w/2-80,220,160,36,menuHover==72);
        drawBtn(g2,"Quit",w/2-80,270,160,36,menuHover==73);
    }

    private void drawConnect(Graphics2D g2){int w=getWidth(),h=getHeight();drawDirtBG(g2,w,h);
        g2.setFont(new Font("PixelPurl",Font.BOLD,28));g2.setColor(new Color(100,200,60));g2.drawString("Connect",w/2-60,60);
        g2.setFont(new Font("PixelPurl",Font.PLAIN,18));g2.setColor(Color.WHITE);g2.drawString("Server Address",w/2-150,130);
        g2.setColor(new Color(40,40,40));g2.fillRect(w/2-150,145,300,35);g2.setColor(Color.WHITE);g2.drawRect(w/2-150,145,300,35);
        g2.setFont(new Font("PixelPurl",Font.PLAIN,16));g2.drawString(typing+(System.currentTimeMillis()/500%2==0?"_":""),w/2-140,170);
        drawBtn(g2,"Connect",w/2-60,220,120,36,menuHover==40);drawBtn(g2,"Back",w/2-60,270,120,36,menuHover==41);
        if(!lastMsg.isEmpty()){g2.setFont(new Font("PixelPurl",Font.PLAIN,14));g2.setColor(Color.YELLOW);g2.drawString(lastMsg,w/2-150,330);}
    }

    private void drawSettings(Graphics2D g2){int w=getWidth(),h=getHeight();drawDirtBG(g2,w,h);
        g2.setFont(new Font("PixelPurl",Font.BOLD,28));g2.setColor(new Color(100,200,60));g2.drawString("Options  v"+VERSION,w/2-90,60);
        g2.setFont(new Font("PixelPurl",Font.PLAIN,18));g2.setColor(Color.WHITE);
        String[] opts={
            "[F1] FPS: "+(showFps?"ON":"OFF"),
            "[F2] Coords: "+(showCoords?"ON":"OFF"),
            "[G] Noclip: "+(noclip?"ON":"OFF"),
            "[M] Music: "+(musicOn?"ON":"OFF"),
            "[F3] Ultra FPS: "+(ultraFps?"ON":"OFF"),
            "[F] Mode: "+(survival?"Survival":"Creative"),
            "[F11] Fullscreen: "+(fullscreen?"ON":"OFF"),
            "[N] Name: "+playerName+(nameEditing?(System.currentTimeMillis()/500%2==0?"_":""):""),
            "[< >] FOV: "+gameFov,
            "[U] Update: "+(updateAvailable?"v"+updateVersion+" AVAILABLE!":"Check GitHub")
        };
        for(int i=0;i<opts.length;i++){
            g2.setColor(i==settingSel?new Color(255,255,100):Color.WHITE);
            g2.drawString(opts[i],w/2-200,110+i*36);
        }
        drawBtn(g2,"Back",w/2-60,h-80,120,36,menuHover==60);
    }

    private void drawConnecting(Graphics2D g2){int w=getWidth(),h=getHeight();drawDirtBG(g2,w,h);
        g2.setFont(new Font("PixelPurl",Font.BOLD,28));g2.setColor(new Color(100,200,60));
        g2.drawString("Connecting...",w/2-100,120);
        g2.setFont(new Font("PixelPurl",Font.PLAIN,18));g2.setColor(Color.WHITE);
        g2.drawString("Receiving world...",w/2-100,180);
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
        if(night<0.5){g2.setColor(new Color(255,255,150,(int)(200*(1-night*2))));g2.fillOval(sunX-15,sunY-15,30,30);}else{g2.setColor(new Color(200,200,220,(int)(200*(night*2-1))));g2.fillOval(sunX-12,sunY-12,24,24);}
        int frame=bobFrame%240;
        for(int i=0;i<4;i++){
            if(ultraFps)break;
            g2.setColor(new Color(255,255,255,160));
            int cx=((int)(500+i*300+frame*2)%(w+600))-300;
            g2.fillOval(cx,30-i*10,80+i*15,20+i*8);g2.fillOval(cx+40,34-i*8,50+i*10,14+i*5);
        }
        if(worldTime>3000&&worldTime<9000&&!ultraFps){
            g2.setColor(new Color(100,140,200,60));
            for(int i=0;i<60;i++){int rx=(i*117+frame*3)%w,ry=(i*83+frame*5)%h;g2.drawLine(rx,ry,rx,ry+8);}
        }
        int sx=camX/TILE,sy=camY/TILE,ex=Math.min(W,sx+gameFov+2),ey=Math.min(H,sy+gameFov*18/25+2);
        for(int x=sx;x<ex;x++)for(int y=sy;y<ey;y++)if(world[x][y]>0)g2.drawImage(tex[Math.min(world[x][y],BLOCK_COUNT-1)],x*TILE-camX,y*TILE-camY,null);
        int pxOff=(int)(px-camX),pyOff=(int)(py-camY);
        int bob=(int)(Math.sin(frame*0.3)*2);
        g2.drawImage(steveImg[0],pxOff-playerW/2,pyOff-playerH/2+bob,null);
        drawNameTag(g2,pxOff,pyOff+bob,playerName,new Color(255,255,255));
        g2.setColor(new Color(0,0,0,30));g2.fillOval(pxOff-8,pyOff+TILE-6,16,4);

        synchronized(remotePlayers){for(RemotePlayer rp:remotePlayers){int rx=(int)(rp.x-camX),ry=(int)(rp.y-camY);g2.drawImage(steveImg[0],rx-playerW/2,ry-playerH/2,null);drawNameTag(g2,rx,ry,rp.name,new Color(150,200,255));}}

        for(Particle pt:particles){
            int alpha=pt.life*255/pt.maxLife;
            g2.setColor(new Color(FB[Math.min(pt.block,FB.length-1)].getRed(),FB[Math.min(pt.block,FB.length-1)].getGreen(),FB[Math.min(pt.block,FB.length-1)].getBlue(),alpha));
            g2.fillRect((int)(pt.x-camX),(int)(pt.y-camY),2+pt.life/5,2+pt.life/5);
        }
        for(DropItem d:drops){int dbob=(int)(Math.sin(d.life*0.1)*2);g2.drawImage(tex[Math.min(d.block,BLOCK_COUNT-1)],(int)(d.x-camX)-6,(int)(d.y-camY)-6+dbob,null);}
        for(Mob m:mobs){
            int mx=(int)(m.x-camX),my=(int)(m.y-camY);
            g2.setColor(m.type==0?new Color(140,100,60):m.type==1?new Color(220,150,180):m.type==2?new Color(80,120,60):m.type==3?new Color(180,180,180):m.type==4?new Color(200,200,200):Color.WHITE);
            g2.fillRect(mx-10,my-14,20,24);
            g2.setColor(Color.WHITE);g2.drawString(m.type==0?"Cow":m.type==1?"Pig":m.type==2?"Zomb":m.type==3?"Sheep":m.type==4?"Skele":"Chick",mx-10,my-18);
            g2.setColor(new Color(0,0,0,100));g2.fillRect(mx-12,my-24,24,3);
            g2.setColor(new Color(255,0,0));g2.fillRect(mx-12,my-24,m.health*24/m.maxHealth,3);
            if(m.hurtT>0)g2.setColor(new Color(255,0,0,100));g2.fillRect(mx-10,my-14,20,24);
        }

        if(mouseIn&&breakX<0){int hx=(mx+camX)/TILE,hy=(my+camY)/TILE;if(isIn(hx,hy)){g2.setColor(new Color(255,255,255,100));g2.drawRect(hx*TILE-camX,hy*TILE-camY,TILE,TILE);}}
        if(breakX>=0){g2.setColor(new Color(255,255,255,100));g2.drawRect(breakX*TILE-camX,breakY*TILE-camY,TILE,TILE);float pct=Math.min(1f,(float)breakTimer/(float)Math.max(1,breakTime));g2.setColor(new Color(0,0,0,80));g2.fillRect(breakX*TILE-camX,breakY*TILE-camY,(int)(TILE*pct),3);}
        drawHUD(g2);
        drawChat(g2);
        if(showFps){g2.setColor(new Color(0,0,0,150));g2.fillRect(getWidth()-70,10,60,16);g2.setColor(Color.YELLOW);g2.setFont(new Font("PixelPurl",Font.PLAIN,10));g2.drawString(fps+" FPS",getWidth()-65,22);fpsCount++;long now=System.currentTimeMillis();if(now-fpsTimer>1000){fps=fpsCount;fpsCount=0;fpsTimer=now;}}
        if(isHost&&server!=null){g2.setFont(new Font("PixelPurl",Font.BOLD,10));g2.setColor(Color.GREEN);g2.drawString("SERVER "+server.getPlayerCount()+" players",10,35);}
        if(client!=null&&client.isConnected()){g2.setFont(new Font("PixelPurl",Font.BOLD,10));g2.setColor(Color.CYAN);g2.drawString("CONNECTED",10,45);}
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
            if(survival){int cnt=getInvCount(idx);if(cnt>0){g2.setFont(new Font("PixelPurl",Font.PLAIN,8));g2.setColor(Color.WHITE);g2.drawString(""+cnt,sx+hs-10,sy+hs-3);}}
        }
        if(!survival){
            g2.setFont(new Font("PixelPurl",Font.PLAIN,10));
            g2.setColor(new Color(0,0,0,180));g2.fillRect(sw/2-60,2,120,16);
            g2.setColor(Color.WHITE);g2.drawString(BNAME[Math.min(selBlock,BLOCK_COUNT-1)],sw/2-55,14);
        }
        g2.setColor(new Color(0,0,0,150));g2.fillRect(10,10,280,24);
        g2.setColor(Color.WHITE);g2.setFont(new Font("PixelPurl",Font.BOLD,12));
        g2.drawString(worldName+(!worldName.isEmpty()?" | ":"")+playerName+(noclip?" NOCLIP":"")+(survival?" S":" C"),15,25);
        if(showCoords){g2.setColor(new Color(0,0,0,150));g2.fillRect(10,36,140,14);g2.setColor(new Color(200,200,200));g2.setFont(new Font("PixelPurl",Font.PLAIN,9));g2.drawString("X:"+(int)(px/TILE)+" Y:"+(int)(py/TILE),15,46);}
    }

    private void drawDeath(Graphics2D g2){g2.setColor(new Color(100,0,0,200));g2.fillRect(0,0,getWidth(),getHeight());g2.setFont(new Font("PixelPurl",Font.BOLD,48));g2.setColor(Color.RED);String t="YOU DIED";g2.drawString(t,(getWidth()-g2.getFontMetrics().stringWidth(t))/2,getHeight()/2-20);g2.setFont(new Font("PixelPurl",Font.BOLD,20));g2.setColor(Color.WHITE);String r="Press ENTER to respawn";g2.drawString(r,(getWidth()-g2.getFontMetrics().stringWidth(r))/2,getHeight()/2+30);}

    private void drawChat(Graphics2D g2){
        int y=getHeight()-130;
        g2.setFont(new Font("PixelPurl",Font.PLAIN,11));
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
        for(int i=0;i<inv.length;i++){int ix=30+(i%8)*60,iy=220+(i/8)*60;if(iy>getHeight()-60)break;g2.setColor(new Color(50,50,50));g2.fillRect(ix,iy,50,50);g2.setColor(Color.GRAY);g2.drawRect(ix,iy,50,50);if(inv[i]>0){g2.drawImage(tex[Math.min(inv[i],BLOCK_COUNT-1)],ix+1,iy+1,null);if(invCount[i]>1){g2.setFont(new Font("PixelPurl",Font.BOLD,10));g2.setColor(Color.WHITE);g2.drawString(""+invCount[i],ix+38,iy+46);}}}
        g2.setFont(new Font("PixelPurl",Font.PLAIN,14));g2.setColor(new Color(200,200,200));g2.drawString("Recipes:",500,40);g2.drawString("Log -> 4 Planks",500,60);g2.drawString("Stone -> Cobblestone",500,80);g2.drawString("2 Planks -> 4 Sticks",500,100);g2.drawString("4 Planks -> Crafting Table",500,120);
    }

    @Override public void keyPressed(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_F1){showFps=!showFps;return;}
        if(e.getKeyCode()==KeyEvent.VK_F2){showCoords=!showCoords;return;}
        if(e.getKeyCode()==KeyEvent.VK_F3){ultraFps=!ultraFps;return;}
        if(e.getKeyCode()==KeyEvent.VK_M){toggleMusic();return;}
        if(e.getKeyCode()==KeyEvent.VK_F11&&(screen==Screen.PLAY||screen==Screen.SETTINGS)){
            fullscreen=!fullscreen;
            JFrame f=(JFrame)SwingUtilities.getWindowAncestor(this);
            if(f!=null){f.dispose();f.setUndecorated(fullscreen);f.setExtendedState(fullscreen?JFrame.MAXIMIZED_BOTH:JFrame.NORMAL);f.setVisible(true);}
            return;
        }
        if(e.getKeyCode()==KeyEvent.VK_G&&(screen==Screen.PLAY||screen==Screen.SETTINGS)){noclip=!noclip;return;}
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
        if(e.getKeyCode()>=0&&e.getKeyCode()<keys.length)keys[e.getKeyCode()]=true;
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
        if(e.getKeyCode()==KeyEvent.VK_T&&screen==Screen.PLAY){chatOpen=true;chatText="";return;}
        if(screen==Screen.CREATE_WORLD){
            if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE&&typing.length()>0)typing=typing.substring(0,typing.length()-1);
            else if(e.getKeyCode()==KeyEvent.VK_ENTER&&!typing.isEmpty()){genWorld(System.currentTimeMillis());screen=Screen.PLAY;}
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
        if(screen==Screen.PLAY&&e.getKeyCode()==KeyEvent.VK_ESCAPE){screen=Screen.PAUSE;return;}
        if(screen==Screen.PAUSE&&e.getKeyCode()==KeyEvent.VK_ESCAPE){screen=Screen.PLAY;return;}
        if(screen==Screen.DEATH&&e.getKeyCode()==KeyEvent.VK_ENTER){px=W/2.0*TILE;py=getGround(W/2)*TILE-playerH/2;health=20;hunger=20;dead=false;fallDist=0;screen=Screen.PLAY;}
        if(screen==Screen.CRAFTING&&e.getKeyCode()==KeyEvent.VK_E){craftingOpen=false;screen=Screen.PLAY;return;}
        if(screen==Screen.CRAFTING&&e.getKeyCode()==KeyEvent.VK_SPACE){int[] r=getCraft();if(r[0]>0){boolean h=true;for(int i=0;i<4;i++)if(craftGrid[i]>0&&!takeFromInv(craftGrid[i],craftCount[i]))h=false;if(h){addToInv(r[0],r[1]);for(int i=0;i<4;i++){craftGrid[i]=0;craftCount[i]=0;}}}}
        if(screen==Screen.PLAY&&e.getKeyCode()==KeyEvent.VK_SPACE){int tx=(int)((px)/TILE),ty=(int)((py+playerH/2)/TILE);for(int dy=ty;dy<H;dy++)if(isSolid(tx,dy)){if(dy>0)py=(dy-1)*TILE;break;}}
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

    private void tryConnect(){
        String[] parts=typing.split(":");
        String ip=parts[0];int port=parts.length>1?Integer.parseInt(parts[1]):25565;
        serverIP=ip;serverPort=port;
        world=null;
        client=new MiniClient(ip,port);
        if(client.connect()){screen=Screen.CONNECTING;lastMsg="";}else{lastMsg="Failed to connect!";msgTimer=120;}
    }

    private boolean loadUnconnectedWorld(){
        File[] f=new File(DATA_DIR).listFiles((d,n)->n.endsWith(".mcw"));
        if(f!=null&&f.length>0){return loadWorld(f[0].getName().replace(".mcw",""));}
        return false;
    }

    @Override public void mouseMoved(MouseEvent e){mx=e.getX();my=e.getY();menuHover=-1;
        int w=getWidth()/2;
        if(screen==Screen.MENU){if(inBtn(mx,my,w-100,140,200,40))menuHover=0;else if(inBtn(mx,my,w-100,190,200,40))menuHover=1;else if(inBtn(mx,my,w-100,240,200,40))menuHover=2;else if(inBtn(mx,my,w-100,290,200,40))menuHover=3;else if(inBtn(mx,my,w-100,340,200,40))menuHover=4;else if(inBtn(mx,my,w-100,390,95,32))menuHover=5;else if(inBtn(mx,my,w+5,390,95,32))menuHover=6;}
        if(screen==Screen.WORLD_LIST){int yy=Math.max(worldList.isEmpty()?130:100+worldList.size()*42+10,350);if(inBtn(mx,my,w-120,yy,240,36))menuHover=10;else if(selectedWorld>=0&&inBtn(mx,my,w-120,yy+46,240,36))menuHover=11;else if(selectedWorld>=0&&inBtn(mx,my,w-60,yy+92,120,36))menuHover=12;else if(inBtn(mx,my,w-60,yy+138,120,36))menuHover=13;}
        if(screen==Screen.CREATE_WORLD){if(inBtn(mx,my,w-60,280,120,36))menuHover=20;else if(inBtn(mx,my,w-60,330,120,36))menuHover=21;}
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
        if(screen==Screen.PAUSE){int bw=getWidth()/2;if(inBtn(mx,my,bw-80,170,160,36))menuHover=70;else if(inBtn(mx,my,bw-80,220,160,36))menuHover=72;else if(inBtn(mx,my,bw-80,270,160,36))menuHover=73;}
    }

    @Override public void mousePressed(MouseEvent e){int wx=e.getX(),wy=e.getY(),w=getWidth()/2;
        if(screen==Screen.MENU){
            if(inBtn(wx,wy,w-100,140,200,40)){refreshWorldList();screen=worldList.isEmpty()?Screen.CREATE_WORLD:Screen.WORLD_LIST;}
            else if(inBtn(wx,wy,w-100,190,200,40)){
                discoveredServers.clear();selectedServer=-1;
                if(discovery!=null)discovery.stopDisc();discovery=new DiscoveryThread();discovery.start();
                screen=Screen.MULTIPLAYER;
            }
            else if(inBtn(wx,wy,w-100,240,200,40)){screen=Screen.SETTINGS;}
            else if(inBtn(wx,wy,w-100,290,200,40)){try{Runtime.getRuntime().exec(new String[]{"flatpak","run","org.prismlauncher.PrismLauncher"});}catch(Exception ex){try{Runtime.getRuntime().exec(new String[]{"/var/home/olda/PrismLauncher-Linux-x86_64.AppImage"});}catch(Exception ex2){}}}
            else if(inBtn(wx,wy,w-100,340,200,40))System.exit(0);
            else if(inBtn(wx,wy,w-100,390,95,32)){try{java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://discord.gg/wAWrPCHR5z"));}catch(Exception ex){}}
            else if(inBtn(wx,wy,w+5,390,95,32)){try{java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://github.com/olda9991/minicraft"));}catch(Exception ex){}}
            return;
        }
        if(screen==Screen.WORLD_LIST){int yy=100;for(int i=0;i<worldList.size();i++){if(inBtn(wx,wy,w-150,yy,300,36)){selectedWorld=i;return;}yy+=42;}if(worldList.isEmpty())yy+=30;yy=Math.max(yy+10,350);
            if(inBtn(wx,wy,w-120,yy,240,36)){typing="My World";screen=Screen.CREATE_WORLD;}
            else if(selectedWorld>=0&&inBtn(wx,wy,w-120,yy+46,240,36)){deleteWorld(worldList.get(selectedWorld));selectedWorld=-1;}
            else if(selectedWorld>=0&&inBtn(wx,wy,w-60,yy+92,120,36)){loadWorld(worldList.get(selectedWorld));}
            else if(inBtn(wx,wy,w-60,yy+138,120,36)){stopNetworking();screen=Screen.MENU;}return;
        }
        if(screen==Screen.CREATE_WORLD){
            if(inBtn(wx,wy,w-60,280,120,36)&&!typing.isEmpty()){worldName=typing;genWorld(System.currentTimeMillis());screen=Screen.PLAY;}
            else if(inBtn(wx,wy,w-60,330,120,36))screen=Screen.WORLD_LIST;return;
        }
        if(screen==Screen.MULTIPLAYER){
            int yy=100+Math.min(discoveredServers.size(),5)*42;
            yy=Math.max(yy+10,300);
            if(inBtn(wx,wy,w-100,yy,200,36)){stopNetworking();loadUnconnectedWorld();screen=Screen.HOST;}
            else if(inBtn(wx,wy,w-100,yy+46,200,36)){typing="localhost:25565";screen=Screen.CONNECT;}
            else if(inBtn(wx,wy,w-100,yy+92,200,36)){discoveredServers.clear();if(discovery!=null)discovery.stopDisc();discovery=new DiscoveryThread();discovery.start();}
            else if(inBtn(wx,wy,w-100,yy+138,200,36)){if(discovery!=null)discovery.stopDisc();screen=Screen.MENU;}
            else if(selectedServer>=0&&selectedServer<discoveredServers.size()){
                DiscoveredServer ds=discoveredServers.get(selectedServer);
                typing=ds.ip+":"+ds.port;
                screen=Screen.CONNECT;
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
            if(inBtn(wx,wy,bw-80,170,160,36)){screen=Screen.PLAY;}
            else if(inBtn(wx,wy,bw-80,220,160,36)){
                saveWorld(worldName.isEmpty()?"world_"+System.currentTimeMillis():worldName);saveSettings();
                refreshWorldList();stopNetworking();screen=Screen.WORLD_LIST;
            }
            else if(inBtn(wx,wy,bw-80,270,160,36)){stopNetworking();screen=Screen.MENU;}
            return;
        }
        if(screen==Screen.HOST){
            if(inBtn(wx,wy,w-80,300,160,36)){
                if(server!=null&&server.isRunning()){server.stopServer();server=null;isHost=false;if(responder!=null){responder.stopDisc();responder=null;}
                    if(client!=null){client.disconnect();client=null;}remotePlayers.clear();screen=Screen.MULTIPLAYER;
                }else{
                    server=new MiniServer(serverPort);isHost=true;server.start();
                    if(responder!=null)responder.stopDisc();responder=new DiscoveryResponder();responder.start();
                    if(!loadUnconnectedWorld()){genWorld(System.currentTimeMillis());worldName="host_"+System.currentTimeMillis();}
                    startWebServer();
                    screen=Screen.PLAY;
                }
            }else if(inBtn(wx,wy,w-60,360,120,36)){stopNetworking();screen=Screen.MULTIPLAYER;}return;
        }
        if(screen==Screen.DEATH)return;
        if(screen==Screen.CRAFTING){int cx=(wx-30)/60,cy=(wy-50)/60,idx=cx+cy*2;if(cx>=0&&cx<2&&cy>=0&&cy<2&&idx>=0&&idx<4){if(craftGrid[idx]>0){addToInv(craftGrid[idx],craftCount[idx]);craftGrid[idx]=0;craftCount[idx]=0;return;}for(int i=0;i<inv.length;i++){int ix=30+(i%8)*60,iy=220+(i/8)*60;if(wx>=ix&&wx<ix+50&&wy>=iy&&wy<iy+50&&inv[i]>0){craftGrid[idx]=inv[i];craftCount[idx]=1;invCount[i]--;if(invCount[i]<=0)inv[i]=0;return;}}}return;}
        if(screen==Screen.PLAY){int tx=(mx+camX)/TILE,ty=(my+camY)/TILE;if(!isIn(tx,ty))return;int pt=(int)(px/TILE),pyt=(int)(py/TILE);if(Math.abs(tx-pt)+Math.abs(ty-pyt)<2)return;
            if(e.getButton()==MouseEvent.BUTTON1){
                boolean hitMob=false;int dmg=selBlock==SWORD?6:3;
                for(Mob m:mobs){if(Math.abs((mx+camX)-m.x)<24&&Math.abs((my+camY)-m.y)<24){int critDmg=dmg;boolean onG=false;int fx=(int)((px+14)/TILE),fy=(int)((py+TILE-4)/TILE);if(isSolid(fx,fy))onG=true;if(!onG){critDmg*=2;addChat("","CRIT! x2");}m.health-=critDmg;m.hurtT=10;m.x+=(m.x>px?8:-8);if(m.health<=0){drops.add(new DropItem(m.x,m.y,m.type==0?RAW_BEEF:m.type==1?RAW_PORK:m.type==3?WOOL:m.type==4?COOKED_BEEF:COOKED_BEEF));drops.add(new DropItem(m.x-10,m.y-10,EXP_ORB));mobs.remove(m);}hitMob=true;break;}}
                if(!hitMob){if(survival&&world[tx][ty]>0){breakX=tx;breakY=ty;breakTimer=0;int spd=1;if(selBlock==PICKAXE)spd=4;if(selBlock==AXE)spd=4;if(selBlock==SHOVEL)spd=4;breakTime=Math.max(1,BT[Math.min(world[tx][ty],BT.length-1)]/spd);}else if(!survival){world[tx][ty]=0;syncBlock(tx,ty,0);}}
            }
            else if(e.getButton()==MouseEvent.BUTTON3&&selBlock>=0&&(!survival||getInvCount(selBlock)>0)){
                if(survival&&(selBlock==RAW_BEEF||selBlock==COOKED_BEEF||selBlock==RAW_PORK||selBlock==COOKED_PORK)){hunger=Math.min(20,hunger+(selBlock==COOKED_BEEF||selBlock==COOKED_PORK?8:4));takeFromInv(selBlock,1);}
                else if(survival&&selBlock==BED){worldTime=6000;addChat("Bed","Good morning!");}
                else{world[tx][ty]=selBlock;if(survival)takeFromInv(selBlock,1);syncBlock(tx,ty,selBlock);}
            }
        }
    }

    @Override public void mouseReleased(MouseEvent e){breakX=-1;breakY=-1;breakTimer=0;}
    @Override public void mouseEntered(MouseEvent e){mouseIn=true;}
    @Override public void mouseExited(MouseEvent e){mouseIn=false;breakX=-1;breakY=-1;breakTimer=0;}
    @Override public void mouseDragged(MouseEvent e){mx=e.getX();my=e.getY();}
    @Override public void mouseClicked(MouseEvent e){}

    @Override public void mouseWheelMoved(MouseWheelEvent e){
        if(screen==Screen.PLAY){
            if(!survival){creativeOffset+=e.getWheelRotation();if(creativeOffset<0)creativeOffset=0;if(creativeOffset>BLOCK_COUNT-10)creativeOffset=BLOCK_COUNT-10;}
            selBlock+=e.getWheelRotation();if(selBlock<1)selBlock=BLOCK_COUNT-1;if(selBlock>=BLOCK_COUNT)selBlock=1;
        }
    }

    class RemotePlayer{double x,y;String name;RemotePlayer(String n,double x,double y){this.name=n;this.x=x;this.y=y;}}

    class MiniServer extends Thread{
        private ServerSocket ss;private boolean running=false;
        private ArrayList<ClientHandler> clients=new ArrayList<>();
        MiniServer(int port){try{ss=new ServerSocket(port);}catch(Exception e){}}
        public void run(){
            running=true;
            while(running){try{ClientHandler ch=new ClientHandler(ss.accept());ch.start();clients.add(ch);}catch(Exception e){if(running)try{Thread.sleep(100);}catch(Exception ex){}break;}}
        }
        void broadcast(String msg,String skip){
            for(ClientHandler ch:clients)if(ch.name!=null&&!ch.name.equals(skip))ch.send(msg);
        }
        void broadcast(String msg){for(ClientHandler ch:clients)if(ch.name!=null)ch.send(msg);}
        int getPlayerCount(){return clients.size()+1;}
        boolean isRunning(){return running;}
        void stopServer(){running=false;try{if(ss!=null)ss.close();}catch(Exception e){}for(ClientHandler ch:clients){try{ch.interrupt();}catch(Exception ex){}}clients.clear();}
        void removeClient(ClientHandler ch){
            clients.remove(ch);broadcast("L "+ch.name);
            synchronized(remotePlayers){remotePlayers.removeIf(rp->rp.name!=null&&rp.name.equals(ch.name));}
        }

        class ClientHandler extends Thread{
            Socket s;PrintWriter out;BufferedReader in;String name="";
            ClientHandler(Socket s){this.s=s;}
            public void run(){
                try{out=new PrintWriter(s.getOutputStream(),true);in=new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String line;
                    while((line=in.readLine())!=null){
                        String[] p=line.split(" ",3);
                        if(p[0].equals("J")){
                            name=p.length>1?p[1]:"Player";
                            synchronized(remotePlayers){remotePlayers.add(new RemotePlayer(name,(int)px,(int)py));}
                            out.println("W "+W+" "+H);
                            StringBuilder batch=new StringBuilder();
                            for(int x=0;x<W;x++)for(int y=0;y<H;y++){
                                batch.append("D ").append(x).append(" ").append(y).append(" ").append(world[x][y]).append("\n");
                                if(batch.length()>5000){out.print(batch.toString());out.flush();batch.setLength(0);}
                            }
                            if(batch.length()>0){out.print(batch.toString());out.flush();}
                            out.println("WD");out.flush();
                            out.println("S "+(int)px+" "+(int)py);
                            out.println("J "+playerName);
                            for(ClientHandler ch:clients)if(ch!=this&&ch.name!=null)out.println("J "+ch.name);
                            broadcast("J "+name,name);
                            for(int hc=Math.max(0,chatMessages.size()-5);hc<chatMessages.size();hc++){
                                String cm=chatMessages.get(hc);
                                int ci=cm.indexOf('>');
                                if(ci>0)out.println("C "+cm.substring(1,ci)+" "+cm.substring(ci+2));
                            }
                        }
                        else if(p[0].equals("P")&&p.length>=3){
                            synchronized(remotePlayers){
                                for(RemotePlayer rp:remotePlayers)if(rp.name!=null&&rp.name.equals(name)){rp.x=Double.parseDouble(p[1]);rp.y=Double.parseDouble(p[2]);}
                            }
                            broadcast("P "+name+" "+p[1]+" "+p[2],name);
                        }
                        else if(p[0].equals("B")&&p.length>=4){
                            int bx=Integer.parseInt(p[1]),by=Integer.parseInt(p[2]),bl=Integer.parseInt(p[3]);
                            if(bx>=0&&bx<W&&by>=0&&by<H){world[bx][by]=bl;}
                            broadcast("B "+bx+" "+by+" "+bl,name);
                        }
                        else if(p[0].equals("C")&&p.length>=3){
                            String msg=p[2];addChat(name,msg);
                            broadcast("C "+name+" "+msg,name);
                        }
                        else if(p[0].equals("N")&&p.length>=2){
                            String oldName=name;name=p[1];
                            synchronized(remotePlayers){remotePlayers.removeIf(rp->rp.name!=null&&rp.name.equals(oldName));remotePlayers.add(new RemotePlayer(name,(int)px,(int)py));}
                            broadcast("N "+oldName+" "+name,"");
                        }
                    }
                }catch(Exception e){}finally{try{s.close();}catch(Exception e){}if(name!=null)removeClient(this);}
            }
            void send(String m){if(out!=null)out.println(m);}
        }
    }

    class MiniClient extends Thread{
        private Socket s;private PrintWriter out;private BufferedReader in;private boolean connected=false,running=false;
        private String host;private int port;
        private int[][] tempWorld=null;
        private boolean worldReady=false;
        MiniClient(String h,int p){host=h;port=p;}
        boolean connect(){
            try{s=new Socket(host,port);out=new PrintWriter(s.getOutputStream(),true);in=new BufferedReader(new InputStreamReader(s.getInputStream()));connected=true;running=true;start();out.println("J "+playerName);return true;}
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
                        final String pname=p[1];final double sx=Double.parseDouble(p[2]),sy=Double.parseDouble(p[3]);
                        SwingUtilities.invokeLater(()->{
                            boolean found=false;
                            synchronized(remotePlayers){
                                for(RemotePlayer rp:remotePlayers)if(rp.name!=null&&rp.name.equals(pname)){rp.x=sx;rp.y=sy;found=true;break;}
                                if(!found)remotePlayers.add(new RemotePlayer(pname,sx,sy));
                            }
                        });
                    }
                    else if(p[0].equals("J")&&p.length>1){
                        final String jname=p[1];
                        SwingUtilities.invokeLater(()->{
                            boolean exists=false;
                            synchronized(remotePlayers){for(RemotePlayer rp:remotePlayers)if(rp.name!=null&&rp.name.equals(jname))exists=true;if(!exists)remotePlayers.add(new RemotePlayer(jname,0,0));}
                        });
                    }
                    else if(p[0].equals("L")&&p.length>1){
                        final String lname=p[1];
                        SwingUtilities.invokeLater(()->{synchronized(remotePlayers){remotePlayers.removeIf(rp->rp.name!=null&&rp.name.equals(lname));}});
                    }
                    else if(p[0].equals("B")&&p.length>=4){
                        final int bx=Integer.parseInt(p[1]),by=Integer.parseInt(p[2]),bl=Integer.parseInt(p[3]);
                        SwingUtilities.invokeLater(()->{if(world!=null&&bx>=0&&bx<world.length&&by>=0&&by<world[0].length)world[bx][by]=bl;});
                    }
                    else if(p[0].equals("C")&&p.length>=3){addChat(p[1],line.substring(line.indexOf(' ',line.indexOf(' ')+1)+1));}
                    else if(p[0].equals("N")&&p.length>=3){
                        final String oldN=p[1],newN=p[2];
                        SwingUtilities.invokeLater(()->{
                            synchronized(remotePlayers){
                                for(RemotePlayer rp:remotePlayers)if(rp.name!=null&&rp.name.equals(oldN)){rp.name=newN;break;}
                            }
                        });
                    }
                }
            }catch(Exception e){}finally{connected=false;try{if(s!=null)s.close();}catch(Exception ex){}}
        }
        void send(String m){if(out!=null)out.println(m);}
        void disconnect(){running=false;try{if(s!=null)s.close();}catch(Exception e){}connected=false;}
        boolean isConnected(){return connected;}
    }

    public static void main(String[] args){JFrame f=new JFrame("MiniCraft");MiniCraft g=new MiniCraft();f.add(g);f.pack();f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);f.setLocationRelativeTo(null);f.setResizable(false);f.setVisible(true);
        f.addWindowListener(new java.awt.event.WindowAdapter(){public void windowClosing(java.awt.event.WindowEvent e){g.saveSettings();g.stopNetworking();}});}
}
