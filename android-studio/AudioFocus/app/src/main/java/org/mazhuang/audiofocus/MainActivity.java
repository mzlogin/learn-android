package org.mazhuang.audiofocus;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AudioManager.OnAudioFocusChangeListener, AdapterView.OnItemSelectedListener {

    private Spinner mKindOfFocusSpinner;
    private Button mRequireFocusButton;
    private Button mAbandonFocusButton;
    private Button mPlayMusicButton;
    private Button mPlayShortButton;

    private AudioManager mAudioManager;
    private AudioPlayer mAudioPlayer;
    private int mKindOfFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initialize();
    }

    private void initViews() {
        mKindOfFocusSpinner = (Spinner) findViewById(R.id.kind_of_focus);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.kind_of_focus, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mKindOfFocusSpinner.setAdapter(adapter);
        mKindOfFocusSpinner.setOnItemSelectedListener(this);

        mRequireFocusButton = (Button) findViewById(R.id.require_focus);
        assert mRequireFocusButton != null;
        mRequireFocusButton.setOnClickListener(this);

        mAbandonFocusButton = (Button) findViewById(R.id.abandon_focus);
        assert mAbandonFocusButton != null;
        mAbandonFocusButton.setOnClickListener(this);

        mPlayMusicButton = (Button) findViewById(R.id.play_music);
        assert mPlayMusicButton != null;
        mPlayMusicButton.setOnClickListener(this);

        mPlayShortButton = (Button) findViewById(R.id.play_short);
        assert mPlayShortButton != null;
        mPlayShortButton.setOnClickListener(this);
    }

    private void initialize() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioPlayer = new AudioPlayer();
        mKindOfFocus = AudioManager.AUDIOFOCUS_GAIN;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.require_focus:
                requireAudioFocus();
                break;
            case R.id.abandon_focus:
                abandonAudioFocus();
                break;
            case R.id.play_music:
                playSound();
                break;
            case R.id.play_short:
                playShortSound();
                break;
        }
    }

    private void requireAudioFocus() {
        int result = mAudioManager.requestAudioFocus(this,
                AudioManager.STREAM_MUSIC,
                mKindOfFocus);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mRequireFocusButton.setEnabled(false);
            mAbandonFocusButton.setEnabled(true);
        } else {
            Toast.makeText(this, getString(R.string.require_focus_failed), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void abandonAudioFocus() {
        int result = mAudioManager.abandonAudioFocus(this);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mRequireFocusButton.setEnabled(true);
            mAbandonFocusButton.setEnabled(false);
        }
    }

    private void playSound() {
        mAudioPlayer.play(this, R.raw.mp3_sample);
    }

    private void playShortSound() {
        mAudioPlayer.play(this, R.raw.overspeed);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        boolean hasFocus = false;
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                hasFocus = true;
                mAudioPlayer.resume();
                break;
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                hasFocus = true;
                // TODO Upper the volume
                break;
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE:
                hasFocus = true;
                mAudioPlayer.resume();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                hasFocus = false;
                abandonAudioFocus();
                mAudioPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                hasFocus = false;
                // TODO Lower the volume
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                hasFocus = false;
                mAudioPlayer.pause();
                break;
        }

        mRequireFocusButton.setEnabled(!hasFocus);
        mAbandonFocusButton.setEnabled(hasFocus);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                mKindOfFocus = AudioManager.AUDIOFOCUS_GAIN;
                break;
            case 1:
                mKindOfFocus = AudioManager.AUDIOFOCUS_GAIN_TRANSIENT;
                break;
            case 2:
                mKindOfFocus = AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK;
                break;
            case 3:
                mKindOfFocus = AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE;
                break;
            default:
                mKindOfFocus = AudioManager.AUDIOFOCUS_GAIN;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mKindOfFocus = AudioManager.AUDIOFOCUS_GAIN;
    }
}
