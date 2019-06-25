package com.czy.ui

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView

/**
 * 点击抖动的图片控件
 */
class ShakeImageView(context: Context, attributeSet: AttributeSet): ImageView(context,attributeSet) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action==MotionEvent.ACTION_DOWN){
            val animator = ValueAnimator.ofFloat( 1.2f, 1f)
            animator.addUpdateListener {
                val value = it.animatedValue as Float
                scaleX=value
                scaleY=value
            }
            animator.setDuration(200)
            animator.start()
        }
        return super.onTouchEvent(event)
    }
}