package org.mazhuang.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends Activity {

	public static final String EXTRA_ANSWER_IS_TRUE = "org.mazhuang.android.geoquiz.answer_is_true";
	public static final String EXTRA_ANSWER_SHOWN = "org.mazhuang.android.geoquiz.answer_shown";
	private static final String KEY_IS_CHEATER = "isCheater";
	private boolean mIsCheater = false;
	private boolean mAnswerIsTrue;
	
	private TextView mAnswerTextView;
	private Button mShowAnswer;
	private TextView mApiLevelTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null) {
			mIsCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER, false);
		}
		
		setContentView(R.layout.activity_cheat);
		
		setAnswerShownResult(mIsCheater);
		
		mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
		
		mAnswerTextView = (TextView)findViewById(R.id.answerTextView);
		
		mShowAnswer = (Button)findViewById(R.id.showAnswerButton);
		mShowAnswer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mIsCheater = true;
				if (mAnswerIsTrue) {
					mAnswerTextView.setText(R.string.true_button);
				} else {
					mAnswerTextView.setText(R.string.false_button);
				}
				setAnswerShownResult(mIsCheater);
			}
		});
		
		mApiLevelTextView = (TextView)findViewById(R.id.apiLevel);
		mApiLevelTextView.setText("API level " + Build.VERSION.SDK_INT);
	}
	
	private void setAnswerShownResult(boolean isAnswerShown) {
		Intent data = new Intent();
		data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
		setResult(RESULT_OK, data);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(KEY_IS_CHEATER, mIsCheater);
	}
}
