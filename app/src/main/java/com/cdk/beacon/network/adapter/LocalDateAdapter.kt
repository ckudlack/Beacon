package com.cdk.beacon.network.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

class LocalDateAdapter {

    @FromJson
    fun fromJson(value: String): LocalDate {
        return LocalDate.parse(value, DateTimeFormat.forPattern("yyyy-MM-dd"))
    }

    @ToJson
    fun toJson(localDate: LocalDate): String {
        return localDate.toString(DateTimeFormat.forPattern("yyyy-MM-dd"))
    }
}
