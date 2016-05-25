package com.example.johanboqvist.myproject;

import android.content.Context;

import com.example.johanboqvist.myproject.Entity.Circler;
import com.example.johanboqvist.myproject.Entity.Coin;
import com.example.johanboqvist.myproject.Entity.Mob;
import com.example.johanboqvist.myproject.Entity.Player;
import com.example.johanboqvist.myproject.Entity.Randomer;
import com.example.johanboqvist.myproject.Entity.Slider;
import com.example.johanboqvist.myproject.Misc.Globals;


import java.util.ArrayList;

/**
 * Created by johanboqvist on 23/05/16.
 */
public class GameData {

    public static int MAP_WIDTH = 40;
    public static int MAP_HEIGHT = 20;
    public float scrollX = 0.f;
    public float scrollY = 0.f;

    public final static int[] LEVELS = {
            R.raw.level1

    };

    public Player player;

    public int coins;
    public int collected = 0;
    private int points = 0;

    public ArrayList<Integer> map;
    public ArrayList<Mob> npcs;

    private MapManager mapManager;
    private Context context;

    public GameData(Context context){
        this.context = context;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int increase){
        this.points += increase;
        if(this.points < 0) this.points = 0;
    }

    public void loadLevel(int level){

        mapManager = new MapManager(context);
        mapManager.loadMap(level);

        map = mapManager.getMap();

        this.coins = 0;
        this.collected = 0;

        player = new Player(Globals.TILE_WIDTH * 10, Globals.TILE_HEIGHT * 4);

        loadNPCs();
    }

    public void loadNPCs(){

        npcs = new ArrayList<Mob>();

        for(int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {

                if(map.get(x + y * MAP_WIDTH) == 'g'){
                    npcs.add(new Slider(x * Globals.TILE_WIDTH, y * Globals.TILE_HEIGHT));
                }  else if(map.get(x + y * MAP_WIDTH) == 'c'){
                    npcs.add(new Circler(x * Globals.TILE_WIDTH, y * Globals.TILE_HEIGHT));
                }   else if(map.get(x + y * MAP_WIDTH) == 'r'){
                    npcs.add(new Randomer(x * Globals.TILE_WIDTH, y * Globals.TILE_HEIGHT));
                } else if(map.get(x + y * MAP_WIDTH) == 'z'){
                    this.coins++;
                    npcs.add(new Coin(x * Globals.TILE_WIDTH, y * Globals.TILE_HEIGHT));
                } else if(map.get(x + y * MAP_WIDTH) == 'p'){
                    int w = (int)((MAP_WIDTH/2) * Globals.TILE_WIDTH);
                    int h = (int)((MAP_HEIGHT/2) * Globals.TILE_HEIGHT);
                    scrollX = -(w/2 - x) + 2 * Globals.TILE_WIDTH;
                    scrollY = -(h/2 - y) + 2 * Globals.TILE_HEIGHT;
                }

            }
        }
    }
}
