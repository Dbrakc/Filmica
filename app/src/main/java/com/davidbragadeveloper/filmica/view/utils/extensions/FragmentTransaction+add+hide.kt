package com.davidbragadeveloper.filmica.view.utils.extensions


import android.support.annotation.IdRes
import android.support.v4.app.Fragment

import android.support.v4.app.FragmentTransaction


fun FragmentTransaction.addFragmentsToContainer
            (@IdRes containerView: Int, fragments: Map<String,Fragment>) : FragmentTransaction{
    fragments.forEach {
        this.add(containerView,it.value, it.key)
    }
    return this
}

fun FragmentTransaction.hideFragments(fragments: List<Fragment>): FragmentTransaction{
    fragments.forEach {
        this.hide(it)
    }
    return this
}