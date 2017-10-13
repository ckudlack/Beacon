package com.cdk.beacon

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cdk.beacon.data.MyLocation
import com.cdk.bettermapsearch.MapPagerAdapter

class LocationPagerAdapter : MapPagerAdapter<MyLocation, ItemViewHolder>() {

    @SuppressLint("MissingSuperCall")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val location = getItemAtPosition(position)
        val formattedDateTime = DateTimeUtils.formatWithWeekday(holder.itemView.context, location.timeStamp)

        holder.position.text = location.latitude.toString() + ", " + location.longitude.toString()
        holder.time.text = formattedDateTime
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.pager_item_view, parent, false))
    }
}