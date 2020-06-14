package com.example.pandora;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

 class Obstacles extends Sprite {

    private Random rand;
    float initX,initY;
      Obstacles(Bitmap img){
        super(img);
        rand=new Random();
        xSpeed=10;
        ySpeed=-10;
        active=false;
       setRandomPos();

    }


    private void setRandomPos(){
        if(rand.nextBoolean()){
            x=0;
            rotateAngle=40+rand.nextInt(70);
        }
        else {
            x=1024;
            rotateAngle=250+rand.nextInt(100);

        }



        y=300+rand.nextInt(900);
    }
    static void update(Obstacles[] obstacles){
        for(Obstacles obstacle:obstacles){
            obstacle.move();
        }
    }
    static void drawObstacles(Obstacles[] obstacles, Canvas canvas){
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
