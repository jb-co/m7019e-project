package com.example.johanboqvist.myproject.Mob;

import android.graphics.Rect;

import java.util.Random;

/**
 * Created by johanboqvist on 20/05/16.
 */
public class Randomer extends Mob {

    private Random random = new Random();
    private int walkLength = 24;
    private int walkCounter = 0;

    public Randomer(float x, float y) {
        super(x, y);

        this.speed = 100.0f;
    }

    @Override
    public Rect getFrame() {
        return new Rect(frame * 16, 16 * 2, frame * 16 + 16, 16 * 2 + 16);
    }

    @Override
    public void update(double delta) {

        setCurrentPos();

        walkCounter++;
        if(walkCounter >= walkLength){
            walkCounter = 0;
            this.dir = random.nextInt(4);
        }


        switch(this.dir){

            case 0: {
                this.y -= speed * delta;
                break;
            }
            case 1: {
                this.x += speed * delta;
                break;
            }
            case 2: {
                this.y += speed * delta;
                break;
            }
            case 3: {
                this.x -= speed * delta;
                break;
            }

        }

        frameCounter++;

        if(frameCounter > frameDelay) {
            frameCounter = 0;
            frame += 1;

            frame = frame % 5;
        }
    }

    @Override
    public void handleCollision() {
        loadCurrentPos();
        walkCounter = walkLength;
    }
}
