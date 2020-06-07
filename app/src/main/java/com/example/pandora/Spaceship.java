package com.example.pandora;

import android.graphics.Bitmap;
import android.graphics.Canvas;


class Spaceship extends Sprite {
    Spaceship(Bitmap bmp) {
        super(bmp);
        isHooked = false;
        minCollideDistance = 200;
        xSpeed = 10;
        ySpeed = -10;
        afterUnhookAngle = -1;
        hookedPlanet = null;
        health=100;
        energy=100;
        rotateAngle=0;
    }
    float health,energy;
    int minCollideDistance;
    Planet hookedPlanet;
    boolean isHooked;
    float afterUnhookAngle;

    double collisionDist(Sprite s2) { //returns the distance between space ship and obstacle
        double x = Math.pow(cx - s2.cx, 2);
        double y = Math.pow(cy - s2.cy, 2);
        return Math.sqrt(x + y);

    }
    boolean reachedBounds(Canvas canvas){
        if (!isHooked) {
            if (y<0) {
                y=canvas.getHeight();
                return true;

            }
            if (x < 0) {
                x=canvas.getWidth();


            }
            else if(x>canvas.getWidth()){
                x=0;
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

        if(energy<0){
            energy=0;

        }
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
        hookedPlanet = null;

    }

    void revolve() {

        energy+=0.1;
        if(energy>100){
            energy=100;
        }
        if (!isHooked) {

            isHooked = true;
        }
        setPos(hookedPlanet.skyhook.cx, hookedPlanet.skyhook.cy);
        float angleOffset = hookedPlanet.skyhook.rotateAngle - rotateAngle + 180;
        rotate(angleOffset);

    }


}
