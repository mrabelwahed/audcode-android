package com.audcode.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Gif(val id : String, val title: String, val previewGifUrl : String , val originalGifUrl:String) : Parcelable