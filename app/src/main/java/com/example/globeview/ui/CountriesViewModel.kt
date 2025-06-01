package com.example.globeview.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.globeview.models.CountriesResponse
import com.example.globeview.models.CountryResponse
import com.example.globeview.models.Data
import com.example.globeview.repository.CrountriesRepository
import com.example.globeview.util.Constants
import com.example.globeview.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CountriesViewModel(app: Application, val countriesRepository: CrountriesRepository): AndroidViewModel(app) {
    val data: MutableLiveData<Resource<CountriesResponse>> = MutableLiveData()
    var countriesPage = 1
    var countriesResponse: CountriesResponse? = null

    val searchCountries: MutableLiveData<Resource<CountryResponse>> = MutableLiveData()
    var searchCountriesResponse: CountryResponse? = null
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null

    init {
        getCountries()
    }

    fun getCountries() = viewModelScope.launch {
        countriesOverviewInternet()
    }

    fun searchCountries(searchQuery: String) = viewModelScope.launch {
        searchCountriesNetwork(searchQuery)
    }

    private fun handleCountryResponse(response: Response<CountriesResponse>): Resource<CountriesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                countriesPage++
                if (countriesResponse == null) {
                    countriesResponse = resultResponse
                } else {
                    val oldCountries = countriesResponse?.data
                    val newCountries = resultResponse.data
                    oldCountries?.addAll(newCountries)
                }
                return Resource.Success(countriesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchCountryResponse(response: Response<CountryResponse>): Resource<CountryResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (searchCountriesResponse == null || newSearchQuery != oldSearchQuery) {
                    oldSearchQuery = newSearchQuery
                } else {
                    val oldCountries = MutableLiveData(searchCountriesResponse?.data)
                    val newCountries = resultResponse.data
                    oldCountries.postValue(newCountries)
                }
                return Resource.Success(searchCountriesResponse ?: resultResponse)
            }
        }
        if(response.code() != 404) {
            return Resource.Error(
                response.message()
            )
        }
        return Resource.Loading()
    }

    fun addToFavorites(data: Data) = viewModelScope.launch {
        countriesRepository.upsert(data)
    }

    fun getFavoriteCountries() = countriesRepository.getFavoriteCountries()

    fun deleteCountry(data: Data) = viewModelScope.launch {
        countriesRepository.deleteCountry(data)
    }

    fun internetConnection(context: Context): Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            hasTransport(android.net.NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }

    private suspend fun countriesOverviewInternet() {
        data.postValue(Resource.Loading())
        try {
            if (internetConnection(this.getApplication())) {
                val response = countriesRepository.getCountries(Constants.QUERY_PAGE_SIZE)
                data.postValue(handleCountryResponse(response))
            } else {
                data.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> data.postValue(Resource.Error("Unable to connect"))
                else -> data.postValue(Resource.Error("No signal"))
            }
        }
    }

    private suspend fun searchCountriesNetwork(searchQuery: String) {
        newSearchQuery = searchQuery
        searchCountries.postValue(Resource.Loading())
        try {
            if (internetConnection(this.getApplication())) {
                val response = countriesRepository.searchForCountries(searchQuery)
                searchCountries.postValue(handleSearchCountryResponse(response))
            } else {
                searchCountries.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> searchCountries.postValue(Resource.Error("Unable to connect"))
                else -> searchCountries.postValue(Resource.Error("No signal"))
            }
        }
    }
}