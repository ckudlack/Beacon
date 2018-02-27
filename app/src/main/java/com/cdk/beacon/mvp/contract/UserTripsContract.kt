package com.cdk.beacon.mvp.contract

import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.presenter.BasePresenter
import com.cdk.beacon.ui.BaseView

interface UserTripsContract {

    interface Presenter : BasePresenter {
        fun getTrips(userId: String)
        fun addTripClicked()
        fun startBeaconClicked(tripId: String)
        fun dontStartBeaconClicked(tripId: String)
    }

    interface View : BaseView {
        fun showTrips(trips: MutableList<BeaconTrip>)
        fun startAddTripActivity()
        fun startMapActivity(tripId: String)
        fun startBeacon(tripId: String)
    }
}