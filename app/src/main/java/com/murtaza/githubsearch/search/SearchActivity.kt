package com.murtaza.githubsearch.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.murtaza.githubsearch.R
import com.murtaza.githubsearch.search.adapters.SearchAdapter
import com.murtaza.githubsearch.search.data.SearchResults
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private lateinit var searchViewModel: SearchViewModel
    private val compositeDisposable = CompositeDisposable()

    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchViewModel = androidx.lifecycle.ViewModelProviders.of(this).get(SearchViewModel::class.java)
        setPlaceHolderVisibility(View.VISIBLE)
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = SearchAdapter()
        recycler_view.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val search: MenuItem = menu!!.findItem(R.id.search)
        val searchView: androidx.appcompat.widget.SearchView = search.actionView as androidx.appcompat.widget.SearchView
        search.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                clearAdapter()
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

        })
        val disposable = searchViewModel.searchUsers(createPublishSubject(searchView))
            .subscribe {
                setPlaceHolderVisibility(View.GONE)
                getUsersWithSearchResult(it)
                playAnimationFile("hourglass.json")
                setErrorMessage("", View.GONE)
            }
        compositeDisposable.add(disposable)
        return super.onCreateOptionsMenu(menu)
    }

    private fun getUsersWithSearchResult(it: String) {
        searchViewModel.getSearchResults(it)
            .observe(this, Observer {
                if (it.error == null) {
                    stopAnimation()
                    setErrorMessage("", View.GONE)
                    setRecyclerViewVisibility(View.VISIBLE)
                    updateData(it.data)
                } else {
                    playAnimationFile("error_404.json")
                    setErrorMessage("Error retrieving user: " + it.error, View.VISIBLE)
                    setRecyclerViewVisibility(View.GONE)
                }
            })
    }

    private fun createPublishSubject(searchView: androidx.appcompat.widget.SearchView): PublishSubject<String> {
        val subject: PublishSubject<String> = PublishSubject.create()
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    clearAdapter()
                } else {
                    newText?.let { subject.onNext(it) }
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                subject.onComplete()
                return true
            }

        })
        return subject
    }

    private fun updateData(searchResults: SearchResults?) {
        adapter.updateSearchListItems(searchResults!!.items)
    }

    private fun playAnimationFile(fileName: String) {
        animation_view.visibility = VISIBLE
        animation_view.setAnimation(fileName)
        animation_view.playAnimation()
    }

    private fun setRecyclerViewVisibility(visibility: Int) {
        recycler_view.visibility = visibility
    }

    private fun setErrorMessage(message: String, visibility: Int) {
        this.error.visibility = visibility
        this.error.text = message
    }

    private fun stopAnimation() {
        animation_view.cancelAnimation()
        animation_view.visibility = View.GONE
    }

    private fun setPlaceHolderVisibility(visibility: Int) {
        placeholder.visibility = visibility
    }

    fun clearAdapter() {
        adapter.searchItemsList.clear()
        adapter.notifyDataSetChanged()
        setPlaceHolderVisibility(View.VISIBLE)
        stopAnimation()
        setErrorMessage("", GONE)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}