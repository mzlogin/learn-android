package org.mazhuang.android.hellomoon;

import android.content.Context;
import android.media.MediaPlayer;

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
	
	public void play(Context c) {
		if (mPaused && mPlayer != null) {
			mPaused = false;
			mPlayer.start();
		} else {
			stop();
			mPlayer = MediaPlayer.create(c, R.raw.one_small_step);
			mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					stop();
				}
			});
			mPlayer.start();
		}
	}
}
