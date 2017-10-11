package com.cdk.beacon.data

data class MyLocation(val longitude: Double, val latitude: Double, val timeStamp: Long) {
    constructor() : this(0.0, 0.0, System.currentTimeMillis())
}
