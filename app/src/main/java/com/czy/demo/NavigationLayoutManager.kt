package com.czy.demo

import android.content.Context
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import kotlin.math.log

class NavigationLayoutManager(val context: Context) : RecyclerView.LayoutManager() {
    val mOrientationHelper: OrientationHelper = OrientationHelper.createOrientationHelper(this, OrientationHelper.HORIZONTAL)

    init {
        isAutoMeasureEnabled = true

    }

    private var listw: Int = 0
    private var scrollw: Int = 0

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if (itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            return
        }
        detachAndScrapAttachedViews(recycler)
        listw = paddingLeft
        for (i in 0..(itemCount - 1)) {
            val view = recycler?.getViewForPosition(i)
            addView(view)
            measureChildWithMargins(view, 0, 0)
            val layoutParams = view!!.layoutParams as ViewGroup.MarginLayoutParams

            view?.layout(listw+layoutParams.leftMargin, layoutParams.topMargin+paddingTop, listw + getDecoratedMeasuredWidth(view)+layoutParams.leftMargin, getDecoratedMeasuredHeight(view)+layoutParams.topMargin+paddingTop)
            listw +=getDecoratedMeasuredWidth(view)+layoutParams.leftMargin+layoutParams.rightMargin
            if (listw >= width)
                break
        }
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        //边界判断 ，填充，滚动，回收
        if (childCount == 0||dx==0)
            return 0

        if (dx > 0) {//向左滑动  <-----

            val lastview =  findViewByPosition(itemCount-1)

            if (lastview!=null){
                val layoutParams = lastview.layoutParams as ViewGroup.MarginLayoutParams
                if (lastview.right+layoutParams.rightMargin - dx < width) {

                    offsetChildrenHorizontal(-(lastview.right+paddingRight+layoutParams.rightMargin - width))
                    return 0
                }
            }
        } else {//向右滑动 ---->
            val firstView = findViewByPosition(0)
            if (firstView!=null){
                val layoutParams = firstView.layoutParams as ViewGroup.MarginLayoutParams
                if (firstView.left-layoutParams.leftMargin - dx > 0) {

                    offsetChildrenHorizontal(-firstView.left+paddingLeft+layoutParams.leftMargin)
                    return 0
                }
            }
        }
        fill(dx, recycler!!)

        mOrientationHelper.offsetChildren(-dx)

        recycleOut(dx, recycler!!)


        return dx
    }

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        logs("onLayoutCompleted")
    }
    //填充
    private fun fill(dx: Int, recycler: RecyclerView.Recycler) {
        if (dx>0){//向左滑 <----
            //查找最后一个View
            var lastview :View?= null
            for(i in (itemCount-1)downTo 0){
                lastview=findViewByPosition(i)
                if (lastview!=null)
                    break
            }
            if (lastview==null)
                return
            val layoutParams = lastview!!.layoutParams as ViewGroup.MarginLayoutParams

            //判断是否需要在右侧添加一个View

            if (lastview.right -dx<= width) {
                do {
                    val position = getPosition(lastview)
                    if (position == itemCount - 1)
                        return
                    val fillview = recycler.getViewForPosition(position + 1)
                    addView(fillview)
                    measureChildWithMargins(fillview, 0, 0)
                    val fillParams = fillview!!.layoutParams as ViewGroup.MarginLayoutParams
                    val lastParams = lastview!!.layoutParams as ViewGroup.MarginLayoutParams
                    fillview.layout(lastview!!.right+fillParams.leftMargin+lastParams.rightMargin, fillParams.topMargin+paddingTop, lastview.right + getDecoratedMeasuredWidth(fillview)+fillParams.leftMargin+lastParams.rightMargin, fillParams.topMargin+paddingTop+getDecoratedMeasuredHeight(fillview))
                    lastview = fillview
                } while ((fillview.right-dx) <= width)

            }
        }
        if (dx < 0) {//向右滑动 ---->
            //查找第一个View
            var firstview :View?= null
            for(i in 0..(itemCount-1)){
                firstview=findViewByPosition(i)
                if (firstview!=null)
                    break
            }
            if (firstview==null)
                return

            val layoutParams = firstview!!.layoutParams as ViewGroup.MarginLayoutParams
            //判断是否需要在左侧添加一个View
            if (firstview.left-dx >= 0) {
                do {
                    val position = getPosition(firstview)
                    if (position == 0) {
                        return
                    }
                    val fillview = recycler.getViewForPosition(position - 1)
                    addView(fillview)

                    measureChildWithMargins(fillview, 0, 0)
                    val fillParams = fillview!!.layoutParams as ViewGroup.MarginLayoutParams
                    val firstParams = firstview!!.layoutParams as ViewGroup.MarginLayoutParams

                    fillview.layout(firstview!!.left -fillParams.rightMargin-firstParams.leftMargin- getDecoratedMeasuredWidth(fillview), fillParams.topMargin+paddingTop, firstview.left-fillParams.rightMargin-firstParams.leftMargin, getDecoratedMeasuredHeight(fillview)+fillParams.topMargin+paddingTop)
                    firstview = fillview

                } while (fillview.left -dx >= 0)
            }
        }

    }

    //回收不显示的View
    private fun recycleOut(dx: Int, recycler: RecyclerView.Recycler) {
        for (i in 0..(childCount - 1)) {
            val view = getChildAt(i) ?: return
            if (view.right < 0) {
                removeAndRecycleView(view, recycler)
            }
            if (view.left > width) {
                removeAndRecycleView(view, recycler)
            }
        }
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        logs("state=$state")
    }
}