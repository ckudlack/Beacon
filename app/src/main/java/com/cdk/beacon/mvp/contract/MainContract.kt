package com.cdk.beacon.mvp.contract

import com.cdk.beacon.mvp.presenter.BasePresenter
import com.cdk.beacon.ui.BaseView

interface MainContract {

    interface Presenter : BasePresenter {
        fun onStartButtonClicked(permissionsGranted: Boolean)
        fun onPermissionsGranted(granted: Boolean)
        fun onLogOutClicked()
        fun onStart(email : String?)
    }

    interface View : BaseView {
        fun startLoginActivity()
        fun scheduleBeaconService()
        fun requestPermissions()
        fun logOut()
        fun startMapActivity()
        fun close()
    }
}