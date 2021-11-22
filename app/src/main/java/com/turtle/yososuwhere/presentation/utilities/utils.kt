package com.turtle.yososuwhere.presentation.utilities

import com.turtle.yososuwhere.presentation.utilities.extensions.getCalendarWithoutTime
import java.util.*


// 현재 Year
fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

// 현재 Month
fun getCurrentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1

// 현재 Day
fun getCurrentDay(): Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

// 날짜 사이 리스트 반환
fun getDateListBetween(startDate: Date, endDate: Date): List<Date> {
    val calendar: Calendar = startDate.getCalendarWithoutTime()
    val endCalendar: Calendar = endDate.getCalendarWithoutTime().apply {
        // 마지막 날 까지 포함
        add(Calendar.DATE, 1)
    }
    return arrayListOf<Date>().apply {
        while (calendar.before(endCalendar)) {
            val result = calendar.time
            this.add(result)
            calendar.add(Calendar.DATE, 1)
        }
    }
}