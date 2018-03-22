package com.cdk.beacon.mvp.usecase

import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.UserTripsDataContract
import rx.Subscriber

class NewTripUseCase(private val repository: UserTripsDataContract.Repository) : UseCase() {

    fun addTrip(userId: String, trip: BeaconTrip, subscriber: Subscriber<List<BeaconTrip>>) {
        execute(repository.addTrip(trip), subscriber)
    }
}