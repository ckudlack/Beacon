package com.cdk.beacon.mvp.contract

import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.repository.BaseRepository
import rx.Observable

interface UserTripsDataContract {

    interface Repository : BaseRepository {
        fun getMyTrips(userId: String): Observable<MutableList<BeaconTrip>>
        fun getTrip(tripId: String): Observable<BeaconTrip>
        fun addTrip(userId: String, trip: BeaconTrip): Observable<List<BeaconTrip>>
        fun getTripsSharedWithMe(userId: String, userEmail: String): Observable<MutableList<BeaconTrip>>
    }
}