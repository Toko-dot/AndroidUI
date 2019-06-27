package com.czy.demo.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.czy.demo.adapter.UiListAdapter
import com.czy.demo.startActivity
import com.czy.ui.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private var datas by Delegates.observable(ArrayList<String>()) { property, oldValue, newValue ->
        adapter.datas = newValue;
        adapter.notifyDataSetChanged()
    }
    private val adapter: UiListAdapter by lazy {
        UiListAdapter(this, datas, R.layout.layout_item_ui) {
            when (it) {
                datas.get(0) -> {
                    startActivity<RuleActivity>()
                }
                datas.get(1) -> {
                    startActivity<HeartActivity>()
                }
                datas.get(2) -> {
                    startActivity<IntroductionActivity>()
                }
                datas.get(3) -> {
                    startActivity<ShakeImgActivity>()
                }
                datas.get(4) -> {
                    startActivity<FlowLayoutActivity>()
                }
                datas.get(5) -> {
                    startActivity<QQCardActivity>()
                }
                datas.get(6)->{
                    startActivity<ManagerLayoutActivity>()
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_ui.layoutManager = LinearLayoutManager(this)
        rv_ui.adapter = adapter
        datas = resources.getStringArray(R.array.list_ui).toList() as ArrayList<String>

    }

    fun onNotify(view: View) {
        adapter.notifyDataSetChanged()
    }


}