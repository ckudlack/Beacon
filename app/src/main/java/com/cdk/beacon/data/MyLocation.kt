package com.cdk.beacon.data

import com.cdk.bettermapsearch.interfaces.MapClusterItem
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class MyLocation(val longitude: Double, val latitude: Double, val timeStamp: Long) : MapClusterItem {

    @Exclude
    var index: Int? = null

    @Exclude
    override var isSelected: Boolean = false

    @Exclude
    override var isViewed: Boolean = false

    @Exclude
    override fun getPosition(): LatLng {
        return LatLng(latitude, longitude)
    }

    @Exclude
    override fun getSnippet(): String {
        return ""
    }

    @Exclude
    override fun getTitle(): String {
        return ""
    }
}
