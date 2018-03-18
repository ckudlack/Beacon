package com.cdk.beacon.mvp.usecase

import com.cdk.beacon.mvp.contract.UserTripsDataContract
import rx.functions.Action0
import rx.functions.Action1

class TripSettingsUseCase(private val repository: UserTripsDataContract.Repository) : UseCase() {

    fun updateTripName(name: String, tripId: String, onSuccess: Action0, onError: Action1<Throwable>) {
        execute(repository.setTripName(tripId, name), onSuccess, onError)
    }

    fun updateSharedUsers(sharedUsers: List<String>, tripId: String, onSuccess: Action0, onError: Action1<Throwable>) {
        execute(repository.setSharedUsers(tripId, sharedUsers), onSuccess, onError)
    }

    fun updateBeaconFrequency(frequency: Int, tripId: String, onSuccess: Action0, onError: Action1<Throwable>) {
        execute(repository.setBeaconFrequency(tripId, frequency), onSuccess, onError)
    }
}