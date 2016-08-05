package com.youshangxiaozi.testweidge;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;


/**
 * @author liangdaijian
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PathAnimatorContainer extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {
    public PathAnimatorContainer(Context context) {
        this(context, null);
    }

    public PathAnimatorContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathAnimatorContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     *
     * @param view
     * @param start
     * @param coordinate
     * @param end
     */
    public void addFavor(View view, PointF start, PointF coordinate, PointF end) {
        if (view != null) {
            if (indexOfChild(view) == -1) {
                view.setVisibility(GONE);
                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                if (start != null) {
                    lp.gravity = Gravity.LEFT | Gravity.TOP;
                    lp.leftMargin = (int) start.x;
                    lp.topMargin = (int) start.y;
                }
                addView(view, lp);
            }
            startPathAnomate(view, start, coordinate, end, 5000, null, new NormalAnimatorListener(view));
        }

    }

    /**
     *
     * @param view
     * @param start
     * @param coordinate
     * @param coordinate2
     * @param end
     */
    public void addFavor(View view, PointF start, PointF coordinate, PointF coordinate2, PointF end) {
        if (view != null) {
            if (indexOfChild(view) == -1) {
                view.setVisibility(GONE);
                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                if (start != null) {
                    lp.gravity = Gravity.LEFT | Gravity.TOP;
                    lp.leftMargin = (int) start.x;
                    lp.topMargin = (int) start.y;
                }
                addView(view, lp);
            }
            startPathAnomate(view, start, coordinate, coordinate2, end, 5000, null, new NormalAnimatorListener(view));
        }

    }

    /**
     * 跑曲线动画
     * @param view 动画的view，要求是子view
     * @param start 起始的点
     * @param coordinate 三阶贝塞尔曲线的控制点
     * @param coordinate2 三阶贝塞尔曲线的控制点
     * @param end 结束点
     * @param duration 时长
     * @param itl 插值器
     * @return
     */
    private boolean startPathAnomate(View view, PointF start, PointF coordinate, PointF coordinate2, PointF end, int duration, Interpolator itl, Animator.AnimatorListener aml) {
        if (start != null && coordinate != null && end != null) {
            Path path = new Path();
            path.moveTo(start.x, start.y);
            path.cubicTo(coordinate.x, coordinate.y, coordinate2.x, coordinate2.y, end.x, end.y);
            return startPathAnomate(view, path, duration, itl, aml);
        } else {
            return false;
        }
    }

    /**
     * 跑曲线动画
     * @param view 动画的view，要求是子view
     * @param start 起始的点
     * @param coordinate 二阶贝塞尔曲线的控制点
     * @param end 结束点
     * @param duration 时长
     * @param itl 插值器
     * @return
     */
    private boolean startPathAnomate(View view, PointF start, PointF coordinate, PointF end, int duration, Interpolator itl, Animator.AnimatorListener aml) {
        if (start != null && coordinate != null && end != null) {
            Path path = new Path();
            path.moveTo(start.x, start.y);
            path.quadTo(coordinate.x, coordinate.y, end.x, end.y);
            return startPathAnomate(view, path, duration, itl, aml);
        } else {
            return false;
        }
    }

    /**
     * 跑曲线动画
     * @param view 动画的view，要求是子view
     * @param path 曲线轨迹
     * @param duration 时长
     * @param itl 插值器
     * @return
     */
    private boolean startPathAnomate(View view, Path path, int duration, Interpolator itl, Animator.AnimatorListener aml) {
        if (view != null) {
            if (indexOfChild(view) == -1) {
                return false;
            }
            PathEvaluator pe = new PathEvaluator(view, path);
            ValueAnimator animator = ValueAnimator.ofObject(pe, new ViewPointF(null, null), new ViewPointF(null, null));
            animator.setDuration(duration);
            if (itl != null) {
                animator.setInterpolator(itl);
            }
            if (aml != null) {
                animator.addListener(aml);
            }
            animator.addUpdateListener(this);
            animator.start();
            return true;
        }
        return false;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        ViewPointF vp = (ViewPointF) animation.getAnimatedValue();
        vp.view.layout((int)vp.point.x, (int) vp.point.y,
                (int)vp.point.x + vp.view.getWidth(),
                (int) vp.point.y + vp.view.getHeight());
    }

    /**
     * 曲线动画通用的TypeEvaluator
     */
    static class PathEvaluator implements TypeEvaluator<ViewPointF> {
        private View view;
        private Path path;
        PathMeasure pm;
        public PathEvaluator(View view, Path path) {
            this.view = view;
            this.path = path;
            pm = new PathMeasure(path, false);
        }

        @Override
        public ViewPointF evaluate(float fraction, ViewPointF startValue, ViewPointF endValue) {
            float[] pos = new float[2];
            pm.getPosTan(pm.getLength() * fraction, pos, null);
            return new ViewPointF(view, new PointF(pos[0], pos[1]));
        }
    }

    /**
     *
     */
    static final class ViewPointF {
        public View view;
        public PointF point;

        public ViewPointF(View view, PointF point) {
            this.view = view;
            this.point = point;
        }
    }

    /**
     *
     */
    class NormalAnimatorListener implements Animator.AnimatorListener {

        private View view;

        public NormalAnimatorListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            view.setVisibility(VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            view.setVisibility(GONE);
            PathAnimatorContainer.this.removeView(view);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            view.setVisibility(GONE);
            PathAnimatorContainer.this.removeView(view);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}
