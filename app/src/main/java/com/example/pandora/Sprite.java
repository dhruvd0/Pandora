package com.example.pandora;

import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;


public class Sprite {
    Bitmap image;
    float x, y;
    private boolean hasMatrix;
    private float rotateAngle;
    float circleAngle;
    float ySpeed;
    private float xSpeed;
    Paint paint;
    float cx,cy;//center of sprite
    double collisionDistance;
    boolean active;//true if sprite is drawn on canvas
    public Sprite() {
        x = 0;
        y = 0;
        hasMatrix = false;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(Color.GREEN);
        active=false;
    }

    private Matrix matrix;

    public Sprite(Bitmap bmp) {
        image = bmp;
        rotateAngle = 0;
        matrix = new Matrix();
        ySpeed = -5;

    }

    void move() {

        this.x += this.xSpeed;
        this.y += this.ySpeed;
        cx=x+image.getWidth()/2;

        cy=y+image.getHeight()/2;
        this.hasMatrix = false;

    }
    double collisionDist(Sprite s2){
        double x=Math.pow(cx-s2.cx,2);
        double y=Math.pow(cy-s2.cy,2);
        double distance=Math.sqrt(x+y);
        return distance;

    }

    void revolve(Sprite planet) {
        moveIncircle(1, planet.image.getWidth() / 2 - 50, planet.x + planet.image.getWidth() / 2, planet.y + planet.image.getHeight() / 2);
    }

    private void moveIncircle(float angle, float radius, float cx, float cy) {
        this.circleAngle += angle;

        if (this.circleAngle >= 360) {
            this.circleAngle = 0;
        }
        float angleOffSet = (float) (this.circleAngle * 3.14 / 180);
        float newX = (float) (cx + radius * Math.cos(angleOffSet));
        float newY = (float) (cy + radius * Math.sin(angleOffSet));

        setPos(newX, newY);
    }

    void setPos(float x, float y) {
        cx=x;
        cy=y;
        this.x = x - image.getWidth() / 2;
        this.y = y - image.getHeight() / 2;


    }

    void rotate(float angle) {
        rotateAngle += angle;
        if (this.rotateAngle >= 360) {
            this.rotateAngle = 0;
        }
        this.matrix = new Matrix();
        this.matrix.reset();
        this.matrix.setTranslate(x, y);
        this.matrix.postRotate(rotateAngle, x + (image.getWidth() / 2), y + (image.getHeight() / 2));
        this.hasMatrix = true;
   /*     float offsetX = image.getWidth() / 2;
        float offsetY = image.getHeight() / 2;
     //   this.x += offsetX;
       // this.y += offsetY;*/


    }

    void setRotateAngle(float angle) {
        rotateAngle = (int) angle;
        this.matrix = new Matrix();
        this.matrix.reset();
        this.matrix.setTranslate(x, y);
        this.matrix.postRotate(rotateAngle, x + (image.getWidth() / 2), y + (image.getHeight() / 2));
        this.hasMatrix = true;
        float offsetX = image.getWidth() / 2;
        float offsetY = image.getHeight() / 2;
        this.x += offsetX;
        this.y += offsetY;
    }

    public void draw(Canvas canvas) {

        active=true;
        if (this.hasMatrix) {


            canvas.drawBitmap(image, matrix, null);
        } else {

            canvas.drawBitmap(image, this.x, this.y, null);

        }
    }

}