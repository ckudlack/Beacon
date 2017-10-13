package com.cdk.beacon

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val position = itemView.findViewById<TextView>(R.id.position)
    val time = itemView.findViewById<TextView>(R.id.date)
}