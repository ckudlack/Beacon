package com.cdk.beacon.mvp

interface MainContract {

    interface Presenter : BasePresenter {
        fun onStartButtonClicked(permissionsGranted: Boolean)
        fun onPermissionsGranted(granted: Boolean)
        fun onLogOutClicked()
        fun onStart(userExists: Boolean)
    }

    interface View : BaseView {
        fun startLoginActivity()
        fun scheduleBeaconService()
        fun requestPermissions()
        fun logOut();
    }
}