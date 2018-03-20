package com.cdk.beacon.mvp.presenter

import com.cdk.beacon.DefaultSubscriber
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.UserTripsContract
import com.cdk.beacon.mvp.usecase.TripsUseCase

class UserTripsPresenter(private var view: UserTripsContract.View, private var useCase: TripsUseCase) : UserTripsContract.Presenter {

    override fun onStop() {
        useCase.clear()
    }

    override fun onStart() {
    }

    override fun onDestroy() {
    }

    override fun getTrips(userId: String, userEmail: String, filterPosition: Int) {
        view.showLoading()
        useCase.getTrips(userId, userEmail, filterPosition, object : DefaultSubscriber<MutableList<BeaconTrip?>>() {
            override fun onNext(t: MutableList<BeaconTrip?>) {
                view.showTrips(t)
                view.hideLoading()
            }
        })
    }

    override fun addTripClicked() {
        view.startAddTripActivity()
    }

    override fun startBeaconClicked(trip: BeaconTrip, isPermissionGranted: Boolean, isUsersTrip: Boolean) {
        if (isPermissionGranted) {
            view.startBeacon(trip)
            view.startMapActivity(trip, isUsersTrip)
        } else {
            view.requestPermissions(trip)
        }
    }

    override fun dontStartBeaconClicked(trip: BeaconTrip, isUsersTrip: Boolean) {
        view.startMapActivity(trip, isUsersTrip)
    }

    override fun tripClicked(trip: BeaconTrip, isActive: Boolean, isUsersTrip: Boolean) {
        when {
            !isUsersTrip -> view.startMapActivity(trip, isUsersTrip)
            !isActive -> view.showAlertDialog(trip, isUsersTrip)
            else -> view.startMapActivity(trip, isUsersTrip)
        }
    }

    override fun onPermissionResult(trip: BeaconTrip?, isGranted: Boolean, isUsersTrip: Boolean) {
        if (isGranted) {
            trip?.let {
                view.startBeacon(trip)
                view.startMapActivity(trip, isUsersTrip)
            }
        }
    }

    override fun onLogOutClicked() {
        view.logOutUser()
    }
}