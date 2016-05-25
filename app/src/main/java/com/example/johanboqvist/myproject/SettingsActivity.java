package com.example.johanboqvist.myproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.johanboqvist.myproject.Misc.Globals;
import com.example.johanboqvist.myproject.Misc.MusicManager;
import com.example.johanboqvist.myproject.Misc.SoundManager;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchMusic;
    private Switch switchSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchMusic = (Switch) findViewById(R.id.switchMusic);
        switchSound = (Switch) findViewById(R.id.switchSound);

        switchMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    MusicManager.MUSIC = true;
                    MusicManager.createPlayer(getApplicationContext());
                    MusicManager.startMusic();
                } else {
                    MusicManager.MUSIC = false;
                    MusicManager.stopMusic();
                    MusicManager.releasePlayer();
                }
            }
        });

        switchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SoundManager.SOUND = isChecked;
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();

        if(!Globals.backPressed) {
            MusicManager.stopMusic();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        Globals.backPressed = false;
        MusicManager.startMusic();

        switchMusic.setChecked(MusicManager.MUSIC);
        switchSound.setChecked(SoundManager.SOUND);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Globals.backPressed = true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
