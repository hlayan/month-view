package com.hlayan.monthview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

class MonthViewHeader(
    context: Context, attrs: AttributeSet, defStyle: Int
) : View(context, attrs, defStyle) {

    private val rowCount = 1
    private val columnCount = 7
    private val rect = Rect()

    private var gridWidth = 0F
    private var gridHeight = 0F

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    override fun onDraw(canvas: Canvas) {
        gridWidth = (width / columnCount).toFloat()
        gridHeight = (height / rowCount).toFloat()
        canvas.drawGrids()
        canvas.drawTitles()
    }

    private fun Canvas.drawTitles() {
        val extraX = gridWidth / 2
        val extraY = gridHeight / 2

        val headers = listOf("S", "M", "T", "W", "T", "F", "S")

        headers.forEachIndexed { index, title ->
            val x = (index * gridWidth) + extraX
            val textPaint = Color.BLACK.textPaint
            drawTextCentred(title, x, extraY, textPaint)
        }
    }

    private fun Canvas.drawGrids() {

        val gridPaint = Paint().apply {
            color = Color.parseColor("#80D3D3D3")
            strokeWidth = 3F
        }

        //Vertical lines
        for (column in 1 until columnCount) {
            val x = column * gridWidth
            drawLine(x, 0F, x, height.toFloat(), gridPaint)
        }

        //Top line
        val topY = 1.5F
        drawLine(0F, topY, width.toFloat(), topY, gridPaint)

        //Bottom line
        val bottomY = gridHeight - 1.5F
        drawLine(0F, bottomY, width.toFloat(), bottomY, gridPaint)

    }

    private fun Canvas.drawTextCentred(text: String, x: Float, y: Float, paint: TextPaint) {
        paint.getTextBounds(text, 0, text.length, rect)
        val centerX = x - rect.exactCenterX()
        val centerY = y - rect.exactCenterY()
        drawText(text, centerX, centerY, paint)
    }

    private val Int.textPaint
        get(): TextPaint {
            val tp = TextPaint(Paint.ANTI_ALIAS_FLAG)
            tp.color = this
            tp.textSize = 30F
            return tp
        }

}