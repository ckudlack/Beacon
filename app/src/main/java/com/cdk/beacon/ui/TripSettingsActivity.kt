package com.cdk.beacon.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
import android.view.MenuItem
import com.cdk.beacon.R
import com.cdk.beacon.data.BeaconTrip

/**
 * A [PreferenceActivity] that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 *
 * See [Android Design: Settings](http://developer.android.com/design/patterns/settings.html)
 * for design guidelines and the [Settings API Guide](http://developer.android.com/guide/topics/ui/settings.html)
 * for more information on developing a Settings UI.
 */
class TripSettingsActivity : AppCompatActivity(), SharedUserFragment.OnListFragmentInteractionListener {

    private lateinit var trip: BeaconTrip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()

        trip = intent.getParcelableExtra("trip")

        supportFragmentManager.beginTransaction().replace(android.R.id.content, TripPreferenceFragment.newInstance(trip.observers)).commit()
    }

    /**
     * Set up the [android.app.ActionBar], if the API is available.
     */
    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == android.R.id.home) {
            if (!super.onOptionsItemSelected(item)) {
                NavUtils.navigateUpFromSameTask(this)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSharedUserRemoved(itemPosition: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class TripPreferenceFragment : PreferenceFragmentCompat() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setHasOptionsMenu(true)
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.pref_settings)

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("example_text"))
            bindPreferenceSummaryToValue(findPreference("broadcast_frequency"))
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            if (preference?.key == "shared_options") {
                android.R.animator.fade_in
                fragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.slide_in_right, 0, 0, R.anim.slide_out_right)?.add(android.R.id.content, SharedUserFragment.newInstance(arguments?.getStringArrayList(SHARED_USERS_LIST)
                        ?: listOf()))?.addToBackStack(null)?.commit()
            }
            return super.onPreferenceTreeClick(preference)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == android.R.id.home) {
                startActivity(Intent(activity, TripSettingsActivity::class.java))
                return true
            }
            return super.onOptionsItemSelected(item)
        }

        companion object {

            private const val SHARED_USERS_LIST = "shared_users_list"

            fun newInstance(sharedUsersList: List<String>): TripPreferenceFragment {
                val fragment = TripPreferenceFragment()
                val args = Bundle()
                args.putStringArrayList(SHARED_USERS_LIST, sharedUsersList as ArrayList<String>?)
                fragment.arguments = args
                return fragment
            }
        }
    }

    companion object {

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

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.summary = stringValue
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
    }
}
