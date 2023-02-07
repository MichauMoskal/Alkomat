package com.example.alkomat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.*

class ListaAlko : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_alko)

        val piwo = findViewById<EditText>(R.id.editTextPiwo)
        val wino = findViewById<EditText>(R.id.editTextWino)
        val wodka = findViewById<EditText>(R.id.editTextWodka)
        val smakowa = findViewById<EditText>(R.id.editTextSmakowa)

        val ilosc = findViewById<EditText>(R.id.editIloscCustom)
        val procenty = findViewById<EditText>(R.id.editTextProcentyCustom)

        val dalej = findViewById<Button>(R.id.buttonDalej)


        var plec = intent.getBooleanExtra("plec", true)
        var waga = intent.getIntExtra("waga", 0)
        var ilePite = intent.getIntExtra("ilePite", 0)
        var hour = intent.getIntExtra("hour", 0)

        dalej.setOnClickListener {

            var alkPiwo = 0.0;
            var alkCustom = 0.0;
            var alkSmakowa = 0.0;
            var alkWodka = 0.0;
            var alkWino = 0.0;

            if (piwo.text.toString() != "")
                alkPiwo = obliczAlko(piwo.text.toString().toInt(), 5)
            if (wino.text.toString() != "")
                alkWino = obliczAlko(wino.text.toString().toInt(), 14)
            if (wodka.text.toString() != "")
                alkWodka = obliczAlko(wodka.text.toString().toInt(), 40)
            if (smakowa.text.toString() != "")
                alkSmakowa = obliczAlko(smakowa.text.toString().toInt(), 30)
            if (ilosc.text.toString() != "" && procenty.text.toString() != "")
                alkCustom =
                    obliczAlko(ilosc.text.toString().toInt(), procenty.text.toString().toInt())

            var alkCal = alkPiwo + alkWino + alkWodka + alkSmakowa + alkCustom

            if (alkCal == 0.0) {
                Toast.makeText(this, "Wybierz alkohol", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var intentOblicz = Intent(applicationContext, PokazAlko::class.java)
            intentOblicz.putExtra("plec", plec);
            intentOblicz.putExtra("waga", waga);
            intentOblicz.putExtra("ilePite", ilePite);
            intentOblicz.putExtra("hour", hour);
            intentOblicz.putExtra("alkCal", alkCal);
            startActivity(intentOblicz)
        }

    }

    fun obliczAlko(ilosc: Int, procenty: Int): Double {
        return ilosc * procenty / 100.00
    }


}
