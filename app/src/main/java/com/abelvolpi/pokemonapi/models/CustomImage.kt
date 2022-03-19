package com.abelvolpi.pokemonapi.models

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomImage(
    val image: Bitmap
) : Parcelable
