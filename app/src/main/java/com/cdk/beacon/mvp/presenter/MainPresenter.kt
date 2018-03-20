package com.cdk.beacon.mvp.presenter

import com.cdk.beacon.mvp.contract.MainContract


class MainPresenter(private var view: MainContract.View) : MainContract.Presenter {

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onStart(email: String?) {
        when (email) {
            null -> view.startLoginActivity()
            else -> view.startTripsActivity()
        }
    }

    override fun onDestroy() {
    }
}