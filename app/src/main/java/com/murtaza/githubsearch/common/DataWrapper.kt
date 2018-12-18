package com.murtaza.githubsearch.common

data class DataWrapper<T>(
    val data: T? = null,
    val error: String?
)
