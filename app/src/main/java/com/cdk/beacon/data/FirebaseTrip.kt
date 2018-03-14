package com.cdk.beacon.data

/**
 * This exists to protect local data classes from changes on the server-side data
 */
data class FirebaseTrip(val name: String, val userId: String, val observers: Map<String, Boolean>?) {
    // This needs to be used by Firebase to create the object
    constructor() : this("", "", mapOf())

    fun toBeaconTrip(tripId: String): BeaconTrip {
        val observersList = mutableListOf<String>()
        observers?.forEach { observersList.add(it.key) }
        return BeaconTrip(mutableListOf(), name, observersList, tripId, userId)
    }
}