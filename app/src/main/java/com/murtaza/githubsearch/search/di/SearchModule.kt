package com.murtaza.githubsearch.search.di

import com.murtaza.githubsearch.search.data.SearchApiInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class SearchModule {

    @Provides
    @SearchScope
    fun providesSearchApiInterface(retrofit: Retrofit): SearchApiInterface {
        return retrofit.create(SearchApiInterface::class.java)
    }
}