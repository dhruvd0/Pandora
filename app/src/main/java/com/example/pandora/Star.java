package com.example.pandora;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Star {
    float x, y, z;
    Paint paint;
    static Random rand;
    static int movingStars[];
    static int randomIndex;

    static void selectRandomStars() {
        movingStars = new int[10];
        for (int i = 0; i < movingStars.length; i++) {
            movingStars[i] = rand.nextInt(300);
        }
    }

    Star() {


        paint = new Paint();
        paint.setColor(Color.WHITE);
        rand = new Random();
        x = rand.nextInt(500);
        y = rand.nextInt(500);
    }

    static void moveRandomStars(Star stars[]) {
        for (int i = 0; i < movingStars.length; i++) {
            randomIndex = movingStars[i];
            float dy = 20 + rand.nextInt(10);
            float dx = 1 + rand.nextInt(5);
            if (randomIndex < stars.length) {
                stars[randomIndex].y += dy;
                //stars[randomIndex].x += dx;
            }
        }
    }

    void setRandomPos(Canvas canvas) {
        x = 10 + rand.nextInt(canvas.getWidth());
        y = 10 + rand.nextInt(5000);
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
        canvas.drawCircle(x, y, 5, paint);
    }
}
