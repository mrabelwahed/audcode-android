package com.gify.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Gif(val id : String, val title: String, val previewGifUrl : String , val originalGifUrl:String) : Parcelable