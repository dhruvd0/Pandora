package com.example.pandora;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Planet extends Sprite {
    int id;
    Planet(Bitmap bmp) {
        image = bmp;
    }

    public void draw(Canvas canvas) {


        canvas.drawBitmap(image, x, y, null);
    }
}
