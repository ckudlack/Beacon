package com.cdk.beacon.mvp.presenter

import com.cdk.beacon.mvp.contract.MainContract


class MainPresenter(private var view: MainContract.View) : MainContract.Presenter {

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onStart(email: String?) {
        if (email == null) {
            view.startLoginActivity()
        } else {
            view.startMapActivity()
        }
    }

    override fun onDestroy() {
    }

    override fun onStartButtonClicked(permissionsGranted: Boolean) {
        if (permissionsGranted) {
            view.scheduleBeaconService()
        } else {
            view.requestPermissions()
        }
    }

    override fun onPermissionsGranted(granted: Boolean) {
        if (granted) {
            view.scheduleBeaconService()
        }
    }

    override fun onLogOutClicked() {
        view.logOut()
        view.startLoginActivity()
    }
}