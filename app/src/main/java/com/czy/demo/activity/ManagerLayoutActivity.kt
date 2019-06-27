package com.czy.demo.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.czy.demo.QQLayoutManager
import com.czy.demo.R
import com.czy.demo.adapter.UiListAdapter
import com.czy.demo.logs
import kotlinx.android.synthetic.main.activity_manager_layout.*
import kotlinx.android.synthetic.main.activity_qqcard.*
import java.util.*
import kotlin.properties.Delegates

/**
 * 动态管理布局
 */
class ManagerLayoutActivity : AppCompatActivity() {
    private var datas by Delegates.observable(ArrayList<String>()) { property, oldValue, newValue ->
        adapter.datas = newValue
        adapter.notifyDataSetChanged()
    }
    private val adapter: UiListAdapter by lazy {
        UiListAdapter(this, datas, R.layout.layout_item_ui) {

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_layout)
        rv_manager.layoutManager = GridLayoutManager(this,2)

        rv_manager.adapter = adapter
        datas = resources.getStringArray(R.array.list_manager_layout).toList() as ArrayList<String>

        itemTouchHelper.attachToRecyclerView(rv_manager)

    }

    val itemTouchHelper= ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN or ItemTouchHelper.UP or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0) {
        override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
            val fromPosition = viewHolder!!.adapterPosition
            val toPosition = target!!.adapterPosition
            adapter.notifyItemMoved(fromPosition,toPosition)
            //数据改变
            for(i in fromPosition..(toPosition-1)){
                if (fromPosition<toPosition)
                    Collections.swap(adapter.datas,i,i+1)
                else
                    Collections.swap(adapter.datas,i,i-1)
            }
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }
    })
}
