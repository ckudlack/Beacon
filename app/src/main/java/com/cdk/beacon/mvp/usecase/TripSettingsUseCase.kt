package com.cdk.beacon.mvp.usecase

import com.cdk.beacon.DefaultSubscriber
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.UserTripsDataContract
import rx.functions.Action0
import rx.functions.Action1

class TripSettingsUseCase(private val repository: UserTripsDataContract.Repository) : UseCase() {

    fun updateTripName(name: String, tripId: String, onSuccess: Action0, onError: Action1<Throwable>) {
        execute(repository.setTripName(tripId, name), onSuccess, onError)
    }

    fun updateSharedUsers(sharedUsers: List<String>, tripId: String, subscriber: DefaultSubscriber<List<String>>) {
        execute(repository.setSharedUsers(tripId, sharedUsers), subscriber)
    }

    fun updateBeaconFrequency(frequency: Int, tripId: String, subscriber: DefaultSubscriber<BeaconTrip>) {
        execute(repository.setBeaconFrequency(tripId, frequency), subscriber)
    }
}