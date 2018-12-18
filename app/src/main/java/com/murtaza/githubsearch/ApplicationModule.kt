package com.murtaza.githubsearch

import android.content.Context
import android.net.ConnectivityManager
import com.murtaza.githubsearch.api.GithubApiInterface
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApplicationModule(private val githubSearch: GithubSearch) {

    private val offlineInterceptor: Interceptor = Interceptor {
        var request = it.request()
        when (isNetworkAvailable()) {
            false -> {
                val maxStale = 60 * 60 * 24 // tolerate 1 day of stale if offline
                request = request.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .build()
            }
        }
        return@Interceptor it.proceed(request)
    }

    private val onlineInterceptor = Interceptor {
        val response = it.proceed(it.request())
        val maxAge = 60 // read from cache for 60 seconds even if online (chosen arbitrarily)
        response.newBuilder()
            .removeHeader("Pragma")
            .removeHeader("Cache-Control")
            .header("Cache-Control", "public, max-age=$maxAge")
            .build()
    }

    private fun getCache(): Cache {
        val cacheSize: Long = 10 * 1024 * 1024
        return Cache(githubSearch.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideRetrofitService(okHttpClient: OkHttpClient): GithubApiInterface {

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit.create(GithubApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(offlineInterceptor)
            .addNetworkInterceptor(onlineInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .cache(getCache())
            .build()
    }

    private fun isNetworkAvailable(): Any {
        val connectivityManager = githubSearch.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}