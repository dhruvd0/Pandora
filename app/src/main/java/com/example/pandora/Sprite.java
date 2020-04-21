package com.example.pandora;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
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
        if(this.y==1){
            this.y=0;
        }


    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void rotate(Canvas canvas, double deg) {

    }

    public void rotate(float angle) {

        Bitmap rotateBitmap = Bitmap.createBitmap(image.getWidth(),
                image.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(rotateBitmap);
        Matrix matrix = new Matrix();
        matrix.postRotate(angle, image.getWidth() / 2, image.getHeight() / 2);
        canvas.drawBitmap(image, matrix, new Paint());
        image = rotateBitmap;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

}