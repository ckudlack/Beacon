package com.cdk.beacon.mvp.presenter

import com.cdk.beacon.DefaultSubscriber
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.NewTripContract
import com.cdk.beacon.mvp.usecase.NewTripUseCase

class NewTripPresenter(private val useCase: NewTripUseCase, private val view: NewTripContract.View) : NewTripContract.Presenter {

    override fun onStop() {
        useCase.clear()
    }

    override fun onStart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addTrip(userId: String, trip: BeaconTrip) {
        useCase.addTrip(userId, trip, object : DefaultSubscriber<List<BeaconTrip>>() {
            override fun onNext(t: List<BeaconTrip>) {
                view.goToStartBeaconActivity()
                onCompleted()
            }
        })
    }
}