package com.example.pandora;

import android.graphics.Bitmap;

import android.graphics.Canvas;

import android.graphics.Matrix;


public class Sprite {
    Bitmap image;
    float x, y;
    boolean hasMatrix;
    float rotateAngle;
    float circleAngle;
    float ySpeed;
    float xSpeed;
    float cx, cy;//center of sprite
    boolean active;//true if sprite is drawn on canvas
    int imgWidth, imgHeight;
    private Matrix matrix;

    Sprite(Bitmap bmp) {
        x = 0;
        y = 0;
        hasMatrix = false;
        active = false;
        rotateAngle = 0;
        matrix = new Matrix();
        image = bmp;
        imgWidth = image.getWidth();
        imgHeight = image.getHeight();

    }

    void move() {

        this.x += this.xSpeed;
        this.y += this.ySpeed;
        cx = x + image.getWidth() / 2;
        cy = y + image.getHeight() / 2;
        this.hasMatrix = false;

    }

    void revolve(Sprite planet) {
        moveInCircle(planet.image.getWidth() / 2 - 50, planet.x + planet.image.getWidth() / 2, planet.y + planet.image.getHeight() / 2);
    }

    void moveInCircle(float radius, float cx, float cy) {


        this.circleAngle += (float) 1;
        float angleOffSet = (float) (this.circleAngle * 3.14 / 180);
        float newX = (float) (cx + radius * Math.cos(angleOffSet));
        float newY = (float) (cy + radius * Math.sin(angleOffSet));

        setPos(newX, newY);
    }

    void setPos(float x, float y) {
        cx = x;
        cy = y;
        this.x = x - image.getWidth() / 2;
        this.y = y - image.getHeight() / 2;


    }

    void rotate(float angle) {

        rotateAngle += angle;
        matrix = new Matrix();
        matrix.reset();
        matrix.setTranslate(x, y);
        matrix.postRotate(rotateAngle, x + (image.getWidth() / 2), y + (image.getHeight() / 2));
        hasMatrix = true;
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