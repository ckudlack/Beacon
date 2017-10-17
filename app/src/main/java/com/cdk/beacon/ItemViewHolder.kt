package com.cdk.beacon

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val position: TextView = itemView.findViewById(R.id.position)
    val time: TextView = itemView.findViewById(R.id.date)
}