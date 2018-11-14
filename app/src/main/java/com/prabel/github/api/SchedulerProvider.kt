package com.prabel.github.api

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun networkScheduler(): Scheduler
    fun uiScheduler(): Scheduler
    fun computationScheduler(): Scheduler
}