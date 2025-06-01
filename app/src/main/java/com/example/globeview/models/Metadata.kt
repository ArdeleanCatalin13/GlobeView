package com.example.globeview.models

data class Metadata(
    val currentOffset: Int,
    val totalCount: Int,
    val current_page: Int,
    val from: Int,
    val last_page: Int,
    val per_page: Int,
    val to: Int,
    val total: Int,
)