package com.cdk.beacon.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobInfo.NETWORK_TYPE_ANY
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.cdk.beacon.DateTimeUtils
import com.cdk.beacon.R
import com.cdk.beacon.data.MyLocation
import com.google.android.gms.location.*
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.toast


class BeaconService : JobService() {

    private var callback: LocationCallback? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun startService(service: Intent?): ComponentName {
        return super.startService(service)
    }

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
                    sendNotification("Location is null - " + DateTimeUtils.formatWithWeekday(baseContext, System.currentTimeMillis()))
                    removeListenerAndFinishJob(jobParams)
                }
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                super.onLocationAvailability(locationAvailability)
                if (locationAvailability?.isLocationAvailable != true) {
                    toast("Location is not available")
                    sendNotification("Location unavailable - " + DateTimeUtils.formatWithWeekday(baseContext, System.currentTimeMillis()))
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
        callback = null
        jobFinished(jobParams, false)
    }

    private fun sendLocationToFirebase(latitude: Double, longitude: Double) {
        // Send these to Firebase

        val currentTimeMillis = System.currentTimeMillis()
        val database = FirebaseDatabase.getInstance()
        val locationReference = database.reference
        val tripId = id

        toast("Sent location")

        val ref = locationReference
                .child("beacons")
                .child(tripId)
                .push()

        ref.setValue(MyLocation(latitude, longitude, currentTimeMillis))

        sendNotification("Location sent at " + DateTimeUtils.formatWithWeekday(this, currentTimeMillis))

//        locationReference.child("trips").child(FirebaseAuth.getInstance().currentUser!!.uid).child(tripId).child("locationId").setValue(ref.key)

        /*val uid = FirebaseAuth.getInstance().currentUser?.uid

        val database = FirebaseDatabase.getInstance()
        val locationReference = database.getReference("users")

        val key = locationReference.push().key
        locationReference.child(key).setValue("blah")*/
    }

    private fun createLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_LOW_POWER
        return locationRequest
    }

    private fun sendNotification(text: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "location"
        val notificationId = 1234

        val builder = NotificationCompat.Builder(this, channelId).setVibrate(null).setSound(null).setOngoing(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channelName = "Beacon"
            val importance = NotificationManager.IMPORTANCE_LOW
            val notificationChannel: NotificationChannel

            notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationChannel.enableVibration(true)
            notificationChannel.enableLights(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(notificationId, builder.build())
    }

    companion object {

        private val JOB_ID = 123
        private val ONE_HOUR = 60 * 60 * 1000L
        private val TEN_SEC = 5 * 1000L
        private lateinit var id: String

        // There is a minimum period of 15 mins for a periodic JobService
        fun schedule(context: Context, tripId: String) {
            val componentName = ComponentName(context, BeaconService::class.java)
            val builder = JobInfo.Builder(JOB_ID, componentName)
                    .setPeriodic(12 * ONE_HOUR)
                    .setRequiresCharging(false)
                    .setRequiredNetworkType(NETWORK_TYPE_ANY)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setRequiresBatteryNotLow(true)
            }

            id = tripId

            val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            scheduler.schedule(builder.build())
        }
    }
}
