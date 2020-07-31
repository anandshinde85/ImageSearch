package com.axxess.imagesearch.searchdetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axxess.imagesearch.common.util.SingleLiveEvent
import com.axxess.imagesearch.networking.comments.GetCommentUseCase
import com.axxess.imagesearch.networking.comments.SaveCommentsUseCase
import com.axxess.imagesearch.storage.comments.Comment
import kotlinx.coroutines.launch

/**
 * View model for accessing comments from database
 * @param saveCommentsUseCase - use case for saving comments
 * @param getCommentUseCase - use case for retrieving comments
 */
class CommentsViewModel @ViewModelInject constructor(
    private val saveCommentsUseCase: SaveCommentsUseCase,
    private val getCommentUseCase: GetCommentUseCase
) : ViewModel() {
    val saveCommentLiveData = SingleLiveEvent<Unit>()
    val fetchCommentLiveData = SingleLiveEvent<Comment>()

    fun saveComment(id: String, comment: String) {
        val saveComment = Comment(id, comment)
        viewModelScope.launch {
            saveCommentsUseCase(saveComment) {
                it.either({}, {
                    saveCommentLiveData.postValue(it)
                })
            }
        }
    }

    fun getComment(id: String) {
        getCommentUseCase(id) {
            it.either({}, {
                fetchCommentLiveData.postValue(it)
            })
        }
    }

}