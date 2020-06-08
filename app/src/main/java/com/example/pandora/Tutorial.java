package com.example.pandora;

import android.content.Context;
import android.graphics.Canvas;

public class Tutorial extends gameView {
    Tutorial(Context context, GameActivity gameActivity) {
        super(context, gameActivity);

    }
    void drawSprites(Canvas canvas){
        Star.drawStars(stars,canvas);
        spaceship.draw(canvas);
    }
    public void update(Canvas canvas){
        if(spaceship.y<0){
            spaceship.y=canvasHeight;


        }

        else {
            spaceship.moveIncircle(1,200,canvas.getWidth()/2,canvas.getHeight()/2);
        }
        Star.moveRandomStars(stars);
    }
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawSprites(canvas);
      
    }
}
