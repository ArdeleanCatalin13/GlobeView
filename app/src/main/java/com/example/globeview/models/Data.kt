package com.example.globeview.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "countries"
)
data class Data(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var iso2: String? = null,
    val capital: String?,
    val current_president: President?,
    val currency: String?,
    val continent: String?,
    val size: String?,
    val population: String?,
//    val code: String,
//    val callingCode: String?,
//    val currencyCodes: List<String>,
//    val flagImageUri: String?,
    val name: String,
    val full_name: String?,
//    val numRegions: Int?,
//    val wikiDataId: String,
    val href: Href,
): Serializable