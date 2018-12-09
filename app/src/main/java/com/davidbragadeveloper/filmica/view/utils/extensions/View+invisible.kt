package com.davidbragadeveloper.filmica.view.utils.extensions

import android.view.View

fun View.makeInvisible(list: List<View>){
    list.forEach { it.visibility = View.INVISIBLE }
}