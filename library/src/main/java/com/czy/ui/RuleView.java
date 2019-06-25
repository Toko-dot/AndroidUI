package com.czy.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

import java.text.DecimalFormat;

/**
 * Created by czy on 2019/5/28.
 */

public class RuleView extends View {

    //显示部分的背景色
    private int colorhigh = Color.WHITE;
    //尺子部分的背景色
    private int colorlow = Color.parseColor("#48D1CC");
    //当前刻度的颜色
    private int colormark = Color.RED;
    //刻度文字颜色
    private int colortrule = Color.BLACK;
    //当前刻度文字颜色
    private int colorRulemark = Color.GREEN;
    //尺子背景色
    private int colorRule = Color.parseColor("#20B2AA");
    //画笔
    private Paint highPaint;
    private Paint lowPaint;
    private Paint markTPaint;
    private Paint ruleTPaint;
    private Paint ruleMarkPaint;
    private Paint rulePaint;
    private Paint unitPaint;
    //刻度数
    private int maxdegreeCount;
    //一刻度的宽度
    private int degreewidth;
    //文字大小（像素）
    private int textsize;
    //刻度文字大小
    private int ruletextsize;
    //指针的X坐标
    private float markX;
    //尺子左侧初始的坐标
    private float ruleLeft;
    //尺子相对指针的偏移值，ruleLeft +offset 是尺子左侧的X坐标
    private float offset = 0;
    //单位
    private String unit;
    private int unitsize;
    //手势
    private GestureDetector gestureDetector;
    private VelocityTracker velocityTracker;
    //绘制是否完成
    boolean isDrawed;


    public RuleView(Context context) {
        this(context, null);
    }

