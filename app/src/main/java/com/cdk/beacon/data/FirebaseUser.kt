package com.cdk.beacon.data

@Suppress("unused")
data class FirebaseUser(val name: String?, val email: String?, val tripsSharedWithMe: Map<String, Boolean>?) {

    // used by Firebase to create the object
    constructor() : this(null, null, null)

    fun toBeaconUser(userId: String): BeaconUser {
        val tripsList = mutableListOf<String>()
        tripsSharedWithMe?.forEach { tripsList.add(it.key) }
        return BeaconUser(userId, name, email, tripsList)
    }
}