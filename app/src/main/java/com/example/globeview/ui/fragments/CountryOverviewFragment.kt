package com.example.globeview.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.InvalidationTracker
import com.example.globeview.R
import com.example.globeview.adapters.CountriesAdapter
import com.example.globeview.databinding.FragmentCountryOverviewBinding
import com.example.globeview.ui.CountriesActivity
import com.example.globeview.ui.CountriesViewModel
import com.example.globeview.util.Constants
import com.example.globeview.util.Resource

class CountryOverviewFragment : Fragment(R.layout.fragment_country_overview) {

    lateinit var countriesViewModel: CountriesViewModel
    lateinit var countriesAdapter: CountriesAdapter
    lateinit var retryButton: Button
    lateinit var errorText: TextView
    lateinit var itemCountriesError: CardView
    lateinit var binding: FragmentCountryOverviewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCountryOverviewBinding.bind(view)

        itemCountriesError = view.findViewById(R.id.itemCountriesOverviewError)

        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_error, null)

        retryButton = view.findViewById(R.id.retryButton)
        errorText = view.findViewById(R.id.errorText)

        countriesViewModel = (activity as CountriesActivity).countryViewModel
        setupCountriesRecycler()

        countriesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("country", it)
            }
            findNavController().navigate(R.id.action_countryOverviewFragment_to_countryFragment3, bundle)
        }

        countriesViewModel.data.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success<*> -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { countriesResponse ->
                        countriesAdapter.differ.submitList(countriesResponse.data.toList())
                        val totalPages =
                            countriesResponse.meta?.total?.div(Constants.QUERY_PAGE_SIZE)?.plus(2)
                        isLastPage = countriesViewModel.countriesPage == totalPages
                        if (isLastPage) {
                            binding.recyclerCountriesOverview.setPadding(0, 0, 0, 0)
                        }

                    }
                }
                is Resource.Error<*> -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "Something went wrong: $message", Toast.LENGTH_LONG).show()
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading<*> -> {
                    showProgressBar()
                }
            }
        })

        retryButton.setOnClickListener {
            countriesViewModel.getCountries()
        }

    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        itemCountriesError.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        itemCountriesError.visibility = View.VISIBLE
        errorText.text = message
        isError = true
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = false
    }

    val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isLastItem && isNotAtBeginning &&
                        isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                countriesViewModel.getCountries()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupCountriesRecycler() {
        countriesAdapter = CountriesAdapter()
        binding.recyclerCountriesOverview.apply {
            adapter = countriesAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@CountryOverviewFragment.scrollListener)
        }
    }
}