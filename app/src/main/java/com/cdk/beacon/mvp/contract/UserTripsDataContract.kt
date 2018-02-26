package com.cdk.beacon.mvp.contract

import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.repository.BaseRepository
import rx.Observable

interface UserTripsDataContract {

    interface Repository : BaseRepository {
        fun getTrips(userId: String): Observable<MutableList<BeaconTrip>>
        fun addTrip(userId: String, trip : BeaconTrip): Observable<List<BeaconTrip>>
    }
}