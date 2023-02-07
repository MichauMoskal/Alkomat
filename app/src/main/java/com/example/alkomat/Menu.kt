package com.example.alkomat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val kalkulator = findViewById<Button>(R.id.buttonKalkulator)
        val rownowaga = findViewById<Button>(R.id.buttonRownowaga)
        val latarka = findViewById<Button>(R.id.buttonLatarka)
        val dmuchanie = findViewById<Button>(R.id.buttonDmuchanie)

        kalkulator.setOnClickListener {
            var intentOne = Intent(applicationContext, DetailsActivity::class.java)
            startActivity(intentOne)
        }
        rownowaga.setOnClickListener {
            var intentTwo = Intent(applicationContext, Rownowaga::class.java)
            startActivity(intentTwo)
        }
        latarka.setOnClickListener {
            var intentThree = Intent(applicationContext, Swiecidlo::class.java)
            startActivity(intentThree)
        }
        dmuchanie.setOnClickListener {
            var intentFour = Intent(applicationContext, Dmuchanie::class.java)
            startActivity(intentFour)
        }

    }
}