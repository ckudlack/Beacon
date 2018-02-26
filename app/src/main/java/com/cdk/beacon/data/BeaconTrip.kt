package com.cdk.beacon.data

data class BeaconTrip(val locations: List<MyLocation>, val name: String, val observers: List<String>, val id: String)