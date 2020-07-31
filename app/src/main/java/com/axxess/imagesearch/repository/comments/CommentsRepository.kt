package com.axxess.imagesearch.repository.comments

import com.axxess.imagesearch.common.util.Either
import com.axxess.imagesearch.storage.DatabaseDataSource
import com.axxess.imagesearch.storage.comments.Comment
import javax.inject.Inject

/**
 * Repository to access comments database table
 * @author Anand Shinde
 */
class CommentsRepository @Inject constructor(private val databaseDataSource: DatabaseDataSource) {
    /**
     * Method to save comment in database
     * @param comment - Comment to be saved
     * @return success response
     */
    suspend fun saveComment(comment: Comment) =
        Either.Right(databaseDataSource.saveComment(comment))

    /**
     * Method to get comment from database
     * @param id - comment id
     * @return success response
     */
    suspend fun getComment(id: String) = Either.Right(databaseDataSource.getComment(id))
}