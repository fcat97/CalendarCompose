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

package media.uqab.coreAndroid.presentation.calendarCompose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
    val showSelectedDateText by remember(selectedDate, showingMonth) {
        derivedStateOf {
            Calendar.getInstance().apply {
                time = selectedDate
            }.let {
                // true if showing month and year is different from selected date's month and year
                showingMonth != it[Calendar.MONTH] + 1 || showingYear != it[Calendar.YEAR]
            }
        }
    }
    val selectedDateText by remember(selectedDate) {
        derivedStateOf {
            SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH).format(selectedDate)
        }
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

            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(25))
                    .clickable {
                    // take to selected date's month
                    Calendar.getInstance().apply {
                        time = selectedDate
                    }.let {
                        showingMonth = it[Calendar.MONTH] + 1
                        showingYear = it[Calendar.YEAR]
                    }
                },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = currentMonthYearText)

                AnimatedVisibility(visible = showSelectedDateText) {
                    Text(
                        text = selectedDateText,
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.secondary
                    )
                }
            }

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
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)) {
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
                    /*LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        items(items = weekDates) { d ->
                            ItemDate(
                                text = if (d == 0) "" else d.toString(),
                                textColor = if (d == todayDate && showingMonth == thisMonth && showingYear == thisYear) {
                                    MaterialTheme.colors.primary
                                } else Color.Unspecified,
                                isSelected = selectedDate.isSelected(d, showingMonth, showingYear),
                                onClick = {
                                    if (d != 0) onDateChange(showingYear, showingMonth, d)
                                },
                            )
                        }
                    }*/
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
                                    if (d != 0) onDateChange(showingYear, showingMonth, d)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}