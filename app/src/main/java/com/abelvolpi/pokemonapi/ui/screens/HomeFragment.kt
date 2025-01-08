package com.abelvolpi.pokemonapi.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abelvolpi.pokemonapi.adapter.HomeAdapter
import com.abelvolpi.pokemonapi.api.ApiResponse
import com.abelvolpi.pokemonapi.databinding.FragmentHomeBinding
import com.abelvolpi.pokemonapi.models.CustomImage
import com.abelvolpi.pokemonapi.models.GenericPokemon
import com.abelvolpi.pokemonapi.utils.Constants.LIMIT_PER_REQUEST
import com.abelvolpi.pokemonapi.viewmodel.HomeViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var pokemonAdapter: HomeAdapter
    private val navController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!::pokemonAdapter.isInitialized)
            pokemonAdapter = HomeAdapter(context = requireContext(), onPokemonClick = ::onPokemonClick)

        initLayout()
        sendRequest(homeViewModel.offSet)
    }

    private fun initLayout() {
        with(binding) {

            homeViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    homeProgressBar.visibility = View.VISIBLE
                    gridRecyclerView.visibility = View.GONE
                } else {
                    homeProgressBar.visibility = View.GONE
                    gridRecyclerView.visibility = View.VISIBLE
                }
            }

            gridRecyclerView.apply {
                layoutManager = GridLayoutManager(requireContext(), 3)
                adapter = pokemonAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                        if (dy > 0) {
                            val visibleCount = (layoutManager as GridLayoutManager).childCount
                            val totalItemCount = (layoutManager as GridLayoutManager).itemCount
                            val pastVisibleItems = (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()

                            if (homeViewModel.loading.value != true) {
                                if ((visibleCount + pastVisibleItems) >= totalItemCount) {
                                    sendRequest(homeViewModel.offSet)
                                }
                            }
                            super.onScrolled(recyclerView, dx, dy)
                        }
                    }
                })
            }
        }
    }

    private fun sendRequest(offSet: Int) {
        homeViewModel.getPokemonList(offSet, LIMIT_PER_REQUEST).observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Success -> {
                    pokemonAdapter.addMorePokemon(response.body.results)
                    homeViewModel.offSet += LIMIT_PER_REQUEST
                }
                is ApiResponse.Failure -> {
                    Log.e("HOME_ERROR", response.exception.toString())
                }
            }
        }
    }

    private fun onPokemonClick(genericPokemon: GenericPokemon?, pokemonImageBitmap: CustomImage?) {
        navController.navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailsFragment(pokemonImageBitmap, genericPokemon)
        )
    }

}