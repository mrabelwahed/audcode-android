package com.gify.di.module

import com.gify.AppConst.BASE_URL
import com.gify.AppConst.TIMEOUT_REQUEST
import com.gify.data.network.AuthInterceptor

import com.gify.di.scope.AppScope
import com.gify.data.network.GifAPI
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
open class NetworkModule {
    @AppScope
    @Provides
    fun provideHttpLogging(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @AppScope
    @Provides
    fun provideAuthInterceptor():AuthInterceptor{
        return  AuthInterceptor()
    }
    @AppScope
    @Provides
    fun provideOkhttpClient(interceptor: HttpLoggingInterceptor , authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(interceptor)
            .connectTimeout(TIMEOUT_REQUEST, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_REQUEST, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_REQUEST, TimeUnit.SECONDS)
            .build()

    @AppScope
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) =
        Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())


    @AppScope
    @Provides
    open fun provideFeedService(builder: Retrofit.Builder) =
        builder.baseUrl(BASE_URL).build().create(GifAPI::class.java)
}