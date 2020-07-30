package com.axxess.imagesearch.networking

import java.util.HashMap

object CacheHelper {
    /**
     * Function to create and return cache key with supplied params
     * @param url: End point of API
     * @param map: params associated with API
     */
    fun updateUrlWithMap(url: String, map: HashMap<String, String>): String {
        var updatedUrl = url
        var queryParameters = ""

        map.entries.forEach {
            if (updatedUrl.contains("{${it.key}}")) {
                updatedUrl = updatedUrl.replace("{${it.key}}", it.value, true)
            } else {
                queryParameters += it.key + "=" + it.value + "&"
            }
        }

        if (queryParameters.isNotEmpty() && queryParameters.endsWith("&")) {
            queryParameters = "?${queryParameters.substring(0, queryParameters.length - 1)}"
        }

        return updatedUrl + queryParameters
    }
}