package com.olda.minicraft;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * MiniCraft Android MainActivity
 * Uses MiniCraftAndroid - direct port from desktop MiniCraft.java
 */
public class MainActivity extends Activity {

    private GameView gameView;
    private MiniCraftAndroid game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FrameLayout root = new FrameLayout(this);
        gameView = new GameView(this);
        game = gameView.getGame();
        root.addView(gameView);

        View overlay = getLayoutInflater().inflate(R.layout.activity_main, root, false);
        root.addView(overlay);
        setContentView(root);

        setupControls(overlay);
    }

    private void setupControls(View overlay) {
        // D-Pad
        setHoldListener(overlay.findViewById(R.id.btnLeft), () -> game.setMoveLeft(true), () -> game.setMoveLeft(false));
        setHoldListener(overlay.findViewById(R.id.btnRight), () -> game.setMoveRight(true), () -> game.setMoveRight(false));
        setHoldListener(overlay.findViewById(R.id.btnUp), () -> game.setMoveUp(true), () -> game.setMoveUp(false));
        setHoldListener(overlay.findViewById(R.id.btnDown), () -> game.setMoveDown(true), () -> game.setMoveDown(false));
        setHoldListener(overlay.findViewById(R.id.btnJump), () -> game.setJump(true), () -> game.setJump(false));

        // Break - hold to break at crosshair
        setHoldListener(overlay.findViewById(R.id.btnBreak), () -> {
            // Break uses crosshair position
            game.handleTouch(gameView.getWidth()/2f, gameView.getHeight()/2f, MotionEvent.ACTION_DOWN);
        }, () -> {
            game.handleTouch(-1, -1, MotionEvent.ACTION_UP);
        });

        // Place - tap to place at crosshair
        overlay.findViewById(R.id.btnPlace).setOnTouchListener((v, e) -> {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                game.setPlace(true);
            }
            return true;
        });

        // Mode toggle
        overlay.findViewById(R.id.btnMode).setOnClickListener(v -> game.toggleMode());

        // Noclip toggle (long press mode button)
        overlay.findViewById(R.id.btnMode).setOnLongClickListener(v -> {
            game.toggleNoclip();
            return true;
        });
    }

    private void setHoldListener(View btn, Runnable onPress, Runnable onRelease) {
        btn.setOnTouchListener((v, e) -> {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                onPress.run();
                return true;
            } else if (e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_CANCEL) {
                onRelease.run();
                return true;
            }
            return false;
        });
    }
}
