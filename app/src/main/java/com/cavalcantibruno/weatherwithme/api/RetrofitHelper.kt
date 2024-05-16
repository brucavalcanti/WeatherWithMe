package com.cavalcantibruno.weatherwithme.api

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    companion object {
        val retrofit = Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}