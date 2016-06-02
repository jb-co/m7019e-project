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
 * FileManager handles opening and reading from a file
 */
public class FileManager {

    private Context         context;
    private BufferedReader  br;

    public FileManager(Context context){
        this.context = context;
    }

    /**
     * Open a file
     * @param resource resource id of file to be opened
     */
    public void openFile(int resource){
        br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(resource)));
    }

    /**
     * Close currently opened file
     */
    public void closeFile() {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a value frome file. Returns -1 if end of file.
     * @return
     */
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
