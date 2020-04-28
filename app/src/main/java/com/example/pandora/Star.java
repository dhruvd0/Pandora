package com.example.pandora;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Star {
    float x,y,z;
    Paint paint;
    Random rand;
    Star(){
        super();
        paint=new Paint();
        paint.setColor(Color.WHITE);
        rand=new Random();
        x=10+rand.nextInt(1000);
        y=10+rand.nextInt(1000);
    }
    void setPos(Canvas canvas){
        x=10+rand.nextInt(canvas.getWidth());
        y=10+rand.nextInt(canvas.getHeight());
    }
    static void drawStars(Star s[],Canvas canvas){
        for(int i=0;i<s.length;i++){
            s[i].draw(canvas);
        }
    }
    static void setStars(Star stars[],Canvas canvas){
        for (int i = 0; i < stars.length; i++) {
            stars[i].setPos(canvas);
        }
    }
    void draw(Canvas canvas){
        canvas.drawCircle(x,y,10,paint);
    }
}
