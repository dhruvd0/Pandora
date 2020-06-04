package com.example.pandora;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


class Spaceship extends Sprite {
    Spaceship(Bitmap bmp) {
        super(bmp);
        isHooked = false;
        minCollideDistance = 200;
        xSpeed = 5;
        ySpeed = -5;
        afterUnhookAngle = -1;
        hookedPLanet = null;
        health=100;
    }
    int health;
    int minCollideDistance;
    Planet hookedPLanet;
    boolean isHooked;
    float afterUnhookAngle;

    double collisionDist(Sprite s2) { //returns the distance between space ship and obstacle
        double x = Math.pow(cx - s2.cx, 2);
        double y = Math.pow(cy - s2.cy, 2);
        return Math.sqrt(x + y);

    }

    boolean reachedXBound(Canvas canvas) {
        if (!isHooked) {
            if (x < 0 || x > canvas.getWidth()) {
                health=0;
                return true;

            }

        }
        return false;
    }
    boolean reachedYBound(Canvas canvas){
        if (!isHooked) {
            if (y<0) {
                y=canvas.getHeight();
                return true;

            }
            else if(y>canvas.getHeight()){
                health=0;
            }

        }
        return false;
    }

    @Override
    void move() {


        double angle = Math.toRadians(rotateAngle);

        this.x += this.xSpeed * Math.sin(angle);
        this.y += this.ySpeed * Math.cos(angle);
        cx = x + (imgWidth / 2);
        cy = y + (imgHeight / 2);
        hasMatrix = false;

    }

    void unhook() {

        isHooked = false;
        double angle = Math.toRadians(180 - rotateAngle);
        float endx = (float) (cx + 50 * Math.sin(angle));
        float endy = (float) (cy + 50 * Math.cos(angle));
        isHooked = false;
        afterUnhookAngle = rotateAngle;
        setPos(endx, endy);
        hookedPLanet = null;

    }

    void revolve() {

        if (!isHooked) {

            isHooked = true;
        }
        setPos(hookedPLanet.skyhook.cx, hookedPLanet.skyhook.cy);
        float angleOffset = hookedPLanet.skyhook.rotateAngle - rotateAngle + 180;
        rotate(angleOffset);

    }


}
