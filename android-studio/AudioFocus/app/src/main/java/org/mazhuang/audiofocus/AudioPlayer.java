package org.mazhuang.audiofocus;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by mazhuang on 2016/7/27.
 */
public class AudioPlayer {
    private MediaPlayer mPlayer;
    private boolean mPaused = false;

    public void stop() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
            mPaused = false;
        }
    }

    public void pause() {
        if (!mPaused && mPlayer != null) {
            mPlayer.pause();
            mPaused = true;
        }
    }

    public void resume() {
        if (mPaused && mPlayer != null) {
            mPaused = false;
            mPlayer.start();
        }
    }

    public void play(Context context, int audioResId) {
        stop();
        mPlayer = MediaPlayer.create(context, audioResId);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });
        mPlayer.start();
    }
}
