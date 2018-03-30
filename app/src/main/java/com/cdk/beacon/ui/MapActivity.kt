package com.cdk.beacon.ui

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.cdk.beacon.LocationListener
import com.cdk.beacon.LocationPagerAdapter
import com.cdk.beacon.MyLocationMarkerRenderer
import com.cdk.beacon.R
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.data.MyLocation
import com.cdk.beacon.mvp.contract.MapContract
import com.cdk.beacon.mvp.datasource.UserLocalDataSource
import com.cdk.beacon.mvp.datasource.UserRemoteDataSource
import com.cdk.beacon.mvp.presenter.MapPresenter
import com.cdk.beacon.mvp.repository.LocationRepository
import com.cdk.beacon.mvp.repository.UserRepository
import com.cdk.beacon.mvp.usecase.MapUseCase
import com.cdk.bettermapsearch.MapPagerView
import com.cdk.bettermapsearch.clustering.MapPagerClusterManager
import com.cdk.bettermapsearch.clustering.MapPagerMarkerRenderer
import com.cdk.bettermapsearch.interfaces.MapReadyCallback
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.*


class MapActivity : AppCompatActivity(), MapReadyCallback<MyLocation>, MapContract.View, GoogleMap.InfoWindowAdapter, LocationListener {

    private lateinit var mapPagerView: MapPagerView<MyLocation>
    private lateinit var presenter: MapContract.Presenter
    private var isUsersTrip: Boolean? = null
    private var trip: BeaconTrip? = null
    private var googleMap: GoogleMap? = null
    private val dialog: ProgressDialog? by lazy {
        indeterminateProgressDialog("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        trip = intent.getParcelableExtra("trip")

        val firestore = FirebaseFirestore.getInstance()
        presenter = MapPresenter(this, MapUseCase(LocationRepository(firestore),
                UserRepository(UserLocalDataSource(defaultSharedPreferences), UserRemoteDataSource(firestore))))

        mapPagerView = findViewById(R.id.map_pager)
        mapPagerView.onCreate(savedInstanceState)
        mapPagerView.setAdapter(LocationPagerAdapter(this))
        mapPagerView.getMapAsync(this)
        mapPagerView.setClusteringEnabled(false)
        mapPagerView.customInfoWindowAdapter = this

        isUsersTrip = intent.getBooleanExtra("isUsersTrip", true)

        title = trip?.name
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        isUsersTrip?.let {
            menu?.findItem(R.id.action_settings)?.isVisible = it
            menu?.findItem(R.id.action_leave_trip)?.isVisible = !it
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_settings -> presenter.settingsButtonClicked()
            R.id.action_leave_trip -> presenter.leaveTripClicked()
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        mapPagerView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapPagerView.onPause()
    }

    override fun onStart() {
        super.onStart()
        mapPagerView.onStart()
        trip?.let { presenter.getLocations("timeStamp", it.id) }
    }

    override fun onStop() {
        super.onStop()
        mapPagerView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapPagerView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapPagerView.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap, clusterManager: MapPagerClusterManager<MyLocation>): MapPagerMarkerRenderer<MyLocation> {
        this.googleMap = googleMap
        return MyLocationMarkerRenderer(this, googleMap, clusterManager)
    }

    override fun getInfoContents(marker: Marker?): View? = null

    override fun getInfoWindow(marker: Marker?): View = View.inflate(this, R.layout.empty_window_view, null)

    private fun createBoundsFromList(items: List<MyLocation>): LatLngBounds {
        val boundsBuilder = LatLngBounds.Builder()
//        val polylineOptions = PolylineOptions()

        items.forEach {
//            polylineOptions.add(it.position)
            boundsBuilder.include(it.position)
        }

//        googleMap?.addPolyline(polylineOptions.width(5f).color(Color.RED))
        return boundsBuilder.build()
    }

    override fun showLoading() {
        dialog?.show()
    }

    override fun hideLoading() {
        dialog?.hide()
    }

    override fun showError(error: Throwable) {
        alert(error.message ?: getString(R.string.error_occurred), getString(R.string.error)).show()
    }

    override fun displayLocations(locations: List<MyLocation>) {
        mapPagerView.updateMapItems(locations, false)
        mapPagerView.moveCameraToBounds(createBoundsFromList(locations), 200)
    }

    override fun launchSettingsActivity() {
        startActivity<TripSettingsActivity>("trip" to (trip ?: listOf<BeaconTrip>()))
    }

    override fun showLeaveTripAlertDialog() {
        alert(getString(R.string.leave_trip_warning), getString(R.string.warning)) {
            yesButton { presenter.leaveTripConfirmed(trip?.id) }
            noButton {}
        }.show()
    }

    override fun goToTripsActivity() {
        finish()
    }

    override fun directionsClicked(latitude: Double, longitude: Double) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=$latitude,$longitude(User's+Location)")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.`package` = "com.google.android.apps.maps"
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }
}
