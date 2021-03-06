package com.cdk.beacon

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cdk.beacon.data.MyLocation
import com.cdk.bettermapsearch.MapPagerAdapter
import kotlinx.android.synthetic.main.pager_item_view.view.*

class LocationPagerAdapter(private val listener: LocationListener) : MapPagerAdapter<MyLocation, ItemViewHolder>() {

    @SuppressLint("MissingSuperCall")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val location = getItemAtPosition(position)
        val formattedDateTime = DateTimeUtils.formatWithWeekday(holder.itemView.context, location.timeStamp)

        holder.position.text = location.latitude.toString() + ", " + location.longitude.toString()
        holder.time.text = formattedDateTime
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
        val itemViewHolder = ItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.pager_item_view, parent, false))
        itemViewHolder.itemView.directions_button.setOnClickListener {
            itemViewHolder.adapterPosition
                    .takeIf { it != RecyclerView.NO_POSITION }
                    ?.let { listener.directionsClicked(backingList[it].latitude, backingList[it].longitude) }
        }
        return itemViewHolder
    }
}

interface LocationListener {
    fun directionsClicked(latitude: Double, longitude: Double)
}