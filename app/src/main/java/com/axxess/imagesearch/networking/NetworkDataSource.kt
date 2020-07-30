package com.axxess.imagesearch.networking

import android.content.Context
import android.util.Log
import com.axxess.imagesearch.common.model.ApiError
import com.axxess.imagesearch.common.model.ApiResponse
import com.axxess.imagesearch.common.util.DateFormat
import com.axxess.imagesearch.common.util.Either
import com.axxess.imagesearch.common.util.GsonUTCDateAdapter
import com.axxess.imagesearch.networking.imagesearch.ImageSearchResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import java.util.*
import javax.inject.Inject

/**
 * Base class for all data sources to perform common functionality i.e. executing network call,
 * handling network response and passing on error or success response to use cases
 * @author Anand Shinde
 */
abstract class NetworkDataSource {

    @Inject
    lateinit var networkUtil: NetworkUtil

    val TAG = javaClass.simpleName

    fun <T, R> request(call: Call<T>): Either<Failure, R> {
        return try {
            if (networkUtil.isConnected()) {
                val response = call.execute()
                if (response.isSuccessful) {
                    val response1: R? = handleApiSuccess(response.body())
                    if (response1 != null) {
                        Either.Right(response1, false)
                    } else {
                        Either.Left(
                            Failure.ServerError("Parsing error", java.lang.Exception()),
                            false
                        )
                    }
                } else {
                    Either.Left(
                        handleApiFailure(
                            response.errorBody()?.string(),
                            response.code()
                        ), false
                    )
                }
            } else {
                Either.Left(Failure.NetworkConnection(), false)
            }
        } catch (exception: Exception) {
            Either.Left(Failure.ServerError(exception.message, exception), false)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T, R> handleApiSuccess(t: T): R? {
        return when (t) {
            is ApiResponse<*> -> {
                if (t.data is CacheableResponse) {
                    t.data.dateCreatedTimeStamp = System.currentTimeMillis()
                }
                t.data as? R ?: t as? R?
            }
            is ImageSearchResponse -> {
                if (t is CacheableResponse) {
                    t.dateCreatedTimeStamp = System.currentTimeMillis()
                }
                t as R
            }
            else -> null
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun handleApiFailure(errorResponseJson: String?, errorResponseCode: Int): Failure {
        if (!errorResponseJson.isNullOrEmpty()) try {
            val gson: Gson = GsonBuilder()
                .setDateFormat(DateFormat.ZULU.it)
                .registerTypeAdapter(Date::class.java, GsonUTCDateAdapter())
                .setLenient()
                .create()
            val errorResponse = JsonParser().parse(errorResponseJson).asJsonObject
            val errorMessage = errorResponse.get("message").asString
            val jsonObject = errorResponse.get("errors").asJsonArray
            val statusCode = errorResponse.get("status").asInt
            val serviceErrors = gson.fromJson(jsonObject, Array<ApiError>::class.java)
            return Failure.ServerError(serviceErrors, errorMessage, statusCode)
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "invalid json syntax on error response")
        }

        if (errorResponseCode == 404) {
            return Failure.Network404Error()
        }

        return Failure.ServerError("Server returned error", Exception())
    }
}