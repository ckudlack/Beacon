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
        useCase.getTrips(userId, userEmail, filterPosition, object : DefaultSubscriber<MutableList<BeaconTrip>>() {
            override fun onNext(t: MutableList<BeaconTrip>) {
                view.showTrips(t)
                view.hideLoading()
            }
        })
    }

    override fun addTripClicked() {
        view.startAddTripActivity()
    }

    override fun startBeaconClicked(tripId: String, isPermissionGranted: Boolean, isUsersTrip: Boolean) {
        if (isPermissionGranted) {
            view.startBeacon(tripId)
            view.startMapActivity(tripId, isUsersTrip)
        } else {
            view.requestPermissions(tripId)
        }
    }

    override fun dontStartBeaconClicked(tripId: String, isUsersTrip: Boolean) {
        view.startMapActivity(tripId, isUsersTrip)
    }

    override fun tripClicked(tripId: String, isActive: Boolean, isUsersTrip: Boolean) {
        when {
            !isUsersTrip -> view.startMapActivity(tripId, isUsersTrip)
            !isActive -> view.showAlertDialog(tripId, isUsersTrip)
            else -> view.startMapActivity(tripId, isUsersTrip)
        }
    }

    override fun onPermissionResult(tripId: String?, isGranted: Boolean, isUsersTrip: Boolean) {
        if (isGranted) {
            tripId?.let {
                view.startBeacon(tripId)
                view.startMapActivity(tripId, isUsersTrip)
            }
        }
    }
}