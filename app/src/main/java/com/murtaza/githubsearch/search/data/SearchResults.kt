package com.murtaza.githubsearch.search.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchResults(
    @SerializedName("items")
    @Expose
    var items: List<SearchItem>
)