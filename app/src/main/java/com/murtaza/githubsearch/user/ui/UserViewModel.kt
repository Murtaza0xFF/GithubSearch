package com.murtaza.githubsearch.user.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.murtaza.githubsearch.common.DataWrapper
import com.murtaza.githubsearch.GithubSearch
import com.murtaza.githubsearch.api.GithubApiInterface
import com.murtaza.githubsearch.user.data.User
import com.murtaza.githubsearch.user.di.UserModule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserViewModel : ViewModel() {
    @Inject
    lateinit var githubApiInterface: GithubApiInterface
    private val compositeDisposable = CompositeDisposable()

    init {
        initializeDagger()
    }

    fun getUser(username: String): MutableLiveData<DataWrapper<User>> {
        val mutableLiveData = MutableLiveData<DataWrapper<User>>()
        val disposable = githubApiInterface
            .getUser(username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ user ->
                mutableLiveData.value = DataWrapper(user, null)
            }, { t: Throwable? ->
                run {
                    mutableLiveData.value = DataWrapper(null, t!!.message)
                }
            })
        compositeDisposable.add(disposable)
        return mutableLiveData

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unsubscribeViewModel() {
        compositeDisposable.clear()
    }

    override fun onCleared() {
        unsubscribeViewModel()
        super.onCleared()
    }

    private fun initializeDagger() = GithubSearch.applicationComponent.plus(UserModule()).inject(this)
}