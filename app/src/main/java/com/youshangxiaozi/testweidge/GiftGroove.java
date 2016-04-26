package com.youshangxiaozi.testweidge;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
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
        groove.addAnimatorUpdateListener(this);
    }

    /**
     * 收起来
     * @param during 毫秒
     */
    public void shrink(int during) {
        if (groove != null) {
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
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (icon != null && animation != null) {
            int dxy = (int)animation.getAnimatedValue();
            icon.layout(dxy, 0, dxy + icon.getWidth(), icon.getHeight());
        }
    }
}
