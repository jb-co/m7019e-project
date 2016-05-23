package com.example.johanboqvist.myproject;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HighScoreActivity extends AppCompatActivity {

    private TextView list;
    private String str;
    private HighScoreManager highScoreManager = new HighScoreManager();

    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        list = (TextView) findViewById(R.id.textList);
        //new getHighScore().execute("http://52.49.26.113/highscore.txt");

        //str = "Johan\n111\nNotFirst\n75\nCloseThird\n44";
        // new WriteHighScore().execute("http://52.49.26.113/highscore.php");

        new Thread() {
            public void run() {
                result = highScoreManager.getHighScore("http://52.49.26.113/highscore.txt");
                messageHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private final Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            list.setText(result);
        }
    };
}



