package com.murtaza.githubsearch.search.di

import com.murtaza.githubsearch.search.SearchViewModel
import dagger.Subcomponent

@Subcomponent(
    modules = [(SearchModule::class)]
)
@SearchScope
interface SearchComponent{
    fun inject(searchModule: SearchViewModel)
}