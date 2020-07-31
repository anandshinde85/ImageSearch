package com.axxess.imagesearch.storage.comments

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CommentsDao {
    @Query("SELECT * FROM $COMMENT_TABLE_NAME where id = :id")
    suspend fun findComment(id: String): Comment?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(comment: Comment)

    @Query("DELETE FROM $COMMENT_TABLE_NAME")
    suspend fun delete()

    @Query("DELETE FROM $COMMENT_TABLE_NAME where id = :id")
    suspend fun deleteById(id: String)
}