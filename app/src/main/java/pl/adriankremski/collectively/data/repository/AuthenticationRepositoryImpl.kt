package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.data.repository.SessionRepository
import pl.adriankremski.collectively.data.datasource.AuthDataSource
import pl.adriankremski.collectively.data.repository.util.OperationRepository
import pl.adriankremski.collectively.data.model.AuthRequest
import pl.adriankremski.collectively.data.model.ResetPasswordRequest
import pl.adriankremski.collectively.data.model.SignUpRequest

class AuthenticationRepositoryImpl(val authDataSource: AuthDataSource,
                                   val operationRepository: OperationRepository,
                                   val sessionRepository: SessionRepository) : AuthenticationRepository {

    override fun loginWithEmail(email: String, password: String): Observable<String> {
        val authRequest = AuthRequest(email, password, Constants.AuthProvider.COOLECTOR)

        return authDataSource.login(authRequest)
                .flatMap { authResponse -> Observable.just(authResponse.token) }
                .doOnNext { sessionRepository.sessionToken = it }
    }

    override fun signUp(username: String, email: String, password: String): Observable<String> {
        return operationRepository.pollOperation(authDataSource.signUp(SignUpRequest(username, email, password)))
                .flatMap {
                    val authRequest = AuthRequest(email, password, Constants.AuthProvider.COOLECTOR)
                    authDataSource.login(authRequest).flatMap { authResponse -> Observable.just(authResponse.token) }
                }
    }

    override fun resetPassword(email: String): Observable<Boolean> {
        return operationRepository.pollOperation(authDataSource.resetPassword(ResetPasswordRequest(email))).flatMap { Observable.just(true) }
    }
}

