package com.example.johanboqvist.myproject;

import android.content.Context;

import com.example.johanboqvist.myproject.Entity.Circler;
import com.example.johanboqvist.myproject.Entity.Coin;
import com.example.johanboqvist.myproject.Entity.Entity;
import com.example.johanboqvist.myproject.Entity.Player;
import com.example.johanboqvist.myproject.Entity.Randomer;
import com.example.johanboqvist.myproject.Entity.Slider;
import com.example.johanboqvist.myproject.Misc.Globals;


import java.util.ArrayList;

/**
 * GameData
 *
 * The GameData class provides state data (entity positions, screen scroll..etc)
 * for the game screen.
 */
public class GameData {

    public final static int     MAP_WIDTH = 40;
    public final static int     MAP_HEIGHT = 20;
    private float               scrollX = 0.f; //screen scroll X
    private float               scrollY = 0.f; //screen scroll Y
    private Player              player;
    private int                 coins; //total amount of coins on current map
    private int                 collected = 0; //collected coins of current map
    private int                 points = 0;

    public ArrayList<Integer>   map;   //holds the current map
    public ArrayList<Entity>    npcs; //holds all the non player entities of current map

    private MapManager          mapManager;
    private Context             context;

    public final static int[]   LEVELS = {  //level resource ids, more levels can safely be added
            R.raw.level2,
            R.raw.level1
    };

    public GameData(Context context){
        this.context = context;
    }

    /**
     * Loads and resets level state and loads non player entities.
     *
     * @param level resource id of level
     */
    public void loadLevel(int level){

        mapManager = new MapManager(context);
        mapManager.loadMap(LEVELS[level]);

        map = mapManager.getMap();

        this.coins = 0;
        this.collected = 0;

        player = new Player(Globals.TILE_WIDTH * 10, Globals.TILE_HEIGHT * 4);

        loadNPCs();
    }

    /**
     * Loads entities from current level in to npcs array. Also sets player spawn pos.
     * Each entity type has its own char in the map array.
     */
    public void loadNPCs(){

        npcs = new ArrayList<Entity>();

        for(int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {

                if(map.get(x + y * MAP_WIDTH) == 'g'){
                    npcs.add(new Slider(x * Globals.TILE_WIDTH, y * Globals.TILE_HEIGHT, 0));
                } else if(map.get(x + y * MAP_WIDTH) == 'h'){
                    npcs.add(new Slider(x * Globals.TILE_WIDTH, y * Globals.TILE_HEIGHT, 1));
                }  else if(map.get(x + y * MAP_WIDTH) == 'c'){
                    npcs.add(new Circler(x * Globals.TILE_WIDTH, y * Globals.TILE_HEIGHT));
                }   else if(map.get(x + y * MAP_WIDTH) == 'r'){
                    npcs.add(new Randomer(x * Globals.TILE_WIDTH, y * Globals.TILE_HEIGHT));
                } else if(map.get(x + y * MAP_WIDTH) == 'z'){  //COIN!
                    this.coins++;
                    npcs.add(new Coin(x * Globals.TILE_WIDTH, y * Globals.TILE_HEIGHT));
                } else if(map.get(x + y * MAP_WIDTH) == 'p'){   //PLAYER SPAWN POINT!
                    int w = (int)((MAP_WIDTH/2) * Globals.TILE_WIDTH);
                    int h = (int)((MAP_HEIGHT/2) * Globals.TILE_HEIGHT);
                    scrollX = x * Globals.TILE_WIDTH - player.getX();
                    scrollY = y * Globals.TILE_HEIGHT - player.getY();
                    player.setStartPos(scrollX, scrollY);
                }

            }
        }
    }

    public void resetPos(){
        scrollX = player.getStartX();
        scrollY = player.getStartY();
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int increase){
        this.points += increase;
        if(this.points < 0) this.points = 0;
    }

    public float getScrollX() {
        return scrollX;
    }

    public float getScrollY() {
        return scrollY;
    }

    public void addScrollX(float add){
        this.scrollX += add;
    }

    public void addScrollY(float add){
        this.scrollY += add;
    }

    public Player getPlayer() {
        return player;
    }

    public int getCoins() {
        return coins;
    }

    public int getCollected() {
        return collected;
    }

    public void addCollected(int inc){
        this.collected += inc;
    }
}
