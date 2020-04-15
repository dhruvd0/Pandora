package com.example.pandora;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

public class Sprite {
    public Bitmap image;
    int x, y;
    int rotation;

    public Sprite() {

    }
    static  Matrix matrix ;
    public Sprite(Bitmap bmp) {
        image = bmp;
        rotation = 0;
        matrix=new Matrix();

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

    public void rotate(Canvas canvas, double deg) {
        this.rotation += deg;
        Bitmap newImg;

        matrix.setRotate(rotation,image.getWidth(),image.getHeight());
        newImg = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        image=newImg;
      //  this.draw(canvas);
    }

    public void draw(Canvas canvas) {


        canvas.drawBitmap(image, x, y, null);
    }
}