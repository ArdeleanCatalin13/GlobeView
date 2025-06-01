package com.example.globeview.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.globeview.R
import com.example.globeview.databinding.FragmentCountryBinding
import com.example.globeview.ui.CountriesActivity
import com.example.globeview.ui.CountriesViewModel
import com.google.android.material.snackbar.Snackbar

class CountryFragment : Fragment(R.layout.fragment_country) {

    lateinit var countriesViewModel: CountriesViewModel
    val args: CountryFragmentArgs by navArgs()
    lateinit var binding: FragmentCountryBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCountryBinding.bind(view)

        countriesViewModel = (activity as CountriesActivity).countryViewModel
        val country = args.country

        country.href?.flag?.let { flagUrl ->
            Glide.with(this@CountryFragment)
                .load(flagUrl)
                .into(binding.imageViewFlag)
        } ?: run {

        }

        // --- Country Names ---
        binding.textViewCountryName.text = country.name
        binding.textViewFullName.text = country.full_name ?: ""
        binding.textViewFullName.isVisible = !country.full_name.isNullOrEmpty()


        // --- Key Facts ---
        binding.textViewCapitalValue.text = country.capital ?: getString(R.string.data_not_available)
        binding.textViewContinentValue.text = country.continent ?: getString(R.string.data_not_available)
        binding.textViewCurrencyValue.text = country.currency ?: getString(R.string.data_not_available)
        binding.textViewPopulationValue.text = country.population ?: getString(R.string.data_not_available)
        binding.textViewSizeValue.text = country.size ?: getString(R.string.data_not_available)

        // --- Current President ---
        country.current_president?.let { president ->
            binding.cardPresident.isVisible = true
            binding.textViewPresidentName.text = president.name ?: getString(R.string.data_not_available)

            val genderDisplay = president.gender?.let { rawDate -> rawDate
            } ?: getString(R.string.data_not_available)

            val appointmentDateDisplay = president.appointment_start_date?.let { rawDate -> rawDate
            } ?: getString(R.string.data_not_available)

            binding.textViewPresidentAppointment.text = getString(R.string.in_office_since, appointmentDateDisplay)
            binding.textViewPresidentGender.text = getString(R.string.gender_label, genderDisplay)


            president.href?.picture?.let { pictureUrl ->
                Glide.with(this@CountryFragment)
                    .load(pictureUrl)
                    .placeholder(R.drawable.baseline_person_24)
                    .error(R.drawable.baseline_person_24)
                    .circleCrop()
                    .into(binding.imageViewPresident)
                binding.imageViewPresident.isVisible = true
            } ?: run {
                binding.imageViewPresident.isVisible = false
            }
        } ?: run {
            binding.cardPresident.isVisible = false
        }

        binding.fab.setOnClickListener {
            countriesViewModel.addToFavorites(country)
            Snackbar.make(view, "Country added to favorites", Snackbar.LENGTH_SHORT).show()
        }
    }

//    private fun formatDate(dateString: String?, outputPattern: String = "yyyy-MM-dd"): String? {
//        if (dateString.isNullOrEmpty()) return null
//        return try {
//            // Attempt to parse ISO 8601 format like "2020-12-01T08:35:51.000000Z"
//            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
//                androidx.compose.ui.text.intl.Locale.getDefault())
//            inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Assuming Z means UTC
//            val date = inputFormat.parse(dateString)
//
//            val outputFormat = SimpleDateFormat(outputPattern, androidx.compose.ui.text.intl.Locale.getDefault())
//            // outputFormat.timeZone = TimeZone.getDefault() // Convert to local timezone if needed
//            date?.let { outputFormat.format(it) }
//        } catch (e: Exception) {
//            Log.e("CountryFragment", "Error parsing date: $dateString", e)
//            // Fallback for simpler date strings like "2020-03-09"
//            try {
//                val inputFormatSimple = SimpleDateFormat("yyyy-MM-dd",
//                    androidx.compose.ui.text.intl.Locale.getDefault())
//                val dateSimple = inputFormatSimple.parse(dateString)
//                val outputFormatSimple = SimpleDateFormat(outputPattern,
//                    androidx.compose.ui.text.intl.Locale.getDefault())
//                dateSimple?.let { outputFormatSimple.format(it) }
//            } catch (e2: Exception) {
//                dateString // Return original if all parsing fails
//            }
//        }
//    }
}