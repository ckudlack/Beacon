package com.cdk.beacon

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.title)
}