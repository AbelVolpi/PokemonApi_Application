package com.abelvolpi.pokemonapi

import android.app.Application
import android.content.Context

class MainApplication : Application() {

    init {
        mainApplicationInstance = this
    }

    companion object {
        private lateinit var mainApplicationInstance: Application

        fun applicationContext(): Context {
            return mainApplicationInstance.applicationContext
        }
    }

}