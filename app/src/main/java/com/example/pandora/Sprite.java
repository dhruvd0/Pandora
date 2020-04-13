package com.example.pandora;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class Sprite {
    private Bitmap image;
    int x, y;
    int rotation;

    public Sprite(Bitmap bmp) {
        image = bmp;
        rotation = 0;


    }

    public void move(int changeInX, int changeInY) {
        int newX = this.x + changeInX;
        int newY = this.y + changeInY;
        if (newX >= 0 && newY >= 0) {
            this.x = newX;
            this.y = newY;
        }


    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void rotate(Canvas canvas, int deg) {
        this.rotation += deg;
        canvas.rotate(rotation, this.x, this.y);
        canvas.drawBitmap(image, x, y, null);

    }

    public void draw(Canvas canvas) {
        Log.i("print", "spaceship");

        canvas.drawBitmap(image, x, y, null);
    }
}