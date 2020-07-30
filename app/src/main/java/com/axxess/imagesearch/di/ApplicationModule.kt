package com.axxess.imagesearch.di

import com.axxess.imagesearch.common.ImageSearchApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {
    @Singleton
    @Provides
    fun providesApplication() : ImageSearchApplication = ImageSearchApplication()
}