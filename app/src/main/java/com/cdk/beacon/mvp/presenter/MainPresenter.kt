package com.cdk.beacon.mvp.presenter

import com.cdk.beacon.DefaultSubscriber
import com.cdk.beacon.data.BeaconUser
import com.cdk.beacon.mvp.contract.MainContract
import com.cdk.beacon.mvp.usecase.MainUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId


class MainPresenter(private val view: MainContract.View, private val useCase: MainUseCase) : MainContract.Presenter {

    override fun clearUserData() {
        useCase.clearUserData()
    }

    override fun onSignInReturnedSuccessfully(userId: String) {
        useCase.getUser(userId, object : DefaultSubscriber<BeaconUser>() {
            override fun onNext(t: BeaconUser) {
                view.startTripsActivity()
            }
        })
    }

    override fun onStart() {
        useCase.getUser()
                ?.takeIf { it.fcmToken != FirebaseInstanceId.getInstance().token }
                ?.takeIf { FirebaseAuth.getInstance().currentUser?.uid != null }
                ?.let {
                    useCase.sendRegistrationToken(FirebaseAuth.getInstance().currentUser!!.uid, FirebaseInstanceId.getInstance().token!!, object : DefaultSubscriber<Boolean>() {
                        override fun onNext(t: Boolean) {
                            view.startTripsActivity()
                        }
                    })
                }
    }

    override fun onStop() = useCase.clear()

    override fun onStart(email: String?) {
        when (email) {
            null -> view.startLoginActivity()
            else -> view.startTripsActivity()
        }
    }

    override fun onDestroy() {
    }
}