package com.cdk.beacon.network.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

class LocalDateTimeAdapter {

    @FromJson
    fun fromJson(value: String): LocalDateTime {
        return LocalDateTime.parse(value, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"))
    }

    @ToJson
    fun toJson(localDateTime: LocalDateTime): String {
        return localDateTime.toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"))
    }
}
