package com.czy.demo.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.czy.demo.*
import com.czy.demo.adapter.MeiNvListAdapter
import com.czy.demo.adapter.UiListAdapter
import kotlinx.android.synthetic.main.activity_qqcard.*
import java.util.*
import kotlin.properties.Delegates

class QQCardActivity : AppCompatActivity() {
    private var datas by Delegates.observable(ArrayList<String>()) { property, oldValue, newValue ->
        adapter.datas = newValue;
        adapter.notifyDataSetChanged()
    }
    private val adapter: MeiNvListAdapter by lazy {
        MeiNvListAdapter(this, datas, R.layout.layout_item_meinv) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qqcard)
        rv_card.layoutManager = QQLayoutManager(this)

        rv_card.adapter = adapter
        datas = resources.getStringArray(R.array.list_meinv).toList() as ArrayList<String>

        itemTouchHelper.attachToRecyclerView(rv_card)


    }

    fun onLike(view:View){
        if (rv_card.childCount==0) {
            toast("没有了")
            return
        }
        val childAt = rv_card.layoutManager.findViewByPosition(adapter.datas.size-1)?:return
        val animator = ObjectAnimator.ofFloat(childAt, "x", childAt.x, childAt.width.toFloat())
        animator.addListener(object :AnimatorListenerAdapter(){
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                view.isEnabled=false
            }
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                adapter.datas.removeAt(adapter.datas.size-1)
                adapter.notifyDataSetChanged()
                view.isEnabled=true
            }
        })
        animator.setDuration(300)
        animator.start()
    }
    fun onUnLike(view:View){
        if (rv_card.childCount==0) {
            toast("没有了")
            return
        }
        val childAt = rv_card.layoutManager.findViewByPosition(adapter.datas.size-1)?:return


        val animator = ObjectAnimator.ofFloat(childAt, "x", childAt.x, -childAt.width.toFloat())
        animator.addListener(object :AnimatorListenerAdapter(){
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                view.isEnabled=false
            }
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                adapter.datas.removeAt(adapter.datas.size-1)
                adapter.notifyDataSetChanged()
                view.isEnabled=true
            }
        })
        animator.setDuration(300)
        animator.start()
    }


    val itemTouchHelper=ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.DOWN or ItemTouchHelper.UP or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            logs("${viewHolder.layoutPosition},${viewHolder.adapterPosition}")
            datas.removeAt(viewHolder.layoutPosition)
            adapter.datas = datas
            adapter.notifyDataSetChanged()
        }
    })
}
