package com.cdk.beacon

import android.content.Context
import android.text.format.DateUtils


class DateTimeUtils {

    companion object {

        fun formatWithWeekday(context: Context, timeStamp: Long): String {
            return format(context, timeStamp, DateUtils.FORMAT_NO_YEAR or DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_WEEKDAY or DateUtils.FORMAT_ABBREV_MONTH or DateUtils.FORMAT_ABBREV_WEEKDAY or DateUtils.FORMAT_SHOW_TIME)
        }

        private fun format(context: Context, timeStamp: Long, flags: Int): String {
            return DateUtils.formatDateTime(context, timeStamp, flags)
        }
    }
}