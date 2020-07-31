package com.anand.mvvmskeletonarchitecture.storage

import androidx.room.*
import com.axxess.imagesearch.storage.DateConverter

@Dao
@TypeConverters(DateConverter::class)
interface ApiCacheDao {

    @Query("SELECT * FROM $API_CACHE_TABLE_NAME where key = :key")
    suspend fun findByKey(key: String): ApiCache?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(apiCache: ApiCache)

    @Query("DELETE FROM $API_CACHE_TABLE_NAME")
    suspend fun delete()

    @Query("DELETE FROM $API_CACHE_TABLE_NAME where key = :key")
    suspend fun deleteByKey(key: String)

    @Query("DELETE FROM $API_CACHE_TABLE_NAME WHERE key LIKE :key")
    suspend fun deleteCampaignCache(key: String)
}