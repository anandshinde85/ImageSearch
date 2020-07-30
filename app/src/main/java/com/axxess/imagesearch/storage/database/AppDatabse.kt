package com.axxess.imagesearch.storage.database

import androidx.room.Database
import androidx.room.TypeConverters
import com.anand.mvvmskeletonarchitecture.storage.ApiCache
import com.axxess.imagesearch.storage.BaseDatabase
import com.axxess.imagesearch.storage.DateConverter

const val DATABASE_NAME = "appdb"

@Database(
    entities = [ApiCache::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class
)
abstract class AppDatabase : BaseDatabase() {
}