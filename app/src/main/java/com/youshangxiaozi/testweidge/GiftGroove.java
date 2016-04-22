package com.youshangxiaozi.testweidge;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by liangdaijian on 16/4/22.
 */
public class GiftGroove extends LinearLayout implements ValueAnimator.AnimatorUpdateListener {
    public GiftGroove(Context context) {
        super(context);
    }

    public GiftGroove(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GiftGroove(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initSubViews();
    }

    ImageView icon;
    BothTransContainer groove;
    private void initSubViews(){
        icon = (ImageView)findViewById(R.id.icon);
        groove = (BothTransContainer) findViewById(R.id.btc);
    }

    private ObjectAnimator mAnimator = null;
    private void animate(int during, int from, int to) {
        if (icon != null && groove != null) {
            if (mAnimator != null && mAnimator.isRunning()) {
                mAnimator.cancel();
                mAnimator = null;
            }
            PropertyValuesHolder dxyUpdate = PropertyValuesHolder.ofInt("dxy", from, to);
            mAnimator = ObjectAnimator.ofPropertyValuesHolder(this, dxyUpdate);
            mAnimator.setDuration(during);
            mAnimator.setInterpolator(new DecelerateInterpolator());
            mAnimator.addUpdateListener(this);
            mAnimator.start();
        }
    }
    /**
     * 收起来
     * @param during 毫秒
     */
    public void shrink(int during) {
        if (groove != null) {
            animate(during, getDxy(), groove.getAnimateWidth() >> 1);
            groove.shrink(during);
        }
    }

    /**
     * 散开
     * @param during 毫秒
     */
    public void spread(int during) {
        if (groove != null) {
            groove.spread(during);
            animate(during, getDxy(), 0);
        }
    }

    public int getDxy() {
        return dxy;
    }

    public void setDxy(int dxy) {
        this.dxy = dxy;
    }

    private int dxy;

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (icon != null) {
            icon.layout(dxy, 0, dxy + icon.getWidth(), icon.getHeight());
        }
    }
}
