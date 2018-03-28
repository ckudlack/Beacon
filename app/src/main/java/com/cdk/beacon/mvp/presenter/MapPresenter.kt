@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package com.cdk.beacon.mvp.presenter

import com.cdk.beacon.DefaultSubscriber
import com.cdk.beacon.data.MyLocation
import com.cdk.beacon.mvp.contract.MapContract
import com.cdk.beacon.mvp.usecase.MapUseCase

class MapPresenter(private var view: MapContract.View, private var useCase: MapUseCase) : MapContract.Presenter {

    override fun settingsButtonClicked() {
        view.launchSettingsActivity()
    }

    override fun getLocations(sortByValue: String, tripId: String) {
        view.showLoading()
        useCase.getLocationList(sortByValue, tripId, object : DefaultSubscriber<List<MyLocation>>() {
            override fun onNext(myLocations: List<MyLocation>) {
                view.hideLoading()
                view.displayLocations(myLocations)
            }
        })
    }

    override fun leaveTripClicked() {
        view.showLeaveTripAlertDialog()
    }

    override fun leaveTripConfirmed(tripId: String?) {
        val user = useCase.getUser()

        user.trips?.let {
            view.showLoading()

            val trips = user.trips.toMutableList()
            trips.remove(tripId)

            useCase.leaveTrip(user.id, trips.toList(), object : DefaultSubscriber<Boolean>() {
                override fun onNext(t: Boolean) {
                    view.hideLoading()
                    view.goToTripsActivity()
                }
            })
        }
    }

    override fun onStop() {
    }

    override fun onStart() {
    }

    override fun onDestroy() {
    }
}