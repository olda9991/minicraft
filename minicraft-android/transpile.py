#!/usr/bin/env python3
"""
MiniCraft Android Transpiler
Converts desktop MiniCraft.java (Swing) → Android-compatible Java
Usage: python3 transpile.py src/MiniCraft.java > android/MiniCraft.java
"""

import re, sys

if len(sys.argv) < 2:
    print("Usage: python3 transpile.py src/MiniCraft.java [output.java]")
    sys.exit(1)

with open(sys.argv[1]) as f:
    src = f.read()

# Replace Swing imports with Android imports
replacements = [
    # Package/imports
    ('import javax.swing.*;', 'import android.content.Context;\nimport android.graphics.*;\nimport android.view.*;'),
    ('import java.awt.*;', ''),
    ('import java.awt.event.*;', ''),
    ('import java.awt.image.BufferedImage;', ''),
    ('import javax.sound.sampled.*;', 'import android.media.*;'),
    
    # Class declaration
    ('extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener', 
     'extends SurfaceView implements SurfaceHolder.Callback, Runnable'),
    
    # Main method → Activity entry point comment
    ('public static void main(String[] args){', '// Android entry point - MainActivity creates this'),
    
    # JFrame setup → removed
    ('JFrame f=new JFrame("MiniCraft");', ''),
    ('f.add(g);', ''),
    ('f.pack();', ''),
    ('f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);', ''),
    ('f.setLocationRelativeTo(null);', ''),
    ('f.setResizable(false);', ''),
    ('f.setVisible(true);', ''),
    
    # SwingUtilities.invokeLater → direct call (Android UI thread handled by Activity)
    ('SwingUtilities.invokeLater(()->', ''),
    
    # Timer → Handler-based timing
    ('private javax.swing.Timer timer;', 'private Thread gameThread;\n    private boolean running = false;\n    private Handler handler;\n    private Runnable tickRunnable;'),
    
    # Window listener → lifecycle handled by Activity
    ('f.addWindowListener(new java.awt.event.WindowAdapter(){public void windowClosing(java.awt.event.WindowEvent e){', 
     '// Window close handled by MainActivity'),
    
    # PaintComponent → onDraw / render
    ('public void paintComponent(Graphics g){', 'public void render(Canvas c){'),
    ('Graphics2D g2=(Graphics2D)g;', 'Paint paint = new Paint();'),
    
    # Key events → touch events (stubbed for now, Activity handles actual input)
    ('@Override public void keyPressed(KeyEvent e){', '// Key events handled by MainActivity → game.setKey()'),
    ('@Override public void keyReleased(KeyEvent e){', '// Key release handled by MainActivity'),
    ('@Override public void keyTyped(KeyEvent e){}', ''),
    
    # Mouse events → touch events
    ('@Override public void mouseClicked(MouseEvent e){', '// Touch handled by MainActivity'),
    ('@Override public void mousePressed(MouseEvent e){', '// Touch handled by MainActivity'),
    ('@Override public void mouseReleased(MouseEvent e){', '// Touch handled by MainActivity'),
    ('@Override public void mouseEntered(MouseEvent e){}', ''),
    ('@Override public void mouseExited(MouseEvent e){}', ''),
    ('@Override public void mouseDragged(MouseEvent e){', '// Drag handled by MainActivity'),
    ('@Override public void mouseMoved(MouseEvent e){', '// Move handled by MainActivity'),
    ('@Override public void mouseWheelMoved(MouseWheelEvent e){', '// Scroll handled by MainActivity'),
    
    # ActionListener → Runnable
    ('@Override public void actionPerformed(ActionEvent e){tick();}', ''),
    
    # BufferedImage → Bitmap (stub - textures loaded via assets)
    ('BufferedImage[] tex', 'Bitmap[] tex'),
    ('BufferedImage logoImg', 'Bitmap logoImg'),
    ('BufferedImage[] logoFrames', 'Bitmap[] logoFrames'),
    ('BufferedImage discIcon', 'Bitmap discIcon'),
    ('BufferedImage ghIcon', 'Bitmap ghIcon'),
    
    # ImageIO.read → BitmapFactory.decodeStream
    ('javax.imageio.ImageIO.read(new File(', 'BitmapFactory.decodeStream(context.getAssets().open("textures/" + '),
    
    # Font → Typeface
    ('new Font("PixelPurl",', 'Typeface.create("monospace",'),
    ('Font.PLAIN', 'Typeface.NORMAL'),
    ('Font.BOLD', 'Typeface.BOLD'),
    
    # Color constructors → Android Color (already same in ARGB)
    ('new Color(0,0,0,0)', '0x00000000'),
    # Keep other Color() as-is since Android graphics.Color exists
    
    # Clip → Canvas clip (stub)
    ('g2.setClip(', 'c.clipRect('),
    
    # AudioClip / Clip → MediaPlayer / SoundPool
    ('Clip musicClip', 'MediaPlayer musicPlayer'),
    ('AudioSystem.getClip()', 'new MediaPlayer()'),
    
    # JOptionPane → AlertDialog (commented, Activity handles dialogs)
    ('JOptionPane.showMessageDialog', '// JOptionPane → use AlertDialog in Activity'),
    
    # JDialog → Dialog (commented)
    ('class RPCVisualizer extends JDialog{', 'class RPCVisualizer {// extends Dialog'),
    
    # SwingUtilities.invokeLater → Activity.runOnUiThread or Handler
    ('SwingUtilities.invokeLater(()->', 'new Handler(Looper.getMainLooper()).post(()->'),
]

