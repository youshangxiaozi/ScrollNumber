package com.youshangxiaozi.testweidge;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;



/**
 * Created by liangdaijian on 16/4/8.
 */
public class SlotNumView extends ViewGroup implements ValueAnimator.AnimatorUpdateListener {

    public SlotNumView(Context context) {
        super(context);
        init();
    }

    public SlotNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlotNumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlotNumView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
    }

    public void setTextView(TextView mTextView) {
        this.mTextView = mTextView;
        if (mTextView != null) {
            mTextView.setText("0");
        }
        invalidate();
    }

    private ObjectAnimator initScaleAnimator1 = null;

    private int getDuration(int interval) {
        LogUtils.i("SlotNumView", "getDuration interval=" + interval);
        if (interval >= 1314) {
            return 30000;
        } else if (interval >= 520) {
            return 15000;
        } else if (interval >= 188) {
            return 5000;
        } else if (interval >= 66) {
            return 2000;
        } else if (interval >= 10) {
            return 1000;
        } else if (interval >= 1){
            return 200;
        }
        return 0;
    }
    public void addCount(int count) {
        result = result + count;
        int duration = getDuration(result - (int)curr);
        if (duration > 0) {
            if (initScaleAnimator1 != null && initScaleAnimator1.isRunning()) {
                initScaleAnimator1.cancel();
                initScaleAnimator1 = null;
            }
            PropertyValuesHolder initScaleX1 = PropertyValuesHolder.ofFloat("curr", curr, result);
            initScaleAnimator1 = ObjectAnimator.ofPropertyValuesHolder(this, initScaleX1);
            initScaleAnimator1.setDuration(duration);
            initScaleAnimator1.setInterpolator(new DecelerateInterpolator());
            initScaleAnimator1.addUpdateListener(this);
            initScaleAnimator1.start();
        }
    }

    private TextView mTextView;

    public void setCurr(float curr) {
        this.curr = curr;
    }

    public float getCurr() {
        return curr;
    }

    private float curr = 0f;
    private int result;
    private int bit = 1000;

    private void drawNumber(Canvas canvas, float cy, int lineSpace, int target, int dx) {
        canvas.save();
        canvas.translate(dx, cy);
        mTextView.setText(String.valueOf(target % 10));
        mTextView.draw(canvas);
        canvas.restore();
        int dy = 0;
        int minus = 0;
        do {
            dy += lineSpace;
            minus++;

            int preTarget = (target - minus) % 10;
            if (preTarget >= 0) {
                canvas.save();
                canvas.translate(dx, cy - dy);
                mTextView.setText(String.valueOf(preTarget));
                mTextView.draw(canvas);
                canvas.restore();
            }

            canvas.save();
            canvas.translate(dx, cy + dy);
            mTextView.setText(String.valueOf((target + minus) % 10));
            mTextView.draw(canvas);
            canvas.restore();

            if (dy > getHeight() >> 1) {
                break;
            }
        } while (true);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mTextView != null) {
            int lineSpace = mTextView.getMeasuredHeight();
            int j = 0;
            int i = bit;
            while(i >= 1) {
                if (way == 0 || i == 1) {
                    float thisCurr = curr / i;
                    int thisTarget = Math.round(thisCurr);
                    float thisCy = (getHeight() >> 1) - (mTextView.getMeasuredHeight() >> 1)
                            + (thisTarget - thisCurr) * lineSpace;
                    drawNumber(canvas, thisCy, lineSpace, thisTarget, mTextView.getMeasuredWidth() * j);
                } else {
                    float thisCurr = curr / i;
                    int thisTarget = thisCurr>0f?(int)Math.floor(thisCurr):(int)Math.ceil(thisCurr);
                    float thisCy = (getHeight() >> 1) - (mTextView.getMeasuredHeight() >> 1);
                    drawNumber(canvas, thisCy, lineSpace, thisTarget, mTextView.getMeasuredWidth() * j);
                }
                j++;
                i = i / 10;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mTextView != null) {
            mTextView.layout(0, 0, mTextView.getMeasuredWidth(), mTextView.getMeasuredHeight());
            LogUtils.i("SlotNumView", "layout");
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mTextView != null) {
            measureChild(mTextView, widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(MeasureSpec.makeMeasureSpec(mTextView.getMeasuredWidth() * 4, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(mTextView.getMeasuredHeight(), MeasureSpec.EXACTLY));
            LogUtils.i("SlotNumView", "measureChild");
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        invalidate();
    }

    private int way = 0;
    public void setWay(int way) {
        this.way = way;
        invalidate();
    }

    public void start_re(){
        curr = 0;
        result = 0;
        if (initScaleAnimator1 != null && initScaleAnimator1.isRunning()) {
            initScaleAnimator1.cancel();
            initScaleAnimator1 = null;
        }
        addCount(1000);
    }

    public void pause() {
        result = (int)Math.ceil(curr);
        if (initScaleAnimator1 != null && initScaleAnimator1.isRunning()) {
            initScaleAnimator1.cancel();
            initScaleAnimator1 = null;
        }
        invalidate();
    }
}
