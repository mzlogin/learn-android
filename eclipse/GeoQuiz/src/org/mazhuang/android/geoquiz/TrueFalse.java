package org.mazhuang.android.geoquiz;

public class TrueFalse {
	private int mQuestion;
	private boolean mTrueQuestion;
	
	public TrueFalse(int Question, boolean trueQuestion){
		mQuestion = Question;
		mTrueQuestion = trueQuestion;
	}

	public int getQuestion() {
		return mQuestion;
	}

	public void setQuestion(int question) {
		mQuestion = question;
	}

	public boolean isTrueQuestion() {
		return mTrueQuestion;
	}

	public void setTrueQuestion(boolean trueQuestion) {
		mTrueQuestion = trueQuestion;
	}
}
