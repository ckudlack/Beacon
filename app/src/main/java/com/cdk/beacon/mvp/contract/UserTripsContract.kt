package com.cdk.beacon.mvp.contract

import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.presenter.BasePresenter
import com.cdk.beacon.ui.BaseView

interface UserTripsContract {

    interface Presenter : BasePresenter {
        fun getTrips(userId: String, userEmail: String, filterPosition: Int)
        fun addTripClicked()
        fun startBeaconClicked(tripId: String, isPermissionGranted: Boolean, isUsersTrip: Boolean)
        fun dontStartBeaconClicked(tripId: String, isUsersTrip: Boolean)
        fun tripClicked(tripId: String, isActive: Boolean, isUsersTrip: Boolean)
        fun onPermissionResult(tripId: String?, isGranted: Boolean, isUsersTrip: Boolean)
    }

    interface View : BaseView {
        fun showTrips(trips: MutableList<BeaconTrip>)
        fun startAddTripActivity()
        fun startMapActivity(tripId: String, isUsersTrip: Boolean)
        fun startBeacon(tripId: String)
        fun showAlertDialog(tripId: String, isUsersTrip: Boolean)
        fun requestPermissions(tripId: String)
    }
}