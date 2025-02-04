package com.abelvolpi.pokemonapi.presentation.screens.home

import android.os.Bundle
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
        sendRequest()
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
        binding?.run {
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
                                sendRequest()
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
                        populateUI(state.data.results)
                    }
                    is UiState.Failure -> {
                        showErrorMessage()
                    }
                }
            }
        }
    }

    private fun populateUI(pokemonList: List<GenericPokemonUiModel>) {
        pokemonAdapter.addMorePokemon(pokemonList)
    }

    private fun showErrorMessage() {
        binding?.run {
            errorMessageTextView.visibility = View.VISIBLE
        }
    }

    private fun sendRequest() {
        homeViewModel.fetchPokemonList()
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
