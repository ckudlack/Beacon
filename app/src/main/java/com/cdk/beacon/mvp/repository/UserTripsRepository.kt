package com.cdk.beacon.mvp.repository

import com.cdk.beacon.RxFirestoreDatabase
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.data.FirebaseTrip
import com.cdk.beacon.mvp.contract.UserTripsDataContract
import com.google.firebase.firestore.FirebaseFirestore
import rx.Observable

class UserTripsRepository(private val database: FirebaseFirestore) : UserTripsDataContract.Repository {
    override fun getAllTrips(userId: String, userEmail: String): Observable<MutableList<BeaconTrip>> {
        return Observable.combineLatest(getMyTrips(userId), getTripsSharedWithMe(userId, userEmail), { list1: MutableList<BeaconTrip>, list2: MutableList<BeaconTrip> ->
            val combinedList = mutableListOf<BeaconTrip>()
            combinedList.addAll(list1)
            combinedList.addAll(list2)
            combinedList
        })
    }

    override fun addTrip(userId: String, trip: BeaconTrip): Observable<List<BeaconTrip>> {
        return RxFirestoreDatabase.addValue(database.collection("trips").add(trip.toFirebaseTrip())).flatMap { isSuccessful ->
            if (isSuccessful) {
                // do something
            }
            Observable.just(ArrayList<BeaconTrip>())
        }
    }

    override fun getMyTrips(userId: String): Observable<MutableList<BeaconTrip>> {
        return RxFirestoreDatabase.getSingleValue(database.collection("trips").whereEqualTo("userId", userId).get()).flatMap { querySnapshotRxFirestoreChildEvent ->
            val tripList = mutableListOf<BeaconTrip>()
            querySnapshotRxFirestoreChildEvent.documents.forEach {
                val firebaseTrip = it.toObject(FirebaseTrip::class.java)
                tripList.add(firebaseTrip.toBeaconTrip(it.id))
            }
            Observable.just(tripList)
        }
    }

    override fun getTrip(tripId: String): Observable<BeaconTrip> {
        return RxFirestoreDatabase.getSingleValue(database.collection("trips").whereEqualTo("tripId", tripId).get()).flatMap { dataSnapshotRxFirebaseChildEvent ->
            val snapshot = dataSnapshotRxFirebaseChildEvent.documents.first()
            val trip = snapshot.toObject(FirebaseTrip::class.java)
            Observable.just(trip.toBeaconTrip(snapshot.id))
        }
    }

    override fun getTripsSharedWithMe(userId: String, userEmail: String): Observable<MutableList<BeaconTrip>> {
        val sanitizedEmail = sanitizeEmailToFirebaseName(userEmail)
        return RxFirestoreDatabase.getSingleValue(database.collection("trips").whereEqualTo("observers.$sanitizedEmail", true).get()).flatMap { querySnapshot ->
            val tripList = mutableListOf<BeaconTrip>()
            querySnapshot.documents.forEach {
                val firebaseTrip = it.toObject(FirebaseTrip::class.java)
                tripList.add(firebaseTrip.toBeaconTrip(it.id))
            }
            Observable.just(tripList)
        }
    }

    private fun sanitizeEmailToFirebaseName(string: String): String {
        val lastIndexOf = string.lastIndexOf('.')
        return string.replaceRange(lastIndexOf, lastIndexOf + 1, "@")
    }
}