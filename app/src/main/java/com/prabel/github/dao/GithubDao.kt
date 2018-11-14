package com.prabel.github.dao

import com.prabel.github.api.ApiService
import com.prabel.github.api.SchedulerProvider
import com.prabel.github.api.model.Repository
import com.prabel.github.api.tools.DefaultError
import com.prabel.github.api.tools.mapToEitherWithError
import io.reactivex.Flowable
import org.funktionale.either.Either
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubDao @Inject constructor(private val apiService: ApiService,
                                    private val schedulerProvider: SchedulerProvider) {

    fun getUserRepositoriesObservable(): Flowable<Either<DefaultError, List<Repository>>> {
        return apiService.getUserRepositories()
                .subscribeOn(schedulerProvider.networkScheduler())
                .mapToEitherWithError()
    }
}
