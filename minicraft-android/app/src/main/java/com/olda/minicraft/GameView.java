package com.olda.minicraft;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * SurfaceView that hosts the MiniCraft game loop and renders the world.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private Thread gameThread;
    private boolean running = false;
    private MiniCraftGame game;
    private Paint paint = new Paint();

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        game = new MiniCraftGame(context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        game.init(getWidth(), getHeight());
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Viewport already set in init; resize if needed
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        running = false;
        try { gameThread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
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

            try { Thread.sleep(2); } catch (InterruptedException e) { break; }
        }
    }

    public MiniCraftGame getGame() { return game; }
}
