package com.mml.drc.utils

import android.content.Context
import android.widget.Toast

/**
 * # expandfuns
 *
 * @author 11324
 * 2019/1/25
 */

fun Context.toastLong(msg:String) {
    Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
}
fun Context.toast(msg:String) {
    toastShort(msg)
}
fun Context.toastShort(msg:String) {
    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
}