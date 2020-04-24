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



    public void rotate(float angle) {
        rotation+=angle;
        this.matrix.reset();
        this.matrix.setTranslate(x,y);
        this.matrix.postRotate((float)rotation, x+(image.getWidth()/2),y+(image.getHeight()/2));

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image,matrix,null);
    }

}