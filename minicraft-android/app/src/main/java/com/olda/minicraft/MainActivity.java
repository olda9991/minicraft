package com.olda.minicraft;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * MiniCraft Android - Main Activity
 * Fullscreen with virtual controls
 */
public class MainActivity extends Activity {

    private GameView gameView;
    private MiniCraftGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FrameLayout root = new FrameLayout(this);
        gameView = new GameView(this);
        game = gameView.getGame();
        root.addView(gameView);

        // Inflate touch control overlay
        View overlay = getLayoutInflater().inflate(R.layout.activity_main, root, false);
        root.addView(overlay);
        setContentView(root);

        setupControls(overlay);
    }

    private void setupControls(View overlay) {
        // D-Pad buttons
        Button btnLeft = overlay.findViewById(R.id.btnLeft);
        Button btnRight = overlay.findViewById(R.id.btnRight);
        Button btnUp = overlay.findViewById(R.id.btnUp);
        Button btnDown = overlay.findViewById(R.id.btnDown);
        Button btnJump = overlay.findViewById(R.id.btnJump);
        Button btnBreak = overlay.findViewById(R.id.btnBreak);
        Button btnPlace = overlay.findViewById(R.id.btnPlace);
        Button btnMode = overlay.findViewById(R.id.btnMode);

        // Movement (hold to move)
        setHoldListener(btnLeft, () -> game.moveLeft = true, () -> game.moveLeft = false);
        setHoldListener(btnRight, () -> game.moveRight = true, () -> game.moveRight = false);
        setHoldListener(btnUp, () -> game.moveUp = true, () -> game.moveUp = false);
        setHoldListener(btnDown, () -> game.moveDown = true, () -> game.moveDown = false);
        setHoldListener(btnJump, () -> game.btnJump = true, () -> game.btnJump = false);

        // Break (hold to break)
        setHoldListener(btnBreak, () -> game.btnBreak = true, () -> game.btnBreak = false);

        // Place (tap to place once)
        btnPlace.setOnTouchListener((v, e) -> {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                game.btnPlace = true;
                // Set touch at crosshair position
                game.touchX = gameView.getWidth() / 2f;
                game.touchY = gameView.getHeight() / 2f;
            }
            return true;
        });

        // Mode toggle
        btnMode.setOnClickListener(v -> game.toggleMode());
    }

    private void setHoldListener(Button btn, Runnable onPress, Runnable onRelease) {
        btn.setOnTouchListener((v, e) -> {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onPress.run();
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    onRelease.run();
                    return true;
            }
            return false;
        });
    }
}
