package com.example.alkomat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timePicker = findViewById<TimePicker>(R.id.time_picker_start)
        val waga = findViewById<NumberPicker>(R.id.number_picker_waga)
        val radioButtonK = findViewById<RadioButton>(R.id.radio_button_K)
        val radioButtonM = findViewById<RadioButton>(R.id.radio_button_M)
        val ilePite = findViewById<NumberPicker>(R.id.number_picker_godziny)

        val buttonDalej = findViewById<Button>(R.id.button_oblicz)

        var hour= timePicker.hour

        waga.minValue=30
        waga.maxValue=200


        ilePite.minValue=0
        ilePite.maxValue=12


        var kg = waga.value
        var plec=false;

        radioButtonK.setOnClickListener{
            plec=false
        }

        radioButtonM.setOnClickListener{
            plec=true
        }


        buttonDalej.setOnClickListener {
            if(radioButtonK.isChecked||radioButtonM.isChecked) {
                var intentNA = Intent(applicationContext, ListaAlko::class.java)

                intentNA.putExtra("plec", plec)
                intentNA.putExtra("waga", waga.value)
                intentNA.putExtra("ilePite", ilePite.value)
                intentNA.putExtra("hour", hour)

                println("plec: $plec waga: ${waga.value} ilePite: ${ilePite.value} hour: $hour")
                startActivity(intentNA)

            }
            else{
                var message = Toast.makeText(this,"Zaznacz płeć",Toast.LENGTH_LONG)
                message.show()
            }
        }
    }



}