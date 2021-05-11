package kr.rowan.digital_museum_gyeonggi

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class VisiblePositionChangeListener : RecyclerView.OnScrollListener {
    interface OnChangeListener {
        fun onFirstVisiblePositionChanged(position: Int)
        fun onLastVisiblePositionChanged(position: Int)
        fun onFirstInvisiblePositionChanged(position: Int)
        fun onLastInvisiblePositionChanged(position: Int)
    }

    private var firstVisiblePosition: Int
    private var lastVisiblePosition: Int
    private val listener: OnChangeListener
    private var layoutManager: LinearLayoutManager

    constructor(linearLayoutManager: LinearLayoutManager, listener: OnChangeListener) {
        this.listener = listener
        firstVisiblePosition = RecyclerView.NO_POSITION
        lastVisiblePosition = RecyclerView.NO_POSITION
        layoutManager = linearLayoutManager
    }

    constructor(
        linearLayoutManager: LinearLayoutManager,
        elevation: ImageView?,
        listener: OnChangeListener
    ) {
        this.listener = listener
        firstVisiblePosition = RecyclerView.NO_POSITION
        lastVisiblePosition = RecyclerView.NO_POSITION
        layoutManager = linearLayoutManager
    }

    fun refreshVisiblePosition() {
        firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val firstPosition = layoutManager.findFirstVisibleItemPosition()
        val lastPosition = layoutManager.findLastVisibleItemPosition()
        if (firstVisiblePosition == RecyclerView.NO_POSITION || lastVisiblePosition == RecyclerView.NO_POSITION) {
            firstVisiblePosition = firstPosition
            lastVisiblePosition = lastPosition
            return
        }
        if (firstPosition < firstVisiblePosition) {
            if (firstVisiblePosition - firstPosition > 1) {
                for (i in 1 until firstVisiblePosition - firstPosition + 1) {
                    listener.onFirstVisiblePositionChanged(firstVisiblePosition - i)
                }
            } else {
                listener.onFirstVisiblePositionChanged(firstPosition)
            }
            firstVisiblePosition = firstPosition
        } else if (firstPosition > firstVisiblePosition) {
            if (firstPosition - firstVisiblePosition > 1) {
                for (i in firstPosition - firstVisiblePosition downTo 1) {
                    listener.onFirstInvisiblePositionChanged(firstPosition - i)
                }
            } else {
                listener.onFirstInvisiblePositionChanged(firstVisiblePosition)
            }
            firstVisiblePosition = firstPosition
        }
        if (lastPosition > lastVisiblePosition) {
            if (lastPosition - lastVisiblePosition > 1) {
                for (i in 1 until lastPosition - lastVisiblePosition + 1) {
                    listener.onLastVisiblePositionChanged(lastVisiblePosition + i)
                }
            } else {
                listener.onLastVisiblePositionChanged(lastPosition)
            }
            lastVisiblePosition = lastPosition
        } else if (lastPosition < lastVisiblePosition) {
            if (lastVisiblePosition - lastPosition > 1) {
                for (i in 0 until lastVisiblePosition - lastPosition) {
                    listener.onLastInvisiblePositionChanged(lastVisiblePosition - i)
                }
            } else {
                listener.onLastInvisiblePositionChanged(lastVisiblePosition)
            }
            lastVisiblePosition = lastPosition
        }
    }
}
