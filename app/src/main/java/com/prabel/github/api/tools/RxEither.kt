package com.prabel.github.api.tools

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import org.funktionale.either.Either
import org.funktionale.either.eitherSequential
import org.funktionale.option.Option
import org.funktionale.option.toOption

fun <T> Flowable<T>.toEither(): Flowable<Either<DefaultError, T>> = map<Either<DefaultError, T>> { Either.right(it) }
        .onErrorResumeNext(io.reactivex.functions.Function {
            Flowable.just<Either<DefaultError, T>>(
                    Either.left(it.toDefaultError()) as Either<DefaultError, T>)
        })

fun <L, R, TL> Either<L, R>.observableMapLeftWithEither(function: (L) -> Flowable<Either<TL, R>>): Flowable<Either<TL, R>> =
        this.fold(function, { Flowable.just(Either.right(it)) })

fun <L, R, TR> Either<L, R>.observableMapRightWithEither(function: (R) -> Flowable<Either<L, TR>>): Flowable<Either<L, TR>> =
        this.fold({ Flowable.just(Either.left(it)) }, function)

fun <L, R, TL> Either<L, R>.observableMapLeft(function: (L) -> Flowable<TL>): Flowable<Either<TL, R>> =
        this.fold({ function(it).map { Either.left(it) } }, { Flowable.just(Either.right(it)) })

fun <L, R, TR> Either<L, R>.observableMapRight(function: (R) -> Flowable<TR>): Flowable<Either<L, TR>> =
        this.fold({ Flowable.just(Either.left(it)) }, { function(it).map { Either.right(it) } })

fun <L, R, TL> Either<L, R>.singleMapLeftWithEither(function: (L) -> Single<Either<TL, R>>): Single<Either<TL, R>> =
        this.fold(function, { Single.just(Either.right(it)) })

fun <L, R, TR> Either<L, R>.singleMapRightWithEither(function: (R) -> Single<Either<L, TR>>): Single<Either<L, TR>> =
        this.fold({ Single.just(Either.left(it)) }, function)

fun <L, R, TL> Either<L, R>.singleMapLeft(function: (L) -> Single<TL>): Single<Either<TL, R>> =
        this.fold({ function(it).map { Either.left(it) } }, { Single.just(Either.right(it)) })

fun <L, R, TR> Either<L, R>.singleMapRight(function: (R) -> Single<TR>): Single<Either<L, TR>> =
        this.fold({ Single.just(Either.left(it)) }, { function(it).map { Either.right(it) } })

// switchMap

fun <L, R, TL> Flowable<Either<L, R>>.switchMapRight(function: (R) -> Flowable<TL>): Flowable<Either<L, TL>> =
        this.switchMap { it.observableMapRight(function) }

fun <L, R, TR> Flowable<Either<L, R>>.switchMapRightWithEither(function: (R) -> Flowable<Either<L, TR>>): Flowable<Either<L, TR>> =
        this.switchMap { it.observableMapRightWithEither(function) }

// flatMap

fun <L, R, TL> Flowable<Either<L, R>>.flatMapRight(function: (R) -> Flowable<TL>): Flowable<Either<L, TL>> =
        this.flatMap { it.observableMapRight(function) }

fun <L, R, TL> Single<Either<L, R>>.flatMapLeftWithEither(function: (L) -> Single<Either<TL, R>>): Single<Either<TL, R>> =
        this.flatMap { it.singleMapLeftWithEither(function) }

fun <L, R, TR> Single<Either<L, R>>.flatMapRightWithEither(function: (R) -> Single<Either<L, TR>>): Single<Either<L, TR>> =
        this.flatMap { it.singleMapRightWithEither(function) }

fun <L, R, TL> Single<Either<L, R>>.flatMapLeft(function: (L) -> Single<TL>): Single<Either<TL, R>> =
        this.flatMap { it.singleMapLeft(function) }

fun <L, R, TR> Single<Either<L, R>>.flatMapRight(function: (R) -> Single<TR>): Single<Either<L, TR>> =
        this.flatMap { it.singleMapRight(function) }

// concatMap

fun <L, R, TL> Flowable<Either<L, R>>.concatMapRight(function: (R) -> Flowable<TL>): Flowable<Either<L, TL>> =
        this.concatMap { it.observableMapRight(function) }

