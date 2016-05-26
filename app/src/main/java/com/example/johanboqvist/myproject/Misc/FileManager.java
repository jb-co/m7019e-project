package com.example.johanboqvist.myproject.Misc;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.johanboqvist.myproject.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by johanboqvist on 12/05/16.
 */
public class FileManager {

    private Context context;
    private File mapFile = null;
    private BufferedReader br;

    public FileManager(Context context){
        this.context = context;
    }

    public void readFile(int resource) {



        ArrayList <Character> outArray = new ArrayList<Character>();
        try {
            int value = 0;

            while((value = br.read()) != -1){
                outArray.add((char)value);
            }

        } catch (IOException e) {
            Log.i("ioexception", "ioexception");
            e.printStackTrace();
        }
    }

    public void openFile(int resource){
        br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(resource)));
    }

    public void closeFile() {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int readValue(){
        int value;

        if(br != null){
            try {
                value = br.read();
                return value;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return -1;

    }
}
