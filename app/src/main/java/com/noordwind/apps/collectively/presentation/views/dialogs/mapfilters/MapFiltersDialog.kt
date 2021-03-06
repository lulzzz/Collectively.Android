package com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.CheckBox
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.model.UserGroup
import com.noordwind.apps.collectively.presentation.extension.uppercaseFirstLetter
import com.noordwind.apps.collectively.presentation.rxjava.RxBus
import com.noordwind.apps.collectively.presentation.settings.dagger.MapFiltersDialogModule
import com.noordwind.apps.collectively.presentation.views.FilterView
import com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.mvp.MapFiltersMvp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_add_remark.*
import java.util.*
import javax.inject.Inject

class MapFiltersDialog : DialogFragment(), Constants, MapFiltersMvp.View {

    companion object {
        fun newInstance(): MapFiltersDialog = MapFiltersDialog()
    }

    @Inject
    lateinit var presenter: MapFiltersMvp.Presenter

    private lateinit var categoriesLayout: ViewGroup
    private lateinit var statesLayout: ViewGroup

    private lateinit var showOnlyMyRemarksCheckButton: CheckBox
    private lateinit var mMapFilterSelectedEventDisposable: Disposable
    private lateinit var dismissListener: DialogInterface.OnDismissListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog)
        TheApp[context].appComponent!!.plusMapFiltersDialogComponent(MapFiltersDialogModule(this)).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_filter_remarks_dialog, container, false)
        categoriesLayout = rootView.findViewById(R.id.categoriesLayout) as ViewGroup
        statesLayout = rootView.findViewById(R.id.statesLayout) as ViewGroup

        rootView.findViewById(R.id.background).setOnClickListener { dismiss() }

        showOnlyMyRemarksCheckButton = rootView.findViewById((R.id.showOnlyMineCheckButton)) as CheckBox

        showOnlyMyRemarksCheckButton.setOnCheckedChangeListener { button, isChecked -> presenter.toggleShouldShowOnlyMyRemarksFilter(isChecked) }

        presenter.loadFilters()
        return rootView
    }

    override fun showCategoryFilters(selectedCategoryFilters: List<String>, allCategoryFilters: List<String>) {
        categoriesLayout.removeAllViews()
        allCategoryFilters.forEach {
            var filterSelected = selectedCategoryFilters.contains(it)

            var filterView = FilterView(context = context,
                    filter = it,
                    isChecked = filterSelected,
                    showIcon = true,
                    type = "category")

            categoriesLayout.addView(filterView, categoriesLayout.childCount)
        }
    }

    override fun showStatusFilters(selectedStatusFilters: List<String>, allStatusFilters: List<String>) {
        statesLayout.removeAllViews()
        allStatusFilters.forEach {
            var filterSelected = selectedStatusFilters.contains(it)

            var filterView = FilterView(context = context,
                    filter = it,
                    isChecked = filterSelected,
                    showIcon = false,
                    type = "state")

            statesLayout.addView(filterView, statesLayout.childCount)
        }
    }

    override fun showUserGroups(allGroups: List<UserGroup>, selectedGroup: String) {
        val groupNames = LinkedList<String>()
        var initialSelection = 0

        allGroups.forEachIndexed { i, group ->
            groupNames.add(group.name.uppercaseFirstLetter())
        }
        groupNames.add(getString(R.string.add_remark_all_groups_target))

        groupNames.forEachIndexed { i, group ->
            if (group.equals(selectedGroup, ignoreCase = true)) {
                initialSelection = i
            }
        }

        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, groupNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        groupsSpinner.adapter = adapter
        groupsSpinner.setSelection(initialSelection)
        groupsSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, selectedItemView: View?, position: Int, p3: Long) {
                presenter.selectGroup(groupsSpinner.selectedItem.toString())
            }
        }
    }

    override fun selectShowOnlyMineRemarksFilter(showOnlyMine: Boolean) {
        showOnlyMyRemarksCheckButton.isChecked = showOnlyMine
    }

    fun setOnDismissListener(dismissListener: DialogInterface.OnDismissListener) {
        this.dismissListener = dismissListener
    }

    override fun onStart() {
        super.onStart()
        val d = dialog
        dialog.setOnDismissListener(dismissListener)
        dialog.setCanceledOnTouchOutside(true)
        if (d != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            d.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            d.window!!.setLayout(width, height)
        }

        mMapFilterSelectedEventDisposable = RxBus.instance
                .getEvents(FilterView.FilterSelectionChangedEvent::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onFilterSelectionChanged(it) })
    }

    fun onFilterSelectionChanged(event: FilterView.FilterSelectionChangedEvent) {
        if (event.type!!.equals("category")) {
            presenter.toggleCategoryFilter(event.filter, event.selected)
        } else {
            presenter.toggleStatusFilter(event.filter, event.selected)
        }
    }

    override fun onStop() {
        super.onStop()

        if (mMapFilterSelectedEventDisposable != null && !mMapFilterSelectedEventDisposable.isDisposed) {
            mMapFilterSelectedEventDisposable.dispose()
        }
    }
}
