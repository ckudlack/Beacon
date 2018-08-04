package com.cdk.beacon.map

import com.airbnb.epoxy.TypedEpoxyController

class MapController : TypedEpoxyController<List<MapItem>>() {

    override fun buildModels(data: List<MapItem>?) {
        data?.forEachIndexed { index, myLocation ->
            mapPagerItemView {
                id(index)
                date(myLocation.timestamp)
            }
        }
    }
}