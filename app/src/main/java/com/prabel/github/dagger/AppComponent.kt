package com.prabel.github.dagger

import com.prabel.github.App
import com.prabel.github.dagger.module.ActivityBinderModule
import com.prabel.github.dagger.module.AndroidModule
import com.prabel.github.dagger.module.AppModule
import com.prabel.github.dagger.module.NetworkModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidSupportInjectionModule::class), (ActivityBinderModule::class), (NetworkModule::class), (AppModule::class), (AndroidModule::class)])
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}
