package com.cdk.beacon.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cdk.beacon.LocationPagerAdapter
import com.cdk.beacon.MyLocationMarkerRenderer
import com.cdk.beacon.R
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
import com.google.firebase.database.FirebaseDatabase

class MapActivity : AppCompatActivity(), MapReadyCallback<MyLocation>, MapContract.View {

    private lateinit var mapPagerView: MapPagerView<MyLocation>
    private lateinit var presenter: MapContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val database = FirebaseDatabase.getInstance()
        presenter = MapPresenter(this, MapUseCase(LocationRepository(database)))

        mapPagerView = findViewById(R.id.map_pager)
        mapPagerView.onCreate(savedInstanceState)
        mapPagerView.setAdapter(LocationPagerAdapter())
        mapPagerView.getMapAsync(this)
        mapPagerView.setClusteringEnabled(false)

        presenter.getLocations("timeStamp", intent.getStringExtra("tripId"))
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
}
