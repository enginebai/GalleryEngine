package com.enginebai.gallery.ui

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.core.KoinComponent

class GridSpaceDecoration(context: Context, @DimenRes private val dimenRes: Int) : RecyclerView.ItemDecoration() {

    private val spacing = context.resources.getDimensionPixelSize(dimenRes)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val totalSpanCount = getTotalSpanCount(parent)

        outRect.top = if (isInTheFirstRow(position, totalSpanCount)) 0 else spacing / 2
        outRect.left = if (isFirstInRow(position, totalSpanCount)) 0 else spacing / 2
        outRect.right = if (isLastInRow(position, totalSpanCount)) 0 else spacing / 2
        outRect.bottom = if (isInTheLastRow(
                position,
                totalSpanCount,
                parent.adapter?.itemCount ?: 0
            )
        ) 0 else spacing / 2
    }

    private fun isInTheFirstRow(position: Int, spanCount: Int): Boolean {
        return position < spanCount
    }

    private fun isInTheLastRow(position: Int, spanCount: Int, itemCount: Int): Boolean {
        return (itemCount - position) <= spanCount
    }

    private fun isFirstInRow(position: Int, spanCount: Int): Boolean {
        return position % spanCount == 0
    }

    private fun isLastInRow(position: Int, spanCount: Int): Boolean {
        return isFirstInRow(position + 1, spanCount)
    }

    private fun getTotalSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        return (layoutManager as? GridLayoutManager)?.spanCount ?: 1
    }
}

class MarginItemDecoration(context: Context, @DimenRes private val dimenRes: Int) : RecyclerView.ItemDecoration(),
    KoinComponent {

    private val spaceHeight = context.resources.getDimensionPixelSize(dimenRes)
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            top = spaceHeight
            left =  spaceHeight
            right = spaceHeight
            bottom = spaceHeight
        }
    }
}
