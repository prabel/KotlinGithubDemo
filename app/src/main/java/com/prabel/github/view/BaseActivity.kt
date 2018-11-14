package com.prabel.github.view

import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : DaggerAppCompatActivity() {

    @Subcomponent
    interface BaseActivitySubComponent : AndroidInjector<BaseActivity> {
        @Subcomponent.Builder
        abstract class Builder : AndroidInjector.Builder<BaseActivity>()
    }

    protected val subscription: CompositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        subscription.clear()
    }
}