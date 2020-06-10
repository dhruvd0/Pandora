package com.example.pandora;

import android.content.Context;
import android.media.MediaPlayer;

public class soundThread extends Thread {
    MediaPlayer mp3;
    int mp3Id;
    soundThread(Context context, int mp3Id){
        this.mp3Id=mp3Id;
        mp3=MediaPlayer.create(context,mp3Id);
    }
    void stopPlaying(){
        mp3.stop();
    }
    void starPlaying(){
        start();
    }
    public void run(){
        mp3.start();
    }

}
