package com.prabel.github.api

import com.prabel.github.api.model.Repository
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {

    @GET("/authorizations")
    fun authorizeUser(@Header("Authorization") token: String): Flowable<ResponseBody>

    @GET("/user/repos?affiliation=owner")
    fun getUserRepositories(): Flowable<List<Repository>>
}
