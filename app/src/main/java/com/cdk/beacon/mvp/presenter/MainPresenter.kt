package com.cdk.beacon.mvp.presenter

import com.cdk.beacon.DefaultSubscriber
import com.cdk.beacon.data.BeaconUser
import com.cdk.beacon.mvp.contract.MainContract
import com.cdk.beacon.mvp.usecase.MainUseCase


class MainPresenter(private val view: MainContract.View, private val useCase: MainUseCase) : MainContract.Presenter {

    override fun onSignInReturnedSuccessfully(userId: String) {
        useCase.getUser(userId, object : DefaultSubscriber<BeaconUser>() {
            override fun onNext(t: BeaconUser) {
                view.startTripsActivity()
            }
        })
    }

    override fun onStart() {
    }

    override fun onStop() = useCase.clear()

    override fun onStart(email: String?) {
        when (email) {
            null -> view.startLoginActivity()
            else -> view.startTripsActivity()
        }
    }

    override fun onDestroy() {
    }
}