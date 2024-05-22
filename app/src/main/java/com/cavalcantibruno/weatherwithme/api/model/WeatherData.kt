package com.cavalcantibruno.weatherwithme.api.model

data class WeatherData(
    val coord: Coord,
    val id: Int,
    val main: Main,
    val list: List<WeatherData>,
    val weather: List<Weather>,
    val dt_txt:String
)