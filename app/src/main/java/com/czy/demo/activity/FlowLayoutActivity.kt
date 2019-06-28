package com.czy.demo.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.czy.demo.R
import com.czy.ui.FlowLayout.Adapter
import kotlinx.android.synthetic.main.activity_flow_layout.*
import kotlin.properties.Delegates

class FlowLayoutActivity : AppCompatActivity() {
    private var datas:ArrayList<String> by Delegates.observable(ArrayList<String>()){
        property, oldValue, newValue ->
        adapter.setDatas(newValue)
        adapter.notifyChange()
    }
    private val adapter: Adapter<View,String> by lazy {
        FlowAdapter(this, datas)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_layout)
        flowlayout.setAdapter(adapter)
        datas=resources.getStringArray(R.array.list_ui).toList() as ArrayList<String>

    }
    class FlowAdapter(context: Context,datas:ArrayList<String>):Adapter<View,String>(context,datas){
        override fun createItemView(pos: Int, parent: ViewGroup?): View {
           return LayoutInflater.from(context).inflate(R.layout.layout_item_flow,parent) as View
        }

        override fun bindView(view: View?, pos: Int) {
            view?.findViewById<TextView>(R.id.tv_content)?.text=datas.get(pos)
        }

    }

}
