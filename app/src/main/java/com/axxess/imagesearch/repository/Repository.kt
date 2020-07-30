package com.axxess.imagesearch.repository

import com.axxess.imagesearch.common.util.Either
import com.axxess.imagesearch.networking.CacheableResponse
import com.axxess.imagesearch.networking.Failure
import com.axxess.imagesearch.storage.DatabaseDataSource
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Named

/**
 * Repository to hook application with servers or local database
 * @author Anand Shinde
 */
const val API_BASE_URL_PARAM = "apiBaseUrl"

abstract class Repository {
    @Inject
    @field:Named(API_BASE_URL_PARAM)
    lateinit var apiBaseUrl: String

    @Inject
    lateinit var databaseDS: DatabaseDataSource

    suspend inline fun <reified T : CacheableResponse> fetchData(
        key: String,
        networkCall: () -> Either<Failure, T>,
        forceRefresh: Boolean = false,
        crossinline onResult: suspend (Either<Failure, T>) -> Unit
    ) {

        // first get the data from cache if we not forcing refresh
        val cachedData: T? = if (!forceRefresh) databaseDS.find(key) else null

        // if there is any data && forceRefresh from cache is false in the cache then update the UI
        if (cachedData != null && !cachedData.forceRefresh)
            onResult(Either.Right(cachedData, cachedData.isLongCacheData()))

        /*
        if any of the below conditions meet then we fire a network call to get the latest data
            1) force refresh is true
            2) there is no cached data
            3) cached data is from long cache
            4) cached forceRefresh is true
        */
        if (forceRefresh || cachedData == null || cachedData.isLongCacheData() || cachedData.forceRefresh) {

            if (cachedData == null || cachedData.forceRefresh)
                onResult(Either.Loading)

            val networkData = networkCall()

            /* small delay to be added to prevent the loss of data in some cases
                please refer to below article to know more
                https://medium.com/@hanyuliu/livedata-may-lose-data-2fffdac57dc9

                - in our app this case arises when we have long cache data and the network connection is off
                - onResult of success due to cache data and onResult of failure due to network connection happens
                  so fast that it updates the underlying livedata at the usecase level twice before the value is dispached to the observer
                - because of this observer will be notified only the failure state which results in unpredictable UI state
             */

            delay(100)

            /*
                - if network call fails, update with Failure with cache flag set depending on
                  whether cached data is present
                - else update the data in db and respond with success

             */
            if (networkData.isLeft) {
                onResult(Either.Left(networkData.left(), cachedData != null))
            } else {
                databaseDS.save(key, networkData.right())
                onResult(Either.Right(networkData.right(), networkData.right().isLongCacheData()))
            }
        }
    }
}