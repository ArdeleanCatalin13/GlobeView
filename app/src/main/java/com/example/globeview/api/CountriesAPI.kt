package com.example.globeview.api

import com.example.globeview.models.CountriesResponse
import com.example.globeview.models.CountryResponse
import com.example.globeview.util.Constants
import com.example.globeview.util.Constants.Companion.QUERY_PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface CountriesAPI {
    @Headers("Authorization: Bearer ${Constants.API_KEY}")
    @GET("api/v1/countries")
    suspend fun getCountries(
        @Query("per_page")
        per_page: Int = QUERY_PAGE_SIZE
    ): Response<CountriesResponse>

    @Headers("Authorization: Bearer ${Constants.API_KEY}")
    @GET("api/v1/countries/{country}")
    suspend fun searchForCountries(@Path("country") searchQuery: String): Response<CountryResponse>

}