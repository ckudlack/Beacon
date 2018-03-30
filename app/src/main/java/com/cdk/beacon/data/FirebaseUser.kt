package com.cdk.beacon.data

@Suppress("unused", "MemberVisibilityCanBePrivate")
data class FirebaseUser(val name: String?, val email: String?, val tripsSharedWithMe: Map<String, Boolean>?, val fcmToken: String?) {

    // used by Firebase to create the object
    constructor() : this(null, null, null, null)

    fun toBeaconUser(userId: String): BeaconUser {
        val tripsList = mutableListOf<String>()
        tripsSharedWithMe?.forEach { tripsList.add(it.key) }
        return BeaconUser(userId, name, email, tripsList, fcmToken)
    }
}