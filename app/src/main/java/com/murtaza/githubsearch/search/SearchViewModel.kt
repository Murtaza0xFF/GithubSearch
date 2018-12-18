package com.murtaza.githubsearch.search

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.OnLifecycleEvent
import com.murtaza.githubsearch.common.DataWrapper
import com.murtaza.githubsearch.GithubSearch
import com.murtaza.githubsearch.api.GithubApiInterface
import com.murtaza.githubsearch.search.data.SearchResults
import com.murtaza.githubsearch.search.di.SearchModule
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchViewModel : androidx.lifecycle.ViewModel(), LifecycleObserver {

    @Inject
    lateinit var githubApiInterface: GithubApiInterface
    private val compositeDisposable = CompositeDisposable()

    init {
        initializeDagger()
    }

    fun searchUsers(subject: PublishSubject<String>): Observable<String> {
        return subject.debounce(1, TimeUnit.MILLISECONDS)
            .filter { item -> item.length > 2 }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getSearchResults(it: String): LiveData<DataWrapper<SearchResults>> {
        val mutableLiveData = MutableLiveData<DataWrapper<SearchResults>>()
        val disposable = githubApiInterface
            .searchUsers(it, "followers", "desc")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ searchResults ->
                mutableLiveData.value = DataWrapper(searchResults, null)
            }, { t: Throwable? ->
                run {
                    mutableLiveData.value =
                            DataWrapper(SearchResults(mutableListOf()), t!!.message)
                }
            })
        compositeDisposable.add(disposable)
        return mutableLiveData
    }

    @OnLifecycleEvent(ON_DESTROY)
    fun unsubscribeViewModel() {
        compositeDisposable.clear()
    }

    override fun onCleared() {
        unsubscribeViewModel()
        super.onCleared()
    }
    private fun initializeDagger() = GithubSearch.applicationComponent.plus(SearchModule()).inject(this)

}