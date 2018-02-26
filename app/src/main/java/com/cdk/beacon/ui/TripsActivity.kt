package com.cdk.beacon.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.cdk.beacon.R
import com.cdk.beacon.TripsAdapter
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.UserTripsContract
import com.cdk.beacon.mvp.presenter.UserTripsPresenter
import com.cdk.beacon.mvp.repository.UserTripsRepository
import com.cdk.beacon.mvp.usecase.TripsUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_trips.*
import org.jetbrains.anko.startActivity

class TripsActivity : AppCompatActivity(), UserTripsContract.View {

    private lateinit var presenter: UserTripsContract.Presenter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var adapter: TripsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)

        firebaseAuth = FirebaseAuth.getInstance()
        presenter = UserTripsPresenter(this, TripsUseCase(UserTripsRepository(FirebaseDatabase.getInstance())))
        adapter = TripsAdapter()
        trips_list.adapter = adapter
        trips_list.layoutManager = LinearLayoutManager(this)

        add_trip_button.setOnClickListener { presenter.addTripClicked() }
    }

    override fun onStart() {
        super.onStart()
        presenter.getTrips(firebaseAuth.currentUser?.uid ?: "")
    }

    override fun showLoading() {
        refresh_layout.isRefreshing = true
    }

    override fun hideLoading() {
        refresh_layout.isRefreshing = false
    }

    override fun showError(error: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTrips(trips: MutableList<BeaconTrip>) {
        adapter.updateItems(trips)
    }

    override fun startAddTripActivity() {
        startActivity<NewTripActivity>()
    }
}
