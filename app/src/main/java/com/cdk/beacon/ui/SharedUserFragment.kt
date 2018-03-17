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

    private var sharedUsersList: List<String>? = null
    private var mListener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedUsersList = arguments?.getStringArrayList(SHARED_USERS_LIST)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shared_users, container, false)

        // Set the adapter
        view.shared_users_list.layoutManager = LinearLayoutManager(context)
        view.shared_users_list.adapter = SharedUserAdapter(sharedUsersList?.toMutableList(), mListener)

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

    interface OnListFragmentInteractionListener {
        fun onSharedUserRemoved(itemPosition: Int)
    }

    companion object {

        private const val SHARED_USERS_LIST = "shared_users_list"

        fun newInstance(sharedUsers: List<String>): SharedUserFragment {
            val fragment = SharedUserFragment()
            val args = Bundle()
            args.putStringArrayList(SHARED_USERS_LIST, sharedUsers as ArrayList<String>?)
            fragment.arguments = args
            return fragment
        }
    }
}
