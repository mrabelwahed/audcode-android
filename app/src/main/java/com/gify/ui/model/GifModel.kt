package com.gify.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class GifModel(
    val id: String,
    val title: String,
    val previewGifUrl: String,
    val originalGifUrl: String,
    val type: Int = 0
) : Parcelable