package com.example.presentationtestproject

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.presentationtestproject.model.SearchResponse
import com.example.presentationtestproject.repository.GitHubApi
import com.example.presentationtestproject.repository.GitHubRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.system.measureTimeMillis

@RunWith(AndroidJUnit4::class)
class GitHubRepositoryTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    private lateinit var repository: GitHubRepository

    @Mock
    private lateinit var gitHubApi: GitHubApi

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = GitHubRepository(gitHubApi)
    }

    @Test
    fun `github api method executes ones with runBlocking`() {
        val searchQuery = "some query"
        val result = Mockito.mock(Deferred::class.java) as Deferred<SearchResponse>

        `when`(gitHubApi.searchGithubAsync(searchQuery)).thenReturn(result)
        runBlocking {
            val totalExecutionTime = measureTimeMillis {
                repository.searchGithubAsync(searchQuery)
            }
            Mockito.verify(gitHubApi, Mockito.times(1)).searchGithubAsync(searchQuery)
            println("Total Execution Time: $totalExecutionTime ms")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `github api method executes ones`() {
        val searchQuery = "some query"
        val result = Mockito.mock(Deferred::class.java) as Deferred<SearchResponse>

        `when`(gitHubApi.searchGithubAsync(searchQuery)).thenReturn(result)
        testCoroutineRule.runBlockingTest {
            val totalExecutionTime = measureTimeMillis {
                repository.searchGithubAsync(searchQuery)
            }
            Mockito.verify(gitHubApi, Mockito.times(1)).searchGithubAsync(searchQuery)
            println("Total Execution Time: $totalExecutionTime ms")
        }
    }

}
