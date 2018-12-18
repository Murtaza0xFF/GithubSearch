package com.murtaza.githubsearch.api

import com.murtaza.githubsearch.search.data.SearchResults
import com.murtaza.githubsearch.user.data.User
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiInterface {

    @GET("/search/users")
    fun searchUsers(
        @Query("q") filter: String,
        @Query("sort") sort: String,
        @Query("order") order: String
    ): Flowable<SearchResults>

    @GET("/users/{user}")
    fun getUser(
        @Path("user") userName: String
    ): Flowable<User>
}