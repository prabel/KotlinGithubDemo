package com.prabel.github.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.prabel.github.R
import com.prabel.github.TokenPreferences
import com.prabel.github.api.tools.ErrorManager
import com.prabel.github.api.tools.onlyLeft
import com.prabel.github.api.tools.onlyRight
import com.prabel.github.utils.showErrorSnackBar
import com.prabel.github.utils.stringText
import com.prabel.github.view.BaseActivity
import com.prabel.github.view.main.RepositoriesActivity
import kotlinx.android.synthetic.main.login_activity.*
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    companion object {
        fun newInstance(context: Context) = Intent(context, LoginActivity::class.java)
    }

    @Inject
    lateinit var presenter: LoginPresenter
    @Inject
    lateinit var tokenPreferences: TokenPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        subscription.addAll(
                presenter.requestSignInObservable
                        .onlyRight()
                        .subscribe { token ->
                            tokenPreferences.edit().setToken(token)
                            startActivity(RepositoriesActivity.newIntent(this))
                            finish()
                        },
                presenter.requestSignInObservable
                        .onlyLeft()
                        .map { ErrorManager.getErrorMessage(it, resources) }
                        .subscribe {
                            showErrorSnackBar(it, container)
                        }
        )

        login_button.setOnClickListener {
            val username = username_edit_text.stringText()
            if (username.isEmpty()) {
                username_text_input.error = getString(R.string.empty_field_error)
                return@setOnClickListener
            }
            val password = password_edit_text.stringText()
            if (password.isEmpty()) {
                password_text_input.error = getString(R.string.empty_field_error)
                return@setOnClickListener
            }

            presenter.userParamsProcessor.offer(UserParams(username, password))
        }
    }
}