package com.example.johanboqvist.myproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class HighScoreActivity extends AppCompatActivity {

    private TextView list;
    private String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        //new getHighScore().execute("http://52.49.26.113/highscore.txt");

        str = "Johan\n111\nNotFirst\n75\nCloseThird\n44";
        new WriteHighScore().execute("http://52.49.26.113/highscore.php");

        list = (TextView) findViewById(R.id.textList);

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

            list.setText(s);

        }
    }

    private class WriteHighScore extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
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


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);



        }
    }
}
