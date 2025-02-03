package com.abelvolpi.pokemonapi.presentation

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    object Finished : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Failure<out T>(val exception: Throwable) : UiState<T>()
}
