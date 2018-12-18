package com.murtaza.githubsearch

data class DataWrapper<T>(
    val data: T? = null,
    val error: String?
)
