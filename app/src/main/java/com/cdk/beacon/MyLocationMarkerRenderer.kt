package com.cdk.beacon

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.cdk.beacon.data.MyLocation
import com.cdk.bettermapsearch.clustering.MapPagerMarkerRenderer
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager


class MyLocationMarkerRenderer(context: Context, map: GoogleMap, clusterManager: ClusterManager<MyLocation>) : MapPagerMarkerRenderer<MyLocation>(context, map, clusterManager) {

    private val clusterText: TextView
    private val clusterItemText: TextView

    init {
        val clusterView = View.inflate(context, R.layout.cluster_view, null)
        clusterText = clusterView.findViewById(R.id.text_primary)
        clusterItemText = View.inflate(context, R.layout.cluster_item_view, null) as TextView

        clusterIconGenerator.setContentView(clusterView)
        clusterItemIconGenerator.setContentView(clusterItemText)
    }

    override fun setClusterItemViewBackground(isSelected: Boolean) {
        clusterItemIconGenerator.setColor(ContextCompat.getColor(context, if (isSelected) android.R.color.black else android.R.color.white))
    }

    override fun setClusterViewBackground(isSelected: Boolean) {
        clusterIconGenerator.setColor(ContextCompat.getColor(context, if (isSelected) android.R.color.black else android.R.color.white))
    }

    override fun setupClusterItemView(item: MyLocation?, isSelected: Boolean) {
        clusterItemText.text = item?.timeStamp.toString()
        clusterItemText.setTextColor(ContextCompat.getColor(context, if (isSelected) android.R.color.white else android.R.color.black))
    }

    override fun setupClusterView(cluster: Cluster<MyLocation>?, isSelected: Boolean) {
        clusterText.text = context.resources.getString(R.string.cluster_text, cluster?.items?.size)
        clusterText.setTextColor(ContextCompat.getColor(context, if (isSelected) android.R.color.white else android.R.color.black))
    }
}