package com.czy.demo.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
        vp_gallery.adapter=adapter
        vp_gallery.setPageTransformer(true){
            page, position ->
//            page.translationX=30f
            page.translationZ=30f
        }
    }
}
