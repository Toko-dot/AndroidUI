package com.czy.ui;

/**
 * Created by Administrator on 2019/6/10.
 */

public class BezierUtils {
    public static float getSecondBezier(float sp,float cp,float ep,float t){
        return (1 - t) * (1 - t) * sp + 2 * t * (1 - t) * cp + t * t * ep;
    }
}
