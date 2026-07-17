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
 * Fullscreen landscape with virtual touch controls.
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

        // Touch overlay with virtual controls
        View overlay = getLayoutInflater().inflate(R.layout.activity_main, root, false);
        root.addView(overlay);
        setContentView(root);

        setupControls(overlay);
    }

    private void setupControls(View overlay) {
        // D-Pad
        Button left = overlay.findViewById(R.id.btnLeft);
        Button right = overlay.findViewById(R.id.btnRight);
        Button up = overlay.findViewById(R.id.btnUp);
        Button down = overlay.findViewById(R.id.btnDown);
        Button jump = overlay.findViewById(R.id.btnJump);
        Button breakBtn = overlay.findViewById(R.id.btnBreak);
        Button placeBtn = overlay.findViewById(R.id.btnPlace);

        setPressListener(left, () -> game.touchLeft = true, () -> game.touchLeft = false);
        setPressListener(right, () -> game.touchRight = true, () -> game.touchRight = false);
        setPressListener(up, () -> game.touchUp = true, () -> game.touchUp = false);
        setPressListener(down, () -> game.touchDown = true, () -> game.touchDown = false);
        setPressListener(jump, () -> game.touchUp = true, () -> game.touchUp = false);

        breakBtn.setOnTouchListener((v, e) -> {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                game.touchBreak = true;
                game.touchBreakX = e.getRawX();
                game.touchBreakY = e.getRawY();
            } else if (e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_CANCEL) {
                game.touchBreak = false;
            }
            return true;
        });

        placeBtn.setOnTouchListener((v, e) -> {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                game.touchPlace = true;
                game.touchBreakX = e.getRawX();
                game.touchBreakY = e.getRawY();
            }
            return true;
        });
    }

    private void setPressListener(Button btn, Runnable onPress, Runnable onRelease) {
        btn.setOnTouchListener((v, e) -> {
            if (e.getAction() == MotionEvent.ACTION_DOWN) onPress.run();
            else if (e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_CANCEL) onRelease.run();
            return true;
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Tap-to-break in world area
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            game.touchBreak = true;
            game.touchBreakX = event.getX();
            game.touchBreakY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            game.touchBreak = false;
        }
        return true;
    }
}
