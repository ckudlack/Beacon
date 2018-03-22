package com.cdk.beacon

import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import org.joda.time.LocalDate
import java.util.*


class DateTimeUtils {

    companion object {

        fun formatWithWeekday(context: Context, timeStamp: Long): String {
            return format(context, timeStamp, DateUtils.FORMAT_NO_YEAR or DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_WEEKDAY or DateUtils.FORMAT_ABBREV_MONTH or DateUtils.FORMAT_ABBREV_WEEKDAY or DateUtils.FORMAT_SHOW_TIME)
        }

        fun formatNumericDate(context: Context, date: LocalDate): String {
            return format(context, date, DateUtils.FORMAT_NUMERIC_DATE)
        }

        fun formatWithDayAndMonth(timeStamp: Long): String {
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = timeStamp
            return DateFormat.format("M/dd", cal).toString()
        }

        fun formatWithTimeOnly(timeStamp: Long): String {
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = timeStamp
            return DateFormat.format("h:mm aa", cal).toString()
        }

        fun formatMarkerLabel(timeStamp: Long): String {
            return when (isToday(timeStamp)) {
                true -> formatWithTimeOnly(timeStamp)
                false -> formatWithDayAndMonth(timeStamp)
            }
        }

        fun isToday(timeStamp: Long): Boolean {
            val smsTime = Calendar.getInstance()
            smsTime.timeInMillis = timeStamp

            val now = Calendar.getInstance()
            return now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)
        }

        private fun format(context: Context, timeStamp: Long, flags: Int): String {
            return DateUtils.formatDateTime(context, timeStamp, flags)
        }

        private fun format(context: Context, date: LocalDate, flags: Int): String {
            return DateUtils.formatDateTime(context, date.toDateTimeAtStartOfDay().millis, flags)
        }
    }
}