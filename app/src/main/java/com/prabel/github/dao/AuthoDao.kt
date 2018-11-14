package com.prabel.github.dao

import com.prabel.github.api.ApiService
import com.prabel.github.api.SchedulerProvider
import com.prabel.github.api.tools.DefaultError
import com.prabel.github.api.tools.mapToEitherWithError
import io.reactivex.Flowable
import okhttp3.ResponseBody
import org.funktionale.either.Either
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthoDao @Inject constructor(
        private val apiService: ApiService,
        private val schedulerProvider: SchedulerProvider
) {
    fun authorizeUser(token: String): Flowable<Either<DefaultError, ResponseBody>> {
        return apiService.authorizeUser(token)
                .subscribeOn(schedulerProvider.networkScheduler())
                .mapToEitherWithError()
    }
}