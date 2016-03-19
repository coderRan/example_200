package com.game.pintu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.game.pintu.view.GamePintuLayout;


public class MainActivity extends Activity {
    private GamePintuLayout gamePintuLayout = null;
    private TextView levelView = null;
    private TextView timeView = null;
    private int[] images = new int[2];
    @Override
    protected void onPause() {
        super.onPause();
        gamePintuLayout.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gamePintuLayout.resume();
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gamePintuLayout = (GamePintuLayout) findViewById(R.id.id_gamePintu);
        levelView = (TextView) findViewById(R.id.id_level);
        timeView = (TextView) findViewById(R.id.id_time);
        gamePintuLayout.setIsTImeEnabled(true);
        final Button pause = (Button) findViewById(R.id.id_pause);




        gamePintuLayout.setOnGameListener(new GamePintuLayout.GamePintuListener() {
            @Override
            public void nextLevel(final int nextLevel) {

                new AlertDialog.Builder(MainActivity.this).setMessage("恭喜过关!")
                        .setPositiveButton("下一关", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gamePintuLayout.nextLevel();
                                levelView.setText("" + nextLevel);

                            }
                        }).show();
            }

            @Override
            public void timeChanged(int currentTime) {
                if (currentTime < 10)
                    timeView.setTextColor(Color.RED);
                timeView.setText("" + currentTime);
            }

            @Override
            public void gameOver() {
                new AlertDialog.Builder(MainActivity.this).setTitle("Game Info")
                        .setMessage("时间结束!").setPositiveButton("重新开始",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gamePintuLayout.restart();
                            }
                        }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
            }
        });
        /* 设置游戏开始暂停监听 */
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gamePintuLayout.isPause()) {
                    pause.setText("暂停游戏");
                    gamePintuLayout.resume();
                } else {
                    pause.setText("开始游戏");
                    gamePintuLayout.pause();
                }
            }
        });
    }

}
