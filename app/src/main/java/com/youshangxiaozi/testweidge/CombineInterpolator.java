package com.youshangxiaozi.testweidge;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by liangdaijian
 * 组合的插值器
 * 可以拆分几个过程，包括加速、减速、匀速
 */
public class CombineInterpolator implements Interpolator {
    /**
     *
     */
    public static enum Type {
        Accelelate, Decelelate, Linear;
    }

    private static final Interpolator sAccelelate = new AccelerateInterpolator();
    private static final Interpolator sDecelelate = new DecelerateInterpolator();
    private static final Interpolator sLinear = new LinearInterpolator();

    private Type[] types;
    private float[] interval;
    private float[] distance;

    /**
     * 构造器
     * @param types 这几个过程的类型，包括加速减速和匀速
     * @param interval 这几个过程分别的时间点， 譬如0.3, 0.6, 1.0
     * @param distance 这几个过程的距离点， 会根据路离点和时间点，会算出各过程的加速度，速度等
     */
    public CombineInterpolator(Type[] types, float[] interval, float[] distance) {
        if ((types == null || types.length <= 0)
                && (interval == null || interval.length <= 0)
                && (distance == null || distance.length <= 0)) {
            // set default values
            types = new Type[]{Type.Accelelate, Type.Linear, Type.Decelelate};
            interval = new float[]{0.4f, 0.8f, 1.0f};
            distance = new float[]{0.3f, 0.7f, 1.0f};
        }
        if (types == null || interval == null || distance == null
                || types.length != interval.length
                || types.length != distance.length
                || interval.length != distance.length) {
            throw new IllegalArgumentException("all the params'length must be the same value, or all are null/empty will take default values");
        } else {
            this.types = types;
            this.interval = interval;
            this.distance = distance;
        }
    }

    private int getStageIndex(float t) {
        for (int i = 0; i < interval.length; i++) {
            if (t <= interval[i]) {
                return i;
            }
        }
        return interval.length - 1;
    }

    @Override
    public float getInterpolation(float t) {
        int index = getStageIndex(t);
        float before_t = index <= 0 ? 0 : interval[index - 1];
        float d_t = interval[index] - before_t;
        float stage_t = (t - before_t) / d_t;
        float before_s = index <= 0 ? 0 : distance[index - 1];
        float d_s = distance[index] - before_s;
        if (types[index] == Type.Accelelate) {
            return before_s
                    + d_s * sAccelelate.getInterpolation(stage_t);
        } else if (types[index] == Type.Decelelate) {
            return before_s
                    + d_s * sDecelelate.getInterpolation(stage_t);
        } else if (types[index] == Type.Linear) {
            return before_s
                    + d_s * sLinear.getInterpolation(stage_t);
        } else {
            throw new RuntimeException("unknown type:" + types[index]);
        }
    }
}
