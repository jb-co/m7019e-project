package com.example.johanboqvist.myproject.Misc;

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
 * Reads and writes to global highscore list kept on Amazon server.
 */
public class HighScoreManager {

    private ArrayList<HighScoreEntry> list = new ArrayList<HighScoreEntry>();

    /**
     * Reads the highscore from aws and inserts entries in list array.
     * @param location file location
     * @return
     */
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

    /**
     * Checks if current score is a highscore, must be run after getHighScore.
     * @param points players points
     * @return  returns position to insert entry or -1 if not a highscore.
     */
    public int isHighScore(int points){

        boolean goodEnough = false;
        int pos = 0; //where to insert new entry in highscore list

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
            return pos;
        } else {
            return -1;
        }
    }

    public void addEntry(int pos, String name, int points){
        list.add(pos, new HighScoreEntry(name, points));
    }

    /**
     * Writes new highscore to text file on aws server
     * @param location  location of php-file which handles writing to text file
     */
    public void writeHighScore(String location){
        String str = "";
        for(HighScoreEntry h : list.subList(0, 5)){
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

    /**
     * Entry structure class.
     */
    private class HighScoreEntry {
        public String name;
        public int points;

        public HighScoreEntry(String name, int points) {
            this.name = name;
            this.points = points;
        }
    }
}
