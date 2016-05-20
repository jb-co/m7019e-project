package com.example.johanboqvist.myproject.Mob;

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

        this.speed = 1.5f;
    }

    @Override
    public void move() {

        setCurrentPos();

        walkCounter++;
        if(walkCounter >= walkLength){
            walkCounter = 0;
            this.dir = random.nextInt(4);
        }


        switch(this.dir){

            case 0: {
                this.y -= speed;
                break;
            }
            case 1: {
                this.x += speed;
                break;
            }
            case 2: {
                this.y += speed;
                break;
            }
            case 3: {
                this.x -= speed;
                break;
            }

        }
    }

    @Override
    public void handleCollision() {
        loadCurrentPos();
        walkCounter = walkLength;
    }
}
