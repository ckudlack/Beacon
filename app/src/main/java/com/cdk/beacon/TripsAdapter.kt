package com.cdk.beacon

import android.app.job.JobInfo
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cdk.beacon.data.BeaconTrip
import kotlinx.android.synthetic.main.trips_header_view.view.*
import kotlinx.android.synthetic.main.trips_item_view.view.*
import org.jetbrains.anko.textResource

class TripsAdapter(private val callback: TripClickedCallback, private var activeJob: JobInfo?, val userId: String?) : RecyclerView.Adapter<TripsAdapter.ViewHolder>() {

    private val tripList: MutableList<BeaconTrip?> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder : ViewHolder

        if (viewType == R.layout.trips_item_view) {
            viewHolder = TripsViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            viewHolder.itemView.setOnClickListener {
                val adapterPosition = viewHolder.adapterPosition
                callback.onTripClicked(tripList[adapterPosition]!!, tripList[adapterPosition]!!.id == activeJob?.extras?.getString("trip_id") ?: "")
            }
        } else {
            viewHolder = TripsHeaderViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            viewHolder.itemView.setOnClickListener {  } // do nothing when clicked
        }
        return viewHolder
    }

    override fun getItemCount(): Int = tripList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is TripsViewHolder -> holder.bind(tripList[position], activeJob?.extras?.getString("trip_id"))
            is TripsHeaderViewHolder -> holder.bind(position == 0)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (tripList[position]) {
            null -> R.layout.trips_header_view
            else -> R.layout.trips_item_view
        }
    }

    fun updateItems(trips: MutableList<BeaconTrip?>, activeJob: JobInfo?) {
        this.activeJob = activeJob
        tripList.clear()
        tripList.addAll(trips)
        notifyDataSetChanged()
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class TripsViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bind(trip: BeaconTrip?, tripId: String?) {
            itemView.trip_name.text = trip?.name
            itemView.trip_active.visibility = if (tripId == trip?.id) View.VISIBLE else View.INVISIBLE
        }
    }

    class TripsHeaderViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bind(isMyTrip: Boolean) {
            itemView.header.textResource = if (isMyTrip) R.string.my_trips else R.string.shared_trips
        }
    }

    interface TripClickedCallback {
        fun onTripClicked(trip: BeaconTrip, isActive: Boolean)
    }
}

