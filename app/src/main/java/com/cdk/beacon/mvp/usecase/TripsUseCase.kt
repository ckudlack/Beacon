package com.cdk.beacon.mvp.usecase

import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.UserTripsDataContract
import rx.Subscriber


class TripsUseCase(private val repository: UserTripsDataContract.Repository) : UseCase() {

    fun getTrips(userId: String, subscriber: Subscriber<MutableList<BeaconTrip>>) {
        execute(repository.getTrips(userId), subscriber)
    }
}