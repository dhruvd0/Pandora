package com.example.pandora;

import android.content.Context;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class gameView extends SurfaceView implements SurfaceHolder.Callback {
        //basic surface class where we would create canvas and draw
    public gameView(Context context) {
        super(context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
