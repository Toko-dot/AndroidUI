package com.czy.demo

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by Administrator on 2019/6/26.
 */
class QQLayoutManager(val context: Context) : RecyclerView.LayoutManager() {
    init {
        isAutoMeasureEnabled = true
    }

    companion object {
        val MAX_COUNT = 3
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        logs("state:${state?.itemCount.toString()}")
        logs("childen:$childCount")
        logs("scrap:${recycler?.scrapList?.size}")
        val itemCount: Int = state?.itemCount ?: 0
        if (state == null || state?.itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            return
        }
        detachAndScrapAttachedViews(recycler)
        logs("layout:$itemCount")

        if (itemCount >= MAX_COUNT) {
            var count = MAX_COUNT - 1
            for (i in (itemCount - MAX_COUNT)..(itemCount - 1)) {

                val view = recycler?.getViewForPosition(i)

                addView(view)
                measureChildWithMargins(view, 0, 0)

                view?.layout((width-paddingLeft-paddingRight)/2-getDecoratedMeasuredWidth(view)/2, (height-paddingTop-paddingBottom) / 2 - getDecoratedMeasuredHeight(view) / 2, (width-paddingLeft-paddingRight)/2+getDecoratedMeasuredWidth(view)/2, (height-paddingTop-paddingBottom) / 2 + getDecoratedMeasuredHeight(view) / 2)

                view?.translationX = count * 10f
                view?.translationY = count * 10f
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view?.translationZ = (MAX_COUNT - count) * 10f
                }
                count--
            }
        } else {
            var count = itemCount - 1
            for (i in 0..(itemCount - 1)) {
                val view = recycler?.getViewForPosition(i)
                addView(view)
                measureChildWithMargins(view, 0, 0)
                view?.layout((width-paddingLeft-paddingRight)/2-getDecoratedMeasuredWidth(view)/2, (height-paddingTop-paddingBottom) / 2 - getDecoratedMeasuredHeight(view) / 2, (width-paddingLeft-paddingRight)/2+getDecoratedMeasuredWidth(view)/2, (height-paddingTop-paddingBottom) / 2 + getDecoratedMeasuredHeight(view) / 2)
                view?.translationX = count * 10f
                view?.translationY = count * 10f
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view?.translationZ = (itemCount - count) * 10f
                }
                count--
            }
        }
        requestLayout()

    }

    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {
        super.onDetachedFromWindow(view, recycler)
        removeAndRecycleAllViews(recycler)
    }

}