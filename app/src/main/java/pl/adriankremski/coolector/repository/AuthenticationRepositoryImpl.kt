package pl.adriankremski.coolector.repository

import android.content.Context
import io.reactivex.Observable
import pl.adriankremski.coolector.Constants
import pl.adriankremski.coolector.TheApp
import pl.adriankremski.coolector.model.AuthRequest
import pl.adriankremski.coolector.model.ResetPasswordRequest
import pl.adriankremski.coolector.model.SignUpRequest
import pl.adriankremski.coolector.network.Api
import javax.inject.Inject

class AuthenticationRepositoryImpl(context: Context) : AuthenticationRepository {
    @Inject
    lateinit var api: Api

    @Inject
    lateinit var operationRepository: OperationRepository

    init {
        TheApp[context].appComponent?.inject(this)
    }

    override fun loginWithEmail(email: String, password: String): Observable<String> {
        val authRequest = AuthRequest(email, password, Constants.AuthProvider.COOLECTOR)
        return api.login(authRequest).flatMap { authResponse -> Observable.just(authResponse.token) }
    }

    override fun signUp(username: String, email: String, password: String): Observable<String> {
        return operationRepository.pollOperation(api.signUp(SignUpRequest(username, email, password)))
                .flatMap {
                    val authRequest = AuthRequest(email, password, Constants.AuthProvider.COOLECTOR)
                    api.login(authRequest).flatMap { authResponse -> Observable.just(authResponse.token) }
                }
    }

    override fun resetPassword(email: String): Observable<Boolean> {
        return operationRepository.pollOperation(api.resetPassword(ResetPasswordRequest(email))).flatMap { Observable.just(true) }
    }
}

