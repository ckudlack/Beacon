package com.cdk.beacon.mvp.contract

import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.presenter.BasePresenter
import com.cdk.beacon.ui.BaseView

interface NewTripContract {

    interface Presenter : BasePresenter {
        fun addTrip(userId: String, trip: BeaconTrip)
        fun onSharedUserAdded(email: String)
    }

    interface View : BaseView {
        fun goToStartBeaconActivity()
        fun addToList(email: String)
        fun showToast(text: Int)
    }
}