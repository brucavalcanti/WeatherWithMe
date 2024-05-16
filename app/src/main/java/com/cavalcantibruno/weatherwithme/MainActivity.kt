package com.cavalcantibruno.weatherwithme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            intent.putExtra("language",infoLanguage)
            startActivity(intent)
        }

        binding.rbPortuguese.setOnClickListener {
            binding.editCityName.hint ="Cidade"
            binding.btnWeather.text ="Verificar o Tempo"
        }

        binding.rbEnglish.setOnClickListener {
            binding.editCityName.hint ="City"
            binding.btnWeather.text ="Verify Weather"
        }
    }

    fun languageSelector ():String {
        val idLanguageButton = binding.rgLanguage.checkedRadioButtonId

        val languageSelected = when(idLanguageButton) {
            R.id.rbEnglish -> "en"
            R.id.rbPortuguese ->"pt-br"
            else -> "en"
        }
        return  languageSelected
    }

}