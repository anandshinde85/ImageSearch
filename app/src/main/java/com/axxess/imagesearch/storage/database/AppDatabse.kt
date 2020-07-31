package com.axxess.imagesearch.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anand.mvvmskeletonarchitecture.storage.ApiCache
import com.anand.mvvmskeletonarchitecture.storage.ApiCacheDao
import com.axxess.imagesearch.storage.DateConverter
import com.axxess.imagesearch.storage.comments.Comment
import com.axxess.imagesearch.storage.comments.CommentsDao

const val DATABASE_NAME = "appdb"

@Database(
    entities = [Comment::class, ApiCache::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun apiCacheDao(): ApiCacheDao
    abstract fun commentsDao(): CommentsDao
}