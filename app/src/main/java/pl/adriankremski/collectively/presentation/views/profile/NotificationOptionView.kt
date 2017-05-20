package pl.adriankremski.collectively.presentation.views

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.notification_option_view.view.*
import pl.adriankremski.collectively.R

class NotificationOptionView(context: Context, option: String, description: String, isChecked: Boolean) : LinearLayout(context) {

    init {
        View.inflate(getContext(), R.layout.notification_option_view, this)
        optionNameLabel.text = option
        optionCheckbox.isChecked = isChecked
        descriptionLabel.text = description
    }

    fun optionName() : String = optionNameLabel.text.toString()

    fun isOptionChecked() : Boolean = optionCheckbox.isChecked
}

