package com.cdk.beacon.mvp.usecase

import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.UserTripsDataContract
import rx.Subscriber


class TripsUseCase(private val repository: UserTripsDataContract.Repository) : UseCase() {

    fun getTrips(userId: String, userEmail: String, pos: Int, subscriber: Subscriber<MutableList<BeaconTrip?>>) {
        when (pos) {
            0 -> { execute(repository.getAllTrips(userId, userEmail), subscriber) }
            1 -> { execute(repository.getMyTrips(userId), subscriber)}
            2 -> { execute(repository.getTripsSharedWithMe(userId, userEmail), subscriber)}
        }
    }
}