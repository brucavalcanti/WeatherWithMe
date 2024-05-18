package com.cavalcantibruno.weatherwithme.api

import com.cavalcantibruno.weatherwithme.api.model.CityData
import com.cavalcantibruno.weatherwithme.api.model.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("weather")
    suspend fun getWeather(
        @Query("lat")lat:Double,
        @Query("lon")lon:Double,
        @Query("appid")apiKey: String,
        @Query("units") unit:String,
        @Query("lang") lang:String
    ) : Response<WeatherData>
    //http://api.openweathermap.org/geo/1.0/direct?q=London&limit=5&appid={API key}
    @GET("direct")
    suspend fun geoConvert(
        @Query("q") cityName:String?,
        @Query("limit") limit:Char,
        @Query("appid") apiKey:String
    ):Response<List<CityData>>
}