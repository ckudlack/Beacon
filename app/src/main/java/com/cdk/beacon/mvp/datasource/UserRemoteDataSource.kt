package com.cdk.beacon.mvp.datasource

import com.cdk.beacon.RxFirestoreDatabase
import com.cdk.beacon.data.BeaconUser
import com.cdk.beacon.data.FirebaseUser
import com.cdk.beacon.mvp.contract.UserDataContract
import com.google.firebase.firestore.FirebaseFirestore
import rx.Observable

class UserRemoteDataSource(private val database: FirebaseFirestore) : UserDataContract.RemoteDataSource {

    override fun setRegistrationToken(userId: String, token: String): Observable<Boolean> {
        return RxFirestoreDatabase.updateValue(database.collection("users").document(userId).update("fcmToken", token))
    }

    override fun getUser(userId: String): Observable<BeaconUser> {
        return RxFirestoreDatabase.getSingleDocument(database.collection("users").document(userId).get()).flatMap { documentSnapshot ->
            Observable.just(documentSnapshot.toObject(FirebaseUser::class.java)?.toBeaconUser(documentSnapshot.id))
        }
    }

    override fun updateTripsSharedWithMe(userId: String, tripsSharedWithMe: List<String>): Observable<Boolean> {
        val tripMap = mutableMapOf<String, Boolean>()
        tripsSharedWithMe.forEach {
            tripMap[it] = true
        }

        return RxFirestoreDatabase.updateValue(database.collection("users").document(userId).update("tripsSharedWithMe", tripMap))
    }
}