package com.example.pandora;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.Random;


public class Planet extends Sprite {
    Sprite skyhook;
    static Random rand;

    Planet(Bitmap bmp) {
        super(bmp);
        rand = new Random();
    }


    static void loadPlanets(Planet[] planets, Resources r,int maxWidth,int maxHeight) {

        int id = R.drawable.planet1;

        for (int i = 0; i < planets.length; i++) {
            planets[i] = new Planet(BitmapFactory.decodeResource(r, id));
            planets[i].skyhook = new Sprite(BitmapFactory.decodeResource(r, R.drawable.skyhook));
            id++;
            float x, y;
            x = 100 + rand.nextInt(maxWidth-200);

            if (i == 0) {

                y = 100 + rand.nextInt(300);
            } else {

                y = planets[i - 1].cy + 600;
            }

            if(y>maxHeight-200){
                y=maxHeight-200-rand.nextInt(300);
            }
            planets[i].setPos(x,y);
        }
    }

    private void drawSkyHook(Canvas canvas) {
        skyhook.draw(canvas);
        skyhook.rotate(skyhook.circleAngle - skyhook.rotateAngle);
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
