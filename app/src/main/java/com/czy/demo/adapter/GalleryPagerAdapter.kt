package com.czy.demo.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.czy.demo.R
import org.jetbrains.anko.find

class GalleryPagerAdapter(val context: Context) :PagerAdapter(){
    private val __datas by lazy {
        context.resources.getStringArray(R.array.list_type_ui)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

    override fun getCount(): Int=if(__datas==null || __datas.isEmpty()) 0 else Int.MAX_VALUE

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_item_vp, null)
        val tv_type = view.find<TextView>(R.id.tv_type)
        tv_type.text=__datas.get(position%__datas.size)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}