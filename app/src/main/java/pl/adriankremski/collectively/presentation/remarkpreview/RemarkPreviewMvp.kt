package pl.adriankremski.collectively.presentation.remarkpreview

import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.data.model.RemarkPreview
import pl.adriankremski.collectively.presentation.mvp.BasePresenter

interface RemarkPreviewMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showRemarkLoading()
        fun showRemarkLoadingNetworkError()
        fun showRemarkLoadingError(message: String)
        fun showLoadedRemark(remark: RemarkPreview)
        fun showPositiveVotes(positiveVotesCount: Int)
        fun showNegativeVotes(negativeVotesCount: Int)
        fun showUserVotedPositively()
        fun showUserVotedNegatively()
    }

    interface Presenter : BasePresenter{
        fun loadRemark(id: String)
    }
}