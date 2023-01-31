package com.example.presentationtestproject.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.presentationtestproject.model.SearchResponse
import com.example.presentationtestproject.repository.GitHubApi
import com.example.presentationtestproject.repository.GitHubRepository
import com.example.presentationtestproject.repository.Repository
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewModel(
    private val repository: Repository = GitHubRepository(
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(GitHubApi::class.java)
    )
) : ViewModel() {

    companion object {
        const val BASE_URL = "https://api.github.com"
        const val SEARCH_QUERY = "some query"
        const val ERROR_TEXT = "Search results or total count are null"
        const val EXCEPTION_TEXT = "Response is null or unsuccessful"
    }

    private val _liveData = MutableLiveData<ScreenState>()
    private val liveData: LiveData<ScreenState> = _liveData
    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable -> handleError(throwable) })

    fun subscribeToLiveData() = liveData

    fun searchGitHub(searchQuery: String) {
        viewModelCoroutineScope.launch {
            _liveData.value = ScreenState.Loading
            val searchResponse = repository.searchGithubAsync(searchQuery)
            val searchResults = searchResponse.searchResults
            val totalCount = searchResponse.totalCount
            if (searchResults != null && totalCount != null) {
                _liveData.value = ScreenState.Success(searchResponse)
            } else {
                _liveData.value =
                    ScreenState.Error(Throwable(ERROR_TEXT))
            }
        }
    }

    private fun handleError(error: Throwable) {
        _liveData.value =
            ScreenState.Error(
                Throwable(
                    error.message ?: EXCEPTION_TEXT
                )
            )
    }

    override fun onCleared() {
        super.onCleared()
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }
}

sealed class ScreenState {
    object Loading : ScreenState()
    data class Success(val searchResponse: SearchResponse) : ScreenState()
    data class Error(val error: Throwable) : ScreenState()
}
