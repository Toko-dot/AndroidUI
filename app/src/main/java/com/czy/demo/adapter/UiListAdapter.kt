package com.czy.demo.adapter

import android.content.Context
import android.widget.TextView
import com.czy.demo.R

/**
 * Created by Administrator on 2019/6/25.
 */
class UiListAdapter( context: Context,  datas: ArrayList<String>, layout: Int,val listener:(String)->Unit): BaseRvAdapter<String>(context,datas,layout) {
    override fun bindView(holder: ViewHolder, data: String, position:Int) {
        val tv_content = holder.find<TextView>(R.id.tv_content)
        tv_content.text="$position:$data"
        holder.itemView.setOnClickListener {
            listener.invoke(data)
        }

    }

}