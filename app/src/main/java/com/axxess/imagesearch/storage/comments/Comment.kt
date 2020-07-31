package com.axxess.imagesearch.storage.comments

import androidx.room.Entity
import androidx.room.PrimaryKey

const val COMMENT_TABLE_NAME = "comments_table"

@Entity(tableName = COMMENT_TABLE_NAME)
data class Comment(
    @PrimaryKey
    val id: String,
    val comment: String
)