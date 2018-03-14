package com.cdk.beacon.mvp.contract

import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.presenter.BasePresenter
import com.cdk.beacon.ui.BaseView

interface UserTripsContract {

    interface Presenter : BasePresenter {
        fun getTrips(userId: String, userEmail: String, filterPosition: Int)
        fun addTripClicked()
        fun startBeaconClicked(tripId: String, isPermissionGranted: Boolean)
        fun dontStartBeaconClicked(tripId: String)
        fun tripClicked(tripId: String, isActive: Boolean)
        fun onPermissionResult(tripId: String?, isGranted: Boolean)
    }

    interface View : BaseView {
        fun showTrips(trips: MutableList<BeaconTrip>)
        fun startAddTripActivity()
        fun startMapActivity(tripId: String)
        fun startBeacon(tripId: String)
        fun showAlertDialog(tripId: String)
        fun requestPermissions(tripId: String)
    }
}