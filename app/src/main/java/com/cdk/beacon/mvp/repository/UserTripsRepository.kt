package com.cdk.beacon.mvp.repository

import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.UserTripsDataContract
import com.google.firebase.database.FirebaseDatabase
import com.kelvinapps.rxfirebase.RxFirebaseDatabase
import rx.Observable

class UserTripsRepository(private val database: FirebaseDatabase) : UserTripsDataContract.Repository {

    override fun addTrip(userId: String, trip: BeaconTrip): Observable<List<BeaconTrip>> {
        database.reference.child("trips").push().setValue(trip).addOnCompleteListener {
            if (it.isSuccessful) {
                val result = it.result
            } else {
                val exception = it.exception
            }
        }
        return Observable.just(ArrayList())
    }

    override fun getTrips(userId: String): Observable<MutableList<BeaconTrip>> {
        return RxFirebaseDatabase.observeValueEvent(database.reference.child("trips").orderByChild("userId").equalTo(userId)).flatMap { dataSnapshotRxFirebaseChildEvent ->
            val tripList = mutableListOf<BeaconTrip>()
            dataSnapshotRxFirebaseChildEvent.children.forEach {
                val trip = it.value as Map<*, *>

                val name = trip["name"] as String
//                val observers = trip["observers"] as Map<*, *>
                val userId = trip["userId"] as String

                tripList.add(BeaconTrip(mutableListOf(), name, mutableListOf(), it.key, userId))
            }

            Observable.just(tripList)
        }
    }

    override fun getTrip(tripId: String): Observable<BeaconTrip> {
        return RxFirebaseDatabase.observeValueEvent(database.reference.child("trips").child(tripId).orderByKey()).flatMap { dataSnapshotRxFirebaseChildEvent ->
            val trip = dataSnapshotRxFirebaseChildEvent.child(tripId).value as Map<*, *>
            val name = trip["name"] as String
//            val observers = trip["observers"] as List<String>
            val userId = trip["userId"] as String

            Observable.just(BeaconTrip(mutableListOf(), name, mutableListOf(), tripId, userId))
        }
    }
}