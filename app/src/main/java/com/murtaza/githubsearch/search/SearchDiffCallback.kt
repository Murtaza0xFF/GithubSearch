package com.murtaza.githubsearch.search

import androidx.recyclerview.widget.DiffUtil
import com.murtaza.githubsearch.search.data.SearchItem


class SearchDiffCallback(
    private val oldSearchItemList: List<SearchItem>,
    private val newSearchItemList: List<SearchItem>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldSearchItemList.size
    }

    override fun getNewListSize(): Int {
        return newSearchItemList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldSearchItemList[oldItemPosition].id == newSearchItemList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldSearchItem = oldSearchItemList[oldItemPosition]
        val newSearchItem = newSearchItemList[newItemPosition]

        return oldSearchItem.login == newSearchItem.login
    }
}
