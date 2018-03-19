package com.cdk.beacon

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.jakewharton.rxrelay.BehaviorRelay
import com.jakewharton.rxrelay.ReplayRelay
import rx.Observable

class RxFirestoreDatabase {

    companion object {
        fun getSingleValue(query: Task<QuerySnapshot>): Observable<QuerySnapshot> {
            val relay: ReplayRelay<QuerySnapshot> = ReplayRelay.create()
            query.addOnCompleteListener {
                relay.call(it.result)
            }

            return relay
        }

        fun getSingleDocument(query: Task<DocumentSnapshot>): Observable<DocumentSnapshot> {
            val relay: ReplayRelay<DocumentSnapshot> = ReplayRelay.create()
            query.addOnCompleteListener {
                relay.call(it.result)
            }

            return relay
        }

        fun addValue(query: Task<DocumentReference>): Observable<Boolean> {
            val relay: BehaviorRelay<Boolean> = BehaviorRelay.create()

            query.addOnSuccessListener {
                relay.call(true)
            }

            query.addOnFailureListener {
                relay.call(false)
            }

            return relay
        }

        fun updateValue(query: Task<Void>): Observable<Boolean> {
            val relay: BehaviorRelay<Boolean> = BehaviorRelay.create()

            query.addOnSuccessListener {
                relay.call(true)
            }

            query.addOnFailureListener {
                relay.call(false)
            }

            return relay
        }
    }

}