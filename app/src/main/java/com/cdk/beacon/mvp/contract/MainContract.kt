package com.cdk.beacon.mvp.contract

import com.cdk.beacon.mvp.presenter.BasePresenter
import com.cdk.beacon.ui.BaseView

interface MainContract {

    interface Presenter : BasePresenter {
        fun onLogOutClicked()
        fun onStart(email : String?)
    }

    interface View : BaseView {
        fun startLoginActivity()
        fun scheduleBeaconService()
        fun requestPermissions()
        fun logOut()
        fun startTripsActivity()
        fun close()
    }
}