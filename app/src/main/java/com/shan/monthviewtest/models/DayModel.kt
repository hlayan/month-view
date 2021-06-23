package com.shan.monthviewtest.models

import java.time.DayOfWeek
import java.time.LocalDate

data class DayModel(
    val selectedLocalDate: LocalDate,
    val localDate: LocalDate
) {

    val firstWeekDay
        get():Int {
            val weekCount = selectedLocalDate.dayOfWeek.value
            return if (weekCount == 7) 0 else weekCount
        }

    val isNotThatMonth = localDate.monthValue != selectedLocalDate.monthValue

    val isToday = localDate.isEqual(LocalDate.now())

    val day = localDate.dayOfMonth

    val isSunday = localDate.dayOfWeek == DayOfWeek.SUNDAY
}