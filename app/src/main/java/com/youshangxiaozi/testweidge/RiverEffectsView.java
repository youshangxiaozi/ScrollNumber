package com.youshangxiaozi.testweidge;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author liangdaijian
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RiverEffectsView extends View implements ValueAnimator.AnimatorUpdateListener {

    private final String KEY_MOVE_X = "key_move_x";
    private final String KEY_MOVE_Y = "key_move_y";
    private final int MARQUEE_DURATE = 1800;

    private Bitmap river;
    private Bitmap mask;
    private Bitmap riverResult;
    private Canvas riverResultCanvas;
    private Bitmap logo;
    private Paint paint;
    private float fullPercents = 1.0f;
    private float riverOfset;
    private PorterDuffXfermode px;
    private ObjectAnimator oaMarquee;

    public RiverEffectsView(Context context) {
        this(context, null);
    }

    public RiverEffectsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private float fixH = 0f;
    public RiverEffectsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.RiverEffectsView, defStyleAttr, 0);

        Drawable r = ta.getDrawable(R.styleable.RiverEffectsView_river);
        if (r instanceof BitmapDrawable) {
            river = ((BitmapDrawable) r).getBitmap();
            fixH = river.getHeight() * 0.07f;
        }

        Drawable l = ta.getDrawable(R.styleable.RiverEffectsView_logo);
        if (l instanceof BitmapDrawable) {
            logo = ((BitmapDrawable) l).getBitmap();
        }

        Drawable m = ta.getDrawable(R.styleable.RiverEffectsView_mask);
        if (m instanceof BitmapDrawable) {
            mask = ((BitmapDrawable) m).getBitmap();
        } else if (river != null) {
            mask = Bitmap.createBitmap(river.getHeight(), river.getHeight(), Bitmap.Config.ARGB_8888);
            mask.eraseColor(Color.TRANSPARENT);
            Canvas c = new Canvas(mask);
            Paint p = new Paint();
            p.setFlags(Paint.ANTI_ALIAS_FLAG);
            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.FILL);
            c.drawCircle(mask.getWidth() >> 1, mask.getHeight() >> 1, mask.getHeight() >> 1, p);
        }
        riverResult = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        riverResult.eraseColor(Color.TRANSPARENT);
        riverResultCanvas = new Canvas(riverResult);
        ta.recycle();

        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        px = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

        PropertyValuesHolder ph_moveX = PropertyValuesHolder.ofFloat(KEY_MOVE_X, riverOfset, mask.getWidth() - river.getWidth());
        oaMarquee = ObjectAnimator.ofPropertyValuesHolder(this, ph_moveX);
        oaMarquee.setDuration(MARQUEE_DURATE);
        oaMarquee.setRepeatCount(ValueAnimator.INFINITE);
        oaMarquee.setRepeatMode(ValueAnimator.RESTART);
        oaMarquee.addUpdateListener(this);

        percents(0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mask.getWidth(), mask.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (river != null && logo != null) {
            riverResultCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            riverResultCanvas.saveLayer(0, 0, riverResultCanvas.getWidth(), riverResultCanvas.getHeight(), null,
                    Canvas.ALL_SAVE_FLAG);
            paint.setXfermode(null);
            riverResultCanvas.drawBitmap(river, riverOfset, riverResultCanvas.getHeight() * (1 - fullPercents) - fixH, paint);
            paint.setXfermode(px);
            riverResultCanvas.drawBitmap(mask, 0, 0, paint);
            riverResultCanvas.restore();

            paint.setXfermode(null);
            canvas.drawBitmap(riverResult, 0, 0, paint);

            canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null,
                    Canvas.ALL_SAVE_FLAG);
            paint.setXfermode(null);
            canvas.drawBitmap(logo, (mask.getWidth() - logo.getWidth()) >> 1,
                    (mask.getHeight() - logo.getHeight()) >> 1, paint);
            paint.setXfermode(px);
            canvas.drawBitmap(riverResult, 0, 0, paint);
            canvas.restore();

        }
    }

    private void startMarquee() {
        if (oaMarquee != null) {
            oaMarquee.start();
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void cancelMarquee() {
        if (oaMarquee != null) {
            if (oaMarquee.isRunning() || oaMarquee.isStarted()) {
                oaMarquee.cancel();
            }
            oaMarquee = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startMarquee();
    }

    @Override
    protected void onDetachedFromWindow() {
        cancelMarquee();
        super.onDetachedFromWindow();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        Object oTx = animation.getAnimatedValue(KEY_MOVE_X);
        if (oTx instanceof Float) {
            riverOfset = (float) oTx;
        }

        Object oTy = animation.getAnimatedValue(KEY_MOVE_Y);
        if (oTy instanceof Float) {
            fullPercents = (float) oTy;
        }

        invalidate();
    }

    /**
     * 设置百分比
     * @param p 百分比
     */
    public void percents(float p) {
        if (p < 0.0f || p > 1.0f) {
            throw new IllegalArgumentException("percents should be 0-1");
        }
        fullPercents = p;
        invalidate();
    }
}