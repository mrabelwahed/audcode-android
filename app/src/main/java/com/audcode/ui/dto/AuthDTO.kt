package com.audcode.ui.dto

import com.google.gson.annotations.SerializedName

class AuthDTO(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

