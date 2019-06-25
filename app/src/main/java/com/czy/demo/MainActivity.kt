package com.czy.demo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.czy.ui.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private var datas by Delegates.observable(ArrayList<String>()){
        property, oldValue, newValue ->
        adapter.datas=newValue;
        adapter.notifyDataSetChanged()
    }
    private val adapter:UiListAdapter by lazy {
        UiListAdapter(this,datas,R.layout.layout_item_ui){
              when(it){
                  datas.get(0)->{
                      startActivity(Intent(this@MainActivity,RuleActivity::class.java))
                  }
                  datas.get(1)->{
                      startActivity(Intent(this@MainActivity,HeartActivity::class.java))
                  }
                  datas.get(2)->{
                      startActivity(Intent(this@MainActivity,IntroductionActivity::class.java))

                  }
                  datas.get(3)->{
                      startActivity(Intent(this@MainActivity,ShakeImgActivity::class.java))
                  }
              }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_ui.layoutManager=LinearLayoutManager(this)
        rv_ui.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        rv_ui.adapter=adapter

        datas=resources.getStringArray(R.array.list_ui).toList() as ArrayList<String>
    }
}
