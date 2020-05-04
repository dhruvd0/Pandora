package com.example.pandora;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

public class Star {
    float x, y, z;
    Paint paint;
    static Random rand;
    static int movingStars[];
    static int randomIndex;
    float radius;
    static void selectRandomStars() {
        movingStars = new int[10];
        for (int i = 0; i < movingStars.length; i++) {
            movingStars[i] = rand.nextInt(300);
        }
    }

    Star(int cW,int cH) {


        paint = new Paint();
        paint.setColor(Color.WHITE);
        rand = new Random();
        x = rand.nextInt(cW);
        y = rand.nextInt(cH);
        radius=1+rand.nextInt(5);
    }

    static void moveRandomStars(Star stars[]) {
        for (int i = 0; i < movingStars.length; i++) {
            randomIndex = movingStars[i];
            float dy = 20 + rand.nextInt(10);
            double b=Math.pow(-1,rand.nextInt(1));
            float dx = (float) (b*rand.nextInt(5));
            if (randomIndex < stars.length) {
                stars[randomIndex].y += dy;
                stars[randomIndex].x += dx;
            }
        }
    }

    void setRandomPos(Canvas canvas) {
        try {
            x = 10 + rand.nextInt(canvas.getWidth());
            y = 10 + rand.nextInt(5000);
        }
        catch (Exception e){
            Log.i("canvas width",Integer.toString(canvas.getWidth()));
        }
    }

    static void drawStars(Star s[], Canvas canvas) {
        for (int i = 0; i < s.length; i++) {

            s[i].draw(canvas);
        }
    }

    static void setStars(Star stars[], Canvas canvas) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].setRandomPos(canvas);
        }
    }

    void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, paint);
    }
}
