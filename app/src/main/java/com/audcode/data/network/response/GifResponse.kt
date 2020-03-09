package com.audcode.data.network.response

import com.google.gson.annotations.SerializedName

data class GifResponse(

    @SerializedName("data")
    val data: ArrayList<GifRes>

)