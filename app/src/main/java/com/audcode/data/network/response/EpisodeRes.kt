package com.audcode.data.network.response

import com.google.gson.annotations.SerializedName

data class EpisodeRes(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("body")
    val body: ArrayList<EpisodeItem>
)

data class EpisodeItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("tags")
    val tags: ArrayList<String>,
    @SerializedName("content")
    val content: String,
    @SerializedName("url")
    val url: String
)
