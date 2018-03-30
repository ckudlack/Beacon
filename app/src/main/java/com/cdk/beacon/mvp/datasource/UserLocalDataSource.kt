package com.cdk.beacon.mvp.datasource

import android.content.SharedPreferences
import com.cdk.beacon.data.BeaconUser
import com.cdk.beacon.mvp.contract.UserDataContract

class UserLocalDataSource(private val sharedPrefs: SharedPreferences) : UserDataContract.LocalDataSource {

    override fun setRegistrationToken(token: String) {
        sharedPrefs.edit().putString(USER_TOKEN, token).apply()
    }

    override fun setTripSharedWithMe(tripsList: List<String>?) {
        val tripSet = mutableSetOf<String>()
        tripsList?.forEach { tripSet.add(it) }

        sharedPrefs.edit().putStringSet(TRIPS_SHARED_WITH_ME, tripSet).apply()
    }

    override fun setUser(user: BeaconUser) {
        sharedPrefs.edit().putString(USER_ID, user.id).apply()
        sharedPrefs.edit().putString(USER_NAME, user.name).apply()
        sharedPrefs.edit().putString(USER_EMAIL, user.email).apply()

        val tripSet = mutableSetOf<String>()
        user.trips?.forEach { tripSet.add(it) }

        sharedPrefs.edit().putStringSet(TRIPS_SHARED_WITH_ME, tripSet).apply()
    }

    override fun getUser(): BeaconUser {
        val uid = sharedPrefs.getString(USER_ID, "")
        val uname = sharedPrefs.getString(USER_NAME, null)
        val email = sharedPrefs.getString(USER_EMAIL, null)
        val tripsSet = sharedPrefs.getStringSet(TRIPS_SHARED_WITH_ME, null)
        val fcmToken = sharedPrefs.getString(USER_TOKEN, null)

        val tripsList = mutableListOf<String>()
        tripsSet.forEach {
            tripsList.add(it)
        }

        return BeaconUser(uid, uname, email, tripsList.toList(), fcmToken)
    }

    companion object {
        const val USER_ID = "user_id"
        const val USER_NAME = "user_name"
        const val USER_EMAIL = "user_email"
        const val TRIPS_SHARED_WITH_ME = "trips_shared_with_me"
        const val USER_TOKEN = "user_token"
    }
}