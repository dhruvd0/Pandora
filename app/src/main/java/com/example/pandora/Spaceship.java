package com.example.pandora;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;

public class Spaceship extends Sprite {
    Spaceship(Bitmap bmp) {
        super(bmp);
        isHooked = false;
        minCollideDistance = 200;
        xSpeed=5;
        ySpeed=-5;
        afterUnhookAngle=-1;
    }

    int minCollideDistance;
    Planet hookedPLanet;
    boolean isHooked;
    float afterUnhookAngle;

    double collisionDist(Sprite s2) { //returns the distance between space ship and obstacle
        double x = Math.pow(cx - s2.cx, 2);
        double y = Math.pow(cy - s2.cy, 2);
        return Math.sqrt(x + y);

    }

    @Override
    void move() {


        double angle = Math.toRadians(rotateAngle);

        this.x += this.xSpeed * Math.sin(angle);
        this.y += this.ySpeed * Math.cos(angle);
        cx = x + image.getWidth() / 2;
        cy = y + image.getHeight() / 2;
        hasMatrix = false;

    }
    void showHeading(Canvas canvas,Paint paint){

        double angle=Math.toRadians(180-rotateAngle);
        float endx= (float) (cx+300*Math.sin(angle));
        float endy= (float) (cy+300*Math.cos(angle));
        canvas.drawLine(cx,cy,endx,endy,paint);
        canvas.drawCircle(endx,endy,10,paint);
    }
    void unhook() {

        isHooked=false;
        double angle=Math.toRadians(180-rotateAngle);
        float endx= (float) (cx+50*Math.sin(angle));
        float endy= (float) (cy+50*Math.cos(angle));
        isHooked = false;
        afterUnhookAngle=rotateAngle;
        setPos(endx,endy);

    }

    void revolve() {

        if (!isHooked) {

            isHooked = true;
        }
        setPos(hookedPLanet.skyhook.cx, hookedPLanet.skyhook.cy);
        float angleOffset=hookedPLanet.skyhook.rotateAngle-rotateAngle+180;
        rotate((float) angleOffset);

    }


}
