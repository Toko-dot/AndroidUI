package com.czy.demo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

/**
 * Created by Administrator on 2019/6/25.
 */
public inline fun Context.toast(msg:Any){
    Toast.makeText(this,msg.toString(),Toast.LENGTH_SHORT).show()
}
public inline fun  <reified cls>Context.startActivity(){
    startActivity(Intent(this,cls::class.java))
}

public inline fun Any.logs(msg:Any){
    Log.d("DebugLog",msg.toString())
}