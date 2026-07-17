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
