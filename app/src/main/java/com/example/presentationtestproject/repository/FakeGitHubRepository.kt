package com.example.presentationtestproject.repository

import com.example.presentationtestproject.model.SearchResponse
import com.example.presentationtestproject.model.SearchResult
import kotlinx.coroutines.delay
import kotlin.random.Random

internal class FakeGitHubRepository : Repository {

    override suspend fun searchGithubAsync(query: String): SearchResponse {
        return generateSearchResponse()
    }

    private fun generateSearchResponse(): SearchResponse {
        val list: MutableList<SearchResult> = mutableListOf()
        for (index in 1..100) {
            list.add(
                SearchResult(
                    id = index,
                    name = "Name: $index",
                    fullName = "FullName: $index",
                    private = Random.nextBoolean(),
                    description = "Description: $index",
                    updatedAt = "Updated: $index",
                    size = index,
                    stargazersCount = Random.nextInt(100),
                    language = "",
                    hasWiki = Random.nextBoolean(),
                    archived = Random.nextBoolean(),
                    score = index.toDouble()
                )
            )
        }
        return SearchResponse(list.size, list)
    }
}
