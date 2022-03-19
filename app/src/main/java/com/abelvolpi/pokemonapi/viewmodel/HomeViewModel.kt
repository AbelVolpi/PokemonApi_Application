package com.abelvolpi.pokemonapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abelvolpi.pokemonapi.api.ApiResponse
import com.abelvolpi.pokemonapi.api.repository.PokemonRepository
import com.abelvolpi.pokemonapi.models.PokemonListResponse
import kotlinx.coroutines.launch

class HomeViewModel : CoroutineViewModel() {

    var offSet: Int = 0

    fun getPokemonList(
        offset: Int? = null,
        limit: Int? = null
    ): LiveData<ApiResponse<PokemonListResponse>> {
        setIsLoading()
        val mutableLiveData = MutableLiveData<ApiResponse<PokemonListResponse>>()

        launch {
            setIsLoading()
            try {
                val pokemonListResponse = PokemonRepository.getPokemonList(offset, limit)
                mutableLiveData.value = ApiResponse.Success(pokemonListResponse)
            } catch (throwable: Throwable) {
                mutableLiveData.value = ApiResponse.Failure(throwable)
            }
            setIsNotLoading()
        }

        setIsNotLoading()
        return mutableLiveData
    }


}