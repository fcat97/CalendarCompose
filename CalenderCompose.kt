/*MIT License

Copyright (c) [2023] [github/fCat97]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/

package media.uqab.coreAndroid.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun CalendarCompose(
    modifier: Modifier = Modifier,
    selectedDate: Date = Date(),
    onDateChange: (y: Int, m: Int, d: Int) -> Unit = { _, _, _ -> }
) {
    var showingMonth by remember {
        mutableStateOf(Calendar.getInstance()[Calendar.MONTH] + 1)
    }
    var showingYear by remember {
        mutableStateOf(Calendar.getInstance()[Calendar.YEAR])
    }
    val currentMonthYearText by remember(showingMonth, showingYear) {
        mutableStateOf(getMonthName(showingMonth, showingYear) + " " + String.format("%4d", showingYear))
    }
    val showingDates by remember(showingMonth, showingYear) {
        mutableStateOf(getMonthDates(showingMonth, showingYear))
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // previous month button
            Surface(
                shape = CircleShape,
                onClick = {
                    if (showingMonth == 1) {
                        showingYear -= 1
                        showingMonth = 12
                    } else showingMonth -= 1
                },
            ) {
                Image(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "previous month",
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(text = currentMonthYearText)

            Surface(
                shape = CircleShape,
                onClick = {
                    if (showingMonth == 12) {
                        showingYear += 1
                        showingMonth = 1
                    } else showingMonth += 1
                }
            ) {
                Image(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "next month",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // dates
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 1..7) {
                    ItemDate(text = getWeekDayText(i).toString(), onClick = {})
                }
            }

            for (r in 1..6) {
                val st = (r - 1) * 7
                val weekDates = showingDates.subList(st, st + 7)

                if (weekDates.any { it != 0 }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (d in weekDates) {
                            ItemDate(
                                text = if (d == 0) "" else d.toString(),
                                textColor = if (d == todayDate && showingMonth == thisMonth && showingYear == thisYear) {
                                    MaterialTheme.colors.primary
                                } else Color.Unspecified,
                                isSelected = selectedDate.isSelected(d, showingMonth, showingYear),
                                onClick = {
                                    onDateChange(showingYear, showingMonth, d)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemDate(
    text: String,
    textColor: Color = Color.Unspecified,
    isSelected: Boolean = false,
    onClick: (d: String) -> Unit,
) {
    Box(
        modifier = Modifier
            .requiredSize(32.dp)
            .background(
                color = if (isSelected) MaterialTheme.colors.secondary else Color.Transparent,
                shape = CircleShape
            )
            .clip(shape = CircleShape)
            .clickable { onClick(text) },
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, color = if (isSelected) MaterialTheme.colors.onPrimary else textColor)
    }

}

private fun Date.isSelected(date: Int, showingMonth: Int, showingYear: Int): Boolean {
    val c = Calendar.getInstance().apply { time = this@isSelected }
    return c[Calendar.DAY_OF_MONTH] == date && c[Calendar.MONTH] == showingMonth - 1 && c[Calendar.YEAR] == showingYear
}
private fun getMonthDates(month: Int, year: Int): List<Int> {
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
private fun getWeekDayText(dayOfWeek: Int): Char = when(dayOfWeek) {
    1 -> 'S'
    2 -> 'M'
    3 -> 'T'
    4 -> 'W'
    5 -> 'T'
    6 -> 'F'
    else -> 'S'
}

private val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
private fun getMonthName(m: Int, y: Int) = Calendar.getInstance().apply {
    set(y, m - 1, 1)
}.let {
    monthFormat.format(it.time)
}

private val today = Calendar.getInstance()
private val todayDate get() = today[Calendar.DAY_OF_MONTH]
private val thisMonth get() = today[Calendar.MONTH] + 1
private val thisYear get() = today[Calendar.YEAR]