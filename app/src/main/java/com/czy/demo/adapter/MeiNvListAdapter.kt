package com.czy.demo.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.czy.demo.R

class MeiNvListAdapter(context: Context,  datas: ArrayList<String>, layout: Int,val listener:(String)->Unit):BaseRvAdapter<String>(context,datas,layout) {
    override fun bindView(holder: ViewHolder, data: String, position: Int) {
            Glide.with(context).load(data).into(holder.find(R.id.ig_meinv))
    }
}