package com.cavalcantibruno.weatherwithme
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cavalcantibruno.weatherwithme.api.model.ForecastDataX
import com.cavalcantibruno.weatherwithme.api.model.WeatherData
import com.cavalcantibruno.weatherwithme.databinding.ForecastItemBinding
import com.squareup.picasso.Picasso

class ForecastAdapter(private val forecastList:List<WeatherData>,
                      private val cityName:String
) :RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    inner class ForecastViewHolder (itemBinding: ForecastItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root)
    {
        private val binding:ForecastItemBinding

        init {
            binding = itemBinding
        }

        fun binding(forecastItem:WeatherData,cityName:String) {
                binding.forecastCityName.text = cityName
                binding.forecastTemp.text = "${forecastItem.main.temp.toInt()}ºC"
                binding.forecastDescription.text = forecastItem.weather.get(0).description
                binding.textDataTime.text = forecastItem.dt_txt
                val imageCode = forecastItem.weather.get(0).icon
                Picasso.get()
                    .load("https://openweathermap.org/img/wn/$imageCode@2x.png")
                    .resize(200, 200)
                    .into(binding.forecastImage)
        }
    }


    //Ao criar o viewHolder -> criar a visualização
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {

        val forecastItemBinding = ForecastItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        Log.i(ProjectConstants.FORECAST_ADAPTER, "onCreateViewHolder: $forecastItemBinding ")
        return ForecastViewHolder(forecastItemBinding)
    }


    //Returns how many items are inserted in the List
    override fun getItemCount(): Int {

        return forecastList.size

    }


    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.binding(forecast, cityName)
        Log.i(ProjectConstants.FORECAST_ADAPTER, "onBindViewHolder: $forecast")
    }
}