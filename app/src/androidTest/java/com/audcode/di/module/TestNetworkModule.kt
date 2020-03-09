package com.audcode.di.module

import com.audcode.data.network.GifAPI
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import retrofit2.Retrofit

@Module
class TestNetworkModule(baseUrl:String) :NetworkModule(baseUrl){
    @Provides
     fun providService(builder: Retrofit.Builder) = Mockito.mock(GifAPI::class.java)

}