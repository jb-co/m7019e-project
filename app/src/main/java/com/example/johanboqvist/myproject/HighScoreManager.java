package com.example.johanboqvist.myproject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by johanboqvist on 24/05/16.
 */
public class HighScoreManager {

    private ArrayList<HighScoreEntry> list = new ArrayList<HighScoreEntry>();

    public String getHighScore(String location){
        try {

            URL url = new URL(location);

            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String buffer = "";
            int counter = 1;
            list.clear();

            while (true) {
                String name = in.readLine();
                if(name == null) break;

                String points = in.readLine();

                buffer += counter + ". " + name + " " + points + "\n";
                list.add(new HighScoreEntry(name, Integer.valueOf(points)));
                counter++;
            }
            in.close();


            return buffer;

        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }

        return null;
    }

    public boolean isHighScore(String name, int points){

        boolean goodEnough = false;
        int pos = 0;

        Iterator it = list.iterator();
        while (it.hasNext()) {
            HighScoreEntry h = (HighScoreEntry) it.next();


            if(points > h.points){
                goodEnough = true;
                break;
            }

            pos++;

        }

        if(goodEnough){
            list.add(pos, new HighScoreEntry(name, points));
            return true;
            //new WriteHighScore().execute("http://52.49.26.113/highscore.php");
        } else {
           // list.setText(s);
            return false;
        }

    }

    public void writeHighScore(String location){
        String str = "";
        for(HighScoreEntry h : list.subList(0, 4)){
            str += h.name + "\n" + h.points + "\n";
        }

        URL url = null;
        try {
            url = new URL(location);
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
    }

    public ArrayList<HighScoreEntry> getList() {
        return list;
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
