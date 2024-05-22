package com.cavalcantibruno.weatherwithme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import com.cavalcantibruno.weatherwithme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var toastError:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnCurrentWeather.setOnClickListener {

            if (binding.editCityName.text.toString().isEmpty()) {
                Toast.makeText(this, "$toastError", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, WeatherActivity::class.java)
                val infoLanguage = languageSelector()
                val cityName = binding.editCityName.text.toString()
                intent.putExtra("language", infoLanguage)
                intent.putExtra("city", cityName)
                startActivity(intent)
                Log.i(ProjectConstants.MAIN_ACTIVITY, "Starting WeatherActivity")
                Log.i(ProjectConstants.MAIN_ACTIVITY,
                    "btnWeather: $cityName -> $infoLanguage")
            }
        }
        binding.btnForecastWeather.setOnClickListener {
            if (binding.editCityName.text.toString().isEmpty()) {
                Toast.makeText(this, "$toastError", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, ForecastActivity::class.java)
                val infoLanguage = languageSelector()
                val cityName = binding.editCityName.text.toString()
                intent.putExtra("language", infoLanguage)
                intent.putExtra("city", cityName)
                startActivity(intent)
                Log.i(ProjectConstants.MAIN_ACTIVITY, "Starting ForecastActivity")
                Log.i(ProjectConstants.MAIN_ACTIVITY,
                    "btnForecastWeather: $cityName -> $infoLanguage")
            }
        }
        //Checks the initial value of the radio button and set the texts
        if(binding.rbPortuguese.isChecked){
            binding.editCity.hint ="Cidade"
            binding.btnCurrentWeather.text ="Tempo Atual"
            binding.btnForecastWeather.text ="Previsão do Tempo"
            toastError = "Informe uma cidade"

        }

        /*Sets the app language, which can be English or Brazilian Portuguese*/
        binding.rbPortuguese.setOnClickListener {
            binding.editCity.hint ="Cidade"
            binding.btnCurrentWeather.text ="Tempo Atual"
            binding.btnForecastWeather.text ="Previsão do Tempo"
            toastError = "Informe uma cidade"
        }

        binding.rbEnglish.setOnClickListener {
            binding.editCity.hint ="City"
            binding.btnCurrentWeather.text ="Current Weather"
            binding.btnForecastWeather.text = "Weather Forecast"
            toastError = "Insert a city name"
        }
    }

    /*Function to set the app language, it verifies which radioButton is checked using their id*/
    fun languageSelector ():String {
        val idLanguageButton = binding.rgLanguage.checkedRadioButtonId

        val languageSelected = when(idLanguageButton) {
            R.id.rbEnglish -> "en"
            R.id.rbPortuguese ->"pt_br"
            else -> "pt_br"
        }
        return  languageSelected
    }

}