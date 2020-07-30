package com.axxess.imagesearch.di

import android.content.Context
import com.axxess.imagesearch.BuildConfig
import com.axxess.imagesearch.common.ImageSearchApplication
import com.axxess.imagesearch.networking.NetworkUtil
import com.axxess.imagesearch.networking.imagesearch.ImageSearchApi
import com.axxess.imagesearch.repository.API_BASE_URL_PARAM
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    private const val NETWORK_TIMEOUT: Long = 30

    @Provides
    fun providesContext(application: ImageSearchApplication): Context =
        application.applicationContext

    @Provides
    @Named(API_BASE_URL_PARAM)
    fun providesApiBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor() = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val request = original.newBuilder()
                .header(
                    BuildConfig.AUTHORIZATION_KEY,
                    BuildConfig.AUTHORIZATION_VALUE
                )
                .build()
            return chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, interceptor: Interceptor) =
        OkHttpClient.Builder()
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun providesNetworkUtil(@ApplicationContext context: Context) = NetworkUtil(context)

    @Singleton
    @Provides
    fun provideImageSearchApi(retrofit: Retrofit): ImageSearchApi =
        retrofit.create(ImageSearchApi::class.java)
}