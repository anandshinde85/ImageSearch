package com.axxess.imagesearch.di

import android.content.Context
import androidx.room.Room
import com.axxess.imagesearch.common.util.DateFormat
import com.axxess.imagesearch.common.util.GsonUTCDateAdapter
import com.axxess.imagesearch.storage.BaseDatabase
import com.axxess.imagesearch.storage.DatabaseDataSource
import com.axxess.imagesearch.storage.database.AppDatabase
import com.axxess.imagesearch.storage.database.DATABASE_NAME
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setDateFormat(DateFormat.ZULU.it)
        .registerTypeAdapter(Date::class.java, GsonUTCDateAdapter())
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideBaseDatabase(@ApplicationContext context: Context): BaseDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDataSource(gson: Gson, baseDatabase: BaseDatabase): DatabaseDataSource =
        DatabaseDataSource(gson, baseDatabase)
}