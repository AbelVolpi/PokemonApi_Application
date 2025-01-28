package com.abelvolpi.pokemonapi.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.platform.app.InstrumentationRegistry

object UiTestUtils {

    fun loadBitmapFromAssets(fileName: String): Bitmap {
        val context = InstrumentationRegistry.getInstrumentation().context
        context.assets.open(fileName).use { inputStream ->
            return BitmapFactory.decodeStream(inputStream)
        }
    }

    fun getJsonContent(fileName: String): String {
        val context = InstrumentationRegistry.getInstrumentation().context
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}