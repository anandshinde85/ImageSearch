package com.axxess.imagesearch.storage

import androidx.room.RoomDatabase
import com.anand.mvvmskeletonarchitecture.storage.ApiCacheDao

abstract class BaseDatabase : RoomDatabase() {
    abstract fun apiCacheDao(): ApiCacheDao
}