// map

fun <L, R, TL, TR> Either<L, R>.map(left: (L) -> TL, right: (R) -> TR): Either<TL, TR> = when (this) {
    is Either.Left -> Either.left(left(this.l))
    is Either.Right -> Either.right(right(this.r))
}

fun <L, R, TL> Either<L, R>.mapLeft(function: (L) -> TL): Either<TL, R> = when (this) {
    is Either.Left -> Either.left(function(this.l))
    is Either.Right -> Either.right(this.r)
}

fun <L, R, TR> Either<L, R>.mapRight(function: (R) -> TR): Either<L, TR> = when (this) {
    is Either.Left -> Either.left(this.l)
    is Either.Right -> Either.right(function(this.r))
}

fun <L, R, TR> Flowable<Either<L, R>>.mapRight(function: (R) -> TR): Flowable<Either<L, TR>> =
        this.map { it.mapRight(function) }

fun <L, R, TL> Flowable<Either<L, R>>.mapLeft(function: (L) -> TL): Flowable<Either<TL, R>> =
        this.map { it.mapLeft(function) }

fun <L, R, TR> Single<Either<L, R>>.mapRight(function: (R) -> TR): Single<Either<L, TR>> =
        this.map { it.mapRight(function) }


fun <L, R> Single<Either<L, R>>.doOnSuccessRight(call: (R) -> Unit): Single<Either<L, R>> =
        this.doOnSuccess {
            when (it) {
                is Either.Right -> call(it.r)
            }
        }

fun <L, R> Single<Either<L, R>>.doOnSuccessLeft(call: (L) -> Unit): Single<Either<L, R>> =
        this.doOnSuccess {
            when (it) {
                is Either.Left -> call(it.l)
            }
        }

// map with Either


fun <L, R, TL> Either<L, R>.mapLeftWithEither(function: (L) -> Either<TL, R>): Either<TL, R> = when (this) {
    is Either.Left -> function(this.l)
    is Either.Right -> Either.right(this.r)
}

fun <L, R, TR> Either<L, R>.mapRightWithEither(function: (R) -> Either<L, TR>): Either<L, TR> = when (this) {
    is Either.Left -> Either.left(this.l)
    is Either.Right -> function(this.r)
}

fun <L, R, TR> Flowable<Either<L, R>>.mapRightWithEither(function: (R) -> Either<L, TR>): Flowable<Either<L, TR>> =
        this.map { it.mapRightWithEither(function) }

fun <L, R, TL> Flowable<Either<L, R>>.mapLeftWithEither(function: (L) -> Either<TL, R>): Flowable<Either<TL, R>> =
        this.map { it.mapLeftWithEither(function) }

fun <L, R, TR> Single<Either<L, R>>.mapRightWithEither(function: (R) -> Either<L, TR>): Single<Either<L, TR>> =
        this.map { it.mapRightWithEither(function) }

// filters

fun <L, R> Flowable<Either<L, R>>.onlyLeft(): Flowable<L> = this.concatMap { it.fold({ Flowable.just(it) }, { Flowable.empty() }) }

fun <L, R> Flowable<Either<L, R>>.onlyRight(): Flowable<R> = this.concatMap { it.fold({ Flowable.empty<R>() }, { Flowable.just(it) }) }

// map


fun <L, R> Flowable<Either<L, R>>.mapToLeftOr(value: L): Flowable<L> = this.map { it.fold({ it }, { value }) }

fun <L, R> Flowable<Either<L, R>>.mapToRightOr(value: R): Flowable<R> = this.map { it.fold({ value }, { it }) }

fun <L, R> Single<Either<L, R>>.mapToLeftOr(value: L): Single<L> = this.map { it.fold({ it }, { value }) }

fun <L, R> Single<Either<L, R>>.mapToRightOr(value: R): Single<R> = this.map { it.fold({ value }, { it }) }

// reduce

fun <L, R> Flowable<List<Either<L, R>>>.eitherSequential(): Flowable<Either<L, List<R>>> = this.map { it.eitherSequential() }

fun <L, R> Single<List<Either<L, R>>>.eitherSequential(): Single<Either<L, List<R>>> = this.map { it.eitherSequential() }


