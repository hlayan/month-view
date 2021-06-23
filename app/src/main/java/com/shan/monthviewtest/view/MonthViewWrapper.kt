package com.shan.monthviewtest.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout

class MonthViewWrapper(
    context: Context, attrs: AttributeSet, defStyle: Int
) : FrameLayout(context, attrs, defStyle) {

    private var onClick: (Int) -> Unit = {}

    private var selectedPosition = 0
    private val rowCount = 6
    private val columnCount = 7

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    init {
        onGlobalLayout { addClickableViews() }
    }

    fun setOnDaySelectListener(onClick: (Int) -> Unit) {
        this.onClick = onClick
    }

    private fun addClickableViews() {
        val gridWidth = width / columnCount
        val gridHeight = height / rowCount
        for (index in 0..41) {
            val x = (index % 7) * gridWidth
            val y = (index / 7) * gridHeight
            val childView = View(context)
            childView.x = x.toFloat()
            childView.y = y.toFloat()
            childView.addRipple()
            childView.setOnClickListener { it.selectDay(index) }
            addView(childView, gridWidth, gridHeight)
        }
    }

    private fun View.selectDay(index: Int) {
        if (index != selectedPosition) {
            val selectedView = getChildAt(selectedPosition)
            selectedView.addRipple()
            selectedPosition = index
            setBackgroundColor(Color.parseColor("#1F000000"))
        } else {
            onClick(index)
        }
    }

    private fun View.onGlobalLayout(callback: () -> Unit) {
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                callback()
            }
        })
    }

    private fun View.addRipple() = with(TypedValue()) {
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
        setBackgroundResource(resourceId)
    }
}