package com.cdk.beacon.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cdk.beacon.MyLocationMarkerRenderer
import com.cdk.beacon.R
import com.cdk.beacon.data.MyLocation
import com.cdk.bettermapsearch.MapPagerView
import com.cdk.bettermapsearch.clustering.MapPagerClusterManager
import com.cdk.bettermapsearch.clustering.MapPagerMarkerRenderer
import com.cdk.bettermapsearch.interfaces.MapReadyCallback
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class MapActivity : AppCompatActivity(), MapReadyCallback<MyLocation> {

    private lateinit var mapPagerView: MapPagerView<MyLocation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapPagerView = findViewById(R.id.map_pager)
        mapPagerView.onCreate(savedInstanceState)
        mapPagerView.getMapAsync(this)
        mapPagerView.setClusteringEnabled(false)

        val database = FirebaseDatabase.getInstance()
        val locationReference = database.reference

        val locationList = ArrayList<MyLocation>()

        locationReference.child("locations").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.value as Map<String, Any>

                value.entries.forEach {
                    val location = it.value as Map<*, *>
                    //Get phone field and append to list

                    val latitude = location["latitude"] as Double
                    val longitude = location["longitude"] as Double
                    val timestamp = location["timeStamp"] as Long

                    locationList.add(MyLocation(latitude, longitude, timestamp))
                }

                mapPagerView.updateMapItems(locationList, false)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
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
}
