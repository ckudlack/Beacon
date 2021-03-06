package com.cdk.beacon.data

import android.os.Parcelable
import com.cdk.bettermapsearch.interfaces.MapClusterItem
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class MyLocation(val longitude: Double, val latitude: Double, val timeStamp: Long) : MapClusterItem, Parcelable {

    @IgnoredOnParcel
    @Exclude
    var index: Int? = null

    @IgnoredOnParcel
    @Exclude
    override var isSelected: Boolean = false

    @IgnoredOnParcel
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
