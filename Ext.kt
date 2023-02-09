package media.uqab.coreAndroid.presentation.calendarCompose

import java.text.SimpleDateFormat
import java.util.*

internal fun Date.isSelected(date: Int, showingMonth: Int, showingYear: Int): Boolean {
    val c = Calendar.getInstance().apply { time = this@isSelected }
    return c[Calendar.DAY_OF_MONTH] == date && c[Calendar.MONTH] == showingMonth - 1 && c[Calendar.YEAR] == showingYear
}
internal fun getMonthDates(month: Int, year: Int): List<Int> {
    val c = Calendar.getInstance().apply {
        this[Calendar.DAY_OF_MONTH] = 1
        this[Calendar.MONTH] = month - 1
        this[Calendar.YEAR] = year
    }

    val starting = c[Calendar.DAY_OF_WEEK] - 1

    val days = mutableListOf<Int>()
    days += (-1 * starting until 0).map { 0 }
    days += (1..c.getActualMaximum(Calendar.DAY_OF_MONTH))
    days += (days.size..42).map { 0 }

    return days
}
internal fun getWeekDayText(dayOfWeek: Int): Char = when(dayOfWeek) {
    1 -> 'S'
    2 -> 'M'
    3 -> 'T'
    4 -> 'W'
    5 -> 'T'
    6 -> 'F'
    else -> 'S'
}

private val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
internal fun getMonthName(m: Int, y: Int) = Calendar.getInstance().apply {
    set(y, m - 1, 1)
}.let {
    monthFormat.format(it.time)
}

internal val today = Calendar.getInstance()
internal val todayDate get() = today[Calendar.DAY_OF_MONTH]
internal val thisMonth get() = today[Calendar.MONTH] + 1
internal val thisYear get() = today[Calendar.YEAR]