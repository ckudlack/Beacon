package com.cdk.beacon.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BeaconTrip(val locations: List<MyLocation>, val name: String, var observers: List<String>, val id: String, val userId: String, val beaconFrequency: Int) : Parcelable {

    fun toFirebaseTrip(): FirebaseTrip {
        val observerMap = mutableMapOf<String, Boolean>()
        observers.forEach { observerMap[sanitizeEmailToFirebaseName(it)] = true }
        return FirebaseTrip(name, userId, observerMap, beaconFrequency)
    }

    companion object {
        fun sanitizeEmailToFirebaseName(string: String): String {
            val lastIndexOf = string.lastIndexOf('.')
            return string.replaceRange(lastIndexOf, lastIndexOf + 1, "@")
        }
    }
}