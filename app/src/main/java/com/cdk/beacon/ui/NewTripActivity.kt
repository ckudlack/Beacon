package com.cdk.beacon.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import com.cdk.beacon.R
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.NewTripContract
import com.cdk.beacon.mvp.presenter.NewTripPresenter
import com.cdk.beacon.mvp.repository.UserTripsRepository
import com.cdk.beacon.mvp.usecase.NewTripUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_new_trip.*
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast

class NewTripActivity : AppCompatActivity(), NewTripContract.View, SharedUserFragment.OnListFragmentInteractionListener {

    private lateinit var presenter: NewTripContract.Presenter
    private var sharedUsers: MutableList<String>? = null
    private var fragment: SharedUserFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip)

        sharedUsers = savedInstanceState?.takeIf { it.containsKey(SHARED_USERS) }?.getStringArrayList(SHARED_USERS) ?: mutableListOf()

        presenter = NewTripPresenter(NewTripUseCase(UserTripsRepository(FirebaseFirestore.getInstance())), this)
        fragment = SharedUserFragment.newInstance(sharedUsers?.toList())

        supportFragmentManager.beginTransaction().replace(R.id.shared_users_content_view, fragment).commit()

        beacon_frequency.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val frequencies = resources.getStringArray(R.array.pref_sync_frequency_titles).asList()
                selector(resources.getString(R.string.pref_title_sync_frequency), frequencies, { _, i ->
                    beacon_frequency.setText(frequencies[i])
                    beacon_frequency.tag = resources.getStringArray(R.array.pref_sync_frequency_values)[i].toInt()
                })
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.trip_added_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.trip_done -> {
                // if there is no current user, force a crash
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                presenter.addTrip(userId, BeaconTrip(ArrayList(), trip_name_edittext.text.toString(), sharedUsers?.toList()
                        ?: listOf(), "", userId, beacon_frequency.tag as Int, System.currentTimeMillis(), System.currentTimeMillis()))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putStringArrayList(SHARED_USERS, sharedUsers as java.util.ArrayList<String>)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(error: Throwable) {
    }

    override fun goToStartBeaconActivity() {
        finish()
    }

    override fun addToList(email: String) {
        sharedUsers?.add(email)
        fragment?.updateSharedUsers(sharedUsers?.toList())
    }

    override fun showToast(text: Int) {
        toast(text)
    }

    override fun onSharedUserRemoved(itemPosition: Int) {
        sharedUsers?.removeAt(itemPosition)
        fragment?.updateSharedUsers(sharedUsers?.toList())
    }

    override fun onSharedUserAdded(email: String) {
        presenter.onSharedUserAdded(email)
    }

    companion object {
        const val SHARED_USERS = "shared_users"
    }
}
