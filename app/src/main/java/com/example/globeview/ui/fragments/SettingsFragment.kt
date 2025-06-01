package com.example.globeview.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import com.example.globeview.R
import com.example.globeview.adapters.CountriesAdapter
import com.example.globeview.databinding.FragmentSearchBinding
import com.example.globeview.ui.CountriesViewModel

class SettingsFragment : Fragment() {
    private lateinit var darkModeSwitch: SwitchCompat
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        darkModeSwitch = view.findViewById(R.id.switchDarkMode)

        sharedPrefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)

        val isDarkMode = sharedPrefs.getBoolean("dark_mode", false)
        darkModeSwitch.isChecked = isDarkMode

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO
            )

            sharedPrefs.edit().putBoolean("dark_mode", isChecked).apply()
        }

        return view
    }
}