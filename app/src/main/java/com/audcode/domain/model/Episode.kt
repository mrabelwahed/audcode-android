package com.audcode.domain.model

data class Episode(
    val id: String,
    val name: String,
    val createdAt: String,
    val content: String?,
    val contentUrl:String?,
    val author: String,
    val tags: ArrayList<String>,
    val url: String
)