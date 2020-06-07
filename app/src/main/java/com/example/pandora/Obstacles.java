package com.example.pandora;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Obstacles extends Sprite {

    Random rand;
    boolean rot;
    public  Obstacles(Bitmap img){
        super(img);
        rand=new Random();
        xSpeed=10;
        ySpeed=-10;

       setRandomPos();

    }
    void setRandomPos(){
        if(rand.nextBoolean()){
            x=0;
            rotateAngle=1+rand.nextInt(179);
        }
        else {
            x=1024;
            rotateAngle=180+rand.nextInt(179);

        }



        y=100+rand.nextInt(700);
    }
    static void update(Obstacles obstacles[]){
        for(Obstacles obstacle:obstacles){
            obstacle.move();
        }
    }
    static void drawObstacles(Obstacles obstacles[],Canvas canvas){
        for(Obstacles obstacle:obstacles){
            obstacle.draw(canvas);
        }
    }
    @Override
    void move() {

        if(x<0||x>1500||y<0||y>1500){
            setRandomPos();
        }
        double angle = Math.toRadians(rotateAngle);
        this.x += this.xSpeed * Math.sin(angle);
        this.y += this.ySpeed * Math.cos(angle);
        cx = x + (imgWidth / 2);
        cy = y + (imgHeight / 2);
        hasMatrix = false;
        float temp=rotateAngle;
        rotateAngle=0;
        rotate(temp);
        rotateAngle=temp;




    }


}
