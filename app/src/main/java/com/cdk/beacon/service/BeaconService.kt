package com.cdk.beacon.service

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import com.cdk.beacon.data.MyLocation
import com.google.android.gms.location.*
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.toast


class BeaconService : JobService() {

    private lateinit var callback: LocationCallback
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onStartJob(params: JobParameters): Boolean {
        // Get location, send to server
        getLocation(params)
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        // Shut everything down
        removeListenerAndFinishJob(params)
        return false
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(jobParams: JobParameters) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        callback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    for (location in locationResult.locations) {
                        // Update UI with location data
                        // ...

                        val latitude = location.latitude
                        val longitude = location.longitude

                        sendLocationToFirebase(latitude, longitude)
                        removeListenerAndFinishJob(jobParams)
                    }
                } else {
                    toast("Location is not available")
                    // TODO: Send notification
                    removeListenerAndFinishJob(jobParams)
                }
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                super.onLocationAvailability(locationAvailability)
                if (locationAvailability?.isLocationAvailable != true) {
                    toast("Location is not available")
                    // TODO: Send notification
                    removeListenerAndFinishJob(jobParams)
                }
            }
        }

        // --- DB Test code ---
        /*val latitude = 34.1234
        val longitude = -122.084

        // Send these to Firebase
        val database = FirebaseDatabase.getInstance()
        val locationReference = database.reference
        val currentTimeMillis = System.currentTimeMillis()

        locationReference
                .child("locations")
                .push()
                .setValue(MyLocation(latitude, longitude, currentTimeMillis))*/

        // --- End test code ---

        fusedLocationProviderClient.requestLocationUpdates(createLocationRequest(), callback, null)
    }

    private fun removeListenerAndFinishJob(jobParams: JobParameters) {
        fusedLocationProviderClient.removeLocationUpdates(callback)
        jobFinished(jobParams, false)
    }

    private fun sendLocationToFirebase(latitude: Double, longitude: Double) {
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

    private fun createLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_LOW_POWER
        return locationRequest
    }

    private fun sendNotification() {

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
