package com.cdk.beacon.data

/**
 * This exists to protect local data classes from changes on the server-side data
 */
@Suppress("unused")
data class FirebaseTrip(val name: String, val userId: String, val observers: Map<String, Boolean>?,
                        val beaconFrequency: Int, val created: Long, val lastUpdated: Long) {
    // This needs to be used by Firebase to create the object
    constructor() : this("", "", mapOf(), 0, 0, 0)

    fun toBeaconTrip(tripId: String): BeaconTrip {
        val observersList = mutableListOf<String>()
        observers?.forEach {
            observersList.add(sanitizeFirebaseNameToEmail(it.key))
        }
        return BeaconTrip(mutableListOf(), name, observersList, tripId, userId, beaconFrequency, created, lastUpdated)
    }

    private fun sanitizeFirebaseNameToEmail(string: String): String {
        val lastIndexOf = string.lastIndexOf('@')
        return string.replaceRange(lastIndexOf, lastIndexOf + 1, ".")
    }
}