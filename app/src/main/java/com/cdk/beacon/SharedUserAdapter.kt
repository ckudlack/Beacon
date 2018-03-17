package com.cdk.beacon

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cdk.beacon.ui.SharedUserFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.shared_user_item_view.view.*

class SharedUserAdapter(private val sharedUsersList: MutableList<String>?, private val callback: OnListFragmentInteractionListener?) : RecyclerView.Adapter<SharedUserAdapter.SharedUserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedUserViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.shared_user_item_view, parent, false)
        val sharedUserViewHolder = SharedUserViewHolder(view)
        sharedUserViewHolder.itemView.remove_user.setOnClickListener {
            callback?.onSharedUserRemoved(sharedUserViewHolder.adapterPosition)
        }
        return sharedUserViewHolder
    }

    override fun onBindViewHolder(holder: SharedUserViewHolder, position: Int) {
        sharedUsersList?.let { holder.bind(sharedUsersList[position]) }
    }

    override fun getItemCount(): Int {
        return sharedUsersList?.size ?: 0
    }

    fun update(usersList: List<String>?) {
        sharedUsersList?.clear()
        usersList?.let { sharedUsersList?.addAll(it) }
        notifyDataSetChanged()
    }

    class SharedUserViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(userEmail: String) {
            itemView.user_email.text = userEmail
        }
    }
}

