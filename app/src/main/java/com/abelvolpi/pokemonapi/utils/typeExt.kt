package com.abelvolpi.pokemonapi.utils

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.abelvolpi.pokemonapi.R

// // TODO verificar se isso otimiza usando um hashmap?
fun String.parseTypeToColor(context: Context): Int {
    return when (this) {
        "bug" -> ContextCompat.getColor(context, R.color.bug_color)
        "dark" -> ContextCompat.getColor(context, R.color.dark_color)
        "dragon" -> ContextCompat.getColor(context, R.color.dragon_color)
        "electric" -> ContextCompat.getColor(context, R.color.electric_color)
        "fairy" -> ContextCompat.getColor(context, R.color.fairy_color)
        "fighting" -> ContextCompat.getColor(context, R.color.fighting_color)
        "fire" -> ContextCompat.getColor(context, R.color.fire_color)
        "flying" -> ContextCompat.getColor(context, R.color.flying_color)
        "ghost" -> ContextCompat.getColor(context, R.color.ghost_color)
        "grass" -> ContextCompat.getColor(context, R.color.grass_color)
        "ground" -> ContextCompat.getColor(context, R.color.ground_color)
        "ice" -> ContextCompat.getColor(context, R.color.ice_color)
        "normal" -> ContextCompat.getColor(context, R.color.normal_color)
        "poison" -> ContextCompat.getColor(context, R.color.poison_color)
        "psychic" -> ContextCompat.getColor(context, R.color.psychic_color)
        "rock" -> ContextCompat.getColor(context, R.color.rock_color)
        "steel" -> ContextCompat.getColor(context, R.color.steel_color)
        "water" -> ContextCompat.getColor(context, R.color.water_color)
        else -> Color.BLACK
    }
}
