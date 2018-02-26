package com.cdk.beacon

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cdk.beacon.data.BeaconTrip
import kotlinx.android.synthetic.main.trips_item_view.view.*

class TripsAdapter(private var tripList: List<BeaconTrip>?) : RecyclerView.Adapter<TripsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TripsViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.trips_item_view, parent, false)
        return TripsViewHolder(view)
    }

    override fun getItemCount(): Int = tripList?.size ?: 0

    override fun onBindViewHolder(holder: TripsViewHolder?, position: Int) {
        holder?.bind(tripList?.get(position))
    }
}

class TripsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    fun bind(trip: BeaconTrip?) {
        itemView.trip_name.text = trip?.name
    }
}