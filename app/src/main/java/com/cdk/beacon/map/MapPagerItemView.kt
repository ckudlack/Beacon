package com.cdk.beacon.map

import android.content.Context
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.cdk.beacon.DateTimeUtils
import com.cdk.beacon.R
import kotlinx.android.synthetic.main.pager_item_view.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class MapPagerItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.pager_item_view, this)
    }

/*    @SuppressLint("SetTextI18n")
    @ModelProp
    fun setPosition(latLng: Position) {
        position.text = "${latLng.latitude}, ${latLng.longitude}"
    }*/

    @ModelProp
    fun setDate(timestamp: Long) {
        date.text = DateTimeUtils.format(context, timestamp)
    }

    @CallbackProp
    fun setClickListener(clickListener: OnClickListener?) {
        directions_button.setOnClickListener(clickListener)
    }

}