# Apply replacements
android = src
for old, new in replacements:
    android = android.replace(old, new)

# Add Android constructor
android = android.replace(
    'public class MiniCraft extends',
    '''public class MiniCraftAndroid extends
    private Context context;
    private int screenW, screenH;
    
    public MiniCraftAndroid(Context ctx) {
        super(ctx);
        this.context = ctx;
        getHolder().addCallback(this);
        init();
    }
    
    private void init() {
        // Initialize game state (arrays, collections)
        inv = new int[BLOCK_COUNT];
        invCount = new int[BLOCK_COUNT];
        craftGrid = new int[4];
        craftCount = new int[4];
        keys = new boolean[1024];
        particles = new java.util.ArrayList<>();
        drops = new java.util.ArrayList<>();
        mobs = new java.util.ArrayList<>();
        dmgNums = new java.util.ArrayList<>();
        arrows = new java.util.ArrayList<>();
        tntList = new java.util.ArrayList<>();
        achieves = new java.util.ArrayList<>();
        chatMessages = new java.util.ArrayList<>();
        musicFiles = new java.util.ArrayList<>();
        sfxFiles = new java.util.ArrayList<>();
        discoveredServers = new java.util.ArrayList<>();
    }
    
    // SurfaceView.Callback
    @Override public void surfaceCreated(SurfaceHolder holder) {
        screenW = getWidth(); screenH = getHeight();
        genWorld(System.currentTimeMillis());
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        screenW = w; screenH = h;
    }
    @Override public void surfaceDestroyed(SurfaceHolder holder) {
        running = false;
        try { gameThread.join(); } catch (Exception e) {}
    }
    
    // Game loop thread
    @Override public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000.0 / 60.0;
        double delta = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            while (delta >= 1) { tick(); delta--; }
            Canvas c = null;
            try {
                c = getHolder().lockCanvas();
                if (c != null) synchronized(getHolder()) { render(c); }
            } finally { if (c != null) getHolder().unlockCanvasAndPost(c); }
            try { Thread.sleep(2); } catch (Exception e) { break; }
        }
    }
    
    // Touch input (called by MainActivity)
    public void handleTouch(float x, float y, int action) {
        mx = (int)x; my = (int)y;
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            mouseIn = true;
            // Auto-break on touch in world
            if (screen == Screen.PLAY) {
                int tx = (mx + camX) / TILE;
                int ty = (my + camY) / TILE;
                if (isIn(tx, ty) && world[tx][ty] > 0) {
                    breakX = tx; breakY = ty; breakTimer = 0;
                    int spd = 1;
                    if (selBlock == PICKAXE) spd = 4;
                    if (selBlock == AXE) spd = 4;
                    if (selBlock == SHOVEL) spd = 4;
                    breakTime = Math.max(1, BT[Math.min(world[tx][ty], BT.length - 1)] / spd);
                }
            }
        } else if (action == MotionEvent.ACTION_UP) {
            mouseIn = false;
            breakX = -1; breakY = -1; breakTimer = 0;
            // Place on tap
            if (screen == Screen.PLAY) {
                int tx = (mx + camX) / TILE;
                int ty = (my + camY) / TILE;
                if (isIn(tx, ty) && world[tx][ty] == AIR && selBlock > 0) {
                    if (!survival || getInvCount(selBlock) > 0) {
                        world[tx][ty] = selBlock;
                        if (survival) takeFromInv(selBlock, 1);
                        blocksPlaced++;
                        spawnParticles(tx, ty, selBlock);
                    }
                }
            }
        }
    }
    
    // Virtual controls
    public void setMoveLeft(boolean v) { keys[KeyEvent.VK_A] = v; }
    public void setMoveRight(boolean v) { keys[KeyEvent.VK_D] = v; }
    public void setMoveUp(boolean v) { keys[KeyEvent.VK_W] = v; }
    public void setMoveDown(boolean v) { keys[KeyEvent.VK_S] = v; }
    public void setJump(boolean v) { keys[KeyEvent.VK_SPACE] = v; }
    public void setKey(int code, boolean v) { if (code >= 0 && code < keys.length) keys[code] = v; }
    
    public class'
)

# Replace KeyEvent references with constants (since we can't import java.awt.event.KeyEvent on Android)
# Map common keys to Android KeyEvent constants or our own
android = android.replace('KeyEvent.VK_A', '65')
android = android.replace('KeyEvent.VK_D', '68')
android = android.replace('KeyEvent.VK_W', '87')
android = android.replace('KeyEvent.VK_S', '83')
android = android.replace('KeyEvent.VK_SPACE', '32')
android = android.replace('KeyEvent.VK_SHIFT', '16')
android = android.replace('KeyEvent.VK_E', '69')
android = android.replace('KeyEvent.VK_F', '70')
android = android.replace('KeyEvent.VK_T', '84')
android = android.replace('KeyEvent.VK_G', '71')
android = android.replace('KeyEvent.VK_N', '78')
android = android.replace('KeyEvent.VK_ENTER', '10')
android = android.replace('KeyEvent.VK_ESCAPE', '27')
android = android.replace('KeyEvent.VK_BACK_SPACE', '8')
android = android.replace('KeyEvent.VK_1', '49')
android = android.replace('KeyEvent.VK_2', '50')
android = android.replace('KeyEvent.VK_3', '51')
android = android.replace('KeyEvent.VK_4', '52')
android = android.replace('KeyEvent.VK_5', '53')
android = android.replace('KeyEvent.VK_6', '54')
android = android.replace('KeyEvent.VK_7', '55')
android = android.replace('KeyEvent.VK_8', '56')
android = android.replace('KeyEvent.VK_9', '57')
android = android.replace('KeyEvent.VK_0', '48')
android = android.replace('KeyEvent.VK_LEFT', '37')
android = android.replace('KeyEvent.VK_RIGHT', '39')
android = android.replace('KeyEvent.VK_UP', '38')
android = android.replace('KeyEvent.VK_DOWN', '40')
android = android.replace('KeyEvent.VK_COMMA', '188')
android = android.replace('KeyEvent.VK_PERIOD', '190')
android = android.replace('KeyEvent.VK_F1', '112')
android = android.replace('KeyEvent.VK_F2', '113')
android = android.replace('KeyEvent.VK_F3', '114')
android = android.replace('KeyEvent.VK_F4', '115')
android = android.replace('KeyEvent.VK_F5', '116')
android = android.replace('KeyEvent.VK_F6', '117')
android = android.replace('KeyEvent.VK_F7', '118')
android = android.replace('KeyEvent.VK_F8', '119')
android = android.replace('KeyEvent.VK_F9', '120')
android = android.replace('KeyEvent.VK_F10', '121')
android = android.replace('KeyEvent.VK_F11', '122')
android = android.replace('KeyEvent.VK_F12', '123')
android = android.replace('KeyEvent.VK_M', '77')
android = android.replace('KeyEvent.VK_R', '82')
android = android.replace('KeyEvent.VK_V', '86')

# Replace g2.drawString → c.drawText
android = android.replace('g2.drawString(', 'c.drawText(')

# Replace g2.drawRect → c.drawRect
android = android.replace('g2.drawRect(', 'c.drawRect(')

# Replace g2.fillRect → c.drawRect with paint
# This is harder - need to set paint color first. For now, stub.
android = android.replace('g2.fillRect(', 'c.drawRect(')

# Replace g2.drawImage → c.drawBitmap
android = android.replace('g2.drawImage(tex[', 'c.drawBitmap(tex[')
android = android.replace('],x*TILE-camX,y*TILE-camY,null)', '], null, new Rect(x*TILE-camX, y*TILE-camY, x*TILE-camX+TILE, y*TILE-camY+TILE), paint)')

# Replace g2.setColor → paint.setColor
android = android.replace('g2.setColor(', 'paint.setColor(')
android = android.replace('g2.setFont(', 'paint.setTextSize(')

# Replace Font metrics → Paint metrics
android = android.replace('g2.getFontMetrics().getHeight()', '(int)paint.getTextSize()')
android = android.replace('g2.getFontMetrics().stringWidth(', '(int)paint.measureText(')
android = android.replace('g2.getFontMetrics().getAscent()', '(int)paint.getTextSize()')

# Replace screen size queries
android = android.replace('getWidth()', 'screenW')
android = android.replace('getHeight()', 'screenH')

# Output
if len(sys.argv) >= 3:
    with open(sys.argv[2], 'w') as f:
        f.write(android)
    print(f"Written: {sys.argv[2]}")
else:
    print(android)
