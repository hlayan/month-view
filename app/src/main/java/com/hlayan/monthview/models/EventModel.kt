package com.hlayan.monthview.models

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class EventModel(
    val eventTitle: String,
    val startDate: LocalDate,
    val endDate: LocalDate
) {
    val dayCount = startDate.until(endDate, ChronoUnit.DAYS) + 1
}
