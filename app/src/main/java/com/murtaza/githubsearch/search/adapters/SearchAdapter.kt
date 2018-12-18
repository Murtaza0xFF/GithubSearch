package com.murtaza.githubsearch.search.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.murtaza.githubsearch.common.PicassoHelper
import com.murtaza.githubsearch.R
import com.murtaza.githubsearch.search.SearchDiffCallback
import com.murtaza.githubsearch.search.data.SearchItem
import com.murtaza.githubsearch.user.ui.UserActivity
import kotlinx.android.synthetic.main.row.view.*

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val picassoHelper: PicassoHelper by lazy {
        PicassoHelper()
    }
    val searchItemsList: MutableList<SearchItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchItemsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchItem: SearchItem = searchItemsList[position]
        holder.itemView.user_name.text = "Username %s".format(searchItem.login)
        if (searchItem.score.toInt() % 2 == 0) {
            holder.itemView.user_avatar.borderColor =
                    ContextCompat.getColor(holder.itemView.user_avatar.context, android.R.color.holo_green_dark)
        } else {
            holder.itemView.user_avatar.borderColor =
                    ContextCompat.getColor(holder.itemView.user_avatar.context, android.R.color.holo_red_dark)
        }
        picassoHelper.setImageView(
            R.drawable.ic_person_outline_black_24dp,
            searchItem.avatarUrl,
            holder.itemView.user_avatar
        )

    }

    fun updateSearchListItems(userList: List<SearchItem>) {
        val diffCallback = SearchDiffCallback(this.searchItemsList, userList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.searchItemsList.clear()
        this.searchItemsList.addAll(userList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.search_item.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val intent = Intent(itemView.context, UserActivity::class.java)
            intent.putExtra("username", searchItemsList[adapterPosition].login)
            itemView.search_item.context.startActivity(intent)
        }

    }

}
