package com.example.pandora;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Planet extends Sprite {
    int id;
    public static void drawPlanets(Planet[] planets,Canvas canvas){
        for (Planet p : planets) {
            p.draw(canvas);
        }
    }
    Planet(Bitmap bmp) {
        image = bmp;
    }

}
