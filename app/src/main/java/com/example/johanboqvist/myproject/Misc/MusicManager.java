package com.example.johanboqvist.myproject.Misc;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.johanboqvist.myproject.R;

/**
 * Created by johanboqvist on 25/05/16.
 */
public class MusicManager {

    public static MediaPlayer   player;
    public static boolean       MUSIC = true;

    public static void createPlayer(Context context) {

        if (player == null) {
            player = MediaPlayer.create(context, R.raw.jibjig);
            player.setLooping(true); // Set looping
            player.setVolume(100, 100);
        }
    }

    public static void startMusic(){

        if(player != null && !player.isPlaying() && MUSIC) {
            player.start();
        }
    }

    public static void stopMusic(){
        if(player != null && player.isPlaying() && MUSIC) {
            player.pause();
        }
    }

    public static void releasePlayer(){
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
