package com.cdk.beacon.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.cdk.beacon.LocationPagerAdapter
import com.cdk.beacon.MyLocationMarkerRenderer
import com.cdk.beacon.R
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.data.MyLocation
import com.cdk.beacon.mvp.contract.MapContract
import com.cdk.beacon.mvp.presenter.MapPresenter
import com.cdk.beacon.mvp.repository.LocationRepository
import com.cdk.beacon.mvp.usecase.MapUseCase
import com.cdk.bettermapsearch.MapPagerView
import com.cdk.bettermapsearch.clustering.MapPagerClusterManager
import com.cdk.bettermapsearch.clustering.MapPagerMarkerRenderer
import com.cdk.bettermapsearch.interfaces.MapReadyCallback
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.startActivity

class MapActivity : AppCompatActivity(), MapReadyCallback<MyLocation>, MapContract.View, GoogleMap.InfoWindowAdapter {

    private lateinit var mapPagerView: MapPagerView<MyLocation>
    private lateinit var presenter: MapContract.Presenter
    private var isUsersTrip: Boolean? = null
    private var trip: BeaconTrip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        trip = intent.getParcelableExtra("trip")

        presenter = MapPresenter(this, MapUseCase(LocationRepository(FirebaseFirestore.getInstance())))

        mapPagerView = findViewById(R.id.map_pager)
        mapPagerView.onCreate(savedInstanceState)
        mapPagerView.setAdapter(LocationPagerAdapter())
        mapPagerView.getMapAsync(this)
        mapPagerView.setClusteringEnabled(false)
        mapPagerView.customInfoWindowAdapter = this

        isUsersTrip = intent.getBooleanExtra("isUsersTrip", true)

        trip?.let { presenter.getLocations("timeStamp", it.id) }

        title = trip?.name
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        isUsersTrip?.let {
            menu?.findItem(R.id.action_settings)?.isVisible = it
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_settings -> presenter.settingsButtonClicked()
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
        return MyLocationMarkerRenderer(this, googleMap, clusterManager)
    }

    override fun getInfoContents(marker: Marker?): View? = null

    override fun getInfoWindow(marker: Marker?): View = View.inflate(this, R.layout.empty_window_view, null)

    private fun createBoundsFromList(items: List<MyLocation>): LatLngBounds {
        val boundsBuilder = LatLngBounds.Builder()
        for (item in items) {
            boundsBuilder.include(item.position)
        }
        return boundsBuilder.build()
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

    override fun displayLocations(locations: List<MyLocation>) {
        mapPagerView.updateMapItems(locations, false)
        mapPagerView.moveCameraToBounds(createBoundsFromList(locations), 200)
    }

    override fun launchSettingsActivity() {
        startActivity<TripSettingsActivity>("trip" to (trip ?: listOf<BeaconTrip>()))
    }
}
