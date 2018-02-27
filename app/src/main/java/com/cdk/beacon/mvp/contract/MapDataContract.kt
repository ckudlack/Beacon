package com.cdk.beacon.mvp.contract

import com.cdk.beacon.data.MyLocation
import com.cdk.beacon.mvp.repository.BaseRepository
import rx.Observable

interface MapDataContract {

    interface Repository : BaseRepository {
        fun getLocationList(sortByValue: String, tripId: String): Observable<List<MyLocation>>
    }
}