package com.example.pandora;

import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;


public class Sprite {
    Bitmap image;
    float x, y;
    boolean hasMatrix;
    float rotateAngle;
    float circleAngle;
    float ySpeed;
    float xSpeed;
    Paint paint;
    float cx, cy;//center of sprite
    double collisionDistance;
    boolean active;//true if sprite is drawn on canvas
    int imgWidth,imgHeight;
    Matrix matrix;

    public Sprite(Bitmap bmp) {
        x = 0;
        y = 0;
        hasMatrix = false;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(Color.GREEN);
        active = false;
        rotateAngle = 0;
        paint.setStyle(Paint.Style.STROKE);
        matrix = new Matrix();
        image = bmp;
        imgWidth=image.getWidth();
        imgHeight=image.getHeight();

    }

    void move() {

        this.x += this.xSpeed;
        this.y += this.ySpeed;
        cx = x + image.getWidth() / 2;
        cy = y + image.getHeight() / 2;
        this.hasMatrix = false;

    }


    void revolve(Sprite planet) {
        moveIncircle(1, planet.image.getWidth() / 2 - 50, planet.x + planet.image.getWidth() / 2, planet.y + planet.image.getHeight() / 2);
    }

     void moveIncircle(float angle, float radius, float cx, float cy) {


        this.circleAngle += angle;
        float angleOffSet = (float) (this.circleAngle * 3.14 / 180);
        float newX = (float) (cx + radius * Math.cos(angleOffSet));
        float newY = (float) (cy + radius * Math.sin(angleOffSet));

        setPos(newX, newY);
    }

    void setPos(float x, float y) {
        cx = x;
        cy = y;
        this.x = (float) (x - image.getWidth() / 2);
        this.y = (float) (y - image.getHeight() / 2);


    }

    void rotate(float angle) {

        rotateAngle += angle;
        matrix = new Matrix();
        matrix.reset();
        matrix.setTranslate(x, y);
        matrix.postRotate(rotateAngle, x + (image.getWidth() / 2), y + (image.getHeight() / 2));
        hasMatrix = true;
   /*     float offsetX = image.getWidth() / 2;
       //        float offsetY = image.getHeight() / 2;
       //     //   this.x += offsetX;
       //       // this.y += offsetY;*/


    }


    public void draw(Canvas canvas) {

        active = true;
        if (hasMatrix) {
            canvas.drawBitmap(image, matrix, null);
        } else {
            canvas.drawBitmap(image, this.x, this.y, null);

        }
    }

}