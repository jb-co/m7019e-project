package com.example.johanboqvist.myproject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.johanboqvist.myproject.Misc.Globals;
import com.example.johanboqvist.myproject.Misc.HighScoreManager;
import com.example.johanboqvist.myproject.Misc.MusicManager;

/**
 * This activity is fired when the player beats the game
 * Highscore list is read to compare the points. Creates a form for the user if there's a new
 * highscore.
 */
public class EndGameActivity extends AppCompatActivity {

    private TextView            title;
    private TextView            textPoints;
    private EditText            name;
    private Button              btn;
    private LinearLayout        linearLayout;
    private HighScoreManager    highScoreManager = new HighScoreManager();
    private int                 position;
    private int                 points;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        Intent mIntent = getIntent();
        points = mIntent.getIntExtra("points", 0);

        textPoints = (TextView) findViewById(R.id.textPoints);
        textPoints.setText("Points: "+points);

        /**read highscore from aws and compare to current points **/
        new Thread(){
            public void run() {
                highScoreManager.getHighScore("http://52.49.26.113/highscore.txt");
                position = highScoreManager.isHighScore(points);
                if(position != -1) { //highscore!!!
                    messageHandler.sendEmptyMessage(0);
                } else {
                    messageHandler.sendEmptyMessage(-1);
                }

            }
        }.start();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(!Globals.backPressed) {
            MusicManager.stopMusic();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Globals.backPressed = false;
        MusicManager.startMusic();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Globals.backPressed = true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private final Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch(msg.what) {
                case -1: noHighScore(); break; //no highscore, so just give the user the bad news
                case 0: createForm(); break; //highscore! enter your name.
                case 1: { //user submitted name
                    finish();
                } break;
            }
        }
    };

    public void noHighScore(){
        title = (TextView) findViewById(R.id.textHi);
        title.setText("No highscore, try again!");
    }

    public void createForm(){
        linearLayout = (LinearLayout) findViewById(R.id.endgame);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        title = (TextView) findViewById(R.id.textHi);
        title.setText("YOU MADE IT TO THE TOP 5!");

        //Name
        name = new EditText(this);
        name.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        //Max 10 character filter
        InputFilter[] fa= new InputFilter[1];
        fa[0] = new InputFilter.LengthFilter(10);
        name.setFilters(fa);

        name.setHint("Enter your name:");
        params2.gravity = Gravity.CENTER_HORIZONTAL;
        name.setLayoutParams(params2);

        //Make sure the textbox isn't empty
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(name.getText())) {
                    btn.setEnabled(false);
                } else {
                    btn.setEnabled(true);
                }
            }
        });

        linearLayout.addView(name);

        btn = new Button(this);
        btn.setText("Submit highscore!");
        btn.setLayoutParams(params2);
        btn.setGravity(Gravity.CENTER_HORIZONTAL);
        btn.setEnabled(false);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {

                        highScoreManager.addEntry(position, name.getText().toString(), points);
                        highScoreManager.writeHighScore("http://52.49.26.113/highscore.php");
                        messageHandler.sendEmptyMessage(1);
                    }
                }.start();

            }
        });

        linearLayout.addView(btn);
    }
}



