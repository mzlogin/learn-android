package org.mazhuang.android.hellomoon;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class HelloMoonFragment extends Fragment {
	
	private Button mPlayButton;
	private Button mStopButton;
	private Button mPauseButton;
	private Button mPlayVideoButton;
	private AudioPlayer mPlayer = new AudioPlayer();
	
	private static final String PLAY_VIDEO = "play_video";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_hello_moon, parent, false);
		
		mPlayButton = (Button)v.findViewById(R.id.hellomoon_playButton);
		mPlayButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPlayer.play(getActivity());
			}
		});
		mPauseButton = (Button)v.findViewById(R.id.hellomoon_pauseButton);
		mPauseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPlayer.pause();
			}
		});
		mStopButton = (Button)v.findViewById(R.id.hellomoon_stopButton);
		mStopButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPlayer.stop();
			}
		});
		
		mPlayVideoButton = (Button)v.findViewById(R.id.hellomoon_playVideoButton);
		mPlayVideoButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PlayVideoFragment fragment = new PlayVideoFragment();
				fragment.show(getActivity().getSupportFragmentManager(), PLAY_VIDEO);
			}
		});
		
		return v;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mPlayer.stop();
	}

}
