package com.cdk.beacon.mvp.usecase

import com.cdk.beacon.DefaultSubscriber
import com.cdk.beacon.data.BeaconUser
import com.cdk.beacon.mvp.contract.UserDataContract

class MainUseCase(private val repository: UserDataContract.Repository) : UseCase() {

    fun getUser(userId: String, subscriber: DefaultSubscriber<BeaconUser>) {
        execute(repository.getUser(userId), subscriber)
    }

    fun sendRegistrationToken(userId: String, token: String, subscriber: DefaultSubscriber<Boolean>) {
        execute(repository.setRegistrationToken(userId, token), subscriber)
    }

    fun getUser(): BeaconUser? = repository.getUser()

    fun clearUserData() {
        repository.clearUserData()
    }
}