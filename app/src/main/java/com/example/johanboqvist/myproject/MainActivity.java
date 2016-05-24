package com.example.johanboqvist.myproject;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private HighScoreManager highScoreManager = new HighScoreManager();
    private TextView textHighScore;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        textHighScore = (TextView) findViewById(R.id.textScore);
        getHighScore();


        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(MainActivity.this, SurfaceActivity.class);
                startActivity(activity);
                }
            });

        Button btnSettings = (Button) findViewById(R.id.btnSettings);
            btnSettings.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    Intent activity = new Intent(MainActivity.this, EndGameActivity.class);
                    startActivity(activity);
                }
            });

    }

    @Override
    protected void onResume() {
        super.onResume();

        getHighScore();
    }

    private final Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            textHighScore.setText(result);
        }
    };

    public void getHighScore(){
        new Thread() {
            public void run() {
                result = highScoreManager.getHighScore("http://52.49.26.113/highscore.txt");
                messageHandler.sendEmptyMessage(0);
            }
        }.start();
    }
}
