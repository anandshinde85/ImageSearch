package com.axxess.imagesearch.searchdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;

import com.axxess.imagesearch.R;
import com.axxess.imagesearch.common.ui.BaseActivity;
import com.axxess.imagesearch.common.util.Utility;
import com.axxess.imagesearch.networking.imagesearch.SearchResults;
import com.axxess.imagesearch.storage.comments.Comment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchDetailsActivity extends BaseActivity {
    public static final String SEARCH_RESULTS_ARG = "search_results";
    public static final String SEARCH_STRING_ARG = "search_string";
    private SearchResults searchResults;
    private String searchString;
    private FrameLayout contentFrame;
    private ProgressBar progressBar;
    private ImageView imageDetail;
    private TextView oldCommentTitle, oldComment;
    private EditText editComment;
    private Button submit;
    private CommentsViewModel commentsViewModel;

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
        commentsViewModel = new ViewModelProvider(this).get(CommentsViewModel.class);
        initObserver();
    }

    @Override
    public void setUpUI() {
        initBaseLayout();
        initDetailViews();
        initListeners();
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

    private void setUpActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        if (searchResults != null && !searchResults.getTitle().isEmpty()) {
            supportActionBar.setTitle(searchResults.getTitle());
        } else {
            supportActionBar.setTitle(searchString);
        }
    }

    private void initBaseLayout() {
        contentFrame = findViewById(R.id.contentFrame);
        progressBar = findViewById(R.id.baseProgressbar);
    }

    private void initDetailViews() {
        imageDetail = findViewById(R.id.imageDetail);
        oldCommentTitle = findViewById(R.id.oldCommentTitle);
        oldComment = findViewById(R.id.oldComment);
        editComment = findViewById(R.id.editComment);
        submit = findViewById(R.id.submitButton);
    }

    private void initListeners() {
        submit.setOnClickListener(view -> {
            String comment = editComment.getText().toString();
            if (comment.isEmpty()) {
                displayToast(getString(R.string.enter_comment_message));
            } else {
                commentsViewModel.saveComment(searchResults.getId(), comment);
            }
        });
    }

    private void initObserver() {
        commentsViewModel.getSaveCommentLiveData().observe(this, saved -> {
            displayToast(getString(R.string.comment_saved));
            displayComment(new Comment(searchResults.getId(), editComment.getText().toString()));
        });

        commentsViewModel.getFetchCommentLiveData().observe(this, comment -> {
            displayComment(comment);
        });
        commentsViewModel.getComment(searchResults.getId());
    }

    private void displayComment(Comment comment) {
        if (comment != null && !comment.getComment().isEmpty()) {
            Utility.show(oldCommentTitle);
            Utility.show(oldComment);
            oldComment.setText(comment.getComment());
        } else {
            Utility.hide(oldCommentTitle);
            Utility.hide(oldComment);
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
