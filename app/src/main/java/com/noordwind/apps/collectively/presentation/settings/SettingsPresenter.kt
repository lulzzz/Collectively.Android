package com.noordwind.apps.collectively.presentation.settings

import com.facebook.AccessToken
import com.noordwind.apps.collectively.data.cache.ProfileCache
import com.noordwind.apps.collectively.data.datasource.Session
import com.noordwind.apps.collectively.domain.interactor.authentication.DeleteAccountUseCase
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver

class SettingsPresenter(val view: SettingsMvp.View,
                        val session: Session,
                        val profileCache: ProfileCache,
                        val deleteAccountUseCase: DeleteAccountUseCase) : SettingsMvp.Presenter {

    override fun logout() {
        session.clear()
        profileCache.clear()
        AccessToken.setCurrentAccessToken(null)
        view.showLogoutSuccess()
    }

    override fun removeAccount() {
        var observer = object : AppDisposableObserver<Boolean>() {

            override fun onStart() {
                super.onStart()
                view.showRemoveAccountProgress()
            }

            override fun onNext(result: Boolean) {
                super.onNext(result)
                view.hideRemoveAccountProgress()
                view.showRemoveAccountSuccess()
                logout()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.hideRemoveAccountProgress()
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                view.showRemoveAccountError(message)
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showRemoveAccountNetworkError()
            }
        }

        deleteAccountUseCase.execute(observer)
    }

    override fun destroy() {
        deleteAccountUseCase.dispose()
    }
}
