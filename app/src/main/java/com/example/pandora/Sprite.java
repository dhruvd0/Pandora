package com.example.pandora;

import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;


public class Sprite {
    Bitmap image;
    float x, y;
    boolean hasMatrix;
    int rotation;
    float angle;

    public Sprite() {
        x = 0;
        y = 0;
        hasMatrix = false;
    }

    Matrix matrix;

    public Sprite(Bitmap bmp) {
        image = bmp;
        rotation = 0;
        matrix = new Matrix();


    }

    public void move(int changeInX, int changeInY) {

        this.x += changeInX;
        this.y += changeInY;
        this.hasMatrix = false;

    }

    void moveIncircle(float angle, float radius, float cx, float cy) {
        this.angle += angle;
        if (this.angle >= 360) {
            this.angle = 0;
        }
        float angleOffSet= (float) (this.angle * 3.14 / 180);
        float newX = (float) (cx + radius * Math.cos(angleOffSet));
        float newY = (float) (cy + radius * Math.sin(angleOffSet));

        setPos(newX, newY);
    }

    public void setPos(float x, float y) {
        this.x = x - image.getWidth() / 2;
        this.y = y - image.getHeight() / 2;
        //  Log.i("Planet y",Integer.toString((int) y));

    }

    public void setPos(float x) {
        this.x = x - image.getWidth() / 2;

    }


    public void rotate(float angle) {
        rotation += angle;
        this.matrix = new Matrix();
        this.matrix.reset();
        this.matrix.setTranslate(x, y);
        this.matrix.postRotate((float) rotation, x + (image.getWidth() / 2), y + (image.getHeight() / 2));
        this.hasMatrix = true;
        float offsetX = image.getWidth() / 2;
        float offsetY = image.getHeight() / 2;
        this.x += offsetX;
        this.y += offsetY;


    }

    void setRotation(float angle) {
        rotation = (int) angle;
        this.matrix = new Matrix();
        this.matrix.reset();
        this.matrix.setTranslate(x, y);
        this.matrix.postRotate((float) rotation, x + (image.getWidth() / 2), y + (image.getHeight() / 2));
        this.hasMatrix = true;
        float offsetX = image.getWidth() / 2;
        float offsetY = image.getHeight() / 2;
        this.x += offsetX;
        this.y += offsetY;
    }

    public void draw(Canvas canvas) {


        if (this.hasMatrix) {
            canvas.drawBitmap(image, matrix, null);
        } else {
            canvas.drawBitmap(image, this.x, this.y, null);
        }
    }

}