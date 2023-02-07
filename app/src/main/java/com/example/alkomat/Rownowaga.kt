package com.example.alkomat


import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlin.math.abs


class Rownowaga : AppCompatActivity() {
    private var sensorManager: SensorManager? = null
    private var gyroscopeSensor: Sensor? = null
    private var gyroscopeEventListener: SensorEventListener? = null
    private var textViewWynik: TextView? = null
    private var textViewOpis: TextView? = null
    private var button: Button? = null
    private var samples = 0
    private var sum = 0.00
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rownowaga)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        gyroscopeSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        button = findViewById<Button>(R.id.buttonBalans)
        textViewWynik = findViewById<TextView>(R.id.textViewWynikRow)
        textViewOpis = findViewById<TextView>(R.id.textViewOpis)
        if (gyroscopeSensor == null) {
            Toast.makeText(this, "Urządzenie nie posiada żyroskopu", Toast.LENGTH_SHORT).show()
            finish()
        }

        gyroscopeEventListener = object : SensorEventListener {
            override fun onSensorChanged(sensorEvent: SensorEvent) {
                samples++
                sum += sensorEvent.values[2]
                if (Math.abs(sensorEvent.values[2]) < 0.4) {
                    button!!.setBackgroundColor(Color.GREEN);
                } else {
                    button!!.setBackgroundColor(Color.RED);
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }
        button!!.setOnClickListener {
            getData()
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(gyroscopeEventListener)
    }

    fun getData() {
        button!!.setText("")
        samples = 0
        sum = 0.0
        sensorManager!!.registerListener(
            gyroscopeEventListener,
            gyroscopeSensor,
            SensorManager.SENSOR_DELAY_FASTEST
        )
        object : CountDownTimer(3000, 100) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                var wynik = sum / samples
                button!!.setText("ponów")
                sensorManager!!.unregisterListener(gyroscopeEventListener)
                textViewOpis!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
                if (abs(wynik) < 0.25) {
                    textViewWynik!!.setText("Trzeźwy")
                    button!!.setBackgroundColor(Color.GREEN)
                    textViewOpis!!.setText(getResources().getString(R.string.rownowagaGreen))
                } else if (abs(wynik) < 0.5) {
                    textViewWynik!!.setText("Lekko Podchmielony")
                    button!!.setBackgroundColor(Color.YELLOW)
                    textViewOpis!!.setText(getResources().getString(R.string.rownowagaYellow))
                } else {
                    textViewWynik!!.setText("Pijaniejki")
                    button!!.setBackgroundColor(Color.RED)
                    textViewOpis!!.setText(getResources().getString(R.string.rownowagaRed))
                }
            }
        }.start()
    }

}