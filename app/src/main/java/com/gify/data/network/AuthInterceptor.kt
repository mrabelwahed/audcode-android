package com.gify.data.network

import android.R
import com.gify.AppConst.keys.API_KEY
import com.gify.AppConst.keys.API_KEY_VALUE
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val originalUrl: HttpUrl = original.url()
        val url = originalUrl.newBuilder()
            .addQueryParameter(API_KEY, API_KEY_VALUE)
            .build()
        val requestBuilder: Request.Builder = original.newBuilder().url(url)
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }

}