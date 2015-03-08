package org.mazhuang.animatedemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LayoutScaleActivity extends Activity {
    
    private RelativeLayout mTitlePanel;
    private TextView mHelloText;
    private Button mScaleBtn;
    private ScaleRunnable mScaleRunnable;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_scale);
        
        mScaleRunnable = new ScaleRunnable();
        
        mHelloText = (TextView)findViewById(R.id.hello_textview);
        
        mTitlePanel = (RelativeLayout)findViewById(R.id.layout_title_panel);
        
        mScaleBtn = (Button)findViewById(R.id.btn_scale);
        mScaleBtn.setOnClickListener(new OnClickListener() {
            private boolean isScaled = false;
            @Override
            public void onClick(View v) {
                if (!mScaleRunnable.isFinished) {
                    return;
                }
                
                if (!isScaled) {
                    isScaled = true;
                    mScaleRunnable.startAnimation(0.5f, 100L);
                } else {
                    isScaled = false;
                    mScaleRunnable.startAnimation(2.0f, 100L);
                }
            }
        });
    }
    
    
    class ScaleRunnable implements Runnable {
        long mDuration;
        boolean isFinished = true;
        float mFactor;
        long mStartTime;
        float mBaseHeight;
        float mBaseTextSize;
        
        @TargetApi(11)
        public void run() {
            ViewGroup.LayoutParams lp = mTitlePanel.getLayoutParams();
            if ((!this.isFinished)) {
                float f1 = ((float) SystemClock.currentThreadTimeMillis() - (float) this.mStartTime)
                        / (float) this.mDuration;
                float factor = 1.0f - f1 * (1.0f - mFactor);
                if (!((mFactor < 1.0f && factor > mFactor) || (mFactor >= 1.0f && factor < mFactor))) {
                	this.isFinished = true;
                	factor = mFactor;
                }
                
                lp.height = (int)(mBaseHeight * factor);
                mTitlePanel.setLayoutParams(lp);
                mHelloText.setTextSize(mBaseTextSize * factor / TypedValue.COMPLEX_UNIT_SP);
                mTitlePanel.post(this);
            }
        }
        
        public void startAnimation(float factor, long duration) {
            this.mStartTime = SystemClock.currentThreadTimeMillis();
            this.mDuration = duration;
            this.mFactor = factor;
            this.isFinished = false;
            this.mBaseHeight = mTitlePanel.getLayoutParams().height;
            this.mBaseTextSize = mHelloText.getTextSize();
            mTitlePanel.post(this);
        }
    }
}
