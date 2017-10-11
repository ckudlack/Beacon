package com.cdk.beacon.data

import com.cdk.bettermapsearch.interfaces.MapClusterItem
import com.google.android.gms.maps.model.LatLng

data class MyLocation(val longitude: Double, val latitude: Double, val timeStamp: Long) : MapClusterItem {

    override var isSelected: Boolean = false

    override var isViewed: Boolean = false

    override fun getPosition(): LatLng {
        return LatLng(latitude, longitude)
    }

    override fun getSnippet(): String {
        return ""
    }

    override fun getTitle(): String {
        return ""
    }

    constructor() : this(0.0, 0.0, System.currentTimeMillis())
}
