package com.example.globeview.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.globeview.R
import com.example.globeview.adapters.CountriesAdapter
import com.example.globeview.databinding.ActivityCountriesBinding
import com.example.globeview.db.CountryDatabase
import com.example.globeview.repository.CrountriesRepository

class CountriesActivity : AppCompatActivity() {

    lateinit var countryViewModel: CountriesViewModel
    lateinit var binding: ActivityCountriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        binding = ActivityCountriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val countriesRepository = CrountriesRepository(CountryDatabase(this))
        val viewModelProviderFactory = CountriesViewModelProviderFactory(application, countriesRepository)
        countryViewModel = ViewModelProvider(this, viewModelProviderFactory).get(CountriesViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.countriesNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}