package com.example.alkomat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.BarChart

class PokazAlko : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokaz_alko)


        //get all extras from previous class
        var plec = intent.getBooleanExtra("plec", true)
        var waga = intent.getIntExtra("waga", 0)
        var ilePite = intent.getIntExtra("ilePite", 0)
        var rozpoczecie = intent.getIntExtra("hour", 0)
        var alkCal = intent.getDoubleExtra("alkCal",0.0)

        val barChart = findViewById<BarChart>(R.id.chartalko)




    }

    fun liczPromile(plec: Boolean, waga: Int, ilePite: Int,rozpoczecie: Int,alkCal: Double): List<Double> {
        var arrList = arrayListOf<Double>()
        var iterations = ilePite *2

    }
}