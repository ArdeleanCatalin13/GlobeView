package com.example.globeview.models

data class CountriesResponse(
    val data: MutableCollection<Data>,
    val links: Link?,
    val meta: Metadata?
)