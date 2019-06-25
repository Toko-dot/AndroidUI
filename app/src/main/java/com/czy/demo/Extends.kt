package com.czy.demo

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * Created by Administrator on 2019/6/25.
 */
fun Context.toast(msg:Any){
    Toast.makeText(this,msg.toString(),Toast.LENGTH_SHORT).show()
}

fun Any.logs(msg:Any){
    Log.d("DebugLog",msg.toString())
}