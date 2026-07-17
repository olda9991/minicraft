package com.olda.minicraft;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * MiniCraft Android entry point.
 *
 * TODO: Replace this scaffold with a real SurfaceView rendering thread
 * that ports the desktop MiniCraft game logic.
 */
public class MainActivity extends Activity {

    private SurfaceView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout root = new FrameLayout(this);
        gameView = new SurfaceView(this);
        root.addView(gameView);

        // TODO: Add virtual D-pad, jump, break/place buttons as children of root
        setContentView(root);

        gameView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override public void surfaceCreated(SurfaceHolder holder) {
                // TODO: start game render thread
            }
            @Override public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {}
            @Override public void surfaceDestroyed(SurfaceHolder holder) {
                // TODO: stop game render thread
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO: map touch to virtual controls and pass to MiniCraft input handler
        return super.onTouchEvent(event);
    }
}
