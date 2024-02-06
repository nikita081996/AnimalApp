package com.example.animalsapp.data

sealed class Result<out T>() {
    object Hidden : Result<Nothing>()
    object Loading : Result<Nothing>()
    class Error(val meta: String? = null, val throwable: Throwable? = null) : Result<Nothing>()
    class Success<T>(val data: T? = null) : Result<T>()
}
