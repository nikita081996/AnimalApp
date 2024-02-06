package com.example.animalsapp.data

import retrofit2.Response

/**
 * This Similar to convertResponseToResult on Rx Observable in Mapper.kt.
 * But for Coroutines flow.
 */

typealias Transform<T, R> = suspend (T?) -> R?

class FlowResponseMapper<T : Any, R : Any>(val transform: Transform<T, R>) {

    suspend operator fun invoke(response: Response<T>): Result<R> {
        val responseBody = response.body()
        return when {
            response.isSuccessful.not() -> {
                this.handleApiError(response = response)
            }

            else -> Result.Success(
                data = transform(responseBody)
            )
        }
    }

    private fun handleApiError(response: Response<T>): Result<R> {
        val responseBody = response.body()
        return when {
            responseBody != null -> {
                Result.Error(
                    meta = responseBody.toString(),
                    throwable = Throwable(responseBody.toString())
                )
            }

            response.errorBody() != null -> {
                Result.Error(
                    meta = "something_went_wrong",
                    throwable = Throwable(response.errorBody().toString())
                )
            }

            else -> Result.Error(
                meta = "something_went_wrong",
                throwable = Throwable("something_went_wrong")
            )
        }
    }
}