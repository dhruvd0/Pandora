package com.example.pandora;

import android.content.Context;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class gameView extends SurfaceView implements SurfaceHolder.Callback {
        //basic surface class where we would create canvas and draw
        //
        mainThread thread;//start a thread when the surface is created;
    public gameView(Context context) {
        super(context);
        thread=new mainThread(getHolder(),this);
        setFocusable(true);
    }
    public void update() {

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        thread.setRunning(true);
        thread.start();//start the thread
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
            thread.setRunning(false);//stops the thread when the surface is destroyed
    }
}
