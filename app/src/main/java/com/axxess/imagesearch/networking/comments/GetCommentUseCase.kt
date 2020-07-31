package com.axxess.imagesearch.networking.comments

import com.axxess.imagesearch.common.usecase.UseCase
import com.axxess.imagesearch.common.util.Either
import com.axxess.imagesearch.networking.Failure
import com.axxess.imagesearch.repository.comments.CommentsRepository
import com.axxess.imagesearch.storage.comments.Comment
import javax.inject.Inject

class GetCommentUseCase @Inject constructor(private val commentsRepository: CommentsRepository) :
    UseCase<Comment, String>() {
    override suspend fun run(params: String, forceRefresh: Boolean): Either<Failure, Comment?> =
        commentsRepository.getComment(params)
}