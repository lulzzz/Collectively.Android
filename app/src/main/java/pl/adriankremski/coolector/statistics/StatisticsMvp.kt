package pl.adriankremski.coolector.statistics

import io.reactivex.disposables.Disposable
import pl.adriankremski.coolector.model.Statistics

interface StatisticsMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showLoading()
        fun showLoadStatisticsError(message: String?)
        fun showStatistics(statistics: Statistics)
        fun showLoadStatisticsNetworkError()
    }

    interface Presenter {
        fun loadStatistics()
    }
}
