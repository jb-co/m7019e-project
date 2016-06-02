package com.example.johanboqvist.myproject;

import android.content.Context;

import com.example.johanboqvist.myproject.Misc.FileManager;

import java.util.ArrayList;

/**
 * Loads map from raw resource file and keeps in map array
 */
public class MapManager {

    private ArrayList<Integer>  map;
    private Context             context;

    public MapManager(Context context){
        this.context = context;
    }

    /**
     * @param resource  reads characters from resource id file and adds to map array
     */
    public void loadMap(int resource){

        FileManager f = new FileManager(context);
        f.openFile(resource);

        //clear the map array
        map = new ArrayList<Integer>();

        int value;
        while((value = f.readValue()) != -1){
            if (value == '\n') continue;   //ignore newline symbol
            map.add(value);
        }

        f.closeFile();
    }

    public ArrayList<Integer> getMap() {
        return map;
    }
}
