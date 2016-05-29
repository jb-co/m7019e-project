package com.example.johanboqvist.myproject;

import android.content.Context;

import com.example.johanboqvist.myproject.Misc.FileManager;

import java.util.ArrayList;

/**
 * Created by johanboqvist on 12/05/16.
 */
public class MapManager {

    private ArrayList<Integer>  map;
    private Context             context;

    public MapManager(Context context){
        this.context = context;
    }

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
