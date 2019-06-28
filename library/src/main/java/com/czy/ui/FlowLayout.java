package com.czy.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by czy on 2019/5/5.
 * 流式布局
 */

public class FlowLayout extends ViewGroup {

    private Adapter adapter;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        adapter.setFlowLayout(this);
        removeAllViews();
        addInitItemView();
        requestLayout();
    }

    private void addInitItemView() {
        int itemCount = adapter.getItemCount();

        for (int i = 0; i < itemCount; i++) {
            FrameLayout frameLayout = new FrameLayout(getContext());
            frameLayout.setClickable(true);
            View itemView = adapter.createItemView(i, frameLayout);
            adapter.bindView(itemView, i);
            addView(itemView);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        int childCount = getChildCount();
        int width = widthSize;
        if (widthMode == MeasureSpec.AT_MOST) {
            int measuredWidth = ((View) getParent()).getMeasuredWidth();
            width = measuredWidth;
        }
        //测量高度
        int height = heightSize;
        if (heightMode == MeasureSpec.AT_MOST) {
            int curWidth = paddingLeft;
            int curHeight = 0;
            int resultH = 0;

            measureChildren(widthMeasureSpec, heightMeasureSpec);
            for (int i = 0; i < childCount; i++) {
                View itemView = getChildAt(i);

                int measuredWidth = itemView.getMeasuredWidth();
                int measuredHeight = itemView.getMeasuredHeight();
                if (itemView.getLayoutParams() instanceof MarginLayoutParams) {
                    MarginLayoutParams layoutParams = (MarginLayoutParams) itemView.getLayoutParams();
                    if (curWidth == paddingLeft && measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin + paddingLeft + paddingRight > width) {
                        curHeight = measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin;
                        resultH += curHeight;
                        curWidth = paddingLeft;
                        curHeight = 0;
                    } else {
                        if (curWidth + measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin  + paddingRight > widthSize) {
                            resultH += curHeight;
                            curHeight = 0;

                            if (measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin > curHeight)
                                curHeight = measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin;
                            curWidth = paddingLeft + measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin;

                        } else {
                            if (measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin > curHeight)
                                curHeight = measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin;
                            curWidth += measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin;

                        }
                    }

                    if (i < childCount - 1) {
                        continue;
                    } else {
                        resultH += curHeight;
                        curWidth = paddingLeft;
                        curHeight = 0;
                    }
                } else {
                    if (curWidth == paddingLeft && measuredWidth + paddingLeft + paddingRight > width) {
                        curHeight = measuredHeight;
                        resultH += curHeight;
                        curWidth = paddingLeft;
                        curHeight = 0;
                    } else {
                        if (curWidth + measuredWidth  + paddingRight > widthSize) {
                            resultH += curHeight;
                            curHeight = 0;

                            if (measuredHeight > curHeight)
                                curHeight = measuredHeight;
                            curWidth = paddingLeft + measuredWidth;

                        } else {
                            if (measuredHeight > curHeight)
                                curHeight = measuredHeight;
                            curWidth += measuredWidth;
                        }
                    }
                    if (i < childCount - 1) {
                        continue;
                    } else {
                        resultH += curHeight;
                        curWidth = paddingLeft;
                        curHeight = 0;
                    }
                }

            }
            height = resultH;
        }
        height += paddingTop + paddingBottom;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        int childCount = getChildCount();
        int width = paddingLeft;
        int maxheight = 0;
        int preheight = paddingTop;

        for (int i = 0; i < childCount; i++) {
            View item = getChildAt(i);
            int measuredWidth = item.getMeasuredWidth();
            int measuredHeight = item.getMeasuredHeight();
            if (item.getLayoutParams() instanceof MarginLayoutParams) {
                MarginLayoutParams layoutParams = (MarginLayoutParams) item.getLayoutParams();
                if (width == paddingLeft && measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin + paddingLeft + paddingRight > getMeasuredWidth()) {
                    item.layout(width + layoutParams.leftMargin, preheight + layoutParams.topMargin, width + layoutParams.leftMargin + measuredWidth, preheight + layoutParams.topMargin + measuredHeight);
                    width = paddingLeft;
                    if (measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin > maxheight)
                        maxheight = measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin;
                    preheight += maxheight;
                } else {
                    if (width + measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin > getMeasuredWidth() - paddingRight) {
                        width = paddingLeft;
                        preheight += maxheight;
                        item.layout(width + layoutParams.leftMargin, preheight + layoutParams.topMargin, width + layoutParams.leftMargin + measuredWidth, preheight + layoutParams.topMargin + measuredHeight);

                        width += measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin;

                        maxheight = measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin;


                    } else {
                        item.layout(width + layoutParams.leftMargin, preheight + layoutParams.topMargin, width + layoutParams.leftMargin + measuredWidth, preheight + layoutParams.topMargin + measuredHeight);
                        width += measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin;

                        if (measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin > maxheight)
                            maxheight = measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin;
                        continue;
                    }
                }
            } else {
                if (width == paddingLeft && measuredWidth + paddingLeft + paddingRight > getMeasuredWidth()) {
                    item.layout(width, preheight, width + measuredWidth, preheight + measuredHeight);
                    width = paddingLeft;
                    if (measuredHeight > maxheight)
                        maxheight = measuredHeight;
                    preheight += maxheight;
                } else {
                    if (width + measuredWidth > getMeasuredWidth() - paddingRight) {
                        width = paddingLeft;
                        preheight += maxheight;
                        item.layout(width, preheight, width + measuredWidth, preheight + measuredHeight);
                        width += measuredWidth;
                        maxheight = measuredHeight;
                    } else {
                        item.layout(width, preheight, width + measuredWidth, preheight + measuredHeight);
                        width += measuredWidth;
                        if (measuredHeight > maxheight)
                            maxheight = measuredHeight;
                        continue;
                    }
                }
            }
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public static abstract class Adapter<V extends View,T> {
        protected Context context;
        private FlowLayout layout;
        protected ArrayList<T> datas;
        public Adapter(Context context, ArrayList<T> datas) {
            this.context = context;
            this.datas=datas;
        }

        public void setDatas(ArrayList<T> datas) {
            this.datas = datas;
        }

        private void setFlowLayout(FlowLayout layout) {
            this.layout = layout;
        }

        protected abstract V createItemView(int pos, ViewGroup parent);

        protected abstract void bindView(V view, int pos);

        protected  int getItemCount(){
            return datas==null?0:datas.size();
        }

        public Context getContext() {
            return context;
        }

        public void notifyChange() {
            layout.removeAllViews();
            layout.addInitItemView();
            layout.requestLayout();
        }
    }

}
