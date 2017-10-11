package com.cdk.beacon.network.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

import java.math.BigDecimal

class BigDecimalAdapter {

    @FromJson
    fun fromJson(value: String): BigDecimal {
        return BigDecimal(value)
    }

    @ToJson
    fun toJson(bigDecimal: BigDecimal): String {
        return bigDecimal.toString()
    }
}
