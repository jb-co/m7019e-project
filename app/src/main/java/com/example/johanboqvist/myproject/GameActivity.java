package com.example.johanboqvist.myproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import com.example.johanboqvist.myproject.Mob.Circler;
import com.example.johanboqvist.myproject.Mob.Mob;
import com.example.johanboqvist.myproject.Mob.Player;
import com.example.johanboqvist.myproject.Mob.Randomer;
import com.example.johanboqvist.myproject.Mob.Slider;

import java.io.InputStream;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    public final static int TILE_SIZE = 96;
    public final static int MAP_WIDTH = 24;
    public final static int MAP_HEIGHT = 8;

    static Bitmap sprites;

    private MapManager mapManager = new MapManager(this);
    private Accelerometer accelerometer;

    private float scrollX = 0.f;
    private float scrollY = 0.f;
    private float speed = 2.5f;

    private float playerX = TILE_SIZE * 6;
    private float playerY = TILE_SIZE * 4;

    private Player player;

    private ArrayList<Integer> map;
    private ArrayList<Mob> npcs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final GameView gameView = new GameView(GameActivity.this);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sprites);
        final int mBitmapHeightAndWidth = (int) getResources().getDimension(
                R.dimen.image_height);
        sprites = Bitmap.createScaledBitmap(bitmap,
                114, 164, false);


        mapManager.loadMap(R.raw.level1);

        map = mapManager.getMap();

        npcs = new ArrayList<Mob>();

        player = new Player(playerX, playerY);



        loadNPCs();

        accelerometer = new Accelerometer(this);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.frame);
        relativeLayout.addView(gameView);

    }

    public synchronized void move(){

        float moveX = accelerometer.getY() * speed;
        float moveY = accelerometer.getX() * speed;

        float mapX = (player.getX() + scrollX);
        float mapY = (player.getY() + scrollY);

        player.move();

        if(moveX > 0){
            player.setDir(1);
        } else {
            player.setDir(-1);
        }

        if(!isCollision(mapX, mapY, moveX, 0)) {
            scrollX += moveX;
        }
        if(!isCollision(mapX, mapY, 0, moveY)) {
            scrollY += moveY;
        }



    }

    public boolean isCollision(float posX, float posY, float moveX, float moveY){
        for(int i = 0; i < 4; i++) {

            int newX = (int)((posX + (i%2)*TILE_SIZE + moveX) / TILE_SIZE);
            int newY = (int)((posY + (i/2)*TILE_SIZE + moveY) / TILE_SIZE);


            if (newX > MAP_WIDTH || newX / TILE_SIZE < 0) {
                return true;
            }
            if (newY > MAP_HEIGHT || newY < 0) {
                return true;
            }

            if (map.get(newX + newY * MAP_WIDTH) == '1') {
                return true;
            }

        }
        return false;
    }


    public void loadNPCs(){

        for(int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {

                if(map.get(x + y * MAP_WIDTH) == 'g'){
                    npcs.add(new Slider(x * TILE_SIZE, y * TILE_SIZE));
                }  else if(map.get(x + y * MAP_WIDTH) == 'c'){
                    npcs.add(new Circler(x * TILE_SIZE, y * TILE_SIZE));
                }   else if(map.get(x + y * MAP_WIDTH) == 'r'){
                    npcs.add(new Randomer(x * TILE_SIZE, y * TILE_SIZE));
                }

            }
        }
    }


    private class GameView extends SurfaceView implements SurfaceHolder.Callback {

        private Thread gameThread = null;
        private SurfaceHolder surfaceHolder;

        public GameView(Context context) {
            super(context);

            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            this.gameThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    Canvas canvas = null;
                    Paint paint = new Paint();

                    int width = getWidth();
                    int height = getHeight();


                    while (true) {

                        for(Mob mob : npcs){

                            //insert check for out of bounds here!
                            mob.move();

                            if(isCollision(mob.getX(), mob.getY(), 0, 0)){
                               mob.handleCollision();
                            }
                        }

                        move();

                        /*if(!surfaceHolder.getSurface().isValid())
                            continue;*/

                        canvas = surfaceHolder.lockCanvas();

                        /* draw here ! */
                        if (null != canvas) {
                            canvas.drawColor(Color.rgb(191, 191, 191));


                            int left = (int)(scrollX / TILE_SIZE) ;
                            int top = (int)(scrollY / TILE_SIZE);

                            for(int y = top; y < top + 12; y++) {
                                if(y < 0 || y > MAP_HEIGHT-1) continue;
                                for(int x = left; x < left + 32; x++) {

                                    if(x < 0 || x > MAP_WIDTH-1) continue;

                                    if((x + y * MAP_WIDTH) >= map.size() || (x+y*MAP_WIDTH < 0)) continue;
                                    int c = map.get(x + y * MAP_WIDTH);

                                    int offsetX = (int)scrollX;
                                    int offsetY = (int)scrollY;

                                        if(c == '1') {
                                            paint.setColor(Color.BLACK);
                                            canvas.drawRect(x * TILE_SIZE - offsetX, y*TILE_SIZE - offsetY,
                                                    x * TILE_SIZE - offsetX + TILE_SIZE, y * TILE_SIZE - offsetY + TILE_SIZE, paint);
                                        } else {
                                            RectF re = new RectF(x * TILE_SIZE - offsetX, y*TILE_SIZE - offsetY,
                                                    x * TILE_SIZE - offsetX + TILE_SIZE, y * TILE_SIZE - offsetY + TILE_SIZE);
                                            RectF d = new RectF(16*5, 0, 16, 16*15 + 16);
                                            canvas.drawBitmap(sprites, re, d, null);
                                        }


                                }
                            }


                            for(Mob mob : npcs){
                                /*paint.setColor(Color.RED);
                                canvas.drawRect(mob.getX() - scrollX, mob.getY() - scrollY,
                                        mob.getX() + TILE_SIZE - scrollX, mob.getY() + TILE_SIZE - scrollY, paint);*/
                                RectF r = new RectF(mob.getX() - scrollX, mob.getY() - scrollY,
                                        mob.getX() + TILE_SIZE - scrollX, mob.getY() + TILE_SIZE - scrollY);
                                canvas.drawBitmap(sprites, mob.getFrame(), r, null );
                            }


                            canvas.drawBitmap(sprites, player.getFrame(), new RectF(player.getX(), player.getY(),
                                    player.getX() + TILE_SIZE, player.getY() + TILE_SIZE), null);
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

        }
    }
}
