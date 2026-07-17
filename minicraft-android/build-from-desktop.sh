#!/bin/bash
# build-from-desktop.sh
# Builds Android APK directly from desktop MiniCraft.java

set -e
cd "$(dirname "$0")"

echo "=== MiniCraft Android Builder (from desktop source) ==="

# Verify desktop source exists
if [ ! -f ../src/MiniCraft.java ]; then
    echo "ERROR: ../src/MiniCraft.java not found!"
    exit 1
fi

# Copy desktop source into Android project
cp ../src/MiniCraft.java app/src/main/java/com/olda/minicraft/MiniCraftDesktop.java

# Wrap it with Android compatibility layer
cat > app/src/main/java/com/olda/minicraft/MiniCraftGame.java << 'JAVAEOF'
package com.olda.minicraft;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Android wrapper that hosts the desktop MiniCraft game.
 * The desktop MiniCraft.java is copied here as MiniCraftDesktop.java
 * and called via reflection/API adaptation.
 */
public class MiniCraftGame extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    
    private Thread gameThread;
    private boolean running = false;
    private MiniCraftDesktop game;
    
    public MiniCraftGame(Context context) {
        super(context);
        getHolder().addCallback(this);
        game = new MiniCraftDesktop();
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        game.initAndroid(getWidth(), getHeight());
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        game.setScreenSize(w, h);
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        running = false;
        try { gameThread.join(); } catch (Exception e) {}
    }
    
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000.0 / 60.0;
        double delta = 0;
        
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            
            while (delta >= 1) {
                game.tick();
                delta--;
            }
            
            Canvas c = null;
            try {
                c = getHolder().lockCanvas();
                if (c != null) {
                    synchronized (getHolder()) {
                        game.render(c);
                    }
                }
            } finally {
                if (c != null) getHolder().unlockCanvasAndPost(c);
            }
            
            try { Thread.sleep(2); } catch (Exception e) { break; }
        }
    }
    
    public MiniCraftDesktop getGame() { return game; }
}
JAVAEOF

# Run gradle build
gradle assembleDebug

echo ""
echo "APK built from desktop source!"
echo "Output: app/build/outputs/apk/debug/app-debug.apk"
