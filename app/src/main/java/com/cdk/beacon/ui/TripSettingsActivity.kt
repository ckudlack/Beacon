package com.cdk.beacon.ui

import android.app.ProgressDialog
import android.app.job.JobScheduler
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import android.view.MenuItem
import com.cdk.beacon.R
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.TripSettingsContract
import com.cdk.beacon.mvp.presenter.TripSettingsPresenter
import com.cdk.beacon.mvp.repository.UserTripsRepository
import com.cdk.beacon.mvp.usecase.TripSettingsUseCase
import com.cdk.beacon.service.BeaconService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.alert

class TripSettingsActivity : AppCompatActivity(), TripSettingsContract.View, SharedUserFragment.OnListFragmentInteractionListener, Listener {

    private lateinit var trip: BeaconTrip
    private lateinit var presenter: TripSettingsContract.Presenter

    private val dialog: ProgressDialog? by lazy {
        indeterminateProgressDialog("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        trip = intent.getParcelableExtra("trip")

        presenter = TripSettingsPresenter(this, TripSettingsUseCase(UserTripsRepository(FirebaseFirestore.getInstance())))

        supportFragmentManager.beginTransaction().replace(android.R.id.content, TripPreferenceFragment.newInstance(trip)).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSharedUserRemoved(itemPosition: Int) = presenter.onSharedUserRemoved(itemPosition, trip)

    override fun onSharedUserAdded(email: String) = presenter.onSharedUserAdded(email, trip)

    override fun showLoading() {
        dialog?.show()
    }

    override fun hideLoading() {
        dialog?.hide()
    }

    override fun showError(error: Throwable) {
        alert(error.message ?: getString(R.string.error_occurred), getString(R.string.error)).show()
    }

    override fun showToast(textRes: Int) {
        toast(textRes)
    }

    override fun updateSharedUsers(sharedUsers: List<String>) {
        trip.observers = sharedUsers
        supportFragmentManager.findFragmentByTag(SHARED_USERS_TAG)?.let {
            (it as SharedUserFragment).updateSharedUsers(sharedUsers)
        }
    }

    override fun setBroadcastToNewFrequency(trip: BeaconTrip) {
        this.trip = trip
        BeaconService.schedule(this, trip)
    }

    override fun returnToTripsActivity() {
        startActivity(intentFor<TripsActivity>().clearTop())
    }

    override fun stopBroadcasting(tripId: String) {
        BeaconService.stopBroadcasting(this, tripId)
    }

    override fun onTripNameChanged(name: String) = presenter.onTripNameChanged(name, trip)

    override fun onBeaconFrequencyChanged(frequency: Int) = presenter.onBeaconFrequencyUpdated(frequency, trip)

    override fun onDeleteTripConfirmed() = presenter.deleteTrip(trip.id)

    class TripPreferenceFragment : PreferenceFragmentCompat() {

        private var listener: Listener? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setHasOptionsMenu(true)
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.pref_settings)

            val trip = arguments?.getParcelable<BeaconTrip>(TRIP)

            val namePreference = findPreference(TRIP_NAME) as EditTextPreference
            namePreference.summary = trip?.name
            namePreference.text = trip?.name
            namePreference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            val frequencyPreference = findPreference(BEACON_FREQ) as ListPreference
            val index = frequencyPreference.findIndexOfValue(trip?.beaconFrequency.toString())
            frequencyPreference.summary = frequencyPreference.entries[index]
            frequencyPreference.value = trip?.beaconFrequency.toString()
            frequencyPreference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            val scheduler = context?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler?
            val job = scheduler?.allPendingJobs?.takeIf { !it.isEmpty() }?.get(0)

            findPreference(STOP_BROADCAST).isVisible = trip?.id == job?.extras?.getString("trip_id")
            findPreference(DELETE_TRIP).isVisible = trip?.userId == FirebaseAuth.getInstance().currentUser?.uid
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            val trip = arguments?.getParcelable<BeaconTrip>(TRIP)

            when {
                preference?.key == "shared_options" -> {

                    fragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.slide_in_right, 0, 0, R.anim.slide_out_right)
                            ?.add(android.R.id.content, SharedUserFragment.newInstance(trip?.observers
                                    ?: listOf()), SHARED_USERS_TAG)?.addToBackStack(null)?.commit()
                }
                preference?.key == STOP_BROADCAST -> alert(getString(R.string.end_trip_broadcast), getString(R.string.stop_broadcasting)) {
                    yesButton {
                        context?.let { BeaconService.stopAllBroadcasts(it) }
                    }
                    noButton { }
                }.show()
                preference?.key == DELETE_TRIP -> alert(getString(R.string.permanent_deletion_warning), getString(R.string.warning)) {
                    yesButton {
                        listener?.onDeleteTripConfirmed()
                    }
                    noButton { }
                }.show()
            }
            return super.onPreferenceTreeClick(preference)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == android.R.id.home) {
                activity?.finish()
                return true
            }
            return super.onOptionsItemSelected(item)
        }


        override fun onAttach(context: Context) {
            super.onAttach(context)
            listener = context as Listener
        }

        override fun onDetach() {
            listener = null
            super.onDetach()
        }

        /**
         * A preference value change listener that updates the preference's summary
         * to reflect its new value.
         */
        private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
            val stringValue = value.toString()

            if (preference is ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                val index = preference.findIndexOfValue(stringValue)

                // Set the summary to reflect the new value.
                preference.setSummary(
                        if (index >= 0)
                            preference.entries[index]
                        else
                            null)

                listener?.onBeaconFrequencyChanged(stringValue.toInt())
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.summary = stringValue
                listener?.onTripNameChanged(preference.summary.toString())
            }
            true
        }

        companion object {

            private const val TRIP = "trip"
            private const val TRIP_NAME = "trip_name"
            private const val BEACON_FREQ = "broadcast_frequency"
            private const val STOP_BROADCAST = "stop_broadcast"
            private const val DELETE_TRIP = "delete_trip"

            fun newInstance(trip: BeaconTrip): TripPreferenceFragment {
                val fragment = TripPreferenceFragment()
                val args = Bundle()
                args.putParcelable(TRIP, trip)
                fragment.arguments = args
                return fragment
            }
        }
    }

    companion object {
        const val SHARED_USERS_TAG = "shared_users_tag"
    }
}

interface Listener {
    fun onTripNameChanged(name: String)
    fun onBeaconFrequencyChanged(frequency: Int)
    fun onDeleteTripConfirmed()
}
