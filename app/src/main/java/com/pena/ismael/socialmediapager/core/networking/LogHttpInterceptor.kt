package com.pena.ismael.socialmediapager.core.networking

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class LogHttpInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        Log.d(
            TAG,
            "Req: ${request.url} -> Resp: ${response.code}}"
        )
        return response
    }

    companion object {
        private val TAG = LogHttpInterceptor::javaClass.name
    }

}