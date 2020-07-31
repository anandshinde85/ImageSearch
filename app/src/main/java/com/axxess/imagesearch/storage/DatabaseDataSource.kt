package com.axxess.imagesearch.storage

import com.anand.mvvmskeletonarchitecture.storage.ApiCache
import com.axxess.imagesearch.networking.CacheTime
import com.axxess.imagesearch.networking.CacheableResponse
import com.axxess.imagesearch.storage.comments.Comment
import com.axxess.imagesearch.storage.database.AppDatabase
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class DatabaseDataSource @Inject constructor(val gson: Gson, val appDatabase: AppDatabase) {

    suspend fun save(key: String, data: CacheableResponse) {
        val value = gson.toJson(data)

        val cacheData = ApiCache(
            getPrefixForKey(key),
            value,
            data.javaClass.simpleName
        )
        appDatabase.apiCacheDao().save(cacheData)
    }

    suspend inline fun <reified T : CacheableResponse> find(
        key: String,
        cacheTime: CacheTime = CacheTime.LONG
    ): T? {
        val cache = appDatabase.apiCacheDao().findByKey(getPrefixForKey(key))

        if (cache != null) {
            val cachable = gson.fromJson(cache.value, T::class.java)
            cachable.forceRefresh = cache.forceRefresh
            if (cachable != null && !cachable.isCacheExpired(cacheTime))
                return cachable
        }
        return null
    }

    suspend fun deleteByKey(key: String) {
        appDatabase.apiCacheDao().deleteByKey(getPrefixForKey(key))
    }

    /**
     * Function to add specific prefix for given key, this is usually user name so that different user
     * in same app can have multiple cached key response
     */
    fun getPrefixForKey(key: String) = key

    /**
     * Function to save comments
     * @param comment - Comment to be stored in database
     */
    suspend fun saveComment(comment: Comment) =
        appDatabase.commentsDao().save(comment)

    /**
     * Function to get saved comments
     * @param id - id of comment saved
     */
    suspend fun getComment(id: String) = appDatabase.commentsDao().findComment(id)
}