package com.example.pandora;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.Random;


public class Planet extends Sprite {
    Sprite skyhook;

    Planet(Bitmap bmp) {
        image = bmp;
    }

    static void loadPlanets(Planet[] planets, Resources r) {

        int id = R.drawable.planet1;
        Random rand = new Random();
        for (int i = 0; i < planets.length; i++) {
            planets[i] = new Planet(BitmapFactory.decodeResource(r, id));
            planets[i].skyhook = new Sprite(BitmapFactory.decodeResource(r, R.drawable.skyhook));
            id++;
            float x = rand.nextInt(400) + 100;
            float posY;
            try {
                posY = rand.nextInt(500) + 2 * planets[i - 1].y + 300;
            } catch (ArrayIndexOutOfBoundsException e) {
                posY = rand.nextInt(300) + 100;
            }

            planets[i].setPos(x, posY);

        }
    }

    private void drawSkyHook(Canvas canvas) {
        skyhook.draw(canvas);
        skyhook.rotate(skyhook.circleAngle-skyhook.rotateAngle);
        skyhook.revolve(this);

    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        rotate((float) 0.5);
        drawSkyHook(canvas);
    }

    static void drawPlanets(Planet[] planets, Canvas canvas) {
        for (Planet p : planets) {


            p.draw(canvas);

        }
    }


}
