package com.murtaza.githubsearch.search.data

import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiInterface {

    @GET("/search/users")
    fun searchUsers(
        @Query("q") filter: String,
        @Query("sort") sort: String,
        @Query("order") order: String
    ): Flowable<SearchResults>

}