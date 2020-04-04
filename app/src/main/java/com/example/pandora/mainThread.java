package com.example.pandora;

import android.view.Surface;
import android.view.SurfaceHolder;

public class mainThread extends Thread {
    SurfaceHolder surfaceHolder;//class that handles the surface functions
    gameView game;
    boolean isRunning;//current state of the thread
    mainThread(SurfaceHolder surfaceHolder,gameView game){
    super();
    this.surfaceHolder=surfaceHolder;
    this.game=game;
    }

    @Override
    public void run() {
        super.run();
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
}
