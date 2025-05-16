package com.example.myapplication.model

data class CityResponse(
    val `data`: List<Data?>?,
    val errors: String?,
    val msg: String?,
    val status: String?
)