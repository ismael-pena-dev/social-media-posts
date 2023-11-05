package com.pena.ismael.socialmediapager.core.networking

sealed class NetworkResult<T> {
    data class Success<T>(
        val data: T
    ): NetworkResult<T>()
    data class Error<T>(
        val message: String?,
        val code: Int? = null
    ): NetworkResult<T>()
}