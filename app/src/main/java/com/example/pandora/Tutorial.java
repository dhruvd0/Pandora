package com.example.pandora;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class Tutorial extends gameView {
    int cycle = 0;
    boolean hookCheck;
    boolean endTutorial;
    Tutorial(Context context, TutorialActivity gameActivity) {
        super(context, gameActivity);
        hookCheck = false;
    }

    void drawSprites(Canvas canvas) {
        Star.drawStars(stars, canvas);
        spaceship.draw(canvas);
    }

    void centerMessage(String text, Canvas canvas) {
        displayText(canvas, text, canvasWidth / 2, canvasHeight / 2);
    }

    public void update(Canvas canvas) {
        if (touchDown) {
            if (cycle == 1) {
                if (spaceship.ySpeed >= -5 || spaceship.xSpeed <= 5) {
                    spaceship.ySpeed = -5;
                    spaceship.xSpeed = 5;
                } else {
                    spaceship.ySpeed += 0.1;
                    spaceship.xSpeed += 0.1;
                }
            } else {
                if (spaceship.ySpeed >= 0 || spaceship.xSpeed <= 0) {
                    spaceship.ySpeed = 0;
                    spaceship.xSpeed = 0;
                } else {
                    spaceship.ySpeed += 0.1;
                    spaceship.xSpeed += 0.1;
                }
            }


        }
        if (spaceship.y < 0) {
            spaceship.y = canvasHeight;
            if (cycle == 1 && spaceship.ySpeed < -5) {
                spaceship.ySpeed = -10;
                spaceship.xSpeed = 10;
            } else if (cycle == 2 && !hookCheck) {
                spaceship.ySpeed = -10;
                spaceship.xSpeed = 10;
            } else {
                cycle++;
            }
            Star.setStars(stars, canvas);
        } else {
            switch (cycle) {
                case 2:
                    spaceship.ySpeed = -10;
                    spaceship.xSpeed = 10;
                    if (spaceship.isHooked) {//handles collision with a planet
                        spaceship.revolve();
                    } else {
                        spaceshipNearPlanet = planetCollision();
                        spaceship.move();

                        if (spaceship.afterUnhookAngle != -1) {
                            spaceship.rotate(spaceship.rotateAngle - spaceship.afterUnhookAngle);
                        }
                        if (spaceship.collisionDist(spaceship.hookedPlanet) > spaceship.minCollideDistance) {
                            spaceship.image = spaceshipImg;
                        }

                    }
                    break;
                default:
                    spaceship.move();
            }
        }
        Star.moveRandomStars(stars);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawSprites(canvas);
        switch (cycle) {
            case 0:
                centerMessage("Initialising and testing Ship Systems", canvas);
                break;
            case 1:
                centerMessage("Hold down to apply brakes", canvas);
                break;
            case 2:
                planets[0].setPos(canvasWidth / 2, canvasHeight - 500);
                planets[0].draw(canvas);
                displayText(canvas, "Ship Glows Green When near a Planet", canvasWidth / 2, 200);
                displayText(canvas, "Tap To Hook to planet and gain Energy", canvasWidth / 2, 300);
                displayText(canvas, "Tap Again to Unhook", canvasWidth / 2, 400);
                break;
            case 4:
                isPlaying = false;
                endTutorial = true;
                break;


        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int e = event.getAction();
        if (e == MotionEvent.ACTION_DOWN) {

            if (cycle != 0) {
                touchDown = true;
            }

        } else if (e == MotionEvent.ACTION_UP) {
            touchDown = false;
            if (cycle == 2) {
                if (spaceshipNearPlanet) {

                    spaceship.isHooked = true;
                    spaceship.image = spaceshipImg;
                    spaceshipNearPlanet = false;
                    hookCheck = true;

                } else if (spaceship.isHooked) {

                    spaceship.hookedPlanet = null;
                    spaceship.unhook();
                    spaceshipNearPlanet = false;
                }
            }


        }


        return true;
    }
}
