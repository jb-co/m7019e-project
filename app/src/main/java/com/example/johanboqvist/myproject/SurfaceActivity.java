package com.example.johanboqvist.myproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.johanboqvist.myproject.Entity.Entity;
import com.example.johanboqvist.myproject.Entity.Player;
import com.example.johanboqvist.myproject.Misc.Accelerometer;
import com.example.johanboqvist.myproject.Misc.Globals;
import com.example.johanboqvist.myproject.Misc.MusicManager;
import com.example.johanboqvist.myproject.Misc.SoundManager;

import java.util.Iterator;

/**
 * Surface activity is the ingame class which holds the SurfaceView and the game loop
 */
public class SurfaceActivity extends AppCompatActivity {

    static Bitmap           sprites;  //the spritesheet in sprites.png

    private Accelerometer   accelerometer;
    private GameData        gameData;

    private GameState       gameState;
    private boolean         touched = false;

    private double          clock = 300;
    private boolean         isRunning = false;
    private boolean         isNewGame = true;
    private SoundManager    soundManager;

    private int             currentLevel = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameData = new GameData(getApplicationContext());
        final GameView gameView = new GameView(SurfaceActivity.this);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sprites);

        sprites = Bitmap.createScaledBitmap(bitmap,
                114, 164, false);

        accelerometer = new Accelerometer(this);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.frame);
        relativeLayout.addView(gameView);

        //Load the sound pool
        soundManager = new SoundManager(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    protected void onPause() {
        super.onPause();

        //make sure to not stop the music on back press.
        if(!Globals.backPressed) {
            MusicManager.stopMusic();
        }

        isRunning = false;
    }

    protected void onResume() {
        super.onResume();

        Globals.backPressed = false;
        MusicManager.startMusic();
    }

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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * movement of the accelerometer
     *
     * @param delta the delta
     */
    public synchronized void move(double delta) {

        float speed = 120f;
        float moveX = accelerometer.getY() * speed * (float)delta * Globals.SCALE_WIDTH;
        float moveY = accelerometer.getX() * speed * (float)delta * Globals.SCALE_HEIGHT;
        float scrollX = gameData.getScrollX();
        float scrollY = gameData.getScrollY();
        Player player = gameData.getPlayer();

        float mapX = (player.getX() + scrollX);
        float mapY = (player.getY() + scrollY);

        player.update(delta);

        if(moveX > 0){
            player.setDir(1);
        } else {
            player.setDir(-1);
        }

        if(!isCollision(mapX, mapY, moveX, 0)) {
            gameData.addScrollX(moveX);
        }
        if(!isCollision(mapX, mapY, 0, moveY)) {
            gameData.addScrollY(moveY);
        }
    }

    /**
     * Checks for collision on the map
     *
     * @param posX  the current pos x
     * @param posY  the current pos y
     * @param moveX the movement done in x
     * @param moveY the movement done in y
     * @return the boolean
     */
    public boolean isCollision(float posX, float posY, float moveX, float moveY){

        //check all four corners of the entity rect.
        for(int i = 0; i < 4; i++) {

            int newX = (int)((posX + (i%2)*Globals.TILE_WIDTH + moveX) / Globals.TILE_WIDTH);
            int newY = (int)((posY + (i/2)*Globals.TILE_HEIGHT + moveY) / Globals.TILE_HEIGHT);

            if (newX > gameData.MAP_WIDTH || newX / Globals.TILE_WIDTH < 0) {
                return true;
            }
            if (newY > gameData.MAP_HEIGHT || newY < 0) {
                return true;
            }

            // '1' is solid wall
            if (gameData.map.get(newX + newY * gameData.MAP_WIDTH) == '1') {
                return true;
            }
        }
        return false;
    }

    /**
     * SurfaceView class holds the canvas to draw on and the gameloop
     */
    private class GameView extends SurfaceView implements SurfaceHolder.Callback {

        private Thread gameThread = null;
        private SurfaceHolder surfaceHolder;

        public GameView(Context context) {
            super(context);

            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);

            //this is for touch events to transition between stages
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(gameState.getClass() == LevelTransition.class) touched = true;
                }
            });
        }

        public void surfaceCreated(SurfaceHolder holder) {

            isRunning = true;
            this.gameThread = new Thread(new Runnable() {

                public void run() {

                    Canvas canvas;

                    /** These globals are for scaling for different screen sizes!
                     * Reference width is 1794.0f and height is 1017.0f (Nexus 5x).
                     * **/
                    Globals.CANVAS_HEIGHT = getHeight();
                    Globals.CANVAS_WIDTH = getWidth();

                    Globals.SCALE_WIDTH =  Globals.CANVAS_WIDTH / 1794.0f;
                    Globals.SCALE_HEIGHT =  Globals.CANVAS_HEIGHT / 1017.0f;

                    Globals.TILE_WIDTH = 96 * Globals.SCALE_WIDTH;
                    Globals.TILE_HEIGHT = 96 * Globals.SCALE_HEIGHT;

                    /** Only load the level if surface is created from a new game **/
                    if(isNewGame){
                        isNewGame = false;
                        gameData.loadLevel(currentLevel);
                    }

                    gameState = new Playing();

                    long now, lastTime = System.nanoTime();
                    double delta = 0;

                    /** main game loop! **/
                    while (isRunning) {
                        now = System.nanoTime();
                        delta = (now - lastTime) / 1000000000.0;
                        lastTime = now;

                        /** update entities. movement is based on delta time */
                        gameState.update(delta);

                        /** check if all coins have been collected */
                        if(gameData.getCollected() == gameData.getCoins()){
                            gameState = new LevelTransition();
                        }

                        if(!surfaceHolder.getSurface().isValid())
                            continue;

                        canvas = surfaceHolder.lockCanvas();

                        /** draw here ! **/
                        if (null != canvas) {
                            gameState.render(canvas);
                            surfaceHolder.unlockCanvasAndPost(canvas);

                        }

                    }
                }
            });

            this.gameThread.start();

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            isRunning = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The GameState classes decide what is done in the game loop during rendering and updates
     */
    private abstract class GameState {
        public abstract void render(Canvas canvas);
        public abstract void update(double delta);
    }

    /**
     * Playing
     */
    private class Playing extends GameState{

        public void render(Canvas canvas) {
            canvas.drawColor(Color.BLACK);

            float scrollX = gameData.getScrollX();
            float scrollY = gameData.getScrollY();

            int left = (int)(scrollX / Globals.TILE_WIDTH);
            int top = (int)(scrollY / Globals.TILE_HEIGHT);

            /**draw the map **/
            for(int y = top; y < top + 13; y++) {
                if(y < 0 || y > gameData.MAP_HEIGHT-1) continue;
                for(int x = left; x < left + 22; x++) {

                    if(x < 0 || x > gameData.MAP_WIDTH-1) continue;

                    if((x + y * gameData.MAP_WIDTH) >= gameData.map.size() || (x+y*gameData.MAP_WIDTH < 0)) continue;
                    int c = gameData.map.get(x + y * gameData.MAP_WIDTH);

                    int offsetX = (int)scrollX;
                    int offsetY = (int)scrollY;

                    /** decide what tile from the map to draw **/
                    Rect tile;
                    if(c == '1') { //solid walls
                        tile = new Rect(16, 16*5, 16 + 16, 16*5 + 16);
                    } else {  //void
                        tile = new Rect(0, 16*5, 16, 16*5 + 16);
                    }

                    RectF tilePos = new RectF(x * Globals.TILE_WIDTH - offsetX, y*Globals.TILE_HEIGHT - offsetY,
                            x * Globals.TILE_WIDTH - offsetX + Globals.TILE_WIDTH, y * Globals.TILE_HEIGHT - offsetY + Globals.TILE_HEIGHT);
                    canvas.drawBitmap(sprites, tile, tilePos, null);

                }
            }

            /** draw entities **/
            for(Entity e : gameData.npcs){
                if(e.isOOB()) continue; //don't draw off screen entities

                RectF r = new RectF(e.getX() - scrollX, e.getY() - scrollY,
                        e.getX() + Globals.TILE_WIDTH - scrollX, e.getY() + Globals.TILE_HEIGHT - scrollY);
                canvas.drawBitmap(sprites, e.getFrame(), r, null );
            }

            /** draw player **/
            Player player = gameData.getPlayer();
            canvas.drawBitmap(sprites, player.getFrame(), new RectF(player.getX(), player.getY(),
                    player.getX() + Globals.TILE_WIDTH, player.getY() + Globals.TILE_HEIGHT), null);


            /** draw the bottom UI **/
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(48*Globals.SCALE_WIDTH);
            String bottomBar = "Coins: "+ gameData.getCollected()+" / "+ gameData.getCoins()
                    + "    Time: " + (int) clock + "    Total points: "+ gameData.getPoints();
            canvas.drawText(bottomBar, 50 * Globals.SCALE_WIDTH, Globals.CANVAS_HEIGHT - 48*Globals.SCALE_HEIGHT, paint);
        }

        /**
         * Update the entities
         *
         * @param delta time passed since last loop
         */
        public void update(double delta){

            float scrollX = gameData.getScrollX();
            float scrollY = gameData.getScrollY();

            clock = clock - delta;

            Iterator<Entity> i = gameData.npcs.iterator();
            while(i.hasNext()) {
                Entity e = i.next();

                e.checkOutOfBounds(scrollX, scrollY);
                if(e.isOOB()) continue; //don't update off screen entities

                e.update(delta);

                if(isCollision(e.getX(), e.getY(), 0, 0)){
                    e.handleCollision();
                }

                Player player = gameData.getPlayer();

                if(e.getRect(scrollX, scrollY).intersect(player.getRect())){
                    if(e.isCollectible()){  //player hit a coin
                        gameData.addCollected(1);
                        gameData.addPoints(25);
                        soundManager.playSound(0, 0.6f);
                        i.remove();
                    } else {             // player got hit by mob
                        soundManager.playSound(1, 1.0f);
                        gameData.resetPos();
                        gameData.addPoints(-10);

                        return;
                    }
                }
            }

            move(delta);
        }
    }

    /**
     * state between levels, ends on screen touch
     */
    private class LevelTransition extends GameState {

        private int bonus;

        public LevelTransition(){
            bonus = (int)clock;
        }

        public void render(Canvas canvas) {
            canvas.drawColor(Color.argb(40, 100, 220, 100));
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(128*Globals.SCALE_WIDTH);

            String title = "STAGE COMPLETE!";
            Rect bounds = new Rect();
            paint.getTextBounds(title, 0, title.length(), bounds);
            int x = (canvas.getWidth() / 2) - (bounds.width() / 2);
            int y = (canvas.getHeight() / 2) - (bounds.height() / 2);
            canvas.drawText(title,  x, y , paint);


            paint.setTextSize(64*Globals.SCALE_WIDTH);
            String points = "Time bonus: " + bonus;
            bounds = new Rect();
            paint.getTextBounds(points, 0, points.length(), bounds);
            x = (canvas.getWidth() / 2) - (bounds.width() / 2);
            y = (canvas.getHeight() / 2) - (bounds.height() / 2);
            canvas.drawText(points, x, y + 64*Globals.SCALE_HEIGHT, paint);
        }

        public void update(double delta) {

            if(touched){
                touched = false;
                gameData.addPoints(bonus);
                clock = 300;
                currentLevel++;

                /* game finished? */
                if(currentLevel >= GameData.LEVELS.length){
                    Intent intent = new Intent(SurfaceActivity.this, EndGameActivity.class);
                    intent.putExtra("points", gameData.getPoints());
                    finish();
                    startActivity(intent);
                } else { /*move to next level */
                    gameData.loadLevel(currentLevel);
                    gameState = new Playing();
                }
            }
        }
    }
}
