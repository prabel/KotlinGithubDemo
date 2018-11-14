package com.prabel.github.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.jacekmarchwicki.universaladapter.ViewHolderManager
import com.jacekmarchwicki.universaladapter.rx.RxUniversalAdapter
import com.prabel.github.R
import com.prabel.github.TokenPreferences
import com.prabel.github.api.tools.ErrorManager
import com.prabel.github.base.BaseViewHolderManager
import com.prabel.github.utils.showErrorSnackBar
import com.prabel.github.view.BaseActivity
import com.prabel.github.view.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class RepositoriesActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context) = Intent(context, RepositoriesActivity::class.java)
    }

    @Inject
    lateinit var tokenPreferences: TokenPreferences
    @Inject
    lateinit var presenter: RepositoriesPresenter

    private val adapter = RxUniversalAdapter(listOf<ViewHolderManager>(
            BaseViewHolderManager(R.layout.item_repository, ::RepositoryViewHolder, RepositoryAdapterItem::class.java)
    ))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (tokenPreferences.getToken() == null) {
            finishAffinity()
            startActivity(LoginActivity.newInstance(this))
            return
        }

        setUpRecyclerView()

        subscription.addAll(
                presenter.itemsObservable
                        .subscribe { adapter.call(it) },
                presenter.errorObservable
                        .map { ErrorManager.getErrorMessage(it, resources) }
                        .subscribe {
                            showErrorSnackBar(it)
                        }
        )
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}
