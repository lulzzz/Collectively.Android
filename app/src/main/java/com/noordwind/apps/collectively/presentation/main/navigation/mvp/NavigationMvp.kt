package com.noordwind.apps.collectively.presentation.main.navigation.mvp

import com.noordwind.apps.collectively.presentation.mvp.BasePresenter

interface NavigationMvp {
    interface View {
        fun showProfile(name: String, avatarUrl: String?)
        fun  showAddress(addressPretty: String)
    }

    interface Presenter : BasePresenter {
        fun loadProfile()
        fun refreshLocation()
    }
}

