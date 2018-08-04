package com.cdk.beacon

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.text.style.RelativeSizeSpan
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import java.util.*


class DateTimeUtils {

    companion object {

        fun formatWithWeekday(context: Context, timeStamp: Long): String {
            return format(context, timeStamp, DateUtils.FORMAT_NO_YEAR or DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_WEEKDAY or DateUtils.FORMAT_ABBREV_MONTH or DateUtils.FORMAT_ABBREV_WEEKDAY or DateUtils.FORMAT_SHOW_TIME)
        }

        fun formatWithDayAndMonth(timeStamp: Long): SpannableStringBuilder {
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = timeStamp

            val date = DateFormat.format("M/dd", cal)

            val builder = SpannableStringBuilder(date)
//            builder.setSpan(RelativeSizeSpan(0.65f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//            builder.setSpan(RelativeSizeSpan(0.65f), 2, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//            builder.setSpan(SuperscriptSpan(), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//            builder.setSpan(SubscriptSpan(), 2, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            return builder
        }

        fun formatWithTimeOnly(timeStamp: Long): SpannableStringBuilder {
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = timeStamp
            val time = DateFormat.format("h:mm aa", cal).toString()

            val builder = SpannableStringBuilder(time)
            builder.setSpan(RelativeSizeSpan(0.7f), time.length - 3, time.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            return builder
        }

        fun formatMarkerLabel(timeStamp: Long): SpannableStringBuilder {
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

        // Apr 13 12:00 AM
        fun format(context: Context, timeStamp: Long): String {
//            val time = Calendar.getInstance()
//            time.timeInMillis = timeStamp

            return format(context, timeStamp, DateUtils.FORMAT_NO_YEAR or DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_ABBREV_MONTH or DateUtils.FORMAT_SHOW_TIME)
        }

        private fun format(context: Context, date: LocalDateTime, flags: Int): String {
            return DateUtils.formatDateTime(context, date.toDateTime().millis, flags)
        }
    }
}