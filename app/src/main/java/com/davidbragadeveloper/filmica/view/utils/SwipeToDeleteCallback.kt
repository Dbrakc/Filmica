package com.davidbragadeveloper.filmica.view.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.davidbragadeveloper.filmica.R

abstract class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){

    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {


        val context = recyclerView.context
        val itemView = viewHolder.itemView

        setUpBackground(context, viewHolder, dX, c)
        setUpIcon(context, itemView, c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun setUpIcon(context: Context, itemView: View, c: Canvas) {
        val checkIcon = ContextCompat.getDrawable(context, R.drawable.ic_check)!!
        val iconMargin = (itemView.height - checkIcon.intrinsicHeight) / 3
        val iconTop = itemView.top + (itemView.height - checkIcon.intrinsicHeight) / 2
        val iconLeft = itemView.left + iconMargin
        val iconRight = iconLeft + checkIcon.intrinsicWidth
        val iconBottom = iconTop + checkIcon.intrinsicHeight

        checkIcon.setBounds(
            iconLeft,
            iconTop,
            iconRight,
            iconBottom
        )

        checkIcon.draw(c)
    }

    private fun setUpBackground(
        context: Context,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        c: Canvas
    ) {
        val color = ContextCompat.getColor(context, R.color.colorPrimary)
        val background = ColorDrawable(color)
        background.setBounds(
            viewHolder.itemView.left,
            viewHolder.itemView.top,
            viewHolder.itemView.left + dX.toInt(),
            viewHolder.itemView.bottom
        )

        background.draw(c)
    }


}