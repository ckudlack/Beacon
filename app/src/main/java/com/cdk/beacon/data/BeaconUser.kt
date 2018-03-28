package com.cdk.beacon.data


data class BeaconUser(val id: String, val name: String?, val email: String?, val trips: List<String>?) {
    fun toFirebaseUser() : FirebaseUser {
        val tripsMap = mutableMapOf<String, Boolean>()
        trips?.forEach { tripsMap[it] = true }
        return FirebaseUser(name, email, tripsMap)
    }
}