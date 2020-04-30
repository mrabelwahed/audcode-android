package com.audcode.data.network.response

import com.google.gson.annotations.SerializedName

data class UserRes (val body : UserResponse)

data class UserResponse(
    @SerializedName("id")
    val id : String,
    @SerializedName("email")
    val email:String
)