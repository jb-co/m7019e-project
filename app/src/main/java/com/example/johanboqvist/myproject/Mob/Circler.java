package com.example.johanboqvist.myproject.Mob;

/**
 * Created by johanboqvist on 20/05/16.
 */
public class Circler extends Mob {

    float rotation = 0;
    float centerX, centerY;
    int r = 200;

    public Circler(float x, float y) {
        super(x, y);

        centerX = x;
        centerY = y;
    }

    @Override
    public void move() {

        x = centerX + (float) Math.cos(rotation) * r;
        y = centerY + (float) Math.sin(rotation) * r;

        rotation += 0.05;
    }

    @Override
    public void handleCollision() {

    }
}
