package com.youshangxiaozi.testweidge;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个RelativeLayout的容器，提供收缩和散开的效果
 * Created by liangdaijian on 16/4/22.
 */
public class BothTransContainer extends RelativeLayout implements ValueAnimator.AnimatorUpdateListener {
    private Direction direction = Direction.CLIP_LEFT;
    private Interpolator interpolator = new DecelerateInterpolator();
    private List<ValueAnimator.AnimatorUpdateListener> mAuls = new ArrayList<ValueAnimator.AnimatorUpdateListener>();

    public BothTransContainer(Context context) {
        super(context);
        init();
    }

    public BothTransContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BothTransContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

    }

    private ObjectAnimator mAnimator = null;
    private void animate(int during, int from, int to) {
        if (width != 0 && height != 0) {
            if (mAnimator != null && mAnimator.isRunning()) {
                mAnimator.cancel();
                mAnimator = null;
            }
            PropertyValuesHolder dxyUpdate = PropertyValuesHolder.ofInt("dxy", from, to);
            mAnimator = ObjectAnimator.ofPropertyValuesHolder(this, dxyUpdate);
            mAnimator.setDuration(during);
            mAnimator.setInterpolator(interpolator);
            mAnimator.addUpdateListener(this);
            mAnimator.start();
        }
    }

    /**
     * 设置插值器
     * @param interpolator 动画的插值器
     */
    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    /**
     * 设置方向
     * @param direction
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * 收起来
     * @param during 毫秒
     */
    public void shrink(int during) {
        if (width != 0 && height != 0) {
            switch (direction) {
                case CLIP_LEFT:
                case CLIP_RIGHT:
                    animate(during, getDxy(), width >> 1);
                    break;
                case CLIP_TOP:
                case CLIP_BOTTOM:
                    animate(during, getDxy(), height >> 1);
                    break;
                default:
                    animate(during, getDxy(), width >> 1);
            }
        }
    }

    /**
     * 散开
     * @param during 毫秒
     */
    public void spread(int during) {
        if (width != 0 && height != 0) {
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


    int width, height;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        if (width != 0 && height != 0) {
            switch (direction) {
                case CLIP_LEFT:
                    canvas.clipRect(dxy, 0, width, height);
                    canvas.translate(-dxy, 0);
                    break;
                case CLIP_RIGHT:
                    canvas.clipRect(0, 0, width - dxy, height);
                    canvas.translate(dxy, 0);
                    break;
                case CLIP_TOP:
                    canvas.clipRect(0, dxy, width, height);
                    canvas.translate(0, -dxy);
                    break;
                case CLIP_BOTTOM:
                    canvas.clipRect(0, 0, width, height - dxy);
                    canvas.translate(0, dxy);
                    break;
                default:
                    canvas.clipRect(dxy, 0, width, height);
                    canvas.translate(-dxy, 0);
            }
        }

        super.dispatchDraw(canvas);
        canvas.restore();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        LogUtils.d("BothTransContainer", "dxy=" + getDxy());
        notifyAnimatorUpdateListener(animation);
        invalidate();
    }

    /**
     * 添加动画update的listener
     * @param aul 动画update的listener
     */
    public void addAnimatorUpdateListener(ValueAnimator.AnimatorUpdateListener aul){
        if (aul != null && !mAuls.contains(aul)) {
            mAuls.add(aul);
        }
    }

    private void notifyAnimatorUpdateListener(ValueAnimator animation){
        for (int i = mAuls.size() - 1; i >= 0; i--) {
            mAuls.get(i).onAnimationUpdate(animation);
        }
    }


    public enum Direction {
        CLIP_LEFT  (0),
        CLIP_TOP (1),
        CLIP_RIGHT (2),
        CLIP_BOTTOM (3);

        Direction(int ni) {
            nativeInt = ni;
        }
        final int nativeInt;
    }
}
