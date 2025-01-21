package com.abelvolpi.pokemonapi.presentation.screens.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abelvolpi.pokemonapi.presentation.UiState
import com.abelvolpi.pokemonapi.presentation.adapters.HomeAdapter
import com.abelvolpi.pokemonapi.presentation.adapters.SpacesItemDecoration
import com.abelvolpi.pokemonapi.presentation.databinding.FragmentHomeBinding
import com.abelvolpi.pokemonapi.presentation.models.CustomImage
import com.abelvolpi.pokemonapi.presentation.models.GenericPokemonUiModel
import com.abelvolpi.pokemonapi.presentation.screens.BaseFragment
import com.abelvolpi.pokemonapi.presentation.utils.Constants.LIMIT_PER_REQUEST
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var pokemonAdapter: HomeAdapter
    private val navController by lazy {
        findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sendRequest(homeViewModel.offSet)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!::pokemonAdapter.isInitialized) {
            pokemonAdapter =
                HomeAdapter(onPokemonClick = ::onPokemonClick)
        }
        initLayout()
        verifyExistingData(savedInstanceState)
        observePokemonListState()
    }

    private fun verifyExistingData(savedInstanceState: Bundle?) {
        if (homeViewModel.pokemonList.isNotEmpty() && savedInstanceState != null) {
            pokemonAdapter.addMorePokemon(homeViewModel.pokemonList)
        }
    }

    private fun initLayout() {
        with(binding) {
            val spacingInPixels = 10
            gridRecyclerView.addItemDecoration(SpacesItemDecoration(spacingInPixels))

            postponeEnterTransition()
            gridRecyclerView.doOnPreDraw {
                startPostponedEnterTransition()
            }
            gridRecyclerView.apply {
                layoutManager = GridLayoutManager(requireContext(), 3)
                adapter = pokemonAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0) {
                            val visibleCount = (layoutManager as GridLayoutManager).childCount
                            val totalItemCount = (layoutManager as GridLayoutManager).itemCount
                            val pastVisibleItems =
                                (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()

                            if (homeViewModel.newPokemonsListState.value != UiState.Loading &&
                                (visibleCount + pastVisibleItems) >= totalItemCount
                            ) {
                                sendRequest(homeViewModel.offSet)
                            }
                            super.onScrolled(recyclerView, dx, dy)
                        }
                    }
                })
            }
        }
    }

    private fun observePokemonListState() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.newPokemonsListState.collect { state ->
                when (state) {
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        handleSuccessState(state.data.results)
                    }
                    is UiState.Failure -> {
                        Log.e("HOME_ERROR", state.exception.toString())
                    }
                }
            }
        }
    }

    private fun handleSuccessState(pokemonList: List<GenericPokemonUiModel>) {
        if (verifyIsNewPokemonBatch(pokemonList)) {
            pokemonAdapter.addMorePokemon(pokemonList)
            homeViewModel.offSet += LIMIT_PER_REQUEST
        }
    }

    private fun verifyIsNewPokemonBatch(results: List<GenericPokemonUiModel>): Boolean {
        val firstPokemon = results.firstOrNull() ?: return false
        return homeViewModel.pokemonList.none { it.number == firstPokemon.number }
    }

    private fun sendRequest(offSet: Int) {
        homeViewModel.fetchPokemonList(offSet, LIMIT_PER_REQUEST)
    }

    private fun onPokemonClick(
        genericPokemonUiModel: GenericPokemonUiModel?,
        pokemonImageBitmap: CustomImage?,
        imageView: ImageView
    ) {
        val extras = FragmentNavigatorExtras(
            imageView to (genericPokemonUiModel?.number ?: "")
        )
        navController.navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                pokemonImageBitmap,
                genericPokemonUiModel
            ),
            extras
        )
    }
}
