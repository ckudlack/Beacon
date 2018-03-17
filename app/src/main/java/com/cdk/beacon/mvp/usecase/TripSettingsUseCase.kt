package com.cdk.beacon.mvp.usecase

import com.cdk.beacon.mvp.contract.UserTripsDataContract
import rx.functions.Action0
import rx.functions.Action1

class TripSettingsUseCase(private val repository: UserTripsDataContract.Repository) : UseCase() {

    fun updateTripName(name: String, tripId: String, onSuccess: Action0, onError: Action1<Throwable>) {
        execute(repository.setTripName(tripId, name), onSuccess, onError)
    }
}