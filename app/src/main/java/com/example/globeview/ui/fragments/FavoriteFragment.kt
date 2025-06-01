package com.example.globeview.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.globeview.R
import com.example.globeview.adapters.CountriesAdapter
import com.example.globeview.databinding.FragmentFavoriteBinding
import com.example.globeview.ui.CountriesActivity
import com.example.globeview.ui.CountriesViewModel
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    lateinit var countriesViewModel: CountriesViewModel
    lateinit var countriesAdapter: CountriesAdapter
    lateinit var binding: FragmentFavoriteBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)

        countriesViewModel = (activity as CountriesActivity).countryViewModel
        setupFavoritesRecycler()

        countriesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("country", it)
            }
            findNavController().navigate(R.id.action_favoriteFragment_to_countryFragment3, bundle)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return  true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val country = countriesAdapter.differ.currentList[position]
                countriesViewModel.deleteCountry(country)
                Snackbar.make(view, "Successfully deleted country", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        countriesViewModel.addToFavorites(country)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerFavourites)
        }

        countriesViewModel.getFavoriteCountries().observe(viewLifecycleOwner, Observer { countries ->
            countriesAdapter.differ.submitList(countries)
        })
    }

    private fun setupFavoritesRecycler() {
        countriesAdapter = CountriesAdapter()
        binding.recyclerFavourites.apply {
            adapter = countriesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}