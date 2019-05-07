package com.cdk.beacon.ui

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cdk.beacon.R
import kotlinx.android.synthetic.main.fragment_filter_list_dialog.*

class FilterListDialogFragment : BottomSheetDialogFragment() {
    private var mListener: Listener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        all_trips.setOnClickListener { mListener?.onFilterItemClicked(0) }
        my_trips.setOnClickListener { mListener?.onFilterItemClicked(1) }
        shared_trips.setOnClickListener { mListener?.onFilterItemClicked(2) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as Listener
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    interface Listener {
        fun onFilterItemClicked(position: Int)
    }

    companion object {
        fun newInstance(): FilterListDialogFragment = FilterListDialogFragment()
    }
}
