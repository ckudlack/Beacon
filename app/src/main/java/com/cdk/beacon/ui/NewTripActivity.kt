package com.cdk.beacon.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.cdk.beacon.R
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.NewTripContract
import com.cdk.beacon.mvp.presenter.NewTripPresenter
import com.cdk.beacon.mvp.repository.UserTripsRepository
import com.cdk.beacon.mvp.usecase.NewTripUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_new_trip.*

class NewTripActivity : AppCompatActivity(), NewTripContract.View {

    private lateinit var presenter: NewTripContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip)

        presenter = NewTripPresenter(NewTripUseCase(UserTripsRepository(FirebaseDatabase.getInstance())), this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.trip_added_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.trip_done -> {
                val userId = (FirebaseAuth.getInstance().currentUser?.uid
                        ?: "")
                presenter.addTrip(userId, BeaconTrip(ArrayList(), trip_name_edittext.text.toString(), listOf(email_to_share.text.toString()), userId))
            }
        }
        return super.onOptionsItemSelected(item)
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

    override fun goToStartBeaconActivity() {
        finish()
    }
}
