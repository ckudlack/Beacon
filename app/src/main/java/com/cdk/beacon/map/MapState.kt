package com.cdk.beacon.map

import com.google.android.gms.maps.model.LatLng

data class MapItem(
        val position: Position,
        val timestamp: Long
)

data class Position(
        val latitude: Double,
        val longitude: Double)

fun Position.toLatLng(): LatLng = LatLng(latitude, longitude)