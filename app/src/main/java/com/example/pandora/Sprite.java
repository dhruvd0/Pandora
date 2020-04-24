package com.example.pandora;

import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;


public class Sprite {
    public Bitmap image;
    int x, y;
    int rotation;

    public Sprite() {

    }
      Matrix matrix ;
    public Sprite(Bitmap bmp) {
        image = bmp;
        rotation = 0;
        matrix=new Matrix();

    }

    public void move(int changeInX, int changeInY) {

        this.x+=changeInX;
        this.y+=changeInY;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }



    public void rotate(float angle) {
        rotation+=angle;
        this.matrix=new Matrix();
        this.matrix.reset();
        this.matrix.setTranslate(x,y);
        this.matrix.postRotate((float)rotation, x+(image.getWidth()/2),y+(image.getHeight()/2));

    }

    public void draw(Canvas canvas) {

       // this.matrix.setTranslate(x,y);
        canvas.drawBitmap(image,matrix,null);
    }
    void draw(Canvas canvas,Boolean xy){
        canvas.drawBitmap(image,x,y,null);
    }

}