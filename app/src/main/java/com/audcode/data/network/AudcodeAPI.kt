package com.audcode.data.network

import com.audcode.data.network.response.AuthRes
import com.audcode.data.network.response.EpisodeRes
import com.audcode.data.network.response.UserRes
import com.audcode.ui.dto.AuthDTO
import com.audcode.ui.dto.UserDTO
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface AudcodeAPI {
    @GET("/api/v1/episode")
    fun getEpisodes(
        @Query("query") query: String?,
        @Query("skip") skip: Long,
        @Query("limit") limit: Int
    ): Flowable<EpisodeRes>

    @POST("/api/v1/user/signup")
    fun createUser(@Body userDTO: UserDTO): Flowable<UserRes>

    @POST("/api/v1/user/login")
    fun authenticateUser(@Body authDTO: AuthDTO): Flowable<AuthRes>
}