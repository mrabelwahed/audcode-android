package com.audcode.data.network.response

import com.google.gson.annotations.SerializedName

data class UserRes(val body: UserResponse)

data class UserResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("email")
    val email: String
)

data class AuthRes(val body: AuthObject)

data class AuthObject(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user : UserResponse
)