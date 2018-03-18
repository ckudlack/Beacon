package com.cdk.beacon.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BeaconTrip(val locations: List<MyLocation>, val name: String, val observers: List<String>, val id: String, val userId: String, val beaconFrequency: Int) : Parcelable {

    fun toFirebaseTrip(): FirebaseTrip {
        val observerMap = mutableMapOf<String, Boolean>()
        observers.forEach { observerMap[it] = true }
        return FirebaseTrip(name, userId, observerMap, beaconFrequency)
    }
}