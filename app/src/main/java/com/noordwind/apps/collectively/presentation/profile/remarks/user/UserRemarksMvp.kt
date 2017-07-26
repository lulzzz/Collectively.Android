package com.noordwind.apps.collectively.presentation.profile.remarks.user

import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter
import io.reactivex.disposables.Disposable

interface UserRemarksMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showRemarksLoading()
        fun showRemarksLoadingError()
        fun showRemarksLoadingNetworkError()
        fun showRemarksLoadingServerError(error: String)
        fun showEmptyScreen()
        fun showLoadedRemarks(comments: List<Remark>)
        fun showFilteredRemarks(remarks: List<Remark>)
    }

    interface Presenter : BasePresenter{
        fun loadUserRemarks()
        fun loadFavoriteRemarks()
        fun checkIfFiltersHasChanged()
    }
}
