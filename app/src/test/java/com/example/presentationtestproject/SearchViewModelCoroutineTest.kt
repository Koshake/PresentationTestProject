package com.example.presentationtestproject

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.presentationtestproject.model.SearchResponse
import com.example.presentationtestproject.repository.GitHubRepository
import com.example.presentationtestproject.search.ScreenState
import com.example.presentationtestproject.search.SearchViewModel
import com.example.presentationtestproject.search.SearchViewModel.Companion.ERROR_TEXT
import com.example.presentationtestproject.search.SearchViewModel.Companion.SEARCH_QUERY
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class SearchViewModelCoroutineTest {
    @get:Rule
    var instantExecutorRuleForViewModel = InstantTaskExecutorRule()

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: GitHubRepository

    private lateinit var searchViewModel: SearchViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchViewModel = SearchViewModel(repository)
    }

    @Test
    fun `coroutines test return value is not null`() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            Mockito.`when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                SearchResponse(1, listOf())
            )

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)
                Assert.assertNotNull(liveData.value)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun `coroutines test return value is Loading`() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                Assert.assertEquals(liveData.value, ScreenState.Loading)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun `searchGithubAsync executes onece`() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            Mockito.`when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                SearchResponse(null, listOf())
            )

            try {
                liveData.observeForever(observer)

                searchViewModel.searchGitHub(SEARCH_QUERY)
                //delay(1)
                verify(repository, times(1)).searchGithubAsync(SEARCH_QUERY)

            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun `coroutines test return value is error`() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            Mockito.`when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                SearchResponse(null, listOf())
            )

            try {
                liveData.observeForever(observer)

                searchViewModel.searchGitHub(SEARCH_QUERY)

                delay(1)

                val value: ScreenState.Error = liveData.value as ScreenState.Error
                Assert.assertEquals(value.error.message, ERROR_TEXT)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun `coroutines test throws exception`() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            Mockito.`when`(repository.searchGithubAsync(SEARCH_QUERY)).thenThrow(
                RuntimeException()
            )

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                delay(1)
                val value: ScreenState.Error = liveData.value as ScreenState.Error
                Assert.assertEquals(value.error.message, SearchViewModel.EXCEPTION_TEXT)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }
}
