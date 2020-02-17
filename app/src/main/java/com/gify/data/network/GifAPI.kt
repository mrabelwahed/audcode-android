package com.gify.data.network

import com.gify.data.network.response.GifResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query


interface GifAPI {
    @GET("v1/gifs/search")
    fun search( @Query("q") query: String, @Query("limit") limit: Int, @Query("offset") offset: Long): Flowable<GifResponse>
}