package com.cdk.beacon.data

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BeaconTrip(@IgnoredOnParcel val locations: List<MyLocation>, val name: String, var observers: List<String>,
                      val id: String, val userId: String, val beaconFrequency: Int, private val created: Long, private val lastUpdated: Long) : Parcelable {

    fun toFirebaseTrip(): FirebaseTrip {
        val observerMap = mutableMapOf<String, Boolean>()
        observers.forEach { observerMap[sanitizeEmailToFirebaseName(it)] = true }
        return FirebaseTrip(name, userId, observerMap, beaconFrequency, created, lastUpdated)
    }

    companion object {
        fun sanitizeEmailToFirebaseName(string: String): String {
            val lastIndexOf = string.lastIndexOf('.')
            return string.replaceRange(lastIndexOf, lastIndexOf + 1, "@")
        }
    }
}