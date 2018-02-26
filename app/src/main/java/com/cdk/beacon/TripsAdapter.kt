package com.cdk.beacon

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cdk.beacon.data.BeaconTrip
import kotlinx.android.synthetic.main.trips_item_view.view.*

class TripsAdapter(private val callback: TripClickedCallback) : RecyclerView.Adapter<TripsAdapter.TripsViewHolder>() {

    private val tripList: MutableList<BeaconTrip> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TripsViewHolder {
        val tripsViewHolder = TripsViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.trips_item_view, parent, false))
        tripsViewHolder.itemView.setOnClickListener { callback.onTripClicked(tripList[tripsViewHolder.adapterPosition].id) }
        return tripsViewHolder
    }

    override fun getItemCount(): Int = tripList.size

    override fun onBindViewHolder(holder: TripsViewHolder?, position: Int) {
        holder?.bind(tripList[position])
    }

    fun updateItems(trips: MutableList<BeaconTrip>) {
        tripList.clear()
        tripList.addAll(trips)
        notifyDataSetChanged()
    }

    class TripsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(trip: BeaconTrip?) {
            itemView.trip_name.text = trip?.name
        }
    }

    interface TripClickedCallback {
        fun onTripClicked(tripId: String)
    }
}

