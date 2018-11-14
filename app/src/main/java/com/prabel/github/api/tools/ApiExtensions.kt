package com.prabel.github.api.tools

import com.google.gson.Gson
import io.reactivex.Flowable
import io.reactivex.Observable
import org.funktionale.either.Either
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toDefaultError(): DefaultError = when (this) {
    is HttpException -> mapErrorCodes(this, code())
    is IOException -> if (this is EmptyBodyError) EmptyBodyError else NetworkError
    else -> UnknownClientError("Fatal error ( " + this.javaClass.name + " )", this)
}

fun <T> Flowable<T>.mapToEitherWithError(): Flowable<Either<DefaultError, T>> =
        map<Either<DefaultError, T>> { Either.right(it) }
                .onErrorReturn { throwable: Throwable ->
                    Either.left(throwable.toDefaultError())
                }

fun <T> Flowable<T>.behaviorRefCount(): Flowable<T> = replay(1).refCount()

fun <T> Observable<T>.behaviorRefCount(): Observable<T> = replay(1).refCount()

private fun mapErrorCodes(throwable: Throwable, errorCode: Int): DefaultError {
    return when (errorCode) {
        401 -> UnauthorizedError(getMessage(throwable, errorCode), throwable)
        402 -> NoCreditError
        403 -> ForbiddenServerError
        404 -> NotFoundError
        in 500..599 -> UnknownServerError("Server error with code: $errorCode", throwable)
        in 400..499 -> UnknownClientError(getMessage(throwable, errorCode), throwable)
        else -> UnknownClientError("Unknown code ($errorCode)", throwable)
    }
}

fun getMessage(throwable: Throwable?, errorCode: Int): String {
    return if (throwable is HttpException) {
        try {
            val errorBody = throwable.response().errorBody()
            Gson().fromJson(errorBody!!.string(), ErrorMessage::class.java).message
        } catch (e: Exception) {
            "Client error with code: $errorCode"
        }
    } else {
        "Client error with code: $errorCode"
    }
}

data class ErrorMessage(
        val code: Int,
        val message: String
)
