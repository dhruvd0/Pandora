package com.example.pandora;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Wormwhole extends Sprite {
    private Star[] stars = new Star[20];
    private Random rand;
    private int type;//0 for wormholeIn,1 for w

    Wormwhole(Bitmap image) {
        super(image);
        rand = new Random();

    }

    void flowingStars() {
        for (Star s : stars) {

            s.x = cx;
            if (type == 0) {
                s.y = cy - 100 - rand.nextInt(50);
            } else {
                s.y = cy + 100 + rand.nextInt(50);
            }
        }
    }

    void moveFlowingStars() {
        for (Star s : stars) {
            if (type == 0) {
                s.y--;
            } else {
                s.y++;
            }

        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

    }

}
