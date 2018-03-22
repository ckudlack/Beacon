package com.cdk.beacon.mvp.presenter

import com.cdk.beacon.DefaultSubscriber
import com.cdk.beacon.R
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.TripSettingsContract
import com.cdk.beacon.mvp.usecase.TripSettingsUseCase
import rx.functions.Action0
import rx.functions.Action1
import java.util.regex.Pattern

class TripSettingsPresenter(private val view: TripSettingsContract.View, private val useCase: TripSettingsUseCase) : TripSettingsContract.Presenter {

    override fun onTripNameChanged(name: String, trip: BeaconTrip) {
        if (name != trip.name) {
            useCase.updateTripName(name, trip.id, Action0 { view.showToast(R.string.trip_name_updated) }, Action1 { error: Throwable -> view.showError(error) })
        }
    }

    override fun onSharedUserRemoved(index: Int, trip: BeaconTrip) {
        val mutableSharedUsers = trip.observers.toMutableList()
        mutableSharedUsers.removeAt(index)

        useCase.updateSharedUsers(mutableSharedUsers.toList(), trip.id, object : DefaultSubscriber<List<String>>() {
            override fun onNext(t: List<String>) {
                view.showToast(R.string.user_removed)
                view.updateSharedUsers(t)
            }
        })
    }

    override fun onSharedUserAdded(email: String, trip: BeaconTrip) {
        if (!isEmailValid(email)) {
            view.showToast(R.string.valid_email_required)
            return
        }

        val mutableSharedUsers = trip.observers.toMutableList()
        mutableSharedUsers.add(email)

        useCase.updateSharedUsers(mutableSharedUsers.toList(), trip.id, object : DefaultSubscriber<List<String>>() {
            override fun onNext(t: List<String>) {
                view.showToast(R.string.user_added)
                view.updateSharedUsers(t)
            }
        })
    }

    override fun onBeaconFrequencyUpdated(frequency: Int, trip: BeaconTrip) {
        if (frequency != trip.beaconFrequency) {
            useCase.updateBeaconFrequency(frequency, trip.id, object : DefaultSubscriber<BeaconTrip>() {
                override fun onNext(t: BeaconTrip) {
                    view.showToast(R.string.frequency_updated)
                    view.setBroadcastToNewFrequency(t)
                }
            })
        }
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

    private fun isEmailValid(email: String): Boolean {
        return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }
}