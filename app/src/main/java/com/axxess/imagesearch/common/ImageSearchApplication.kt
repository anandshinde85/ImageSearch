package com.axxess.imagesearch.common

import android.app.Application
import com.axxess.imagesearch.common.usecase.ResilienceUseCase
import com.axxess.imagesearch.common.usecase.UseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers

@HiltAndroidApp
class ImageSearchApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        UseCase.dispatcher = Dispatchers.IO
        ResilienceUseCase.dispatcher = Dispatchers.IO
    }
}