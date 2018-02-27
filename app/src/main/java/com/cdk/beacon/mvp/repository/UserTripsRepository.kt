package com.cdk.beacon.mvp.repository

import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.UserTripsDataContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kelvinapps.rxfirebase.RxFirebaseDatabase
import rx.Observable

class UserTripsRepository(private val database: FirebaseDatabase) : UserTripsDataContract.Repository {

    override fun addTrip(userId: String, trip: BeaconTrip): Observable<List<BeaconTrip>> {
        database.reference.child("trips").child(userId).push().setValue(trip).addOnCompleteListener {
            if (it.isSuccessful) {
                val result = it.result
            } else {
                val exception = it.exception
            }
        }
        return Observable.just(ArrayList())
    }

    override fun getTrips(userId: String): Observable<MutableList<BeaconTrip>> {
        return RxFirebaseDatabase.observeValueEvent(database.reference.child("trips").child(userId).orderByKey()).flatMap { dataSnapshotRxFirebaseChildEvent ->
            val tripList = mutableListOf<BeaconTrip>()
            dataSnapshotRxFirebaseChildEvent.children.forEach {
                val trip = it.value as Map<*, *>

                val name = trip["name"] as String
                val observers = trip["observers"] as List<String>

                tripList.add(BeaconTrip(mutableListOf(), name, observers, it.key))
            }

            Observable.just(tripList)
        }
    }

    override fun getTrip(tripId: String): Observable<BeaconTrip> {
        return RxFirebaseDatabase.observeValueEvent(database.reference.child("trips").child(FirebaseAuth.getInstance().currentUser!!.uid).child(tripId).orderByKey()).flatMap { dataSnapshotRxFirebaseChildEvent ->
            val trip = dataSnapshotRxFirebaseChildEvent.child(tripId).value as Map<*, *>
            val name = trip["name"] as String
            val observers = trip["observers"] as List<String>

            Observable.just(BeaconTrip(mutableListOf(), name, observers, tripId))
        }
    }
}