package com.prabel.github.view.main

import com.prabel.github.api.model.Repository
import com.prabel.github.utils.KotlinBaseAdapterItem
import rx.Observer

data class RepositoryAdapterItem(val repository: Repository,
                                 val itemClickObserver: Observer<Any>) : KotlinBaseAdapterItem<Long> {
    override fun itemId(): Long = repository.id
}
