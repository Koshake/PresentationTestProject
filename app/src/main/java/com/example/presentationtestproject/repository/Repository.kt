package com.example.presentationtestproject.repository

import com.example.presentationtestproject.model.SearchResponse

interface Repository {
    suspend fun searchGithubAsync(
        query: String
    ): SearchResponse
}
