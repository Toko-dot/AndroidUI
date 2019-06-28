package com.czy.demo.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class GalleryPagerAdapter(val context: Context) :PagerAdapter(){
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

    override fun getCount(): Int=5

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val textView = TextView(context)
        textView.text="$position"
        container.addView(textView)
        return textView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}