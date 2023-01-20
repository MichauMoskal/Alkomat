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
        var alkCal = intent.getDoubleExtra("alkCal", 0.0)

        val barChart = findViewById<BarChart>(R.id.chartalko)


    }

    //obliczanie promili w odstępie pół godziny
    fun liczPromile(
        plec: Boolean, // true- mezczyzna
        waga: Int,
        ilePite: Int,
        rozpoczecie: Int,
        alkCal: Double
    ): List<Double> {
        // lista poszczegolnych faz picia alkoholu
        var arrList = arrayListOf<Double>()
        //liczba iteracji (co pol godziny wiec razy 2)
        var iterations = ilePite * 2
        //wspolczynnik wchlaniania
        var const = 0.00
        var czas = rozpoczecie*1.00

        if (plec == true)
            const = 0.73
        else
            const = 0.66

        var BAC = 0.00
        // jesli pito dluzej niz 0 godziny
        if (ilePite > 0) {
            for (i in 1..iterations) {
                //licz promile
                BAC += ((alkCal / iterations)) / (waga * const) - (0.15/2)
                if(BAC<0)
                    BAC=0.00
                czas+= 0.5
                arrList.add(BAC)
                print("$czas  ")
                println("$BAC+\n")

            }
        }
        //jeśli podano 0 godzin
        else {
            BAC = ((alkCal) * 5.14) / (waga * const)
        }
        println("Skonczono picie\n")
        //obliczanie promili podczas trzezwienia
        do{
            BAC -= (0.15)/2
            czas+= 0.5
            if (BAC < 0)
                BAC = 0.0
            arrList.add(BAC)
            print("$czas  ")
            println("$BAC+\n")

        }while (BAC > 0)
        println("Skonczono Trzezwienie\n")
        return arrList
    }
}