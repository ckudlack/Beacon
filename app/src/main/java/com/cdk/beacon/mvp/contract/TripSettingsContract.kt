package com.cdk.beacon.mvp.contract

import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.presenter.BasePresenter
import com.cdk.beacon.ui.BaseView

interface TripSettingsContract {

    interface View : BaseView {
        fun showToast(textRes: Int)
    }

    interface Presenter : BasePresenter {
        fun onTripNameChanged(name: String, trip: BeaconTrip)
        fun onSharedUserRemoved(index: Int, tripId: String)
    }
}