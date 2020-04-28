package com.example.pandora;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.Random;

public class Planet extends Sprite {

    public static void loadPlanets(Planet[] planets, Resources r){
        Random rand=new Random();
        int id = R.drawable.planet1;
        for (int i = 0; i < 5; i++) {
            planets[i] = new Planet(BitmapFactory.decodeResource(r, id));
            id++;
            float x=rand.nextInt(300);
            float posY;
            try {
                posY = rand.nextInt(600) + planets[i - 1].y + 100;
            }
            catch (ArrayIndexOutOfBoundsException e){
                posY=rand.nextInt(300);
            }
            planets[i].setPos(x,posY);
        }
    }
    public static void drawPlanets(Planet[] planets,Canvas canvas){
        for (Planet p : planets) {
          p.rotate(3);
            p.draw(canvas);
        }
    }
    Planet(Bitmap bmp) {
        image = bmp;
    }

}
