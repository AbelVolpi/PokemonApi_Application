package com.abelvolpi.pokemonapi.utils

import android.content.Context
import android.widget.ImageView
import com.abelvolpi.pokemonapi.utils.Constants.URLS.PNG
import com.abelvolpi.pokemonapi.utils.Constants.URLS.POKEMON_IMAGE_URL
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun ImageView.setImageUsingGlide(context: Context, pokemonNumber: String) {
    Glide.with(context)
        .load(POKEMON_IMAGE_URL + pokemonNumber + PNG)
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}