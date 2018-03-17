package com.cdk.beacon

import android.app.job.JobInfo
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cdk.beacon.data.BeaconTrip
import kotlinx.android.synthetic.main.trips_item_view.view.*

class TripsAdapter(private val callback: TripClickedCallback, private val activeJob: JobInfo?) : RecyclerView.Adapter<TripsAdapter.TripsViewHolder>() {

    private val tripList: MutableList<BeaconTrip> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TripsViewHolder {
        val tripsViewHolder = TripsViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.trips_item_view, parent, false))
        tripsViewHolder.itemView.setOnClickListener {
            val adapterPosition = tripsViewHolder.adapterPosition
            callback.onTripClicked(tripList[adapterPosition], tripList[adapterPosition].id == activeJob?.extras?.getString("trip_id") ?: "")
        }
        return tripsViewHolder
    }

    override fun getItemCount(): Int = tripList.size

    override fun onBindViewHolder(holder: TripsViewHolder?, position: Int) {
        holder?.bind(tripList[position], activeJob?.extras?.getString("trip_id") ?: "")
    }

    fun updateItems(trips: MutableList<BeaconTrip>) {
        tripList.clear()
        tripList.addAll(trips)
        notifyDataSetChanged()
    }

    class TripsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(trip: BeaconTrip?, tripId: String) {
            itemView.trip_name.text = trip?.name
            itemView.trip_active.visibility = if (tripId == trip?.id) View.VISIBLE else View.INVISIBLE
        }
    }

    interface TripClickedCallback {
        fun onTripClicked(trip: BeaconTrip, isActive: Boolean)
    }
}

