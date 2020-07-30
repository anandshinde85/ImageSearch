package com.axxess.imagesearch.networking.imagesearch

import android.os.Parcelable
import com.axxess.imagesearch.networking.*
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageSearchResponse(
    val data: List<SearchResults>,
    val success: Boolean,
    val status: Int
) :
    Parcelable, CacheableResponse() {
    companion object {
        private const val SEARCH_STRING = "q"
        fun createCacheKey(baseUrl: String, searchString: String): String {
            val url = "$baseUrl$IMAGE_SEARCH_URL"
            val map = HashMap<String, String>()
            map[SEARCH_STRING] = searchString
            return CacheHelper.updateUrlWithMap(url, map)
        }
    }

    override val shortCacheTime: Long
        get() = ONE_HOUR

    override val longCacheTime: Long
        get() = ONE_DAY * 30
}

@Parcelize
data class SearchResults(val id: String, val title: String, val images: List<Image>?) : Parcelable


@Parcelize
data class Image(
    @SerializedName("id")
    val imgId: String,
    val views: String?,
    val link: String
) : Parcelable