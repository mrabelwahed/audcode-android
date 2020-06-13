package com.audcode.ui.home.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EpisodeModel(
    val id: String,
    val name: String,
    val createdAt: String,
    val content: String?,
    val contentUrl: String?,
    val author: String,
    val tags: ArrayList<String>,
    var isPlaying: Boolean = false,
    var isSaved:Boolean = false,
    var url: String = ""

) : Parcelable

