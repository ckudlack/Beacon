package com.cdk.beacon.data

data class BeaconTrip(val locations: List<MyLocation>, val name: String, val observers: Map<String, String>, val id: String, val userId: String) {

}