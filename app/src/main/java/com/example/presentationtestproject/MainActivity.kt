package com.example.presentationtestproject

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.presentationtestproject.details.DetailsActivity
import com.example.presentationtestproject.model.SearchResult
import com.example.presentationtestproject.search.ScreenState
import com.example.presentationtestproject.search.SearchResultAdapter
import com.example.presentationtestproject.search.SearchViewModel
import com.example.presentationtestproject.search.ViewSearchContract
import kotlinx.android.synthetic.main.activity_main.progressBar
import kotlinx.android.synthetic.main.activity_main.recyclerView
import kotlinx.android.synthetic.main.activity_main.searchEditText
import kotlinx.android.synthetic.main.activity_main.toDetailsActivityButton
import kotlinx.android.synthetic.main.activity_main.totalCountTextView
import java.util.Locale

class MainActivity : AppCompatActivity(), ViewSearchContract {

    private val adapter = SearchResultAdapter()
    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
    }
    private var totalCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        viewModel.subscribeToLiveData().observe(this) { onStateChange(it) }
    }

    private fun onStateChange(screenState: ScreenState) {
        when (screenState) {
            is ScreenState.Success -> {
                val searchResponse = screenState.searchResponse
                val totalCount = searchResponse.totalCount
                progressBar.visibility = View.GONE
                with(totalCountTextView) {
                    visibility = View.VISIBLE
                    text =
                        String.format(
                            Locale.getDefault(),
                            getString(R.string.results_count),
                            totalCount
                        )
                }

                totalCount?.let { this.totalCount = it }
                searchResponse.searchResults?.let {
                    adapter.updateResults(it)
                }
            }
            is ScreenState.Loading -> {
                progressBar.visibility = View.VISIBLE
            }
            is ScreenState.Error -> {
                progressBar.visibility = View.GONE
                Toast.makeText(this, screenState.error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView() {
        setQueryListener()
        setRecyclerView()
        toDetailsActivityButton.setOnClickListener {
            startActivity(DetailsActivity.getIntent(this, totalCount))
        }
    }

    private fun setRecyclerView() {
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun setQueryListener() {
        searchEditText.setOnEditorActionListener(
            TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = searchEditText.text.toString()
                    if (query.isNotBlank()) {
                        viewModel.searchGitHub(query)
                        return@OnEditorActionListener true
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.enter_search_word),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@OnEditorActionListener false
                    }
                }
                false
            }
        )
    }

    override fun displaySearchResults(
        searchResults: List<SearchResult>,
        totalCount: Int
    ) {
        with(totalCountTextView) {
            visibility = View.VISIBLE
            text = String.format(Locale.getDefault(), getString(R.string.results_count), totalCount)
        }

        this.totalCount = totalCount
        adapter.updateResults(searchResults)
    }

    override fun displayError() {
        Toast.makeText(this, getString(R.string.undefined_error), Toast.LENGTH_SHORT).show()
    }

    override fun displayError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun displayLoading(show: Boolean) = when (show) {
        true -> progressBar.visibility = View.VISIBLE
        false -> progressBar.visibility = View.GONE
    }

}
