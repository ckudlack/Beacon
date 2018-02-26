package com.cdk.beacon.mvp.presenter

import com.cdk.beacon.DefaultSubscriber
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.UserTripsContract
import com.cdk.beacon.mvp.usecase.TripsUseCase

class UserTripsPresenter(private var view: UserTripsContract.View, private var useCase: TripsUseCase) : UserTripsContract.Presenter {

    override fun onStop() {
        useCase.clear()
    }

    override fun onStart() {
    }

    override fun onDestroy() {
    }

    override fun getTrips(userId: String) {
        view.showLoading()
        useCase.getTrips(userId, object : DefaultSubscriber<MutableList<BeaconTrip>>() {
            override fun onNext(t: MutableList<BeaconTrip>) {
                view.showTrips(t)
                view.hideLoading()
            }
        })
    }

    override fun addTripClicked() {
        view.startAddTripActivity()
    }

    override fun startBeaconClicked() {
        view.startBeacon()
        view.startMapActivity()
    }

    override fun dontStartBeaconClicked() {
        view.startMapActivity()
    }
}