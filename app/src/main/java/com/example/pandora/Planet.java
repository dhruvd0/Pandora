package com.example.pandora;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;



public class Planet extends Sprite {
    Sprite skyhook;

    Planet(){

    }
    public static void loadPlanets(Planet[] planets, Resources r) {

        int id = R.drawable.planet1;
        Random rand=new Random();
        for (int i = 0; i < planets.length; i++) {
            planets[i] = new Planet(BitmapFactory.decodeResource(r, id));
            planets[i].skyhook=new Sprite(BitmapFactory.decodeResource(r,R.drawable.skyhook));
            id++;
            float x = rand.nextInt(400) + 100;
            float posY;
            try {
                posY = rand.nextInt(500) + 2*planets[i - 1].y + 300;
            } catch (ArrayIndexOutOfBoundsException e) {
                posY = rand.nextInt(300)+100;
            }

            planets[i].setPos(x, posY);

        }
    }
    public void drawSkyHook(Canvas canvas){


        skyhook.draw(canvas);
        skyhook.setRotateAngle(skyhook.circleAngle);
        skyhook.revolve(this);


        Log.i("rotate:",Float.toString( skyhook.rotateAngle));
        Log.i("circle:",Float.toString(skyhook.circleAngle));
      /*  Paint  paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(Color.GREEN);
        canvas.drawCircle(x+image.getWidth()/2,y+image.getHeight()/2,20,paint);*/

    }
    public void draw(Canvas canvas){
        super.draw(canvas);
        drawSkyHook(canvas);
    }
    public static void drawPlanets(Planet[] planets, Canvas canvas) {
        for (Planet p : planets) {

             p.rotate(1);
            p.draw(canvas);
            p.drawSkyHook(canvas);
        }
    }

    Planet(Bitmap bmp) {
        image = bmp;
    }

}
