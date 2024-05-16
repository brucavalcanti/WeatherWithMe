package com.cavalcantibruno.weatherwithme

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.cavalcantibruno.weatherwithme.databinding.ActivityWeatherBinding

class WeatherActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val languageSelected = intent.getStringExtra("language").toString()

        if(languageSelected.equals("en")){
            binding.btnCurrent.text = "Current"
            binding.btnForecast.text = "Forecast"
        }else
        {
            binding.btnCurrent.text = "Tempo Atual"
            binding.btnForecast.text = "Previsão do Tempo"
        }


        binding.btnCurrent.setOnClickListener {
            binding.textTeste.text = "Current"
        }
        binding.btnForecast.setOnClickListener {
            binding.textTeste.text = "Forecast"
        }
    }
}