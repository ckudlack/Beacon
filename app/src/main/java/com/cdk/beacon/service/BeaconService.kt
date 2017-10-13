package com.cdk.beacon.service

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import com.cdk.beacon.data.MyLocation
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase

class BeaconService : JobService() {

    override fun onStartJob(params: JobParameters): Boolean {
        // Get location, send to server
        sendLocation()
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        // Shut everything down
        return false
    }

    @SuppressLint("MissingPermission")
    private fun sendLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val lastLocation = fusedLocationProviderClient.lastLocation

        // --- Test code ---
        val latitude = 34.1234
        val longitude = -122.084

        // Send these to Firebase
        val database = FirebaseDatabase.getInstance()
        val locationReference = database.reference
        val currentTimeMillis = System.currentTimeMillis()

        locationReference
                .child("locations")
                .push()
                .setValue(MyLocation(latitude, longitude, currentTimeMillis))

        // --- End test code ---


        /*lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude

                // Send these to Firebase
                val database = FirebaseDatabase.getInstance()
                val locationReference = database.reference
                val currentTimeMillis = System.currentTimeMillis()

                toast("Sent location")

                locationReference
                        .child("locations")
                        .push()
                        .setValue(MyLocation(latitude, longitude, currentTimeMillis))
            }
        }*/
    }

    companion object {

        private val JOB_ID = 123
        private val SIX_HOURS = 6 * 60 * 60 * 1000L
        private val TEN_SEC = 5 * 1000L

        // There is a minimum period of 15 mins for a periodic JobService
        fun schedule(context: Context) {
            val componentName = ComponentName(context, BeaconService::class.java)
            val builder = JobInfo.Builder(JOB_ID, componentName)
//                    .setPeriodic(FIVE_SEC)
//                    .setRequiresBatteryNotLow(true)
                    .setRequiresCharging(false)
                    .setMinimumLatency(TEN_SEC)
                    .setOverrideDeadline((5 * TEN_SEC))

            val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            scheduler.schedule(builder.build())
        }
    }
}