fun <L, R1, R2> foldEither(e1: Either<L, R1>, e2: Either<L, R2>): Either<L, Pair<R1, R2>> =
        e1.fold({ Either.left(it) }, { e1Right -> e2.fold({ Either.left(it) }, { e2Right -> Either.right(Pair(e1Right, e2Right)) }) })

fun <L, R1, R2, R3> foldEither(e1: Either<L, R1>, e2: Either<L, R2>, e3: Either<L, R3>): Either<L, Triple<R1, R2, R3>> =
        e1.fold({ Either.left(it) }, { e1Right -> e2.fold({ Either.left(it) }, { e2Right -> e3.fold({ Either.left(it) }, { e3Right -> Either.right(Triple(e1Right, e2Right, e3Right)) }) }) })

infix operator @JvmName("plus")
fun <L, R1, R2> Either<L, R1>.plus(e2: Either<L, R2>): Either<L, Pair<R1, R2>> =
        this.fold({ Either.left(it) }, { e1Right -> e2.fold({ Either.left(it) }, { e2Right -> Either.right(Pair(e1Right, e2Right)) }) })


fun <L, R, TL> Either<L, R>.observableMapLeftWithEither(function: (L) -> Observable<Either<TL, R>>): Observable<Either<TL, R>> =
        this.fold(function, { Observable.just(Either.right(it)) })

fun <L, R, TR> Either<L, R>.observableMapRightWithEither(function: (R) -> Observable<Either<L, TR>>): Observable<Either<L, TR>> =
        this.fold({ Observable.just(Either.left(it)) }, function)

fun <L, R, TL> Either<L, R>.observableMapLeft(function: (L) -> Observable<TL>): Observable<Either<TL, R>> =
        this.fold({ function(it).map { Either.left(it) } }, { Observable.just(Either.right(it)) })

fun <L, R, TR> Either<L, R>.observableMapRight(function: (R) -> Observable<TR>): Observable<Either<L, TR>> =
        this.fold({ Observable.just(Either.left(it)) }, { function(it).map { Either.right(it) } })

// switchMap

fun <L, R, TL> Observable<Either<L, R>>.switchMapRight(function: (R) -> Observable<TL>): Observable<Either<L, TL>> =
        this.switchMap { it.observableMapRight(function) }

fun <L, R, TR> Observable<Either<L, R>>.switchMapRightWithEither(function: (R) -> Observable<Either<L, TR>>): Observable<Either<L, TR>> =
        this.switchMap { it.observableMapRightWithEither(function) }

// flatMap

fun <L, R, TL> Observable<Either<L, R>>.flatMapRight(function: (R) -> Observable<TL>): Observable<Either<L, TL>> =
        this.flatMap { it.observableMapRight(function) }

// concatMap

fun <L, R, TL> Observable<Either<L, R>>.concatMapRight(function: (R) -> Observable<TL>): Observable<Either<L, TL>> =
        this.concatMap { it.observableMapRight(function) }

fun <L, R, TR> Observable<Either<L, R>>.mapRight(function: (R) -> TR): Observable<Either<L, TR>> =
        this.map { it.mapRight(function) }

fun <L, R, TR> Observable<Either<L, R>>.mapRightWithEither(function: (R) -> Either<L, TR>): Observable<Either<L, TR>> =
        this.map { it.mapRightWithEither(function) }

// filters

fun <L, R> Observable<Either<L, R>>.onlyLeft(): Observable<L> = this.concatMap { it.fold({ Observable.just(it) }, { Observable.empty() }) }

fun <L, R> Observable<Either<L, R>>.onlyRight(): Observable<R> = this.concatMap { it.fold({ Observable.empty<R>() }, { Observable.just(it) }) }

// map


fun <L, R> Observable<Either<L, R>>.mapToLeftOr(value: L): Observable<L> = this.map { it.fold({ it }, { value }) }

fun <L, R> Observable<Either<L, R>>.mapToRightOr(value: R): Observable<R> = this.map { it.fold({ value }, { it }) }

// reduce

fun <L, R> Observable<List<Either<L, R>>>.eitherSequential(): Observable<Either<L, List<R>>> = this.map { it.eitherSequential() }

fun <T> Flowable<Either<DefaultError, T>>.optionalErrorFlowable(): Flowable<Option<DefaultError>> = map { it.fold({ it.toOption() }, { Option.None }) }