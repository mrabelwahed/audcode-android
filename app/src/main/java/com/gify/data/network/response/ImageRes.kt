package com.gify.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.chipotie.gifinder.model.OriginalGif
import io.chipotie.gifinder.model.PreviewGif
import kotlinx.android.parcel.Parcelize

/*
 * @author savirdev on 25/03/19
 */

@Parcelize
data class ImageRes(
    @SerializedName("preview_gif")
    val previewGif: PreviewGif,

    @SerializedName("original")
    val originalGif: OriginalGif,

    @SerializedName("fixed_height")
    val downsizedLargeGif: DownsizedLargeGifRes
) : Parcelable