package com.cdk.beacon.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cdk.beacon.R
import com.cdk.beacon.SharedUserAdapter
import kotlinx.android.synthetic.main.fragment_shared_users.view.*
import java.util.*

class SharedUserFragment : Fragment() {

    private var mListener: OnListFragmentInteractionListener? = null
    private var adapter: SharedUserAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shared_users, container, false)

        adapter = SharedUserAdapter(arguments?.getStringArrayList(SHARED_USERS_LIST)?.toMutableList(), mListener)

        // Set the adapter
        view.shared_users_list.layoutManager = LinearLayoutManager(context)
        view.shared_users_list.adapter = adapter

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context?.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    fun updateSharedUsers(users: List<String>?) {
        adapter?.updateItems(users)
    }

    interface OnListFragmentInteractionListener {
        fun onSharedUserRemoved(itemPosition: Int)
        fun onSharedUserAdded(email: String)
    }

    companion object {

        private const val SHARED_USERS_LIST = "shared_users_list"

        fun newInstance(sharedUsers: List<String>?): SharedUserFragment {
            val fragment = SharedUserFragment()
            val args = Bundle()
            args.putStringArrayList(SHARED_USERS_LIST, if(sharedUsers?.isEmpty() != false) arrayListOf<String>() else sharedUsers as ArrayList<String>?)
            fragment.arguments = args
            return fragment
        }
    }
}
