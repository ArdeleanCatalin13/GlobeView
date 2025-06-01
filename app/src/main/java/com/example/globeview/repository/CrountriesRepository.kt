package com.example.globeview.repository

import com.example.globeview.api.RetrofitInstance
import com.example.globeview.db.CountryDatabase
import com.example.globeview.models.CountryResponse
import com.example.globeview.models.Data
import retrofit2.Response
import retrofit2.http.Path

class CrountriesRepository(val db: CountryDatabase) {

    suspend fun getCountries(limit: Int) =
        RetrofitInstance.api.getCountries(limit)

    suspend fun searchForCountries(searchQuery: String): Response<CountryResponse> =
        RetrofitInstance.api.searchForCountries(searchQuery)

    suspend fun upsert(data: Data) = db.getCountryDao().upsert(data)

    fun getFavoriteCountries() = db.getCountryDao().getAllCountries()

    suspend fun deleteCountry(data: Data) = db.getCountryDao().deleteCountry(data)
}