package com.prabel.github.view.main

import com.jacekmarchwicki.universaladapter.BaseAdapterItem
import com.prabel.github.api.SchedulerProvider
import com.prabel.github.api.tools.behaviorRefCount
import com.prabel.github.api.tools.onlyLeft
import com.prabel.github.api.tools.onlyRight
import com.prabel.github.dao.GithubDao
import io.reactivex.Flowable
import rx.subjects.PublishSubject
import javax.inject.Inject

class RepositoriesPresenter @Inject constructor(githubDao: GithubDao,
                                                schedulerProvider: SchedulerProvider
) {
    private val openIssuesForRepository = PublishSubject.create<Any>()

    private val repositoriesObservable = githubDao.getUserRepositoriesObservable()
            .observeOn(schedulerProvider.uiScheduler())
            .behaviorRefCount()

    val itemsObservable: Flowable<List<BaseAdapterItem>> = repositoriesObservable
            .onlyRight()
            .map { repositories -> repositories.map { RepositoryAdapterItem(it, openIssuesForRepository) } }

    val errorObservable = repositoriesObservable
            .onlyLeft()
}
