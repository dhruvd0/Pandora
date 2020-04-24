package com.example.pandora;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


@SuppressLint("AppCompatCustomView")
public class CustomBitmap extends ImageView {
    Bitmap image;
    int x, y;
    int rotation;
    static Matrix matrix;
    public CustomBitmap(Context context) {
        super(context);
    }

    public CustomBitmap(Bitmap bmp, Context context) {
        super(context);
        this.image = bmp;
        this.setImageBitmap(image);
        matrix=new Matrix();

    }

    public void rotate(float angle) {

        Bitmap rotateBitmap = Bitmap.createBitmap(image.getWidth(),
                image.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(rotateBitmap);
        Matrix matrix = new Matrix();
        matrix.postRotate(angle, this.getWidth() / 2, this.getHeight() / 2);

        image = rotateBitmap;

        try (FileOutputStream out = new FileOutputStream("rotated.png")) {
            rotateBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            Log.i("print","saving img");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void move(int changeInX, int changeInY) {
        int newX = this.x + changeInX;
        int newY = this.y + changeInY;

        this.x = newX;
        this.y = newY;

    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }


    void drawCustomBitmap(Canvas canvas) {

        canvas.drawBitmap(image, x, y, null);

    }
}
