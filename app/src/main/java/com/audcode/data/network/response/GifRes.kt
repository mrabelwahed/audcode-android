package com.audcode.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class GifRes(

    @SerializedName("type")
    val type : String,

    @SerializedName("id")
    val id : String,

    @SerializedName("url")
    val url : String,

    @SerializedName("username")
    val username : String,

    @SerializedName("rating")
    val rating : String,

    @SerializedName("images")
    val images: ImageRes,

    @SerializedName("title")
    val title: String

) : Parcelable