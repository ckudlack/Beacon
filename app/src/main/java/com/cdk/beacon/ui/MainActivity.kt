package com.cdk.beacon.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cdk.beacon.R
import com.cdk.beacon.mvp.contract.MainContract
import com.cdk.beacon.mvp.datasource.UserLocalDataSource
import com.cdk.beacon.mvp.datasource.UserRemoteDataSource
import com.cdk.beacon.mvp.presenter.MainPresenter
import com.cdk.beacon.mvp.repository.UserRepository
import com.cdk.beacon.mvp.usecase.MainUseCase
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.startActivityForResult


@Suppress("unused")
class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this,
                MainUseCase(UserRepository(UserLocalDataSource(defaultSharedPreferences),
                        UserRemoteDataSource(FirebaseFirestore.getInstance()))))

        presenter.onStart(FirebaseAuth.getInstance().currentUser?.email)
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(error: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startLoginActivity() {
        val providers = listOf(
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.EmailBuilder().build())

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), LOGIN_ACTIVITY_REQUEST_CODE)
    }

    override fun startTripsActivity() {
        startActivityForResult<TripsActivity>(TRIPS_ACTIVITY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TRIPS_ACTIVITY_REQUEST_CODE && resultCode != TripsActivity.RESULT_LOGGED_OUT) {
            finish()
        } else if (requestCode == LOGIN_ACTIVITY_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)

            // Successfully signed in
            if (resultCode == Activity.RESULT_OK) {
                // crash if there is no user
                presenter.onSignInReturnedSuccessfully(FirebaseAuth.getInstance().currentUser!!.uid)
            } else {
                // Sign in failed
                /*if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                showSnackbar(R.string.unknown_error);*/
            }
        } else if (requestCode == TRIPS_ACTIVITY_REQUEST_CODE && resultCode == TripsActivity.RESULT_LOGGED_OUT) {
            startLoginActivity()
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_CODE = 1234
        private const val TRIPS_ACTIVITY_REQUEST_CODE = 4325
        private const val LOGIN_ACTIVITY_REQUEST_CODE = 2598
    }
}
