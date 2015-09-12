package org.mazhuang.android.hellomoon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayVideoFragment extends DialogFragment {

	private VideoView mVideoView;
	private MediaController mMediaController;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View v = getActivity().getLayoutInflater()
				.inflate(R.layout.fragment_play_video, null);
		
		mVideoView = (VideoView)v.findViewById(R.id.hellomoon_playVideoView);
		Uri resourceUri = Uri.parse("android.resource://org.mazhuang.android.hellomoon/" + R.raw.vid_20150110_211545);
		//File file = new File("/sdcard/Download/vid_20150110_211545.mp4");
		mMediaController = new MediaController(getActivity());
		mVideoView.setMediaController(mMediaController);
		mMediaController.setMediaPlayer(mVideoView);
		//mVideoView.setVideoPath(file.getAbsolutePath());
		mVideoView.setVideoURI(resourceUri);
		mVideoView.requestFocus();
		mVideoView.start();
		
		mVideoView.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				PlayVideoFragment.this.dismiss();
			}
		});
				
		return new AlertDialog.Builder(getActivity())
			.setView(v)
			.create();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		//mVideoView.requestFocus();
		//mVideoView.start();
	}
	
}
