package com.dicoding.consumerapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite(
    var username: String? = null,
    var name: String? = null,
    var photoUser: String? = null
) : Parcelable