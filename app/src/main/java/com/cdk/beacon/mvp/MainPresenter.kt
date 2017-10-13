package com.cdk.beacon.mvp


class MainPresenter(private var view: MainContract.View) : MainContract.Presenter {

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onStart(userExists: Boolean) {
        if (!userExists) {
            view.startLoginActivity()
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