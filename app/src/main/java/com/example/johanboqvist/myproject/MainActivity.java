package com.example.johanboqvist.myproject;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.johanboqvist.myproject.Misc.Globals;
import com.example.johanboqvist.myproject.Misc.HighScoreManager;
import com.example.johanboqvist.myproject.Misc.MusicManager;

public class MainActivity extends AppCompatActivity {

    private HighScoreManager highScoreManager = new HighScoreManager();
    private TextView textHighScore;
    private String result;
    private Button btnStart;
    private Button btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MusicManager.createPlayer(this);

        textHighScore = (TextView) findViewById(R.id.textScore);

        //Start game button
       btnStart = (Button) findViewById(R.id.button);
        btnStart.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(MainActivity.this, SurfaceActivity.class);
                Globals.homePressed = false;
                startActivity(activity);
            }
        });

        // Settings button
        btnSettings = (Button) findViewById(R.id.btnSettings);
            btnSettings.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    Intent activity = new Intent(MainActivity.this, SettingsActivity.class);
                    Globals.homePressed = false;
                    startActivity(activity);
                }
            });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Globals.homePressed = true;
        getHighScore();
        MusicManager.startMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(Globals.homePressed){
            MusicManager.stopMusic();
        }
    }

    @Override
    protected void onDestroy() {
        MusicManager.stopMusic();
        MusicManager.releasePlayer();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();

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
