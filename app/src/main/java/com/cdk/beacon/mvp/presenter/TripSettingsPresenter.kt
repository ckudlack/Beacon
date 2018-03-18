package com.cdk.beacon.mvp.presenter

import com.cdk.beacon.R
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.TripSettingsContract
import com.cdk.beacon.mvp.usecase.TripSettingsUseCase
import rx.functions.Action0
import rx.functions.Action1

class TripSettingsPresenter(private val view: TripSettingsContract.View, private val useCase: TripSettingsUseCase) : TripSettingsContract.Presenter {

    override fun onTripNameChanged(name: String, trip: BeaconTrip) {
        if (name != trip.name) {
            useCase.updateTripName(name, trip.id, Action0 { view.showToast(R.string.trip_name_updated) }, Action1 { error: Throwable -> view.showError(error) })
        }
    }

    override fun onSharedUserRemoved(index: Int, trip: BeaconTrip) {
        val mutableSharedUsers = trip.observers.toMutableList()
        mutableSharedUsers.removeAt(index)

        useCase.updateSharedUsers(mutableSharedUsers.toList(), trip.id, Action0 { view.showToast(R.string.trip_name_updated) }, Action1 { error: Throwable -> view.showError(error) })
    }

    override fun onStop() {
        useCase.clear()
    }

    override fun onStart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}