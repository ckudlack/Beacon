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

    override fun startBeaconClicked(tripId: String, isPermissionGranted: Boolean) {
        if (isPermissionGranted) {
            view.startBeacon(tripId)
            view.startMapActivity(tripId)
        } else {
            view.requestPermissions(tripId)
        }
    }

    override fun dontStartBeaconClicked(tripId: String) {
        view.startMapActivity(tripId)
    }

    override fun tripClicked(tripId: String, isActive: Boolean, isUsersTrip: Boolean) {
        when {
            !isUsersTrip -> view.startMapActivity(tripId)
            !isActive -> view.showAlertDialog(tripId)
            else -> view.startMapActivity(tripId)
        }
    }

    override fun onPermissionResult(tripId: String?, isGranted: Boolean) {
        if (isGranted) {
            tripId?.let {
                view.startBeacon(tripId)
                view.startMapActivity(tripId)
            }
        }
    }
}