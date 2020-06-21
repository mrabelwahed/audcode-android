package com.audcode.data.network.response

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body

data class UserRes(@SerializedName("body") val body: UserResponse)

data class UserResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("email")
    val email: String
)

data class AuthRes(@SerializedName("body") val body: AuthObject)

data class AuthObject(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user : UserAuthResponse
)

data class UserAuthResponse(
    @SerializedName("_id")
    val id: String?,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("email")
    val email: String
)