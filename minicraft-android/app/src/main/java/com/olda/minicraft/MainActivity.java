package com.olda.minicraft;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

/**
 * MiniCraft Android MainActivity
 * Cross-platform multiplayer with Windows/Linux desktop
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
        // Movement
        setHoldListener(overlay.findViewById(R.id.btnLeft), () -> game.setMoveLeft(true), () -> game.setMoveLeft(false));
        setHoldListener(overlay.findViewById(R.id.btnRight), () -> game.setMoveRight(true), () -> game.setMoveRight(false));
        setHoldListener(overlay.findViewById(R.id.btnUp), () -> game.setMoveUp(true), () -> game.setMoveUp(false));
        setHoldListener(overlay.findViewById(R.id.btnDown), () -> game.setMoveDown(true), () -> game.setMoveDown(false));
        setHoldListener(overlay.findViewById(R.id.btnJump), () -> game.setJump(true), () -> game.setJump(false));

        // Break
        setHoldListener(overlay.findViewById(R.id.btnBreak), () -> {
            game.handleTouch(gameView.getWidth()/2f, gameView.getHeight()/2f, MotionEvent.ACTION_DOWN);
        }, () -> {
            game.handleTouch(-1, -1, MotionEvent.ACTION_UP);
        });

        // Place
        overlay.findViewById(R.id.btnPlace).setOnTouchListener((v, e) -> {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                game.setPlace(true);
            }
            return true;
        });

        // Mode
        overlay.findViewById(R.id.btnMode).setOnClickListener(v -> game.toggleMode());
        overlay.findViewById(R.id.btnMode).setOnLongClickListener(v -> {
            game.toggleNoclip();
            return true;
        });

        // Host server
        overlay.findViewById(R.id.btnHost).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Host Server");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setText("25565");
            builder.setView(input);
            builder.setPositiveButton("Host", (dialog, which) -> {
                int port = Integer.parseInt(input.getText().toString());
                game.startServer(port);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });

        // Join server
        overlay.findViewById(R.id.btnJoin).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Join Server");
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_join, null);
            final EditText ipInput = dialogView.findViewById(R.id.inputIP);
            final EditText portInput = dialogView.findViewById(R.id.inputPort);
            ipInput.setText("192.168.1.");
            portInput.setText("25565");
            builder.setView(dialogView);
            builder.setPositiveButton("Join", (dialog, which) -> {
                String ip = ipInput.getText().toString();
                int port = Integer.parseInt(portInput.getText().toString());
                game.connectToServer(ip, port);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });

        // Stop network
        overlay.findViewById(R.id.btnStop).setOnClickListener(v -> game.stopNetworking());
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
