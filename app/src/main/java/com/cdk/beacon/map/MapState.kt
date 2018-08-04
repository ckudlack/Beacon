package com.cdk.beacon.map

data class MapItem(
        val position: Position,
        val timestamp: Long
)

data class Position(
        val latitude: Double,
        val longitude: Double)