package com.axxess.imagesearch.common.model

/**
 * Base response class for all API response.
 * @author Anand Shinde
 */
data class ApiResponse<T>(
    val data: T, // Expected response
    val success: Boolean,
    val status: Int
)

/**
 * Error class holding error info in case API have thrown errors
 * @author Anand Shinde
 */
data class ApiError(
    val error: String,
    val request: String,
    val method: String
)