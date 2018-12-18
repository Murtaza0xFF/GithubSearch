package com.murtaza.githubsearch.user.di

import com.murtaza.githubsearch.user.data.UserApiInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class UserModule {

    @Provides
    @UserScope
    fun providesUserApiInterface(retrofit: Retrofit): UserApiInterface{
        return retrofit.create(UserApiInterface::class.java)
    }
}