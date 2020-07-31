package com.axxess.imagesearch.searchdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.axxess.imagesearch.R;
import com.axxess.imagesearch.common.ui.BaseActivity;
import com.axxess.imagesearch.common.util.Utility;
import com.axxess.imagesearch.networking.imagesearch.SearchResults;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SearchDetailsActivity extends BaseActivity {
    public static final String SEARCH_RESULTS_ARG = "search_results";
    public static final String SEARCH_STRING_ARG = "search_string";
    private SearchResults searchResults;
    private String searchString;
    private FrameLayout contentFrame;
    private ProgressBar progressBar;
    private ImageView imageDetail;
    private TextView oldCommentTitle, oldComment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            searchResults = intent.getParcelableExtra(SEARCH_RESULTS_ARG);
            searchString = intent.getStringExtra(SEARCH_STRING_ARG);
        }
        setUpActionBar();
        loadSearchDetails();
    }

    private void loadSearchDetails() {
        if (searchResults != null && !searchResults.getImages().isEmpty()) {
            Utility.loadImage(imageDetail, searchResults.getImages().get(0).getLink());
        }
    }

    @Override
    public void showContent() {
        Utility.show(contentFrame);
        Utility.hide(progressBar);
    }

    @Override
    public void showLoading() {
        Utility.hide(contentFrame);
        Utility.show(progressBar);
    }

    @Override
    public void hideLoading() {
        Utility.show(contentFrame);
        Utility.hide(progressBar);
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
        initBaseLayout();
        initDetailViews();
    }

    private void initDetailViews() {
        imageDetail = findViewById(R.id.imageDetail);
        oldCommentTitle = findViewById(R.id.oldCommentTitle);
        oldComment = findViewById(R.id.oldComment);
    }

    private void initBaseLayout() {
        contentFrame = findViewById(R.id.contentFrame);
        progressBar = findViewById(R.id.baseProgressbar);
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
