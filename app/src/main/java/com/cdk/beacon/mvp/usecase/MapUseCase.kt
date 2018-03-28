package com.cdk.beacon.mvp.usecase

import com.cdk.beacon.data.BeaconUser
import com.cdk.beacon.data.MyLocation
import com.cdk.beacon.mvp.contract.MapDataContract
import com.cdk.beacon.mvp.contract.UserDataContract
import rx.Subscriber


class MapUseCase(private val repository: MapDataContract.Repository, private val userRepository: UserDataContract.Repository) : UseCase() {

    fun getLocationList(sortByValue: String, tripId: String, subscriber: Subscriber<List<MyLocation>>) {
        execute(repository.getLocationList(sortByValue, tripId), subscriber)
    }

    fun leaveTrip(userId: String, tripsSharedWithMe: List<String>, subscriber: Subscriber<Boolean>) {
        execute(userRepository.updateTripsSharedWithMe(userId, tripsSharedWithMe), subscriber)
    }

    fun getUser(): BeaconUser = userRepository.getUser()
}