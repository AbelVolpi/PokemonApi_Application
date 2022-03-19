package com.abelvolpi.pokemonapi.api

sealed class ApiResponse<T : Any> {
    data class Success<T : Any>(val body: T) : ApiResponse<T>()
    data class Failure<T : Any>(val exception: Throwable) : ApiResponse<T>()
}
