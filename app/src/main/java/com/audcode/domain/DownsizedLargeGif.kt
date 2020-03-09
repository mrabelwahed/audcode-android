package com.audcode.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/*
 * @author savirdev on 26/03/19
 */
@Parcelize
data class DownsizedLargeGif(

    @SerializedName("url")
    val url : String
) : Parcelable