package com.czy.demo.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import com.czy.demo.R
import com.czy.demo.adapter.GalleryPagerAdapter
import kotlinx.android.synthetic.main.activity_view_pager.*

class ViewPagerActivity : AppCompatActivity() {
    private val adapter:GalleryPagerAdapter by lazy {
        GalleryPagerAdapter(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)
        vp_gallery.pageMargin=vp_gallery.paddingLeft/2
        vp_gallery.setPageTransformer(false){
            page, position ->
            page.scaleY=-position*position*0.05f+1f
            page.alpha=-position*position*0.5f+1f
        }
        vp_gallery.adapter=adapter

    }
}
