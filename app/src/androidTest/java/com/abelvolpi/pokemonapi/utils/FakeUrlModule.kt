package com.abelvolpi.pokemonapi.utils

import com.abelvolpi.pokemonapi.data.di.UrlModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UrlModule::class]
)
object FakeBaseUrlModule {
    @Provides
    fun provideUrl(): String = "http://localhost:8080/"
}
