package com.audcode.data.network

import com.audcode.data.network.response.EpisodeRes
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query


interface EpisodeApi {
    @GET("/api/v1/episode")
    fun getEpisodes(@Query("query") query: String? ,  @Query("skip") skip:Long, @Query("limit") limit:Int):Flowable<EpisodeRes>
}