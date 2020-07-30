package com.axxess.imagesearch.common.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.axxess.imagesearch.R
import com.axxess.imagesearch.common.util.hide
import com.axxess.imagesearch.common.util.show
import kotlinx.android.synthetic.main.base_activity.*

/**
 * Activity to be extended by all activities for performing common tasks of app.
 *      Responsibilities:
 *      1. Common layout with content frame to inflate child layout
 *      2. Show and hide loading
 *      3. Show and hide error messages
 * @author Anand Shinde
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)
        contentFrame.addView(layoutInflater.inflate(getLayoutResourceId(), null))
        setUpUI()
    }

    /**
     * Method to display screen content
     */
    abstract fun showContent()

    /**
     * Function to show progress bar and hide screen content
     */
    abstract fun showLoading()

    /**
     * Function to show screen content and hide loading
     */
    abstract fun hideLoading()

    /**
     * Function to hide content frame and display error
     * @param errorMessage - error message
     */
    abstract fun showError(errorMessage: String = getString(R.string.default_error_message))

    /**
     * Method to display content and hide error message
     */
    abstract fun hideError()

    /**
     * Method to get layout resource id
     */
    abstract fun getLayoutResourceId(): Int

    /**
     * Method for setting up UI components of activity
     */
    abstract fun setUpUI()
}