package com.prabel.github.dagger.module

import com.prabel.github.view.login.LoginActivity
import com.prabel.github.view.main.RepositoriesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBinderModule {

    @ContributesAndroidInjector
    abstract fun provideLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun provideMainActivity(): RepositoriesActivity

}
