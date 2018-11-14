package com.prabel.github.dagger.module

import android.content.Context
import android.os.Process
import com.prabel.github.App
import com.prabel.github.TokenPreferences
import com.prabel.github.api.SchedulerProvider
import com.prabel.github.dagger.annotations.DaggerAnnotation
import com.prabel.github.view.BaseActivity
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Singleton

@Module(subcomponents = [(BaseActivity.BaseActivitySubComponent::class)])
class AppModule {

    @Provides
    @Singleton
    @DaggerAnnotation.ForApplication
    fun provideContext(application: App): Context = application.applicationContext

    @Provides
    @Singleton
    fun userPreferences(@DaggerAnnotation.ForApplication context: Context): TokenPreferences = TokenPreferences(context)
    @Module
    companion object {
        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
        private val CORE_POOL_SIZE = CPU_COUNT * 2 + 1
        private val MAXIMUM_POOL_SIZE = CPU_COUNT * 4 + 1
        private const val KEEP_ALIVE_SECONDS = 1

        @JvmStatic
        @Provides
        @Singleton
        fun provideSchedulerProvider(): SchedulerProvider {
            return object : SchedulerProvider {
                override fun computationScheduler(): Scheduler {
                    val workQueue = LinkedBlockingDeque<Runnable>()
                    return Schedulers.from(ThreadPoolExecutor(
                            CORE_POOL_SIZE,
                            MAXIMUM_POOL_SIZE,
                            KEEP_ALIVE_SECONDS.toLong(),
                            TimeUnit.SECONDS,
                            workQueue,
                            NamedThreadFactory("computation-github", Process.THREAD_PRIORITY_DEFAULT)))
                }

                override fun networkScheduler() = Schedulers.io()
                override fun uiScheduler() = AndroidSchedulers.mainThread()
            }
        }

        private class NamedThreadFactory constructor(private val name: String, private val threadPriority: Int) : ThreadFactory {
            private val threadNo = AtomicInteger(0)

            override fun newThread(runnable: Runnable): Thread {
                val threadName = name + ":" + threadNo.incrementAndGet()
                return Thread({
                    android.os.Process.setThreadPriority(threadPriority)
                    runnable.run()
                }, threadName)
            }
        }
    }
}
