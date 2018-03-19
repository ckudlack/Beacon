package com.cdk.beacon.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.cdk.beacon.R
import com.cdk.beacon.mvp.contract.MainContract
import com.cdk.beacon.mvp.presenter.MainPresenter
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity

@Suppress("unused")
class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart(FirebaseAuth.getInstance().currentUser?.email)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.logout -> {
                presenter.onLogOutClicked()
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(error: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startLoginActivity() {
        startActivity<LoginActivity>()
    }

    override fun scheduleBeaconService() {
    }

    override fun startTripsActivity() {
        startActivity<TripsActivity>()
    }

    override fun close() {
        finish()
    }

    override fun logOut() {
        FirebaseAuth.getInstance().signOut()
    }

    companion object {
        private val LOCATION_PERMISSION_CODE = 1234
    }
}
