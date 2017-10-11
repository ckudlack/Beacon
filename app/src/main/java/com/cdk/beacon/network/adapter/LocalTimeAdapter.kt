package com.cdk.beacon.network.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat

class LocalTimeAdapter {

    @FromJson
    fun fromJson(value: String): LocalTime {
        return LocalTime.parse(value, DateTimeFormat.forPattern("HH:mm"))
    }

    @ToJson
    fun toJson(localTime: LocalTime): String {
        return localTime.toString(DateTimeFormat.forPattern("HH:mm"))
    }
}
