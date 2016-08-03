package com.yy.iheima.widget.imageview;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Liangdaijian on 2016/8/1.
 */
public class CShaderImageView extends ImageView implements ValueAnimator.AnimatorUpdateListener {

    private final String KEY_MOVE_X = "key_move_x";
    private final String KEY_MOVE_Y = "key_move_y";

    private Drawable mDrawable;
    private Bitmap mBmMask;
    private Paint mPaint;
    private int mTx, mTy;
    ObjectAnimator mOa;

    public CShaderImageView(Context context) {
        this(context, null);
    }

    public CShaderImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CShaderImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDrawable = getDrawable();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();
        setMeasuredDimension(height, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDrawable != null && mBmMask != null) {
            int sc = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null,
                    Canvas.ALL_SAVE_FLAG);
            mDrawable.setBounds(mTx, mTy, mTx + mDrawable.getIntrinsicWidth(), mTy + mDrawable.getIntrinsicHeight());
            mDrawable.draw(canvas);
            canvas.drawBitmap(mBmMask, 0,0, mPaint);
            canvas.restoreToCount(sc);
        }
    }

    public void startMarquee() {
        if (mOa == null) {
            PropertyValuesHolder ph_moveX = PropertyValuesHolder.ofInt(KEY_MOVE_X, mTx, mDrawable.getIntrinsicHeight() - mDrawable.getIntrinsicWidth());
            PropertyValuesHolder ph_moveY = PropertyValuesHolder.ofInt(KEY_MOVE_Y, mTy, mDrawable.getIntrinsicHeight());
            mOa = ObjectAnimator.ofPropertyValuesHolder(this, ph_moveX, ph_moveY);
            mOa.setDuration(600);
            mOa.setRepeatCount(ValueAnimator.INFINITE);
            mOa.setRepeatMode(ValueAnimator.REVERSE);
            mOa.addUpdateListener(this);
            mOa.start();
        }
    }

    public void cancelMarquee() {
        if (mOa != null) {
            mOa.cancel();
            mOa = null;
        }
    }

    private void makeMaskBitmap(int r) {
        releaseMaskBitmap();
        mBmMask = Bitmap.createBitmap(r << 1, r << 1, Bitmap.Config.ARGB_8888);
        mBmMask.eraseColor(Color.TRANSPARENT);
        Canvas c = new Canvas(mBmMask);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
        c.drawCircle(r, r, r, p);
    }

    private void releaseMaskBitmap() {
        if (mBmMask != null) {
            if (!mBmMask.isRecycled()) {
                mBmMask.recycle();
            }
            mBmMask = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        if (mDrawable != null) {
            makeMaskBitmap(mDrawable.getIntrinsicHeight() >> 1);
        }
        super.onAttachedToWindow();
        startMarquee();
    }

    @Override
    protected void onDetachedFromWindow() {
        cancelMarquee();
        releaseMaskBitmap();
        super.onDetachedFromWindow();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        Object oTx = animation.getAnimatedValue(KEY_MOVE_X);
        if (oTx instanceof Integer) {
            mTx = (int) oTx;
        }

        Object oTy = animation.getAnimatedValue(KEY_MOVE_Y);
        if (oTy instanceof Integer) {
            mTy = (int) oTy;
        }

        invalidate();
    }
}
