package com.audcode.ui.login.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    val id: String,
    val email: String,
    var authToken : String? = null
) : Parcelable