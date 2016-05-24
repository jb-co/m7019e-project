package com.example.johanboqvist.myproject;

import android.content.Context;

import com.example.johanboqvist.myproject.Mob.Circler;
import com.example.johanboqvist.myproject.Mob.Coin;
import com.example.johanboqvist.myproject.Mob.Mob;
import com.example.johanboqvist.myproject.Mob.Player;
import com.example.johanboqvist.myproject.Mob.Randomer;
import com.example.johanboqvist.myproject.Mob.Slider;

import java.util.ArrayList;

/**
 * Created by johanboqvist on 23/05/16.
 */
public class GameData {

    public final static int TILE_SIZE = 96;
    public final static int MAP_WIDTH = 24;
    public final static int MAP_HEIGHT = 8;
    public final static int[] LEVELS = {
            R.raw.level1

    };

    public Player player;

    public int coins;
    public int collected = 0;

    public ArrayList<Integer> map;
    public ArrayList<Mob> npcs;

    private MapManager mapManager;
    private Context context;

    public GameData(Context context){
        this.context = context;

        player = new Player(TILE_SIZE * 10, TILE_SIZE * 4);
    }

    public void loadLevel(int level){

        mapManager = new MapManager(context);
        mapManager.loadMap(level);

        map = mapManager.getMap();

        this.coins = 0;
        this.collected = 0;

        loadNPCs();
    }

    public void loadNPCs(){

        npcs = new ArrayList<Mob>();

        for(int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {

                if(map.get(x + y * MAP_WIDTH) == 'g'){
                    npcs.add(new Slider(x * TILE_SIZE, y * TILE_SIZE));
                }  else if(map.get(x + y * MAP_WIDTH) == 'c'){
                    npcs.add(new Circler(x * TILE_SIZE, y * TILE_SIZE));
                }   else if(map.get(x + y * MAP_WIDTH) == 'r'){
                    npcs.add(new Randomer(x * TILE_SIZE, y * TILE_SIZE));
                } else if(map.get(x + y * MAP_WIDTH) == 'z'){
                    this.coins++;
                    npcs.add(new Coin(x * TILE_SIZE, y * TILE_SIZE));
                }

            }
        }
    }
}
