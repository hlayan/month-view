package com.shan.monthviewtest.view

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.shan.monthviewtest.models.EventModel
import java.time.LocalDate

class MonthViewEvent(
    context: Context, attrs: AttributeSet?, defStyle: Int
) : View(context, attrs, defStyle) {

    private var gridWidth = 0F
    private var gridHeight = 0F

    private val rowCount = 6
    private val columnCount = 7
    private val eventMargin = 8

    private val arrayList = arrayListOf<Int>()
    private val rect = Rect()

    init {
        for (index in 0..41) {
            arrayList.add(index, 2)
        }
    }

    private val eventModels = arrayListOf(
        getEventModel(4, 12),
        getEventModel(20, 21),
        getEventModel(18, 20),
        getEventModel(11, 14),
        getEventModel(11, 13),
        getEventModel(11, 17),
        getEventModel(11, 12),
    )

    private val localDateList = LocalDate.now().dayModels

    private val LocalDate.dayModels
        get(): ArrayList<LocalDate> {

            val selectedLocalDate = withDayOfMonth(1)
            val firstWeekDay = selectedLocalDate.dayOfWeek.value
            val dayModelList = arrayListOf<LocalDate>()

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
                dayModelList.add(localDate)
            }
            return dayModelList
        }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun onDraw(canvas: Canvas) {
        gridWidth = (width / columnCount).toFloat()
        gridHeight = (height / rowCount).toFloat()
        canvas.drawEvents()
    }

    fun updateEvent(events: ArrayList<EventModel>) {
        eventModels.clear()
        eventModels.addAll(events)
        eventModels.sortByDescending { it.dayCount }
        eventModels.sortBy { it.startDate }
        invalidate()
    }

    private fun Canvas.drawEvents() {

        eventModels.sortByDescending { it.dayCount }
        eventModels.sortBy { it.startDate }

        eventModels.forEach { eventModel ->

            val index = localDateList.indexOf(eventModel.startDate)

//            val index = eventModels.indexOfFirst { it.startDate == eventModel.startDate }

            if (index == -1) return@forEach

            drawEvent(index, eventModel)

            for (i in 0 until eventModel.dayCount) {
                val position = index + i.toInt()
                if (position < 42) arrayList[position]++
            }

        }

    }

    private fun Canvas.drawEvent(index: Int, eventModel: EventModel) {

        val rowIndex = index / 7L
        val columnIndex = index % 7L

        val x = columnIndex * gridWidth
        val y = rowIndex * gridHeight

        val nextLineEventList = getNextLineEventList(columnIndex, eventModel.dayCount)

        var localIndex = 0L
        nextLineEventList.forEachIndexed { i, count ->

            localIndex = when (i) {
                0 -> index.toLong()
                1 -> localIndex + count
                else -> localIndex + 7
            }

            val alreadyEvent = arrayList[localIndex.toInt()]

            val left = if (i == 0) x else 0F
            val top = (y + ((gridHeight / 7) * alreadyEvent)) + (gridHeight * i)
            val right = left + (count * gridWidth)
            val bottom = top + (gridHeight / 7.5F)

            if (top > height) return@forEachIndexed

            drawEventBackground(left, top, right, bottom)

            val centerVerticalY = top + ((bottom - top) / 2)
            val textPaint = Color.WHITE.textPaint
            drawEventTitle(eventModel.eventTitle, left, centerVerticalY, right - left, textPaint)
        }

    }

    private fun getNextLineEventList(columnIndex: Long, dayCount: Long): ArrayList<Long> {
        val totalCount = dayCount + columnIndex
        val arrayList = arrayListOf<Long>()
        if (totalCount > 7) {
            for (index in 1..(totalCount / 7)) {
                val checkedCount = if (index == 1L) (7L - columnIndex) else 7
                arrayList.add(checkedCount)
            }
            val remainCount = totalCount % 7
            if (remainCount != 0L) arrayList.add(remainCount)
        } else {
            arrayList.add(dayCount)
        }
        return arrayList
    }

    private fun Canvas.drawEventTitle(
        text: String,
        x: Float,
        y: Float,
        eventWidth: Float,
        textPaint: TextPaint
    ) {
        textPaint.getTextBounds(text, 0, text.length, rect)
        val centerY = y - rect.exactCenterY()
        val centerX = x + (30 / 2)

        val ellipsized = TextUtils.ellipsize(
            text, textPaint,
            eventWidth - eventMargin,
            TextUtils.TruncateAt.END
        )
        drawText(text, 0, ellipsized.length, centerX, centerY, textPaint)
    }

    private fun Canvas.drawEventBackground(left: Float, top: Float, right: Float, bottom: Float) {
        val rectF = RectF(left + eventMargin, top, right - eventMargin, bottom)
        val eventPaint = Color.parseColor("#308CCD").textPaint
        drawRoundRect(rectF, 8F, 8F, eventPaint)
    }

    private val Int.textPaint
        get(): TextPaint {
            val tp = TextPaint(Paint.ANTI_ALIAS_FLAG)
            tp.color = this
            tp.textSize = 30F
            return tp
        }

    private fun getEventModel(from: Int, to: Int): EventModel {
        return EventModel(
            "This is test.",
            LocalDate.of(2021, 5, from),
            LocalDate.of(2021, 5, to)
        )
    }

}