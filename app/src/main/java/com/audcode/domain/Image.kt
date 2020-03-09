package com.audcode.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.chipotie.gifinder.model.OriginalGif
import io.chipotie.gifinder.model.PreviewGif
import kotlinx.android.parcel.Parcelize

/*
 * @author savirdev on 25/03/19
 */

@Parcelize
data class Image(
    @SerializedName("preview_gif")
    val previewGif: PreviewGif,

    @SerializedName("original")
    val originalGif: OriginalGif,

    @SerializedName("fixed_height")
    val downsizedLargeGif: DownsizedLargeGif
) : Parcelable