package com.example.pandora;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class playerSprite {
    private Bitmap image;

    public playerSprite(Bitmap bmp) {
        image = bmp;
    }

    public void draw(Canvas canvas) {
        Log.i("print","spaceship");
        canvas.drawBitmap(image, 100, 100, null);
    }
}