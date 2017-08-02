package com.noordwind.apps.collectively.presentation.authentication.retrievepassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.view_login_progress.*
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.repository.AuthenticationRepository
import com.noordwind.apps.collectively.data.repository.util.ConnectivityRepository
import com.noordwind.apps.collectively.domain.interactor.authentication.RetrievePasswordUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.extension.setGone
import com.noordwind.apps.collectively.presentation.extension.setVisible
import com.noordwind.apps.collectively.presentation.extension.showResetPasswordErrorDialog
import javax.inject.Inject

class ResetPasswordActivity : AppCompatActivity(), ResetPasswordMvp.View {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ResetPasswordActivity::class.java)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var authenticationRepository: AuthenticationRepository

    @Inject
    lateinit var connectivityRepository: ConnectivityRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    lateinit var presenter : ResetPasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_reset_password)
        titleLabel.text = getString(R.string.retrieve_password_screen_title)

        presenter = ResetPasswordPresenter(this,
                RetrievePasswordUseCase(authenticationRepository, ioThread, uiThread),
                connectivityRepository)

        mResetPasswordButton.setOnClickListener { presenter.resetPassword(emailInput.text.toString()) }
    }

    override fun showInvalidEmailError() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_invalid_email), Snackbar.LENGTH_LONG).show();

    override fun showLoading() {
        progressView.setVisible()
    }

    override fun hideLoading() {
        progressView.setGone()
    }

    override fun showNetworkError() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_no_network), Snackbar.LENGTH_LONG).show()

    override fun showResetPasswordSuccess() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.password_reset_success), Snackbar.LENGTH_LONG).show()

    override fun showResetPasswordServerError(message: String?) {
        showResetPasswordErrorDialog(message!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}