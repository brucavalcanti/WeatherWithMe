package com.cavalcantibruno.weatherwithme

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cavalcantibruno.weatherwithme.api.RetrofitHelper
import com.cavalcantibruno.weatherwithme.api.WeatherAPI
import com.cavalcantibruno.weatherwithme.api.model.CityData
import com.cavalcantibruno.weatherwithme.api.model.ForecastDataX
import com.cavalcantibruno.weatherwithme.api.model.WeatherData
import com.cavalcantibruno.weatherwithme.databinding.ActivityForecastBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ForecastActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityForecastBinding.inflate(layoutInflater)
    }

    private val retrofitGeo by lazy {
        RetrofitHelper.retrofitGeo
    }

    private val retrofitWeather  by lazy {
        RetrofitHelper.retrofitWeather
    }

    private var cityName:String?=null

     var forecastList : ForecastDataX? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val city = intent.getStringExtra("city").toString()
        val languageSelected = intent.getStringExtra("language").toString()
        val apiKey = "{APIKey}"

        binding.btnBack.setOnClickListener {
            finish()
        }

        if(languageSelected.equals("pt_br")) {
            binding.btnBack.text = "Voltar"
            binding.textForecast.text="Previsão do Tempo de 5 dias"}
        else
        {
            binding.btnBack.text = "Return"
            binding.textForecast.text="5 days Weather Forecast"
        }

        Log.i(ProjectConstants.FORECAST_ACTIVITY, "onCreate: Starting Coroutine ")
         CoroutineScope(Dispatchers.IO).launch{
              geoConvert(city,'1',apiKey,languageSelected)
             Log.i(ProjectConstants.FORECAST_ACTIVITY,
                 "onCreate: Coroutine Successful Executed")
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
            Log.i(ProjectConstants.FORECAST_ACTIVITY,"geoConvert: GET requisition error ")
        }

        if(dataReturn!=null)
        {
            Log.i(ProjectConstants.GEO_CONVERT, "ForecastActivity -> geoConvert: $dataReturn")
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
                            Log.i(ProjectConstants.GEO_CONVERT,
                                "ForecastActivity -> geoConvert: Starting getForecast ")
                           getForecast(cityLat, cityLon, apiKey, "metric", languageSelected)
                        }
                    }else {
                        if(languageSelected.equals("en"))
                        {
                            binding.textForecast.text = "Error, City not found or invalid"
                        }else
                        {
                            binding.textForecast.text = "Erro, cidade não encontrada ou invalida"
                        }
                        Log.i(ProjectConstants.GEO_CONVERT,
                            "ForecastActivity -> DataReturn: $dataReturn")

                    }
                }
            }
        }
    }

    suspend fun getForecast(lat:Double,lon:Double,apiKey: String,unit:String,languageSelected: String)
    {
        var dataReturn : Response<ForecastDataX>?=null
        try {
            val weatherData = retrofitWeather.create(WeatherAPI::class.java)
            dataReturn = weatherData.getForecast(lat,lon,apiKey,unit,languageSelected)

        }catch (e:Exception){
            e.printStackTrace()
            Log.i(ProjectConstants.FORECAST_ACTIVITY, "getForecast: GET requisition error ")
        }

        if(dataReturn !=null)
        {
            if(dataReturn.isSuccessful)
            {
                val getWeather = dataReturn.body()
                if (getWeather != null) {
                    forecastList = getWeather
                    var tempList:List<WeatherData> = emptyList()
                    //Filter to only shows specific items
                    for(item in forecastList!!.list.indices)
                    {
                        if(forecastList!!.list[item].dt_txt.contains("06:00") ||
                            forecastList!!.list[item].dt_txt.contains("12:00") ||
                            forecastList!!.list[item].dt_txt.contains("18:00"))
                        {
                            tempList+= forecastList!!.list[item]
                        }
                    }
                    withContext(Dispatchers.Main){
                        Log.i(ProjectConstants.FORECAST_ACTIVITY,
                            "getForecast: Working on Main Scope ")
                        binding.rvForecast.adapter = forecastList?.let {
                            ForecastAdapter(tempList,cityName!!
                        ) }
                        binding.rvForecast.layoutManager =  LinearLayoutManager(this@ForecastActivity)
                    }
                }

            }

        }
    }
}