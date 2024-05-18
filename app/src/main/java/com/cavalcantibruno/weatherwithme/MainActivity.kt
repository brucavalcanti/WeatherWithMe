package com.cavalcantibruno.weatherwithme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import com.cavalcantibruno.weatherwithme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnWeather.setOnClickListener {
            val intent = Intent(this,WeatherActivity::class.java)
            val infoLanguage = languageSelector()
            val cityName = binding.editCityName.text.toString()
            intent.putExtra("language",infoLanguage)
            intent.putExtra("city",cityName)

            startActivity(intent)
            Log.i("info_Weather", "btnWeather: $cityName -> $infoLanguage")
        }

        binding.rbPortuguese.setOnClickListener {
            binding.editCity.hint ="Cidade"
            binding.btnWeather.text ="Verificar o Tempo"
        }

        binding.rbEnglish.setOnClickListener {
            binding.editCity.hint ="City"
            binding.btnWeather.text ="Verify Weather"
        }
    }

    /*Function to set the app language, it's verify which radioButton is checked using their id*/
    fun languageSelector ():String {
        val idLanguageButton = binding.rgLanguage.checkedRadioButtonId

        val languageSelected = when(idLanguageButton) {
            R.id.rbEnglish -> "en"
            R.id.rbPortuguese ->"pt_br"
            else -> "en"
        }
        return  languageSelected
    }

}