@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package com.cdk.beacon.mvp.presenter

import com.cdk.beacon.DefaultSubscriber
import com.cdk.beacon.data.MyLocation
import com.cdk.beacon.mvp.usecase.MapUseCase
import com.cdk.beacon.mvp.contract.MapContract

class MapPresenter(private var view: MapContract.View, private var useCase: MapUseCase) : MapContract.Presenter {

    override fun getLocations(sortByValue: String) {
        useCase.getLocationList(sortByValue, object : DefaultSubscriber<List<MyLocation>>() {
            override fun onNext(myLocations: List<MyLocation>) {
                view.displayLocations(myLocations)
            }
        })
    }

    override fun onStop() {
    }

    override fun onStart() {
    }

    override fun onDestroy() {
    }
}