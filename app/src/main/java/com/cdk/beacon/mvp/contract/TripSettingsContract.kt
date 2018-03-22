package com.cdk.beacon.mvp.contract

import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.presenter.BasePresenter
import com.cdk.beacon.ui.BaseView

interface TripSettingsContract {

    interface View : BaseView {
        fun showToast(textRes: Int)
        fun updateSharedUsers(sharedUsers: List<String>)
        fun setBroadcastToNewFrequency(trip: BeaconTrip)
        fun returnToTripsActivity()
        fun stopBroadcasting(tripId: String)
    }

    interface Presenter : BasePresenter {
        fun onTripNameChanged(name: String, trip: BeaconTrip)
        fun onSharedUserRemoved(index: Int, trip: BeaconTrip)
        fun onBeaconFrequencyUpdated(frequency: Int, trip: BeaconTrip)
        fun onSharedUserAdded(email: String, trip: BeaconTrip)
        fun deleteTrip(tripId: String)
    }
}