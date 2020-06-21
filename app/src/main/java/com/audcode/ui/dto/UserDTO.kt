package com.audcode.ui.dto

import com.google.gson.annotations.SerializedName

class UserDTO(
    @SerializedName("fullName")
    val fullName:String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

