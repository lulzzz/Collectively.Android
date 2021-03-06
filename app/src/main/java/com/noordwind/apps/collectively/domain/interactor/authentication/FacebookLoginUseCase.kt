package com.noordwind.apps.collectively.domain.interactor.authentication

import com.noordwind.apps.collectively.data.model.Profile
import com.noordwind.apps.collectively.data.repository.AuthenticationRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable

class FacebookLoginUseCase(val authenticationRepository: AuthenticationRepository,
                   useCaseThread: UseCaseThread,
                   postExecutionThread: PostExecutionThread) : UseCase<Pair<Profile, String>, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: String?): Observable<Pair<Profile, String>> = authenticationRepository.loginWithFacebookToken(params!!)
}

