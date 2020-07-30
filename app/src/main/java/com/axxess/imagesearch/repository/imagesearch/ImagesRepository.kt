package com.axxess.imagesearch.repository.imagesearch

import com.axxess.imagesearch.common.util.Either
import com.axxess.imagesearch.networking.Failure
import com.axxess.imagesearch.networking.imagesearch.ImageSearchResponse
import com.axxess.imagesearch.networking.imagesearch.ImagesNetworkDataSource
import com.axxess.imagesearch.repository.Repository
import javax.inject.Inject

class ImagesRepository @Inject constructor(
    private val imagesNetworkDataSource: ImagesNetworkDataSource
) : Repository() {
    suspend fun searchImages(
        searchString: String,
        forceRefresh: Boolean = false,
        onResult: suspend (Either<Failure, ImageSearchResponse>) -> Unit
    ) {
        val key = ImageSearchResponse.createCacheKey(apiBaseUrl, searchString)
        fetchData(
            key,
            { imagesNetworkDataSource.searchImages(searchString) },
            forceRefresh,
            onResult
        )
    }
}