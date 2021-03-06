package com.noordwind.apps.collectively.presentation.views.dialogs.remarkfilters

import com.noordwind.apps.collectively.data.model.UserGroup
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter


interface FiltersMvp {
    interface View {
        fun showRemarkCategoryFilters(selectedFilters: List<String>, allFilters: List<String>)
        fun showRemarkStatusFilters(selectedFilters: List<String>, allFilters: List<String>)
        fun showUserGroups(allGroups: List<UserGroup>, selectedGroup: String)
    }

    interface Presenter : BasePresenter{
        fun loadFilters()
        fun toggleRemarkCategoryFilter(filter: String, selected: Boolean)
        fun toggleRemarkStatusFilter(filter: String, selected: Boolean)
        fun selectGroup(selectedGroup: String)
    }
}
