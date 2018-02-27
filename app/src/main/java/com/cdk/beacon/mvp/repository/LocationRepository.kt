package com.cdk.beacon.mvp.repository

import com.cdk.beacon.data.MyLocation
import com.cdk.beacon.mvp.contract.MapDataContract
import com.google.firebase.database.FirebaseDatabase
import com.kelvinapps.rxfirebase.RxFirebaseDatabase
import rx.Observable

class LocationRepository(private val database: FirebaseDatabase) : MapDataContract.Repository {

    override fun getLocationList(sortByValue: String, tripId: String): Observable<List<MyLocation>> {
        return RxFirebaseDatabase.observeValueEvent(database.reference.child("beacons").child(tripId).orderByChild(sortByValue)).flatMap { dataSnapshotRxFirebaseChildEvent ->
            val locationList = ArrayList<MyLocation>()
            dataSnapshotRxFirebaseChildEvent.children.forEach {
                val location = it.value as Map<*, *>

                val latitude = location["latitude"] as Double
                val longitude = location["longitude"] as Double
                val timestamp = location["timeStamp"] as Long

                val loc = MyLocation(latitude, longitude, timestamp)
                loc.index = locationList.size
                locationList.add(loc)
            }

            Observable.just(locationList)
        }
    }
}

