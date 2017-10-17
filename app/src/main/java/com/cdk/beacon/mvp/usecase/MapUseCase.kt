package com.cdk.beacon.mvp.usecase

import com.cdk.beacon.data.MyLocation
import com.cdk.beacon.mvp.contract.MapDataContract
import rx.Subscriber


class MapUseCase(private val repository: MapDataContract.Repository) : UseCase() {

    fun getLocationList(sortByValue: String, subscriber: Subscriber<List<MyLocation>>) {
        execute(repository.getLocationList(sortByValue), subscriber)
    }
}