package com.cdk.beacon.mvp.repository

import com.cdk.beacon.RxFirestoreDatabase
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.UserTripsDataContract
import com.google.firebase.firestore.FirebaseFirestore
import rx.Observable

class UserTripsRepository(private val database: FirebaseFirestore) : UserTripsDataContract.Repository {

    override fun addTrip(userId: String, trip: BeaconTrip): Observable<List<BeaconTrip>> {
        return RxFirestoreDatabase.addValue(database.collection("trips").add(trip)).flatMap { isSuccessful ->
            if (isSuccessful) {
            }
            Observable.just(ArrayList<BeaconTrip>())
        }
    }

    override fun getMyTrips(userId: String): Observable<MutableList<BeaconTrip>> {
        return RxFirestoreDatabase.getSingleValue(database.collection("trips").whereEqualTo("userId", userId).get()).flatMap { querySnapshotRxFirestoreChildEvent ->
            val tripList = mutableListOf<BeaconTrip>()
            querySnapshotRxFirestoreChildEvent.documents.forEach {
                val dataMap = it.data
                val name = dataMap["name"] as String
//                val observers = dataMap["observers"] as Map<String, Boolean>

                tripList.add(BeaconTrip(mutableListOf(), name, mapOf(), it.id, userId))
            }

            Observable.just(tripList)
        }
    }

    override fun getTrip(tripId: String): Observable<BeaconTrip> {
        return RxFirestoreDatabase.getSingleValue(database.collection("trips").whereEqualTo("tripId", tripId).get()).flatMap { dataSnapshotRxFirebaseChildEvent ->
            val trip = dataSnapshotRxFirebaseChildEvent.documents.first().data as Map<*, *>
            val name = trip["name"] as String
//            val observers = trip["observers"] as List<String>
            val userId = trip["userId"] as String

            Observable.just(BeaconTrip(mutableListOf(), name, mapOf(), tripId, userId))
        }
    }
}