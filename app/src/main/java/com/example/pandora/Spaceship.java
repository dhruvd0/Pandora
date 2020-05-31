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
        minCollideDistance=300;
    }
    int minCollideDistance;
    Planet hookedPLanet;
    boolean isHooked;

    double collisionDist(Sprite s2) { //returns the distance between space ship and obstacle
        double x = Math.pow(cx - s2.cx, 2);
        double y = Math.pow(cy - s2.cy, 2);
        return Math.sqrt(x + y);

    }

    void unhook() {
        isHooked = false;
        setPos(hookedPLanet.cx, hookedPLanet.cy - minCollideDistance-100);
    }

    void revolve() {

        if (!isHooked) {

            isHooked = true;
        }
        setPos(hookedPLanet.skyhook.cx, hookedPLanet.skyhook.cy);
        setRotateAngle(hookedPLanet.skyhook.rotateAngle+180);

    }


}
