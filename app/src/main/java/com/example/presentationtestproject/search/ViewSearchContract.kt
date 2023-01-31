package com.example.presentationtestproject.search

import com.example.presentationtestproject.model.SearchResult

internal interface ViewSearchContract {
    fun displaySearchResults(
        searchResults: List<SearchResult>,
        totalCount: Int
    )

    fun displayError()
    fun displayError(error: String)
    fun displayLoading(show: Boolean)
}
