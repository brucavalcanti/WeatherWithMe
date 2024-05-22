package com.cavalcantibruno.weatherwithme.api.model

data class ForecastDataX(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherData>,
    val message: Int
)