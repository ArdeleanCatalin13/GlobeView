package com.example.globeview.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.globeview.repository.CrountriesRepository

class CountriesViewModelProviderFactory(val app: Application, val countriesRepository: CrountriesRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CountriesViewModel(app, countriesRepository) as T
    }

}