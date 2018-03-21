package com.cdk.beacon

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cdk.beacon.ui.SharedUserFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.add_shared_user_item_view.view.*
import kotlinx.android.synthetic.main.shared_user_item_view.view.*

class SharedUserAdapter(usersList: List<String>?, private val callback: OnListFragmentInteractionListener?) : RecyclerView.Adapter<SharedUserAdapter.ViewHolder>() {

    private val sharedUsersList = mutableListOf<String>()

    init {
        usersList?.let { sharedUsersList.addAll(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder: ViewHolder
        val view = LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)

        if (viewType == R.layout.shared_user_item_view) {
            viewHolder = SharedUserViewHolder(view)
            viewHolder.itemView.remove_user.setOnClickListener {
                val itemPosition = viewHolder.adapterPosition
                callback?.onSharedUserRemoved(itemPosition)
            }
        } else {
            viewHolder = AddSharedUserViewHolder(view)
            viewHolder.itemView.add_shared_user_button.setOnClickListener {
                callback?.onSharedUserAdded(viewHolder.itemView.user_name_edittext.text.toString())
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is SharedUserViewHolder -> holder.bind(sharedUsersList[position])
            is AddSharedUserViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return (sharedUsersList.size) + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) R.layout.add_shared_user_item_view else R.layout.shared_user_item_view
    }

    private fun removeItem(position: Int) {
        sharedUsersList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateItems(users: List<String>?) {
        users?.let {
            sharedUsersList.clear()
            sharedUsersList.addAll(users)
            notifyDataSetChanged()
        }
    }

    class SharedUserViewHolder(itemView: View?) : ViewHolder(itemView) {
        fun bind(userEmail: String) {
            itemView.user_email.text = userEmail
        }
    }

    class AddSharedUserViewHolder(itemView: View?) : ViewHolder(itemView) {
        fun bind() {
            itemView.user_name_edittext.text.clear()
        }
    }

    abstract class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}

