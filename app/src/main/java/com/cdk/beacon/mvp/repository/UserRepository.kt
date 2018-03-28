package com.cdk.beacon.mvp.repository

import com.cdk.beacon.data.BeaconUser
import com.cdk.beacon.mvp.contract.UserDataContract
import rx.Observable

class UserRepository(private val localDataSource: UserDataContract.LocalDataSource, private val remoteDataSource: UserDataContract.RemoteDataSource) : UserDataContract.Repository {

    override fun setUser(user: BeaconUser) {
        localDataSource.setUser(user)
    }

    override fun getUser(): BeaconUser {
        return localDataSource.getUser()
    }

    override fun getUser(userId: String): Observable<BeaconUser> {
        return remoteDataSource.getUser(userId)
                .map {
                    localDataSource.setUser(it)
                    it
                }
    }

    override fun updateTripsSharedWithMe(userId: String, tripsSharedWithMe: List<String>): Observable<Boolean> {
        return remoteDataSource.updateTripsSharedWithMe(userId, tripsSharedWithMe)
                .flatMap { remoteDataSource.getUser(userId) }
                .map {
                    localDataSource.setTripSharedWithMe(it.trips)
                    true
                }
    }
}