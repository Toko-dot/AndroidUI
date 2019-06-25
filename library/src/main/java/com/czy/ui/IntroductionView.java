package com.czy.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * 收缩的文本显示控件
 * Created by czy on 2019/5/22.
 */

public class IntroductionView extends View {
    private int maxHeight;
    private int minHeight;
    private int showHeight;
    private int showLines;
    private int totalLine;
    private TextPaint paint;
    private GestureDetector gestureDetector;
    private List<String> lineText;
    private String text;

    public IntroductionView(Context context) {
        this(context,null);
    }

    public IntroductionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, onGestureListener);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IntroductionView);
        showLines=typedArray.getInt(R.styleable.IntroductionView_showLines,3);
        text=typedArray.getString(R.styleable.IntroductionView_text);
        typedArray.recycle();

        paint=new TextPaint();
        paint.setAntiAlias(true);
        paint.setTextSize(sp2px(context,16));
        initContent();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float top = getPaint().getFontMetrics().top;
        for (int i=0;i<totalLine;i++){
            canvas.drawText(lineText.get(i),0,-top+i*getOneLineHeight(),getPaint());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, showHeight+getPaddingTop()+getPaddingBottom());
    }

    public TextPaint getPaint() {
        return paint;
    }

    /**
     * 将展示的内容转化为行集合
     * @param content
     * @return
     */
    private List<String> converText(String content){
        ArrayList<String> list = new ArrayList<>();
        if (TextUtils.isEmpty(content))
            return list;
        String line="";
        char[] chars = content.toCharArray();
        for (int i=0;i<chars.length;i++){
            if ((line+chars[i]).endsWith("\n")){
                list.add(line+chars[i]);
                line="";
            }else if ((getPaint().measureText(line+chars[i]))>getWidth()){
                list.add(line);
                line=""+chars[i];
            }else {
                line+=chars[i];
            }
        }
        if (!TextUtils.isEmpty(line))
            list.add(line);
        return list;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!TextUtils.isEmpty(text)){
            initContent();
        }
    }

    private void initContent() {
        lineText = converText(text.toString());

        totalLine =lineText.size();

        maxHeight = measureLinesHeight(totalLine);

        if (showLines>totalLine)
            showLines=totalLine;

        minHeight = measureLinesHeight(showLines);
        if (showHeight==0)
            showHeight=minHeight;

        requestLayout();
    }

    public void setText(String text){
        this.text=text;
        initContent();
    }

    /**
     * 测量高度
     * @param lines
     * @return
     */
    private int measureLinesHeight(int lines){
        Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        return lines*getOneLineHeight();
    }

    /**
     * 设置展示的行数
     * @param lines
     */
    public void setShowLines(int lines) {
        if (lines>totalLine)
            return;
        showLines=lines;

        minHeight = measureLinesHeight(showLines);
        showHeight=minHeight;
        requestLayout();
    }

    /**
     * 每行数据的高度
     * @return
     */
    private int getOneLineHeight(){
        Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        float lh = fontMetrics.bottom - fontMetrics.top;
        if (lh*10%10==0){
            return (int) lh;
        }else {
            return (int) lh+1;
        }
    }


    public void close(){
        ValueAnimator animator = ValueAnimator.ofInt(maxHeight, minHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                showHeight=((int) animation.getAnimatedValue());
                requestLayout();
            }
        });
        animator.addListener(new AnimatorListenerAdapter(){

            @Override
            public void onAnimationStart(Animator animation) {
                open=false;
            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(300);
        animator.start();
    }

    public void open(){
        ValueAnimator animator = ValueAnimator.ofInt(minHeight,maxHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                showHeight=((int) animation.getAnimatedValue());
                requestLayout();
            }
        });

        animator.addListener(new AnimatorListenerAdapter(){

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                open=true;
            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(300);
        animator.start();
    }
    private boolean open;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return gestureDetector.onTouchEvent(event);
    }

    GestureDetector.OnGestureListener onGestureListener=new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (open){
                close();
            }else {
                open();
            }
            return true;
        }
    };
    public int dp2px(Context context, float dpValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }
        return 0;
    }
    public  int sp2px(Context context,float spValue){
        float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue*fontScale+0.5f);
    }
}
