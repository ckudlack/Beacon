package com.cdk.beacon.mvp.contract

import com.cdk.beacon.data.BeaconUser
import com.cdk.beacon.mvp.datasource.BaseDataSource
import com.cdk.beacon.mvp.repository.BaseRepository
import rx.Observable

interface UserDataContract {

    interface Repository : BaseRepository {
        fun updateTripsSharedWithMe(userId: String, tripsSharedWithMe: List<String>) : Observable<Boolean>
        fun getUser(userId: String) : Observable<BeaconUser>
        fun getUser() : BeaconUser
        fun setUser(user : BeaconUser)
    }

    interface RemoteDataSource : BaseDataSource {
        fun getUser(userId: String): Observable<BeaconUser>
        fun updateTripsSharedWithMe(userId: String, tripsSharedWithMe: List<String>): Observable<Boolean>
    }

    interface LocalDataSource : BaseDataSource {
        fun getUser(): BeaconUser
        fun setUser(user: BeaconUser)
        fun setTripSharedWithMe(tripsList : List<String>?)
    }
}