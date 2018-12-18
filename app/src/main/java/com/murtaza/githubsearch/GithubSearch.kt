package com.murtaza.githubsearch

import android.app.Application
import com.facebook.stetho.Stetho
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import javax.inject.Inject

class GithubSearch : Application() {
    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }

    @Inject
    lateinit var okHttpClient: OkHttpClient

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
        val picasso = Picasso
            .Builder(this)
            .downloader(OkHttp3Downloader(okHttpClient))
            .build()
        picasso.setIndicatorsEnabled(true)
        picasso.isLoggingEnabled = true
        Picasso.setSingletonInstance(picasso)
        Stetho.initializeWithDefaults(this)
    }

}