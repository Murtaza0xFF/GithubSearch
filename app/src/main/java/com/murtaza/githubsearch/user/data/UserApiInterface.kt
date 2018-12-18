package com.murtaza.githubsearch.user.data

import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiInterface {

    @GET("/users/{user}")
    fun getUser(
        @Path("user") userName: String
    ): Flowable<User>

}