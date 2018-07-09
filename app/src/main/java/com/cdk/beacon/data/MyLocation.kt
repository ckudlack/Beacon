package com.cdk.beacon.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class MyLocation(val longitude: Double, val latitude: Double, val timeStamp: Long) : Parcelable {

    @IgnoredOnParcel
    @Exclude
    var index: Int? = null

    @IgnoredOnParcel
    @Exclude
    var isSelected: Boolean = false

    @Exclude
    fun getPosition(): LatLng {
        return LatLng(latitude, longitude)
    }

    @Exclude
    fun getSnippet(): String {
        return ""
    }

    @Exclude
    fun getTitle(): String {
        return ""
    }
}
