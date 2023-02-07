package com.example.alkomat

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

// zmienna globalna dla klasy, aby przekazac godziny do labelu osi x
private lateinit var entries: ArrayList<Entry>

data class BACAtTime(val bac: Double, val time: String) {
}

class TimeXAxisValueFormatter(private val entries: ArrayList<Entry>) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val entry = entries[value.toInt()]
        return entry.data as String
    }
}

class PokazAlko : AppCompatActivity() {
    var textViewMax: TextView? = null
    var textViewCzas: TextView? = null
    var textViewCzysty: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokaz_alko)
        textViewCzas=findViewById(R.id.textViewCzas)
        textViewCzysty=findViewById(R.id.textViewGramy)
        textViewMax=findViewById(R.id.textViewMax)
        val buttonZakoncz=findViewById<Button>(R.id.button_zakończ)

        //get all extras from previous class
        val plec = intent.getBooleanExtra("plec", true)
        val waga = intent.getIntExtra("waga", 0)
        val ilePite = intent.getIntExtra("ilePite", 0)
        val rozpoczecie = intent.getIntExtra("hour", 0)
        val alkCal = intent.getDoubleExtra("alkCal", 0.0)

        textViewCzysty!!.setText("Wypiłeś $alkCal gram czystego alkoholu")

        val chart = findViewById<LineChart>(R.id.chartalko)

        val data = LineData(getLineDataSet(liczPromile(plec, waga, ilePite, rozpoczecie, alkCal)))
        chart.data = data
        chart.setTouchEnabled(true)
        chart.setDrawGridBackground(false)
        chart.setScaleEnabled(true)
        chart.description.isEnabled = true
        chart.legend.isEnabled = true

        val xAxis = chart.xAxis
        chart.xAxis.valueFormatter = TimeXAxisValueFormatter(entries)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f

        val leftAxis = chart.axisLeft
        leftAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${Math.round(value * 1000).toFloat() / 1000} %"
            }
        }
        leftAxis.setLabelCount(8, false)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)

        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f


//        chart.animateX(1000)
        chart.invalidate()

        buttonZakoncz.setOnClickListener{
            var intentZakoncz = Intent(applicationContext, Menu::class.java)
            startActivity(intentZakoncz)
        }

    }

    //obliczanie promili w odstępie pół godziny
    fun liczPromile(
        plec: Boolean, // true- mezczyzna
        waga: Int,
        ilePite: Int,
        rozpoczecie: Int,
        alkCal: Double
    ): ArrayList<BACAtTime> {
        // lista poszczegolnych faz picia alkoholu
        val arrList = ArrayList<BACAtTime>()
        //liczba iteracji (co pol godziny wiec razy 2)
        var iterations = ilePite * 2
        //wspolczynnik wchlaniania
        val const: Double
        var czas = rozpoczecie * 1.00
        var strTime: String
        if (plec == true)
            const = 0.73
        else
            const = 0.66

        var BAC = 0.00
        // jesli pito dluzej niz 0 godziny
        if (ilePite > 0) {
            for (i in 1..iterations) {
                //licz promile
                BAC += ((alkCal / iterations)) / (waga * const) - (0.15 / 2)
                if (BAC < 0)
                    BAC = 0.00
                czas += 0.5
                czas = czas % 24
                if (czas % 1 == 0.0) {
                    strTime = "${czas.toInt()}:00"
                } else {
                    strTime = "${czas.toInt()}:30"
                }
                arrList.add(BACAtTime(BAC, strTime))

            }
        }
        //jeśli podano 0 godzin
        else {
            BAC = ((alkCal) * 5.14) / (waga * const)
        }
        textViewMax!!.setText("Największe stężenie: ${String.format("%.2f", BAC)}‰")
        println("Skonczono picie\n")
        //obliczanie promili podczas trzezwienia
        do {
            iterations++
            BAC -= (0.15) / 2
            czas += 0.5
            czas = czas % 24
            if (BAC < 0)
                BAC = 0.0
            if (czas % 1 == 0.0) {
                strTime = "${czas.toInt()}:00"
            } else {
                strTime = "${czas.toInt()}:30"
            }
            arrList.add(BACAtTime(BAC, strTime))
            print("$strTime  ")
            println("$BAC+\n")

        } while (BAC > 0)


        if (iterations % 1 == 0) {
            strTime = "${iterations/2}:00"
        } else {
            strTime = "${iterations/2}:30"
        }
        textViewCzas!!.setText("Od rozpoczęcia picia trzeźwiałeś: ${strTime} godzin")
        println("Skonczono Trzezwienie\n")
        return arrList
    }

    private fun getLineDataSet(lista: ArrayList<BACAtTime>): LineDataSet {
        entries = ArrayList<Entry>()
        for (i in 0 until lista.size) {
            val (bac, time) = lista[i]
            entries.add(Entry(i.toFloat(), bac.toFloat(), time))
        }
        val set = LineDataSet(entries, "Promile w czasie")
        set.color = Color.rgb(0, 0, 255)
        set.valueTextSize = 9f
        set.valueFormatter = DefaultValueFormatter(2)
        set.valueTextColor = Color.MAGENTA
        return set
    }
}