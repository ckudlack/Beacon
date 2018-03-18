package com.cdk.beacon.ui

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
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

            val namePreference = findPreference("trip_name")
            namePreference.summary = arguments?.getParcelable<BeaconTrip>(TRIP)?.name
            namePreference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            bindPreferenceSummaryToValue(findPreference("broadcast_frequency"))
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            if (preference?.key == "shared_options") {
                val trip = arguments?.getParcelable<BeaconTrip>(TRIP)

                fragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.slide_in_right, 0, 0, R.anim.slide_out_right)
                        ?.add(android.R.id.content, SharedUserFragment.newInstance(trip?.observers
                                ?: listOf()))?.addToBackStack(null)?.commit()
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

        /**
         * Binds a preference's summary to its value. More specifically, when the
         * preference's value is changed, its summary (line of text below the
         * preference title) is updated to reflect the value. The summary is also
         * immediately updated upon calling this method. The exact display format is
         * dependent on the type of preference.

         * @see .sBindPreferenceSummaryToValueListener
         */
        private fun bindPreferenceSummaryToValue(preference: Preference) {
            // Set the listener to watch for value changes.
            preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            // Trigger the listener immediately with the preference's
            // current value.
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.context)
                            .getString(preference.key, ""))
        }

        companion object {

            private const val TRIP = "trip"

            fun newInstance(trip: BeaconTrip): TripPreferenceFragment {
                val fragment = TripPreferenceFragment()
                val args = Bundle()
                args.putParcelable(TRIP, trip)
                fragment.arguments = args
                return fragment
            }
        }
    }
}

interface Listener {
    fun onTripNameChanged(name: String)
    fun onBeaconFrequencyChanged(frequency: Int)
}
