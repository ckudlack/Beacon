package com.cdk.beacon.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.cdk.beacon.R
import com.cdk.beacon.service.BeaconService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("unused")
class MainActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        start.setOnClickListener({ startButtonClicked() })
    }

    override fun onStart() {
        super.onStart()
        /*if (firebaseAuth.currentUser != null) {
            // Do something... or not
        } else {
            firebaseAuth.createUserWithEmailAndPassword("thebigcheese40@gmail.com", "asw0rd").addOnCompleteListener(this, { task ->
                if (task.isSuccessful) {

                } else {

                }
            })
        }*/
    }

    fun startButtonClicked() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            BeaconService.schedule(this)
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_CODE && grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            BeaconService.schedule(this)
        }
    }

    companion object {
        private val LOCATION_PERMISSION_CODE = 1234
    }
}
