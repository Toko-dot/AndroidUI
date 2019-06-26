package com.czy.attar

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import android.widget.OverScroller


/**
 * Created by czy on 2019/6/25.
 */
class CardView(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {
    private lateinit var downPoint: Point
    private lateinit var prePoint: Point
    companion object {
        val CARD_ROTATION_DEGREES=90
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downPoint = Point(event.rawX, event.rawY)
                prePoint = Point(event.rawX, event.rawY)
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.rawX - prePoint.x
                val dy = event.rawY - prePoint.y

                translationX = translationX + dx
                prePoint.x = event.rawX

                translationY = translationY + dy
                prePoint.y = event.rawY

                rotation = CARD_ROTATION_DEGREES*translationX/width
                alpha=1-Math.abs(translationX/width)
            }
            MotionEvent.ACTION_UP->{

                    translationX = 0f
                    translationY = 0f
                    rotation = 0f
                    alpha = 1f
                    return true

            }
        }
        return super.onTouchEvent(event)
    }
    data class Point(var x: Float, var y: Float)
}