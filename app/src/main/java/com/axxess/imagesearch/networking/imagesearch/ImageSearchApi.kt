package com.axxess.imagesearch.networking.imagesearch

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val IMAGE_SEARCH_URL = "3/gallery/search/1"
const val QUERY_PARAM = "q"

interface ImageSearchApi {
    @GET(IMAGE_SEARCH_URL)
    fun searchImages(@Query(QUERY_PARAM) searchString: String): Call<ImageSearchResponse>
}