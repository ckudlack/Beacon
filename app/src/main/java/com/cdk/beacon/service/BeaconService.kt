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
import android.os.Build
import android.os.PersistableBundle
import android.support.v4.app.NotificationCompat
import com.cdk.beacon.DateTimeUtils
import com.cdk.beacon.R
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.data.MyLocation
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.toast


class BeaconService : JobService() {

    private var callback: LocationCallback? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

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

        val tripId = jobParams.extras.get(TRIP_ID) as String

        callback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    for (location in locationResult.locations) {
                        // Update UI with location data
                        // ...

                        val latitude = location.latitude
                        val longitude = location.longitude

                        sendLocationToFirebase(latitude, longitude, tripId)
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

        fusedLocationProviderClient?.requestLocationUpdates(createLocationRequest(), callback, null)
    }

    private fun removeListenerAndFinishJob(jobParams: JobParameters) {
        fusedLocationProviderClient?.removeLocationUpdates(callback)
        callback = null
        jobFinished(jobParams, false)
    }

    private fun sendLocationToFirebase(latitude: Double, longitude: Double, tripId: String) {
        // Send these to Firebase

        val currentTimeMillis = System.currentTimeMillis()
        val database = FirebaseFirestore.getInstance()

        database.collection("locations")
                .document(tripId)
                .collection("beacons")
                .add(MyLocation(longitude, latitude, currentTimeMillis))

        sendNotification("Location sent at " + DateTimeUtils.formatWithWeekday(this, currentTimeMillis))
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

        private const val JOB_ID = 123
        private const val ONE_MINUTE = 60 * 1000L
        private const val TRIP_ID = "trip_id"

        // There is a minimum period of 15 mins for a periodic JobService
        fun schedule(context: Context, trip: BeaconTrip) {
            val componentName = ComponentName(context, BeaconService::class.java)

            val bundle = PersistableBundle()
            bundle.putString(TRIP_ID, trip.id)

            val freqInMillis = trip.beaconFrequency * ONE_MINUTE

            val builder = JobInfo.Builder(JOB_ID, componentName)
                    .setRequiresCharging(false)
                    .setRequiredNetworkType(NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .setPeriodic(freqInMillis)
                    .setExtras(bundle)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setRequiresBatteryNotLow(true)
            }

            val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            scheduler.cancelAll()
            scheduler.schedule(builder.build())
        }

        fun stopAllBroadcasts(context: Context) {
            val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            scheduler.cancelAll()
        }

        fun stopBroadcasting(context: Context, tripId: String) {
            val scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val runningJob = scheduler.allPendingJobs.takeIf { !it.isEmpty() }?.get(0)?.takeIf { it.extras.getString(TRIP_ID) == tripId }
            runningJob?.let { scheduler.cancel(it.id) }
        }
    }
}
