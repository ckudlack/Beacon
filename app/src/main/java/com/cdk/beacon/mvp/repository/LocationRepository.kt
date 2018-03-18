package com.cdk.beacon.mvp.repository

import com.cdk.beacon.RxFirestoreDatabase
import com.cdk.beacon.data.MyLocation
import com.cdk.beacon.mvp.contract.MapDataContract
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import rx.Observable

class LocationRepository(private val database: FirebaseFirestore) : MapDataContract.Repository {

    override fun getLocationList(sortByValue: String, tripId: String): Observable<List<MyLocation>> {
        return RxFirestoreDatabase.getSingleValue(database.collection("locations").document(tripId).collection("beacons").orderBy("timeStamp").get()).flatMap { querySnapshot: QuerySnapshot ->
            val locationList = ArrayList<MyLocation>()
            querySnapshot.documents.forEach {
                val latitude = it.data["latitude"] as Double
                val longitude = it["longitude"] as Double
                val timestamp = it["timeStamp"] as Long

                val loc = MyLocation(longitude, latitude, timestamp)
                loc.index = locationList.size
                locationList.add(loc)
            }
            Observable.just(locationList)
        }
    }
}

