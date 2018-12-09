package com.davidbragadeveloper.filmica.view.utils

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class BaseRecyclerViewScrollListener (
    val layoutManager: RecyclerView.LayoutManager,
    val lastVisibleItemPositionCallback: ()->Int,
    val onLoadMoreCallback : (Int) -> Unit
): RecyclerView.OnScrollListener() {

    var currentPage = 0
    var itemsInLastLoad = 0
    var loading = true
    val startPageIndex = 0
    var visibleItemsBeforeReloading : Int = 5

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = layoutManager.itemCount

        lastVisibleItemPosition= lastVisibleItemPositionCallback.invoke()

        if (loading && (totalItemCount > itemsInLastLoad)) {
            loading = false;
            itemsInLastLoad = totalItemCount;
        }

        if (!loading && (lastVisibleItemPosition + visibleItemsBeforeReloading) > totalItemCount
            && recyclerView.adapter!!.itemCount > visibleItemsBeforeReloading) {
            currentPage++;
            onLoadMoreCallback.invoke(currentPage);
            loading = true;
        }

    }

    fun resetState() {
        this.currentPage = this.startPageIndex
        this.visibleItemsBeforeReloading= 0
        this.loading = true
    }
}

class GridRecyclerViewScrollListener (
    gridLayoutManager: GridLayoutManager,
    onLoadMoreCallback: (Int) -> Unit
): BaseRecyclerViewScrollListener(
    layoutManager = gridLayoutManager,
    lastVisibleItemPositionCallback = {
        gridLayoutManager.findLastVisibleItemPosition()
    },
    onLoadMoreCallback = onLoadMoreCallback
) {

    init {
        visibleItemsBeforeReloading * gridLayoutManager.spanCount
    }

}

class LinearRecyclerViewScrollListener (
    linearLayoutManager: LinearLayoutManager,
    onLoadMoreCallback: (Int) -> Unit
): BaseRecyclerViewScrollListener(
    layoutManager = linearLayoutManager,
    lastVisibleItemPositionCallback = {
        linearLayoutManager.findLastVisibleItemPosition()
    },
    onLoadMoreCallback = onLoadMoreCallback
)







