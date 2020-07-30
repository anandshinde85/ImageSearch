package com.axxess.imagesearch.imagesearch

import androidx.hilt.lifecycle.ViewModelInject
import com.axxess.imagesearch.common.viewmodel.BaseViewModel
import com.axxess.imagesearch.networking.imagesearch.ImageSearchResponse
import com.axxess.imagesearch.networking.imagesearch.ImageSearchUseCase
import javax.inject.Inject

class ImageSearchViewModel @ViewModelInject constructor(var imageSearchUseCase: ImageSearchUseCase) :
    BaseViewModel<String, ImageSearchResponse>(imageSearchUseCase) {

    override fun load(query: String, forceRefresh: Boolean) {
        super.load(query, forceRefresh)
        imageSearchUseCase(query, forceRefresh)
    }

    public override fun onCleared() {
        imageSearchUseCase.cancel()
        super.onCleared()
    }
}