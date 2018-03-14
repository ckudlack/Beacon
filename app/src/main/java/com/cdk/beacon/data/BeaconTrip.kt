package com.cdk.beacon.data

data class BeaconTrip(val locations: List<MyLocation>, val name: String, val observers: List<String>, val id: String, val userId: String) {

    fun toFirebaseTrip(): FirebaseTrip {
        val observerMap = mutableMapOf<String, Boolean>()
        observers.forEach { observerMap[it] = true }
        return FirebaseTrip(name, userId, observerMap)
    }
}