    public RuleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        gestureDetector = new GestureDetector(context, onGestureListener);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RuleView);
        float dimension = typedArray.getDimension(R.styleable.RuleView_textsize, 20);
        textsize = dp2px(context, dimension);
        float ruledimension = typedArray.getDimension(R.styleable.RuleView_ruletextsize, 14);
        ruletextsize = dp2px(context, ruledimension);
        maxdegreeCount = typedArray.getInt(R.styleable.RuleView_maxdegreeCount, 25);

        float degreewidthdimension = typedArray.getDimension(R.styleable.RuleView_degreewidth, 10);
        degreewidth = dp2px(context, degreewidthdimension);
        unit = typedArray.getString(R.styleable.RuleView_unit);

        float unitdimension = typedArray.getDimension(R.styleable.RuleView_unitsize, 14);
        unitsize = sp2px(context, unitdimension);

        colorhigh = typedArray.getColor(R.styleable.RuleView_colorhigh, colorhigh);
        colorlow = typedArray.getColor(R.styleable.RuleView_colorhigh, colorlow);
        colormark = typedArray.getColor(R.styleable.RuleView_colorhigh, colormark);
        colortrule = typedArray.getColor(R.styleable.RuleView_colorhigh, colortrule);
        colorRulemark = typedArray.getColor(R.styleable.RuleView_colorhigh, colorRulemark);
        colorRule = typedArray.getColor(R.styleable.RuleView_colorhigh, colorRule);

        setBackgroundColor(Color.WHITE);
        highPaint = new Paint();
        highPaint.setAntiAlias(true);
        highPaint.setColor(colorhigh);

        lowPaint = new Paint();
        lowPaint.setAntiAlias(true);
        lowPaint.setColor(colorlow);

        markTPaint = new Paint();
        markTPaint.setColor(colormark);
        markTPaint.setAntiAlias(true);
        markTPaint.setTextSize(textsize);

        ruleTPaint = new Paint();
        ruleTPaint.setAntiAlias(true);
        ruleTPaint.setColor(colortrule);
        ruleTPaint.setTextSize(ruletextsize);

        unitPaint = new Paint();
        unitPaint.setTextSize(unitsize);
        unitPaint.setAntiAlias(true);
        unitPaint.setColor(Color.BLACK);

        ruleMarkPaint = new Paint();
        ruleMarkPaint.setAntiAlias(true);
        ruleMarkPaint.setColor(colorRulemark);

        rulePaint = new Paint();
        rulePaint.setAntiAlias(true);
        rulePaint.setColor(colorRule);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        /**
         * 绘制背景色
         */
        int height = getHeight();
        int width = getWidth();
        RectF hrectF = new RectF(0, 0, width, height / 2);
        highPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(hrectF, highPaint);
        RectF lrectF = new RectF(0, height / 2, width, height);
        canvas.drawRect(lrectF, lowPaint);

        //绘制当前刻度数，根据偏移值算出指针所指的刻度
        checkPaintTextSize(markTPaint, height / 2);

        Rect bounds = new Rect();
        markTPaint.getTextBounds((getCurrentDegree() + unit), 0, (getCurrentDegree() + unit).length(), bounds);
        int tw = bounds.right - bounds.left;
        int th = bounds.bottom - bounds.top;
        canvas.drawText((getCurrentDegree() + unit), width / 2 - tw / 2, height / 4 + th / 2, markTPaint);

        //绘制尺子
        for (int i = 0; i <= maxdegreeCount; i++) {
            if (i % 10 == 0) {
                rulePaint.setStrokeWidth(degreewidth / 7);
                canvas.drawLine(i * degreewidth + ruleLeft + offset, height / 2, i * degreewidth + ruleLeft + offset, height * 3 / 4, rulePaint);
                checkPaintTextSize(ruleTPaint, height / 4 - 20);

                Rect rbounds = new Rect();
                ruleTPaint.getTextBounds(String.valueOf(i), 0, String.valueOf(i).length(), rbounds);
                int rtw = rbounds.right - rbounds.left;
                int rth = rbounds.bottom - rbounds.top;

                canvas.drawText(String.valueOf(i), i * degreewidth + ruleLeft + offset - rtw / 2, height * 3 / 4 + 20 + rth, ruleTPaint);
                if (!TextUtils.isEmpty(unit)) {
                    unitPaint.setTextSize(ruleTPaint.getTextSize() - 2);
                    canvas.drawText(unit, i * degreewidth + ruleLeft + offset + 10 + rtw / 2, height * 3 / 4 + rth, unitPaint);
                }
            } else {
                rulePaint.setStrokeWidth(degreewidth / 7);
                canvas.drawLine(i * degreewidth + ruleLeft + offset, height / 2, i * degreewidth + ruleLeft + offset, height * 3 / 5, rulePaint);
            }
        }

        //当前刻度标记
        Path path = new Path();
        float centerw = (float) width / 2;
        float woffset = (float) degreewidth / 4;
        path.moveTo(centerw - woffset, (float) height / 2);
        path.lineTo(centerw + woffset, (float) height / 2);
        path.lineTo(centerw, height * 8 / 15);

        canvas.drawPath(path, ruleMarkPaint);

        isDrawed = true;
    }

    private void checkPaintTextSize(Paint paint, float height) {
        Rect rbounds = new Rect();
        paint.getTextBounds("0123456789", 0, "0123456789".length(), rbounds);
        int rth = rbounds.bottom - rbounds.top;
        if (rth > height) {
            paint.setTextSize(paint.getTextSize() - 1);
            checkPaintTextSize(paint, height);
        }
    }

    //计算当前刻度
    private int getCurrentDegree() {
        int res = (int) ((markX - (ruleLeft + offset)) / degreewidth);
        if (res < 0)
            res = 0;
        if (res > maxdegreeCount)
            res = maxdegreeCount;
        return res;
    }

    /**
     * @return 尺子的总宽度
     */
    private float getMaxRuleWidth() {
        return maxdegreeCount * degreewidth;
    }

    /**
     * @return 指针距离尺子左侧的距离
     */
    private float computeMark2RuleX() {
        return (markX - getRuleX());
    }

    /**
     * @return 尺子左侧的x坐标
     */
    private float getRuleX() {
        return ruleLeft + offset;
    }

    /**
     * @return 指针的x坐标
     */
    private float getMarkX() {
        return markX;
    }

    /**
     * 初始化尺子和指针的坐标数据
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ruleLeft = getMeasuredWidth() / 2;
        markX = getMeasuredWidth() / 2;
    }

    /**
     * 滑动处理
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);

        if (event.getAction() == MotionEvent.ACTION_UP) {

            /**
             * 边界处理
             */
            if (computeMark2RuleX() == 0 || computeMark2RuleX() == getMaxRuleWidth()) {
                return true;
            }

            //指针在尺子左侧
            if (computeMark2RuleX() < 0) {
                offset = markX - ruleLeft;
                invalidate();
                return true;
            }
            //指针在尺子右侧
            if (computeMark2RuleX() > getMaxRuleWidth()) {
                offset = markX - ruleLeft - maxdegreeCount * degreewidth;
                invalidate();
                return true;
            }
            /**
             * 惯性滑动处理
             */
            velocityTracker.computeCurrentVelocity(1000);
            int xVelocity = (int) velocityTracker.getXVelocity();
            new Thread(new FlingUtil(getContext(), xVelocity)).start();

            //指针是否停留在刻度上
            if (computeMark2RuleX() % degreewidth != 0) {
                offset = markX - ruleLeft - degreewidth * ((int) computeMark2RuleX() / degreewidth);
                invalidate();
                return true;
            }
        }
        return gestureDetector.onTouchEvent(event);
    }

    GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            if (e.getY() > getHeight() / 2) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (distanceX > 0) {//向左划
                if (computeMark2RuleX() == maxdegreeCount * degreewidth)
                    return true;
                offset -= distanceX;
            } else if (distanceX < 0) {//向右划
                if (computeMark2RuleX() == 0)
                    return true;
                offset += -distanceX;
            } else {
                return true;
            }
            invalidate();
            return true;
        }

    };

    class FlingUtil implements Runnable {
        private OverScroller overScroller;
        int vx;

        public FlingUtil(Context context, int vx) {
            this.vx = vx;
            overScroller = new OverScroller(context);
        }


        @Override
        public void run() {
            overScroller.fling(0, 0, vx, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
            int prex = 0;
            while (overScroller.computeScrollOffset()) {
                if (!isDrawed)
                    continue;
                int currX = overScroller.getCurrX();
                int dx = currX - prex;
                if (computeMark2RuleX() + dx <= 0) {
                    offset = markX - ruleLeft;
                    postInvalidate();
                    break;
                }
                if (computeMark2RuleX() + dx >= getMaxRuleWidth()) {
                    offset = markX - ruleLeft - maxdegreeCount * degreewidth;
                    postInvalidate();
                    break;
                }
                offset += dx;
                prex = currX;
                postInvalidate();
                isDrawed = false;
            }
            //刻度停留
            if (computeMark2RuleX() % degreewidth != 0) {
                offset = markX - ruleLeft - degreewidth * ((int) computeMark2RuleX() / degreewidth);
                postInvalidate();
            }

        }
    }

    public int dp2px(Context context, float dpValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }
        return 0;
    }

    public int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
