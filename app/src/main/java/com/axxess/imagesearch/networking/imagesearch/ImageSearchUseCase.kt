package com.axxess.imagesearch.networking.imagesearch

import com.axxess.imagesearch.common.usecase.ResilienceUseCase
import com.axxess.imagesearch.repository.imagesearch.ImagesRepository
import javax.inject.Inject

class ImageSearchUseCase @Inject constructor(private val imagesRepository: ImagesRepository) :
    ResilienceUseCase<ImageSearchResponse, String>() {
    override suspend fun run(params: String, forceRefresh: Boolean) {
        imagesRepository.searchImages(params, forceRefresh, ::updateData)
    }
}