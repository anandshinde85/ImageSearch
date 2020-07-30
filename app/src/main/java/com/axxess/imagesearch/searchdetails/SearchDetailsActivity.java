package com.axxess.imagesearch.searchdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;

import com.axxess.imagesearch.R;
import com.axxess.imagesearch.common.ui.BaseActivity;
import com.axxess.imagesearch.networking.imagesearch.SearchResults;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SearchDetailsActivity extends BaseActivity {
    public static String SEARCH_RESULTS_ARG = "search_results";
    public static String SEARCH_STRING_ARG = "search_string";
    private SearchResults searchResults;
    private String searchString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            searchResults = intent.getParcelableExtra(SEARCH_RESULTS_ARG);
            searchString = intent.getStringExtra(SEARCH_STRING_ARG);
        }
        setUpActionBar();
    }

    @Override
    public void showContent() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(@NotNull String errorMessage) {

    }

    @Override
    public void hideError() {

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_search_details;
    }

    @Override
    public void setUpUI() {
    }

    private void setUpActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        if (searchResults != null && !searchResults.getTitle().isEmpty()) {
            supportActionBar.setTitle(searchResults.getTitle());
        } else {
            supportActionBar.setTitle(searchString);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
