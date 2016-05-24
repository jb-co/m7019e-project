package com.example.johanboqvist.myproject;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EndGameActivity extends AppCompatActivity {

    private TextView title;
    private EditText name;
    private Button btn;
    private LinearLayout linearLayout;
    private HighScoreManager highScoreManager = new HighScoreManager();

    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);


        new Thread(){
            public void run() {
                highScoreManager.getHighScore("http://52.49.26.113/highscore.txt");
                if(highScoreManager.isHighScore("serru", 1001)) {
                    messageHandler.sendEmptyMessage(0);
                }


            }
        }.start();

    }

    private final Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch(msg.what) {
                case 0: createForm(); break;
                case 1: finish(); break;
            }

        }
    };


    public void createForm(){
        linearLayout = (LinearLayout) findViewById(R.id.endgame);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        //Title
        /*
        title = new TextView(this);

        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setLayoutParams(params);
        linearLayout.addView(title);*/
        title = (TextView) findViewById(R.id.textHi);
        title.setText("YOU MADE IT TO THE TOP 5!");

        //Name
        name = new EditText(this);
        name.setHint("Enter your name:");
        params2.gravity = Gravity.CENTER_HORIZONTAL;
        name.setLayoutParams(params2);
        linearLayout.addView(name);

        btn = new Button(this);
        btn.setText("Submit highscore!");
        btn.setLayoutParams(params2);
        btn.setGravity(Gravity.CENTER_HORIZONTAL);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        highScoreManager.writeHighScore("http://52.49.26.113/highscore.php");
                        messageHandler.sendEmptyMessage(1);
                    }
                }.start();

            }
        });

        linearLayout.addView(btn);



    }
}



