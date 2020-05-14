package com.example.pandora;

public class collisionThread extends Thread {
    gameView game;

    collisionThread(gameView game) {
        this.game = game;
    }

    boolean isCollidingWithPlanets() {
        for (Planet p : game.planets) {

            if (p.active) {
                if (game.spaceship.collisionDist(p) < 150) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void run() {
        if(isCollidingWithPlanets()){
            game.spaceship.ySpeed=0;
        }
    }
}
