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
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.johanboqvist.myproject.Entity.Mob;
import com.example.johanboqvist.myproject.Entity.Player;
import com.example.johanboqvist.myproject.Misc.Accelerometer;
import com.example.johanboqvist.myproject.Misc.Globals;
import com.example.johanboqvist.myproject.Misc.MusicManager;
import com.example.johanboqvist.myproject.Misc.SoundManager;

import java.util.Iterator;


public class SurfaceActivity extends AppCompatActivity {

    static Bitmap sprites;

    private Accelerometer accelerometer;
    private GameData gameData;

    private GameState gameState;
    private boolean touched = false;

    private double clock = 300;
    private boolean isRunning = false;
    private SoundManager soundManager;



    private int currentLevel = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final GameView gameView = new GameView(SurfaceActivity.this);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sprites);

        sprites = Bitmap.createScaledBitmap(bitmap,
                114, 164, false);



        accelerometer = new Accelerometer(this);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.frame);
        relativeLayout.addView(gameView);

        soundManager = new SoundManager(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);



    }

    @Override
    protected void onPause() {
        super.onPause();

        if(!Globals.backPressed) {
            MusicManager.stopMusic();
        }

        isRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Globals.backPressed = false;
        MusicManager.startMusic();


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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public synchronized void move(double delta) {

        float speed = 120f;
        float moveX = accelerometer.getY() * speed * (float)delta * Globals.SCALE_WIDTH;
        float moveY = accelerometer.getX() * speed * (float)delta * Globals.SCALE_HEIGHT;
        Player player = gameData.player;

        float mapX = (player.getX() + gameData.scrollX);
        float mapY = (player.getY() + gameData.scrollY);

        player.update(delta);

        if(moveX > 0){
            player.setDir(1);
        } else {
            player.setDir(-1);
        }

        if(!isCollision(mapX, mapY, moveX, 0)) {
            gameData.scrollX += moveX;
        }
        if(!isCollision(mapX, mapY, 0, moveY)) {
            gameData.scrollY += moveY;
        }
    }

    public boolean isCollision(float posX, float posY, float moveX, float moveY){
        for(int i = 0; i < 4; i++) {

            int newX = (int)((posX + (i%2)*Globals.TILE_WIDTH + moveX) / Globals.TILE_WIDTH);
            int newY = (int)((posY + (i/2)*Globals.TILE_HEIGHT + moveY) / Globals.TILE_HEIGHT);

            if (newX > gameData.MAP_WIDTH || newX / Globals.TILE_WIDTH < 0) {
                return true;
            }
            if (newY > gameData.MAP_HEIGHT || newY < 0) {
                return true;
            }

            if (gameData.map.get(newX + newY * gameData.MAP_WIDTH) == '1') {
                return true;
            }
        }
        return false;
    }




    private class GameView extends SurfaceView implements SurfaceHolder.Callback {

        private Thread gameThread = null;
        private SurfaceHolder surfaceHolder;

        public GameView(Context context) {
            super(context);

            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);

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
                @Override
                public void run() {

                    Canvas canvas;

                    Globals.CANVAS_HEIGHT = getHeight();
                    Globals.CANVAS_WIDTH = getWidth();

                    Globals.SCALE_WIDTH =  Globals.CANVAS_WIDTH / 1794.0f;
                    Globals.SCALE_HEIGHT =  Globals.CANVAS_HEIGHT / 1017.0f;

                    Globals.TILE_WIDTH = 96 * Globals.SCALE_WIDTH;//(int) Globals.CANVAS_WIDTH / 20;
                    Globals.TILE_HEIGHT = 96 * Globals.SCALE_HEIGHT;

                    gameData = new GameData(getApplicationContext());
                    gameData.loadLevel(GameData.LEVELS[currentLevel]);

                    gameState = new Playing();

                    long now, lastTime = System.nanoTime();
                    double delta = 0;

                    while (isRunning) {
                        now = System.nanoTime();
                        delta = (now - lastTime) / 1000000000.0;
                        lastTime = now;

                        gameState.update(delta);

                        if(gameData.collected == gameData.coins){
                            gameState = new LevelTransition();
                        }

                        if(!surfaceHolder.getSurface().isValid())
                            continue;

                        canvas = surfaceHolder.lockCanvas();

                        /* draw here ! */
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

    private abstract class GameState {
        public abstract void render(Canvas canvas);
        public abstract void update(double delta);
    }

    private class Playing extends GameState{

        @Override
        public void render(Canvas canvas) {
            canvas.drawColor(Color.BLACK);

            int left = (int)(gameData.scrollX / Globals.TILE_WIDTH) ;
            int top = (int)(gameData.scrollY / Globals.TILE_HEIGHT);
            //int w = (int)(Globals.CANVAS_WIDTH / Globals.TILE_SIZE) + Globals.TILE_SIZE;
            //int h = (int)(Globals.CANVAS_HEIGHT / Globals.TILE_SIZE) + Globals.TILE_SIZE;

            for(int y = top; y < top + 13; y++) {
                if(y < 0 || y > gameData.MAP_HEIGHT-1) continue;
                for(int x = left; x < left + 22; x++) {

                    if(x < 0 || x > gameData.MAP_WIDTH-1) continue;

                    if((x + y * gameData.MAP_WIDTH) >= gameData.map.size() || (x+y*gameData.MAP_WIDTH < 0)) continue;
                    int c = gameData.map.get(x + y * gameData.MAP_WIDTH);

                    int offsetX = (int)gameData.scrollX;
                    int offsetY = (int)gameData.scrollY;

                    if(c == '1') {
                        RectF re = new RectF(x * Globals.TILE_WIDTH - offsetX, y*Globals.TILE_HEIGHT - offsetY,
                                x * Globals.TILE_WIDTH - offsetX + Globals.TILE_WIDTH, y * Globals.TILE_HEIGHT - offsetY + Globals.TILE_HEIGHT);
                        Rect d = new Rect(16, 16*5, 16 + 16, 16*5 + 16);
                        canvas.drawBitmap(sprites, d, re, null);
                    } else {
                        RectF re = new RectF(x * Globals.TILE_WIDTH - offsetX, y*Globals.TILE_HEIGHT - offsetY,
                                x * Globals.TILE_WIDTH - offsetX + Globals.TILE_WIDTH, y * Globals.TILE_HEIGHT - offsetY + Globals.TILE_HEIGHT);
                        Rect d = new Rect(0, 16*5, 16, 16*5 + 16);
                        canvas.drawBitmap(sprites, d, re, null);
                    }


                }
            }


            for(Mob mob : gameData.npcs){
                RectF r = new RectF(mob.getX() - gameData.scrollX, mob.getY() - gameData.scrollY,
                        mob.getX() + Globals.TILE_WIDTH - gameData.scrollX, mob.getY() + Globals.TILE_HEIGHT - gameData.scrollY);
                canvas.drawBitmap(sprites, mob.getFrame(), r, null );
            }

            Player player = gameData.player;
            canvas.drawBitmap(sprites, player.getFrame(), new RectF(player.getX(), player.getY(),
                    player.getX() + Globals.TILE_WIDTH, player.getY() + Globals.TILE_HEIGHT), null);


            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(48*Globals.SCALE_WIDTH);
            String bottomBar = "Coins: "+ gameData.collected+" / "+ gameData.coins
                    + "    Time: " + (int) clock + "    Total points: "+ gameData.getPoints();
            canvas.drawText(bottomBar, 50 * Globals.SCALE_WIDTH, Globals.CANVAS_HEIGHT - 48*Globals.SCALE_HEIGHT, paint);
        }

        public void update(double delta){

            clock = clock - delta;

            Iterator<Mob> i = gameData.npcs.iterator();
            while(i.hasNext()) {
                Mob mob = i.next();

                //insert check for out of bounds here!
                mob.update(delta);

                if(isCollision(mob.getX(), mob.getY(), 0, 0)){
                    mob.handleCollision();
                }

                if(mob.getRect(gameData.scrollX, gameData.scrollY).intersect(gameData.player.getRect())){
                    if(mob.isCollectible()){  //player hit a coin
                        gameData.collected++;
                        gameData.addPoints(25);
                        soundManager.playSound(0, 0.6f);
                        i.remove();
                    } else {             // player got hit by mob
                        soundManager.playSound(1, 1.0f);
                        gameData.scrollX = 0;
                        gameData.scrollY = 0;
                        gameData.player.setX(10 * Globals.TILE_WIDTH);
                        gameData.player.setY(6 * Globals.TILE_HEIGHT);
                        gameData.addPoints(-10);
                        gameData.player.setDead(true);

                        return;

                    }
                }
            }

            move(delta);
        }
    }


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

        @Override
        public void update(double delta) {

            if(touched){
                touched = false;
                gameData.addPoints(bonus);
                gameData.scrollX = 0;
                gameData.scrollY = 0;
                clock = 300;
                currentLevel++;

                /* game finished? */
                if(currentLevel >= GameData.LEVELS.length){
                    Intent intent = new Intent(SurfaceActivity.this, EndGameActivity.class);
                    intent.putExtra("points", gameData.getPoints());
                    finish();
                    startActivity(intent);
                } else {
                    gameData.loadLevel(currentLevel);
                    gameState = new Playing();
                }
            }
        }


    }
}
