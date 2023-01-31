package com.example.presentationtestproject.repository

import com.example.presentationtestproject.model.SearchResponse
import kotlinx.coroutines.delay

internal class GitHubRepository(private val gitHubApi: GitHubApi) : Repository {

    override suspend fun searchGithubAsync(query: String): SearchResponse {
        delay(2_000)
        return gitHubApi.searchGithubAsync(query).await()
    }
}
