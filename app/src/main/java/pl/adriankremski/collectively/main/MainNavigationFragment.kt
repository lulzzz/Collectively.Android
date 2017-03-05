package pl.adriankremski.collectively.main

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main_navigation.*
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.profile.ProfileActivity
import pl.adriankremski.collectively.remarkpreview.RemarkActivity
import pl.adriankremski.collectively.statistics.StatisticsActivity

class MainNavigationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater?.inflate(R.layout.fragment_main_navigation, container, false)
        return layout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mProfileOptionView.setOnClickListener { openProfile() }
        mStatisticsOptionView.setOnClickListener { openStatistics() }
        mSettingsOptionView.setOnClickListener { openSettings() }
        mFanpageOptionView.setOnClickListener { openFanPage() }
        mShowRemark.setOnClickListener { openShowRemarkPage() }
    }

    fun openProfile() {
        ProfileActivity.start(context)
    }

    fun openStatistics() {
        StatisticsActivity.start(context)
    }

    fun openSettings() {
        Snackbar.make(activity.findViewById(android.R.id.content), "Settings", Snackbar.LENGTH_SHORT).show();
    }

    fun openShowRemarkPage() {
        RemarkActivity.start(context)
    }

    fun openFanPage() {
        Snackbar.make(activity.findViewById(android.R.id.content), "Fanpage", Snackbar.LENGTH_SHORT).show();
    }
}
