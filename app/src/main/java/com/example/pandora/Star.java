package com.example.pandora;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

class Star {
    float x, y;
    private Paint paint;
    private static Random rand;
    private static int[] movingStars;
    private float radius;

    static void selectRandomStars(Star[] s) {
        movingStars = new int[50];
        for (int i = 0; i < movingStars.length; i++) {
            movingStars[i] = rand.nextInt(s.length);
        }
    }

    Star(int cW, int cH) {


        paint = new Paint();
        paint.setColor(Color.WHITE);

        rand = new Random();
        x = rand.nextInt(cW);
        y = rand.nextInt(cH);
        radius = 1 + rand.nextInt(5);
    }

    static void moveRandomStars(Star[] stars) {

        for (int i = 0; i < movingStars.length; i++) {
            int randomIndex = movingStars[i];
            float dy = 20 + rand.nextInt(10);
            double b = Math.pow(-1, rand.nextInt(1));
            float dx = (float) (b * rand.nextInt(5));
            if (randomIndex < stars.length) {
                stars[randomIndex].y += dy;
                stars[randomIndex].x += dx;
            }
        }
    }

    private void setRandomPos(Canvas canvas) {
        try {
            x = 10 + rand.nextInt(canvas.getWidth());
            y = 10 + rand.nextInt(5000);
        } catch (Exception ignored) {

        }
    }

    static void drawStars(Star[] s, Canvas canvas) {
        Star.moveRandomStars(s);
        for (int i = 0; i < s.length; i++) {

            s[i].draw(canvas);
        }
    }

    static void setStars(Star[] stars, Canvas canvas) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].setRandomPos(canvas);
        }
    }

    void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, paint);
    }
}
