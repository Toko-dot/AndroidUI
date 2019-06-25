package com.czy.demo

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Administrator on 2019/6/21.
 */
abstract class BaseRvAdapter<T>(val context: Context, var datas: ArrayList<T> = arrayListOf(), val layout: Int) : RecyclerView.Adapter<BaseRvAdapter<T>.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindView(holder,datas.get(position))
    }

    abstract fun bindView(holder: ViewHolder,data: T)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun <V : View> find(id: Int): V {
            return itemView.findViewById(id) as V
        }
    }
}