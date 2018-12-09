package com.davidbragadeveloper.filmica.view.utils.listeners

import android.support.v7.widget.GridLayoutManager

class GridPaginatorRecyclerScrollListener (
    gridLayoutManager: GridLayoutManager,
    onLoadMoreCallback: (Int) -> Unit
    ): BasePaginatorRecyclerScrollListener(
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