package com.example.pandora;

import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;


public class Sprite {
    public Bitmap image;
    float x, y;

    int rotation;
    float angle;
    public Sprite() {

    }
      Matrix matrix ;
    public Sprite(Bitmap bmp) {
        image = bmp;
        rotation = 0;
        matrix=new Matrix();


    }

    public void move(int changeInX, int changeInY) {

        float newx=this.x+changeInX;
        float newy=this.y+changeInY;
        setPos(newx,newy);
    }
    void moveIncircle(float angle,float radius,float cx,float cy){
        this.angle+=angle;
        if(this.angle>=360){
            this.angle=0;
        }
        float newX= (float) (cx-radius*Math.cos(this.angle*3.14/180));
        float newY= (float) (cy-radius*Math.sin(this.angle*3.14/180));
        setPos(newX,newY);
    }
    public void setPos(float x, float y) {
        this.x = x-image.getWidth()/2;
        this.y = y-image.getHeight()/2;
    }



    public void rotate(float angle) {
        rotation+=angle;
        this.matrix=new Matrix();
        this.matrix.reset();
        this.matrix.setTranslate(x,y);
        this.matrix.postRotate((float)rotation, x+(image.getWidth()/2),y+(image.getHeight()/2));

    }

    public void draw(Canvas canvas,Boolean hasMatrix) {

       // this.matrix.setTranslate(x,y);
        canvas.drawBitmap(image,matrix,null);
    }
    void draw(Canvas canvas){
        canvas.drawBitmap(image,x,y,null);

    }

}