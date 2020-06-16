package com.example.pandora;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

public class Tutorial extends gameView {
    int cycle = 0;
    boolean hookCheck;
    boolean endTutorial;
    int progress = 0;
    Obstacles tutorialMeteor;
    boolean isColliding = false;

    Tutorial(Context context, TutorialActivity gameActivity) {
        super(context, gameActivity);
        hookCheck = false;
        tutorialMeteor = new Obstacles(BitmapFactory.decodeResource(getResources(), R.drawable.meteor));

    }

    void drawSprites(Canvas canvas) {
        Star.drawStars(stars, canvas);
        spaceship.draw(canvas);
        if (cycle == 3) {
            if (!tutorialMeteor.active) {
                tutorialMeteor.setPos(canvasWidth / 2, 100);
                tutorialMeteor.rotateAngle = 180;

            }
            tutorialMeteor.draw(canvas);
        }
    }

    void centerMessage(String text, Canvas canvas) {
        displayText(canvas, text, canvasWidth / 2, canvasHeight / 2);
    }

    public void update(Canvas canvas) {
        if (cycle > 1) {
            spaceship.ySpeed = -5;
            spaceship.xSpeed = 5;
        }
        if (touchDown) {
            if (cycle == 1) {
                displayText(canvas, "Braking depletes ENERGY", canvasWidth / 2, 300);
                if (spaceship.ySpeed >= -5 || spaceship.xSpeed <= 5) {
                    spaceship.ySpeed = -5;
                    spaceship.xSpeed = 5;
                    progress += 5;
                } else {
                    spaceship.ySpeed += 0.1;
                    spaceship.xSpeed += 0.1;
                    progress += -1 * (spaceship.ySpeed + 5);
                    spaceship.energy -= 0.3;


                }
            }


        }
        if (spaceship.y < 0) {

            spaceship.y = canvasHeight;
            if (cycle == 1 && spaceship.ySpeed < -5) {
             /*   spaceship.ySpeed = -10;
                spaceship.xSpeed = 10;*/
            } else if (cycle == 2 && !hookCheck) {
                /*spaceship.ySpeed = -10;
                spaceship.xSpeed = 10;*/
            } else {
                cycle++;
                if (cycle >= 4) {
                    isPlaying = false;
                }
                progress = 0;
            }
            Star.setStars(stars, canvas);
        } else {
            switch (cycle) {


                case 2:

                    if (spaceship.isHooked) {//handles collision with a planet
                        spaceship.revolve();
                        if (progress >= 360) {
                            progress = 360;
                        } else {
                            progress += 5;
                        }
                    } else {
                        spaceshipNearPlanet = planetCollision();
                        spaceship.move();

                        spaceship.rotateAngle = 0;
                        if (spaceship.collisionDist(spaceship.hookedPlanet) > spaceship.minCollideDistance) {
                            spaceship.image = spaceshipImg;
                        }

                    }
                    break;
                case 3:
                    if (spaceship.x != canvasWidth / 2) {
                        spaceship.x = canvasWidth / 2;
                    }
                    if (spaceship.collisionDist(tutorialMeteor) <= 100) {
                        isColliding = true;

                        progress += 50;
                        spaceship.health -= 2;
                        if (spaceship.health < 0) {
                            spaceship.health = 0;
                        }
                    }

                    spaceship.move();
                    break;
                default:
                    spaceship.move();
            }
        }
        Star.moveRandomStars(stars);
    }

    void progressCircle(int change, Canvas canvas) {
        int y = (int) canvasHeight - 200;
        int x = (int) canvasWidth / 2;
        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        Paint p = new Paint();
        if (progress >= 355) {
            p.setColor(Color.GREEN);
        } else {
            p.setColor(Color.WHITE);
        }
        if (cycle == 3) {
            p.setColor(Color.RED);
        }
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
        progress += change;

        RectF r = new RectF(spaceship.cx - 50, spaceship.cy - 50, spaceship.cx + 50, spaceship.cy + 50);
        // canvas.drawRect(r, circlePaint);
        canvas.drawArc(r, 0, progress, false, p);
    }

    public void draw(Canvas canvas) {

        super.draw(canvas);
        drawSprites(canvas);

        switch (cycle) {
            case 0:
                progressCircle(3, canvas);
                centerMessage("Initialising and testing Ship Systems", canvas);

                break;
            case 1:
                progressCircle(0, canvas);
                centerMessage("Hold down to apply brakes", canvas);

                break;
            case 2:
                Log.i("log", "" + spaceship.rotateAngle);
                planets[0].setPos(canvasWidth / 2, canvasHeight - 700);
                planets[0].draw(canvas);
                progressCircle(0, canvas);
                displayText(canvas, "Ship Glows Green When near a Planet", canvasWidth / 2, 200);
                if (progress == 360) {
                    displayText(canvas, "Tap Again to Unhook", canvasWidth / 2, 400);
                }
                displayText(canvas, "Tap To Hook to planet and gain Energy", canvasWidth / 2, 300);

                break;
            case 3:
                progressCircle(0, canvas);
                if (isColliding) {
                    displayText(canvas, "Upon collision they will DEPLETE ship HEALTH", canvasWidth / 2, 300);
                }
                displayText(canvas, "Watch Out for METEORS", canvasWidth / 2, 200);
                tutorialMeteor.move();
                break;
            case 4:
                isPlaying = false;
                endTutorial = true;
                break;


        }
    }

    @Override
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
