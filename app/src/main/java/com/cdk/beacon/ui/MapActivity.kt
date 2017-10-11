package com.cdk.beacon.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cdk.beacon.R
import com.cdk.beacon.data.MyLocation
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class MapActivity : AppCompatActivity() {

    var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val database = FirebaseDatabase.getInstance()
        val locationReference = database.reference

        val locationList = ArrayList<MyLocation>()

        locationReference.child("locations").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value: Map<String, Any> = dataSnapshot.value as Map<String, Any>

                value.entries.forEach {
                    val location = it.value as Map<*, *>
                    //Get phone field and append to list

                    val latitude = location["latitude"] as Double
                    val longitude = location["longitude"] as Double
                    val timestamp = location["timeStamp"] as Long

                    locationList.add(MyLocation(latitude, longitude, timestamp))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        val mapFragment : MapFragment? = fragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
        mapFragment?.getMapAsync { googleMap -> this.googleMap = googleMap }

        locationList.forEach {
            googleMap?.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)))
        }
    }


}
