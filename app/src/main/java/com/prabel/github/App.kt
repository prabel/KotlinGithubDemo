package com.prabel.github

import android.content.Context
import android.support.multidex.MultiDex
import android.util.Log
import com.prabel.github.dagger.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import rx.plugins.RxJavaHooks

class App : DaggerApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        rxThrowableCatcher()
    }

    private fun rxThrowableCatcher() {
        RxJavaHooks.setOnError { throwable -> Log.e("RxError", " Message: ", throwable) }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent
            .builder()
            .create(this)
}