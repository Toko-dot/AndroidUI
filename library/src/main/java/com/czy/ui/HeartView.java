package com.czy.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Canvas;

import android.graphics.Paint;

import android.support.annotation.Nullable;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by czy on 2019/6/10.
 * 直播点赞效果
 */

public class HeartView extends View {
    private  Paint paint;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private float controlX;
    private float controlY;

    private float t;
    private View view;
    private List<Control> controls;
    Bitmap bitmap ;
    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
    }
    public HeartView(Context context) {
        super(context);
    }

    public HeartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        startX=getWidth()/2;
        startY=getHeight()-bitmap.getHeight();
        endX=getWidth()/2;
        endY=0;
        //点击的心
        paint.setARGB(255,255,255,255);
        canvas.drawBitmap(bitmap,startX,startY,paint);

        if (controls==null||controls.size()==0)
            return;
        //向上飘的心
        for (int i=0;i<controls.size();i++){
            paint.setARGB((int) ((1-controls.get(i).getT())*255),255,255,255);
            canvas.drawBitmap(bitmap,BezierUtils.getSecondBezier(startX,controls.get(i).getControlX(),endX,controls.get(i).getT()),BezierUtils.getSecondBezier(startY,controls.get(i).getControlY(),endY,controls.get(i).getT()),paint);
        }
    }

    /**
     * 点击生成心
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                if (x>=startX&&x<=startX+bitmap.getWidth()&&y>=startY&&y<=startY+bitmap.getHeight()){
                    createHeart();
                    return true;
                }
            break;
        }
        return super.onTouchEvent(event);
    }

    public void createHeart(){
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(1000);
        final Control control = new Control();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (controls==null)
                    controls=new ArrayList<>();
                controlX = (float) (Math.random() * getWidth());
                controlY = (float) (Math.random() * getHeight());
                control.setControlX(controlX);
                control.setControlY(controlY);
                controls.add(control);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                controls.remove(control);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                t = (float) animation.getAnimatedValue();
                control.setT(t);
                invalidate();
            }
        });

        valueAnimator.start();
    }
    class Control{
        float controlX;
        float controlY;
        float t;

        public float getT() {
            return t;
        }

        public void setT(float t) {
            this.t = t;
        }

        public float getControlX() {
            return controlX;
        }

        public void setControlX(float controlX) {
            this.controlX = controlX;
        }

        public float getControlY() {
            return controlY;
        }

        public void setControlY(float controlY) {
            this.controlY = controlY;
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        bitmap.recycle();
    }
}
