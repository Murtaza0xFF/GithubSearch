package com.murtaza.githubsearch.user.di

import com.murtaza.githubsearch.user.UserViewModel
import dagger.Subcomponent

@Subcomponent(
    modules = [(UserModule::class)]
)
@UserScope
interface UserComponent {
    fun inject(userViewModel: UserViewModel)
}