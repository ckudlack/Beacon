package com.cdk.beacon.ui

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.EditTextPreference
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.MenuItem
import com.cdk.beacon.R
import com.cdk.beacon.data.BeaconTrip
import com.cdk.beacon.mvp.contract.TripSettingsContract
import com.cdk.beacon.mvp.presenter.TripSettingsPresenter
import com.cdk.beacon.mvp.repository.UserTripsRepository
import com.cdk.beacon.mvp.usecase.TripSettingsUseCase
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.toast

class TripSettingsActivity : AppCompatActivity(), TripSettingsContract.View, SharedUserFragment.OnListFragmentInteractionListener, Listener {

    private lateinit var trip: BeaconTrip
    private lateinit var presenter: TripSettingsContract.Presenter

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(error: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showToast(textRes: Int) = toast(textRes)

    override fun updateSharedUsers(sharedUsers: List<String>) {
        trip.observers = sharedUsers
        val sharedUserFragment = supportFragmentManager.findFragmentByTag(SHARED_USERS_TAG)
        sharedUserFragment?.let {
            (it as SharedUserFragment).updateSharedUsers(sharedUsers)
        }
    }

    override fun onTripNameChanged(name: String) = presenter.onTripNameChanged(name, trip)

    override fun onBeaconFrequencyChanged(frequency: Int) = presenter.onBeaconFrequencyUpdated(frequency, trip)

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
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            if (preference?.key == "shared_options") {
                val trip = arguments?.getParcelable<BeaconTrip>(TRIP)

                fragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.slide_in_right, 0, 0, R.anim.slide_out_right)
                        ?.add(android.R.id.content, SharedUserFragment.newInstance(trip?.observers
                                ?: listOf()), SHARED_USERS_TAG)?.addToBackStack(null)?.commit()
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
}
