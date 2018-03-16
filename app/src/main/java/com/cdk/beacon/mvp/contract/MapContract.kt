package com.cdk.beacon.mvp.contract

import com.cdk.beacon.data.MyLocation
import com.cdk.beacon.mvp.presenter.BasePresenter
import com.cdk.beacon.ui.BaseView

interface MapContract {

    interface Presenter : BasePresenter {
        fun getLocations(sortByValue: String, tripId: String)
        fun settingsButtonClicked()
    }

    interface View : BaseView {
        fun displayLocations(locations: List<MyLocation>)
        fun launchSettingsActivity()
    }
}