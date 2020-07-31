package com.axxess.imagesearch.imagesearch

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.axxess.imagesearch.R
import com.axxess.imagesearch.common.model.Resource
import com.axxess.imagesearch.common.ui.BaseActivity
import com.axxess.imagesearch.common.util.hide
import com.axxess.imagesearch.common.util.hideKeyboard
import com.axxess.imagesearch.common.util.show
import com.axxess.imagesearch.networking.imagesearch.ImageSearchResponse
import com.axxess.imagesearch.networking.imagesearch.SearchResults
import com.axxess.imagesearch.searchdetails.SearchDetailsActivity
import com.axxess.imagesearch.searchdetails.SearchDetailsActivity.SEARCH_RESULTS_ARG
import com.axxess.imagesearch.searchdetails.SearchDetailsActivity.SEARCH_STRING_ARG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_image_search.*

@AndroidEntryPoint
class ImageSearchActivity : BaseActivity() {

    companion object {
        private const val SEARCH_TEXT = "search_text"
        private const val SEARCH_CHAR_THRESHOLD =
            3 // To be used when we have to search items as and when user types
    }

    private var searchText: String? = null
    private val searchViewModel: ImageSearchViewModel by viewModels()
    private lateinit var imageSearchAdapter: ImageSearchAdapter

    override fun getLayoutResourceId() = R.layout.activity_image_search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            searchText = it.getString(SEARCH_TEXT).toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putString(SEARCH_TEXT, searchText)
        }
    }

    override fun setUpUI() {
        initObserver()
        initSearchView()
        initSearchGrid()
    }

    private fun initObserver() {
        searchViewModel.singleLiveData.takeUnless { it.hasActiveObservers() }
            ?.observe(this, Observer {
                handleSearchResponse(it)
            })
    }

    /**
     * Initialise search view with listeners
     */
    private fun initSearchView() = searchView.run {
        searchText?.let {
            if (it.isNotEmpty()) {
                setQuery(searchText, false)
                hideKeyboard()
            }
        }
        setIconifiedByDefault(false)
        isIconified = false

        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                searchText = newText
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                searchImages(query)
                return false
            }
        })
    }

    private fun initSearchGrid() {
        imageSearchAdapter = ImageSearchAdapter(onSearchItemClick = { onSearchClick(it) })
        searchGrid.layoutManager =
            GridLayoutManager(this, resources.getInteger(R.integer.grid_columns))
        searchGrid.adapter = imageSearchAdapter
    }

    private fun onSearchClick(searchResults: SearchResults) {
        val searchDetailIntent = Intent(this, SearchDetailsActivity::class.java).apply {
            putExtra(SEARCH_RESULTS_ARG, searchResults)
            putExtra(SEARCH_STRING_ARG, searchText)
        }
        startActivity(searchDetailIntent)
    }

    private fun handleSearchResponse(resourceState: Resource<ImageSearchResponse?>?) {
        when (resourceState) {
            is Resource.Loading, is Resource.LoadedWithApiInProgress -> showLoading()
            is Resource.Loaded -> showResults(resourceState.data)
            is Resource.BaseFailed -> showError()
        }
    }

    private fun showResults(searchResponse: ImageSearchResponse?) {
        hideLoading()
        searchResponse?.let {
            imageSearchAdapter.updateDataSet(it.data)
        }
    }

    private fun searchImages(searchString: String) {
        searchViewModel.loadData(searchString)
    }

    /**
     * Method to display screen content
     */
    override fun showContent() {
        searchGrid.show()
        progressbar.hide()
        errorInfo.hide()
    }

    /**
     * Function to show progress bar and hide screen content
     */
    override fun showLoading() {
        searchGrid.hide()
        errorInfo.hide()
        progressbar.show()
    }

    /**
     * Function to show screen content and hide loading
     */
    override fun hideLoading() {
        searchGrid.show()
        progressbar.hide()
    }

    /**
     * Function hide content frame and display error
     * @param errorMessage - error message
     */
    override fun showError(errorMessage: String) {
        searchGrid.hide()
        progressbar.hide()
        errorInfo.apply {
            text = errorMessage
            show()
        }
    }

    /**
     * Method to display content frame and hide error message
     */
    override fun hideError() {
        searchGrid.show()
        errorInfo.hide()
    }
}