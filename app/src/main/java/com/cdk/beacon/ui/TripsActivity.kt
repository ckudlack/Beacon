package com.cdk.beacon.ui

import android.Manifest
import android.app.job.JobScheduler
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.cdk.beacon.R
import com.cdk.beacon.TripsAdapter
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.UserTripsContract
import com.cdk.beacon.mvp.presenter.UserTripsPresenter
import com.cdk.beacon.mvp.repository.UserTripsRepository
import com.cdk.beacon.mvp.usecase.TripsUseCase
import com.cdk.beacon.service.BeaconService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_trips.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton

class TripsActivity : AppCompatActivity(), UserTripsContract.View, TripsAdapter.TripClickedCallback, FilterListDialogFragment.Listener {

    private lateinit var presenter: UserTripsContract.Presenter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var adapter: TripsAdapter

    private var scheduler: JobScheduler? = null

    //TODO: Find a better way than caching this
    private var selectedTrip: BeaconTrip? = null

    private var filterDialog: FilterListDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)

        scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        firebaseAuth = FirebaseAuth.getInstance()
        presenter = UserTripsPresenter(this, TripsUseCase(UserTripsRepository(FirebaseFirestore.getInstance())))
        adapter = TripsAdapter(this, if (scheduler?.allPendingJobs?.isEmpty() == true) null else scheduler?.allPendingJobs?.get(0), firebaseAuth.currentUser?.uid)
        filterDialog = FilterListDialogFragment.newInstance()

        trips_list.adapter = adapter
        trips_list.layoutManager = LinearLayoutManager(this)

        add_trip_button.setOnClickListener { presenter.addTripClicked() }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.currentUser?.let {
            presenter.getTrips(it.uid, it.email ?: "", 0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.trips, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_filter -> filterDialog?.show(supportFragmentManager, "dialog")
            R.id.logout -> presenter.onLogOutClicked()
        }
        return super.onOptionsItemSelected(item)
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

    override fun showTrips(trips: MutableList<BeaconTrip?>) {
        adapter.updateItems(trips, if (scheduler?.allPendingJobs?.isEmpty() == true) null else scheduler?.allPendingJobs?.get(0))
    }

    override fun startAddTripActivity() = startActivity<NewTripActivity>()

    override fun startMapActivity(trip: BeaconTrip, isUsersTrip: Boolean) =
            startActivity<MapActivity>("trip" to trip, "isUsersTrip" to isUsersTrip)

    override fun startBeacon(trip: BeaconTrip) = BeaconService.schedule(this, trip)

    override fun showAlertDialog(trip: BeaconTrip, isUsersTrip: Boolean) {
        alert("This will pause any other active trip", "Start broadcasting?") {
            yesButton {
                presenter.startBeaconClicked(trip, ContextCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED, isUsersTrip)
            }
            noButton { presenter.dontStartBeaconClicked(trip, isUsersTrip) }
        }.show()
    }

    override fun requestPermissions(trip: BeaconTrip) {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_CODE)
    }

    override fun logOutUser() {
        firebaseAuth.signOut()
        setResult(RESULT_LOGGED_OUT)
        finish()
    }

    override fun onTripClicked(trip: BeaconTrip, isActive: Boolean) {
        selectedTrip = trip
        presenter.tripClicked(trip, isActive, selectedTrip?.userId == firebaseAuth.currentUser?.uid)
    }

    override fun onFilterItemClicked(position: Int) {
        filterDialog?.dismiss()
        firebaseAuth.currentUser?.let {
            presenter.getTrips(it.uid, it.email ?: "", position)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        presenter.onPermissionResult(selectedTrip, requestCode == LOCATION_PERMISSION_CODE && grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED, selectedTrip?.userId == firebaseAuth.currentUser?.uid)
    }

    companion object {
        private const val LOCATION_PERMISSION_CODE = 1234
        const val RESULT_LOGGED_OUT = 3810
    }
}
