package com.shan.monthviewtest.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.shan.monthviewtest.models.DayModel
import java.time.LocalDate

class MonthView(
    context: Context, attrs: AttributeSet?, defStyle: Int
) : View(context, attrs, defStyle) {

    private val dayModels = LocalDate.now().dayModels
    private val rect = Rect()

    private val rowCount = 6
    private val columnCount = 7

    private var gridWidth = 0F
    private var gridHeight = 0F

    private val LocalDate.dayModels
        get(): ArrayList<DayModel> {

            val selectedLocalDate = withDayOfMonth(1)
            val firstWeekDay = selectedLocalDate.dayOfWeek.value
            val dayModelList = arrayListOf<DayModel>()

            for (index in 0..41L) {
                val localDate = if (firstWeekDay == 7) {
                    selectedLocalDate.plusDays(index)
                } else {
                    if (index < firstWeekDay) {
                        selectedLocalDate.minusDays((firstWeekDay - index))
                    } else {
                        selectedLocalDate.plusDays((index - firstWeekDay))
                    }
                }
                val dayModel = DayModel(selectedLocalDate, localDate)
                dayModelList.add(dayModel)
            }
            return dayModelList
        }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun onDraw(canvas: Canvas) {
        gridWidth = (width / columnCount).toFloat()
        gridHeight = (height / rowCount).toFloat()
        canvas.drawGrids()
        canvas.drawDayNumbers()
    }

    fun updateDate(localDate: LocalDate) {
        dayModels.clear()
        dayModels.addAll(localDate.dayModels)
        invalidate()
    }

    fun getSelectedDate(index: Int) = dayModels[index].localDate

    private fun Canvas.drawDayNumbers() {

        val extraX = gridWidth / 2
        val extraY = gridHeight / 7

        dayModels.forEachIndexed { index, dayModel ->
            val x = ((index % 7) * gridWidth) + extraX
            val y = ((index / 7) * gridHeight) + extraY

            val textPaint = when {
                dayModel.isToday -> {
                    val paint = Color.parseColor("#308CCD").textPaint
                    drawCircle(x, y, 30F, paint)
                    Color.WHITE.textPaint
                }
                dayModel.isSunday -> Color.RED.textPaint
                dayModel.isNotThatMonth -> Color.parseColor("#808080").textPaint
                else -> Color.BLACK.textPaint
            }

            val dayNumber = dayModel.day.toString()
            drawTextCentred(dayNumber, x, y, textPaint)
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

        //Horizontal lines
        for (row in 1 until rowCount) {
            val y = row * gridHeight
            drawLine(0F, y, width.toFloat(), y, gridPaint)
        }

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
