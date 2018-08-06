package com.cdk.beacon.ui

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.cdk.beacon.LocationListener
import com.cdk.beacon.R
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.data.MyLocation
import com.cdk.beacon.map.MapController
import com.cdk.beacon.map.MapItem
import com.cdk.beacon.map.Position
import com.cdk.beacon.mvp.contract.MapContract
import com.cdk.beacon.mvp.datasource.UserLocalDataSource
import com.cdk.beacon.mvp.datasource.UserRemoteDataSource
import com.cdk.beacon.mvp.presenter.MapPresenter
import com.cdk.beacon.mvp.repository.LocationRepository
import com.cdk.beacon.mvp.repository.UserRepository
import com.cdk.beacon.mvp.usecase.MapUseCase
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_map.*
import org.jetbrains.anko.*


class MapActivity : AppCompatActivity(), MapContract.View, GoogleMap.InfoWindowAdapter, LocationListener, GoogleMap.OnMarkerClickListener {

    private lateinit var presenter: MapContract.Presenter
    private var isUsersTrip: Boolean? = null
    private var trip: BeaconTrip? = null
    private var googleMap: GoogleMap? = null
    private val dialog: ProgressDialog? by lazy {
        indeterminateProgressDialog("")
    }
    private val controller = MapController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        trip = intent.getParcelableExtra("trip")

        val firestore = FirebaseFirestore.getInstance()
        presenter = MapPresenter(this, MapUseCase(LocationRepository(firestore),
                UserRepository(UserLocalDataSource(defaultSharedPreferences), UserRemoteDataSource(firestore))))

        val mapFragment = fragmentManager
                .findFragmentById(R.id.map) as MapFragment

        mapFragment.getMapAsync {
            googleMap = it
            googleMap?.setOnMarkerClickListener(this)
        }

        isUsersTrip = intent.getBooleanExtra("isUsersTrip", true)

        title = trip?.name

        setupRecyclerView()
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recycler_view)
    }

    private fun setupRecyclerView() = with(recycler_view) {
        layoutManager = android.support.v7.widget.LinearLayoutManager(this@MapActivity, RecyclerView.HORIZONTAL, false)
        adapter = controller.adapter
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

    override fun onStart() {
        super.onStart()
        trip?.let { presenter.getLocations("timeStamp", it.id) }
    }

    override fun getInfoContents(marker: Marker?): View? = null

    override fun getInfoWindow(marker: Marker?): View = View.inflate(this, R.layout.empty_window_view, null)

    private fun createBoundsFromList(items: List<MyLocation>): LatLngBounds {
        val boundsBuilder = LatLngBounds.Builder()
//        val polylineOptions = PolylineOptions()

        items.forEach {
//            polylineOptions.add(it.position)
            boundsBuilder.include(it.getPosition())
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
        createDotMarkers(locations)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(createBoundsFromList(locations), 100))
        controller.setData(locations.map { MapItem(Position(it.latitude, it.longitude), it.timeStamp) }.toList())
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        val index = marker?.tag?.toString()?.toIntOrNull()
        recycler_view.scrollToPosition(index!!)
        return true
    }

    private fun createDotMarkers(locations: List<MyLocation>) {
        locations.forEachIndexed { index, myLocation ->
            if (index == 0 || index == locations.size - 1) {
                val marker = googleMap?.addMarker(MarkerOptions()
                        .title(if (index == 0) getString(R.string.start) else getString(R.string.today))
                        .position(LatLng(myLocation.latitude, myLocation.longitude)))
                marker?.tag = index.toString()
            } else {
                val marker = googleMap?.addMarker(MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_dot))
                        .position(LatLng(myLocation.latitude, myLocation.longitude)))
                marker?.tag = index.toString()
            }
        }
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

    override fun goToTripsActivity() = finish()

    override fun directionsClicked(latitude: Double, longitude: Double) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=$latitude,$longitude(User's+Location)")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.`package` = "com.google.android.apps.maps"
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }
}
