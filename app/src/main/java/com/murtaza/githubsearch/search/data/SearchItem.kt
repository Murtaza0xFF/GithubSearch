package com.murtaza.githubsearch.search.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchItem(
    @SerializedName("avatar_url")
    @Expose
    val avatarUrl: String,

    @SerializedName("login")
    @Expose
    val login: String,

    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("score")
    @Expose
    val score: Double
)