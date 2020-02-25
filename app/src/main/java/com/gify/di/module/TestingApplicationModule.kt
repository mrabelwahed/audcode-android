package com.gify.di.module

import com.gify.AppConst
import com.gify.data.network.GifAPI
import retrofit2.Retrofit

class TestingApplicationModule : NetworkModule() {
    override fun provideFeedService(builder: Retrofit.Builder) =
        builder.baseUrl(AppConst.BASE_URL).build().create(GifAPI::class.java)

}