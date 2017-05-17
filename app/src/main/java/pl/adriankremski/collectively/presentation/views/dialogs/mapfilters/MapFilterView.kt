package pl.adriankremski.collectively.presentation.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.remark_map_filter.view.*
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.presentation.extension.iconOfCategory
import pl.adriankremski.collectively.presentation.rxjava.RxBus


class MapFilterView(context: Context, filter: String, isChecked: Boolean) : LinearLayout(context) {

    init {
        View.inflate(getContext(), R.layout.remark_map_filter, this)
        filterIconLabel.setImageDrawable(ContextCompat.getDrawable(getContext(), filter.iconOfCategory()!!))
        filterLabel.text = filter

        mapFilterCheckbox.isChecked = isChecked
        mapFilterCheckbox.setOnCheckedChangeListener { compoundButton, isSelected -> RxBus.instance.postEvent(MapFilterSelectionChangedEvent(isSelected, filter))}
    }

    class MapFilterSelectionChangedEvent(val selected: Boolean, val filter: String)
}

