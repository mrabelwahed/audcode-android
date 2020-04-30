package com.audcode.di.module

import com.audcode.AppConst.TIMEOUT_REQUEST
import com.audcode.data.network.AuthInterceptor
import com.audcode.data.network.AudcodeAPI

import com.audcode.di.scope.AppScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
 open class NetworkModule(var baseUrl:String) {
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
            //.addInterceptor(authInterceptor)
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
     fun provideFeedService(builder: Retrofit.Builder) =
        builder.baseUrl(baseUrl).build().create(AudcodeAPI::class.java)
}