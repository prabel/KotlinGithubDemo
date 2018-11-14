package com.prabel.github.api.tools

import android.content.res.Resources
import com.prabel.github.R

object ErrorManager {
    fun getErrorMessage(error: DefaultError,
                        resources: Resources): String = when (error) {
        is NetworkError -> resources.getString(R.string.network_error)
        is NotFoundError -> "(404) ${resources.getString(R.string.connection_error)}"
        is UnknownServerError -> error.userMessage
        is UnknownClientError -> error.userMessage ?: error.info.toString()
        is UnauthorizedError -> error.userMessage ?: error.toString()
        else -> error.toString()
    }
}