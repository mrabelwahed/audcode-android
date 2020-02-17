package io.chipotie.gifinder.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/*
 * @author savirdev on 25/03/19
 */

@Parcelize
data class PreviewGif(

    @SerializedName("url")
    val url: String,

    @SerializedName("width")
    val width: String,

    @SerializedName("height")
    val height: String,

    @SerializedName("size")
    val size: String
) : Parcelable