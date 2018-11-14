package com.prabel.github.api.tools

import java.io.IOException

interface DefaultError

object NetworkError : DefaultError
/**
 * This error we will get when user dont have permisson
 */
object ForbiddenServerError : DefaultError
object NoCreditError : DefaultError
object NotFoundError : DefaultError
object EmptyBodyError: DefaultError, IOException()
data class UnauthorizedError(val userMessage: String?, val info: Any?) : DefaultError
data class UnknownClientError(val userMessage: String?, val info: Any?) : DefaultError
data class UnknownServerError(val userMessage: String, val info: Any?) : DefaultError