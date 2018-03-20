package com.cdk.beacon.mvp.contract

import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.presenter.BasePresenter
import com.cdk.beacon.ui.BaseView

interface UserTripsContract {

    interface Presenter : BasePresenter {
        fun getTrips(userId: String, userEmail: String, filterPosition: Int)
        fun addTripClicked()
        fun startBeaconClicked(trip: BeaconTrip, isPermissionGranted: Boolean, isUsersTrip: Boolean)
        fun dontStartBeaconClicked(trip: BeaconTrip, isUsersTrip: Boolean)
        fun tripClicked(trip: BeaconTrip, isActive: Boolean, isUsersTrip: Boolean)
        fun onPermissionResult(trip: BeaconTrip?, isGranted: Boolean, isUsersTrip: Boolean)
        fun onLogOutClicked()
    }

    interface View : BaseView {
        fun showTrips(trips: MutableList<BeaconTrip?>)
        fun startAddTripActivity()
        fun startMapActivity(trip: BeaconTrip, isUsersTrip: Boolean)
        fun startBeacon(trip: BeaconTrip)
        fun showAlertDialog(trip: BeaconTrip, isUsersTrip: Boolean)
        fun requestPermissions(trip: BeaconTrip)
        fun logOutUser()
    }
}