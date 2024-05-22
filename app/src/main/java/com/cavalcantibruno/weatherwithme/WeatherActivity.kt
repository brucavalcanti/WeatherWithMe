package com.cavalcantibruno.weatherwithme

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cavalcantibruno.weatherwithme.api.RetrofitHelper
import com.cavalcantibruno.weatherwithme.api.WeatherAPI
import com.cavalcantibruno.weatherwithme.api.model.CityData
import com.cavalcantibruno.weatherwithme.api.model.WeatherData
import com.cavalcantibruno.weatherwithme.databinding.ActivityWeatherBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

class WeatherActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }

    private val retrofitGeo by lazy {
        RetrofitHelper.retrofitGeo
    }

    private val retrofitWeather by lazy {
        RetrofitHelper.retrofitWeather
    }

    //Variable used to set the City name accordingly with the selected language
    private var cityName:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        Picasso.get().setLoggingEnabled(true)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val city = intent.getStringExtra("city").toString()
        val languageSelected = intent.getStringExtra("language").toString()
        val apiKey = "{ApiKey}"

        Log.i(ProjectConstants.WEATHER_ACTIVITY, "onCreate: Starting Coroutine")
        CoroutineScope(Dispatchers.IO).launch {
            geoConvert(city,'1',apiKey,languageSelected)
            Log.i(ProjectConstants.WEATHER_ACTIVITY, "onCreate: Coroutine Successful Executed")
        }

        with(binding) {
            backButton.setOnClickListener {
                finish()
            }
            if (languageSelected.equals("en")) {
                backButton.text = "Return"
                btnCurrent.text = "Current"
            } else {
                btnCurrent.text = "Tempo Atual"
                backButton.text = "Voltar"
            }

        }
    }
    suspend fun geoConvert(city:String,limit:Char,apiKey:String,languageSelected:String)
    {
        var dataReturn : Response<List<CityData>>?=null

        try {
            val cityData = retrofitGeo.create(WeatherAPI::class.java)
            dataReturn = cityData.geoConvert(city,limit,apiKey)

        }catch (e:Exception){
            e.printStackTrace()
            Log.i(ProjectConstants.WEATHER_ACTIVITY,
                "geoConvert: ${e.message}")
        }

        if(dataReturn!=null)
        {
            Log.i(ProjectConstants.WEATHER_ACTIVITY, "geoConvert: $dataReturn")
            if(dataReturn.isSuccessful) {
                val geoCity = dataReturn.body()
                /*Verifies if the object that returned is null or empty, in case it's empty
                    returns an error message, which can mean that the city is not in the API
                    database or that the city inserted by the user has typo or does not exist
                 */
                if (geoCity != null) {
                    if(geoCity.isNotEmpty()) {
                        geoCity?.forEach { item ->
                            val cityLon = item.lon
                            val cityLat = item.lat

                            if (languageSelected.equals("en")) {
                                cityName = item?.local_names?.en
                            } else {
                                cityName = item?.local_names?.pt
                            }
                            getWeather(cityLat, cityLon, apiKey, "metric", languageSelected)
                        }
                    }else {
                        Log.i(ProjectConstants.GEO_CONVERT, "DataReturn: $dataReturn")
                        withContext(Dispatchers.Main) {
                            if(languageSelected.equals("en")) {
                                binding.temperature.text = ""
                                binding.weatherDescription.text =""
                                binding.tempMaxId.text=""
                                binding.tempMinId.text=""
                                binding.idCityName.text="Error, City not found or invalid"
                            } else {
                                binding.temperature.text = ""
                                binding.weatherDescription.text =""
                                binding.tempMaxId.text=""
                                binding.tempMinId.text=""
                                binding.idCityName.text="Erro, Cidade não encontrada ou invalida"
                            }
                        }
                    }
                }
            }
        }
    }

    suspend fun getWeather(lat:Double,lon:Double,apiKey: String,unit:String,languageSelected: String)
    {
        var dataReturn : Response<WeatherData>?=null
        try {
            val weatherData = retrofitWeather.create(WeatherAPI::class.java)
            dataReturn = weatherData.getWeather(lat,lon,apiKey,unit,languageSelected)

        }catch (e:Exception){
            e.printStackTrace()
            Log.i(ProjectConstants.WEATHER_ACTIVITY,
                "getWeather: ${e.message} ")
        }

        if(dataReturn !=null)
        {
            if(dataReturn.isSuccessful)
            {
                val getWeather = dataReturn.body()
                val temp = getWeather?.main?.temp
                val minTemp = getWeather?.main?.temp_min
                val maxTemp = getWeather?.main?.temp_max
                val descriptionCurrentWeather = getWeather?.weather?.get(0)?.description
                val imageCode = getWeather?.weather?.get(0)?.icon
                Log.i(ProjectConstants.OPEN_WEATHER, "imageCode: $imageCode ")
                withContext(Dispatchers.Main){
                    Log.i(ProjectConstants.WEATHER_ACTIVITY,
                        "getWeather: Working on Main Coroutine")
                    with(binding)
                    {

                        temperature.text = "${temp?.toInt()} ºC "
                        idCityName.text= cityName.toString()
                        weatherDescription.text=descriptionCurrentWeather.toString().capitalize()
                        tempMaxId.text= "${maxTemp?.toInt()} ºC"
                        tempMinId.text="${minTemp?.toInt()} ºC"
                        Picasso.get()
                            .load("https://openweathermap.org/img/wn/$imageCode@2x.png")
                            .resize(200,200)
                            .into(weatherImage)
                    }
                    Log.i(ProjectConstants.WEATHER_ACTIVITY, "getWeather: UI updated")
                }

            }

        }
    }

    override fun onStop() {
        super.onStop()
        Log.i(ProjectConstants.WEATHER_ACTIVITY, "onStop: onStop called, Activity being closed")
        finish()
    }
}