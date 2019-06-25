package com.czy.demo

import android.content.Context
import android.widget.TextView
import com.czy.ui.R

/**
 * Created by Administrator on 2019/6/25.
 */
class UiListAdapter( context: Context,  datas: ArrayList<String>, layout: Int,val listener:(String)->Unit):BaseRvAdapter<String>(context,datas,layout) {
    override fun bindView(holder: ViewHolder, data: String) {
        val tv_content = holder.find<TextView>(R.id.tv_content)
        tv_content.text=data
        holder.itemView.setOnClickListener {
            listener.invoke(data)
        }
    }

}