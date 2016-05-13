package com.example.johanboqvist.myproject;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    public final static int TILE_SIZE = 64;

    private MapManager mapManager = new MapManager(this);
    private Accelerometer accelerometer;

    private float scrollX = 0.f;
    private float scrollY = 0.f;
    private float speed = 1.5f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final GameView gameView = new GameView(GameActivity.this);


        mapManager.loadMap(R.raw.level1);

        accelerometer = new Accelerometer(this);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.frame);
        relativeLayout.addView(gameView);

    }

    public synchronized void move(){

        scrollX += accelerometer.getY() * speed;
        scrollY += accelerometer.getX() * speed;



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


                        move();

                        /*if(!surfaceHolder.getSurface().isValid())
                            continue;*/

                        canvas = surfaceHolder.lockCanvas();

                        /* draw here ! */
                        if (null != canvas) {
                            canvas.drawColor(Color.WHITE);
                            ArrayList<Integer> map = mapManager.getMap();

                            int left = (int)(scrollX / TILE_SIZE) ;
                            int top = (int)(scrollY / TILE_SIZE);

                            for(int y = top; y < top + 18; y++) {

                                if(y < 0 || y > 2) continue;
                                for(int x = left; x < left + 24; x++) {

                                    if(x < 0 || x > 5) continue;

                                    if((x + y * 6) >= map.size()) break;
                                    int c = map.get(x + y * 6);

                                    int offsetX = (int)scrollX;
                                    int offsetY = (int)scrollY;

                                        if(c == '1') {
                                            paint.setColor(Color.GREEN);
                                            canvas.drawRect(x * TILE_SIZE - offsetX, (y * TILE_SIZE) * 6 -offsetY,
                                                    x * TILE_SIZE - offsetX + TILE_SIZE, (y * TILE_SIZE) * 6 - offsetY + TILE_SIZE, paint);
                                        } else {
                                           // paint.setColor(Color.RED);
                                           // canvas.drawRect(x * 48, 100, x * 48 + 48, 148, paint);
                                        }


                                }
                            }

                            paint.setColor(Color.RED);
                            canvas.drawRect(width/2 - 32, height/2 - 32, width/2 + 32, height/2 + 32, paint);
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
