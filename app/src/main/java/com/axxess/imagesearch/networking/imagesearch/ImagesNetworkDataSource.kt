package com.axxess.imagesearch.networking.imagesearch

import com.axxess.imagesearch.common.util.Either
import com.axxess.imagesearch.networking.Failure
import com.axxess.imagesearch.networking.NetworkDataSource
import javax.inject.Inject

class ImagesNetworkDataSource @Inject constructor(private val imageSearchApi: ImageSearchApi) :
    NetworkDataSource() {
    fun searchImages(searchString: String): Either<Failure, ImageSearchResponse> = request(imageSearchApi.searchImages(searchString))
}