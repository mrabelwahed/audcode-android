package com.audcode.domain.model

data class User(
    val id: String?,
    val fullName:String,
    val email: String,
    var token:String? = null
)