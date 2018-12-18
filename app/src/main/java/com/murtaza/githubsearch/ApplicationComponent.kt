package com.murtaza.githubsearch

import com.murtaza.githubsearch.search.di.SearchComponent
import com.murtaza.githubsearch.search.di.SearchModule
import com.murtaza.githubsearch.user.di.UserComponent
import com.murtaza.githubsearch.user.di.UserModule
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [(ApplicationModule::class)]
)
@Singleton
interface ApplicationComponent {
    fun inject(githubSearch: GithubSearch)
    fun plus(searchModule: SearchModule): SearchComponent
    fun plus(userModule: UserModule): UserComponent
}