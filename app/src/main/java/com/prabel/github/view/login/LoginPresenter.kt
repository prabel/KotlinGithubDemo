package com.prabel.github.view.login

import com.prabel.github.api.SchedulerProvider
import com.prabel.github.api.tools.mapRight
import com.prabel.github.dao.AuthoDao
import com.prabel.github.utils.TokenUtils
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject

class LoginPresenter @Inject constructor(schedulerProvider: SchedulerProvider,
                                         authDao: AuthoDao
) {

    val userParamsProcessor: PublishProcessor<UserParams> = PublishProcessor.create()

    val requestSignInObservable = userParamsProcessor
            .map { (username, password) -> TokenUtils.create(username, password) }
            .switchMap { token ->
                authDao.authorizeUser(token)
                        .mapRight { token }
            }
            .observeOn(schedulerProvider.uiScheduler())
            .replay()
            .refCount()

}

data class UserParams(val username: String,
                      val password: String)