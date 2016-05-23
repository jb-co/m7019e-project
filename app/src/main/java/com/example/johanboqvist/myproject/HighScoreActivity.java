package com.example.johanboqvist.myproject;

import android.os.AsyncTask;
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
    private ArrayList <HighScoreEntry> highList = new ArrayList<HighScoreEntry>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        list = (TextView) findViewById(R.id.textList);
        new getHighScore().execute("http://52.49.26.113/highscore.txt");

        //str = "Johan\n111\nNotFirst\n75\nCloseThird\n44";
       // new WriteHighScore().execute("http://52.49.26.113/highscore.php");



    }

    public void newEntry(String name, int points){
        new getHighScore().execute("http://52.49.26.113/highscore.txt");
    }


    private class getHighScore extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {

                URL url = new URL(params[0]);

                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                String buffer = "";
                int counter = 1;

                while (true) {
                    String name = in.readLine();
                    if(name == null) break;

                    String points = in.readLine();

                    buffer += counter + ". " + name + " " + points + "\n";
                    highList.add(new HighScoreEntry(name, Integer.valueOf(points)));
                    counter++;
                }
                in.close();


                return buffer;

            } catch (MalformedURLException e) {
            } catch (IOException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String n = "new Entry";
            int points = 88;
            boolean goodEnough = false;
            int pos = 0;

            Iterator it = highList.iterator();
            while (it.hasNext()) {
                HighScoreEntry h = (HighScoreEntry) it.next();


                if(points > h.points){
                    goodEnough = true;
                    break;
                }

                pos++;

            }

            if(goodEnough){
                highList.add(pos, new HighScoreEntry(n, points));
                new WriteHighScore().execute("http://52.49.26.113/highscore.php");
            } else {
                list.setText(s);
            }



        }
    }

    private class WriteHighScore extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            str = "";
            for(HighScoreEntry h : highList){
                str += h.name + "\n" + h.points + "\n";
            }

            URL url = null;
            try {
                url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                HttpURLConnection http = (HttpURLConnection)connection;
                http.setDoOutput(true);
                http.setRequestMethod("POST");
                DataOutputStream dos = new DataOutputStream(http.getOutputStream());

                dos.writeBytes("str="+str);
                dos.flush();
                dos.close();
                int responseCode = http.getResponseCode();


                http.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            list.setText(s);

        }
    }

    private class HighScoreEntry {
        public String name;
        public int points;

        public HighScoreEntry(String name, int points) {
            this.name = name;
            this.points = points;
        }
    }
}
