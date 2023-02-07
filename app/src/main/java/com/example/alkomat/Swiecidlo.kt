package com.example.alkomat

import android.hardware.Sensor
import androidx.appcompat.app.AppCompatActivity
import android.hardware.SensorEventListener
import android.widget.TextView
import android.hardware.SensorManager
import android.os.Bundle
import android.hardware.SensorEvent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.view.View
import android.widget.ToggleButton

class Swiecidlo : AppCompatActivity(), SensorEventListener {
    var cameraManager: CameraManager? = null
    var cameraid = ""
    var aSwitch: ToggleButton? = null
    var textView12: TextView? = null
    var sensorManager: SensorManager? = null
    var sensor: Sensor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swiecidlo)

        textView12 = findViewById<View>(R.id.textViewLatarka) as TextView?
        aSwitch = findViewById<ToggleButton>(R.id.ButtonSwiatlo)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (aSwitch?.isChecked == true) {
            if (event.sensor.type == Sensor.TYPE_LIGHT) {
                var lux = event.values[0]
                if (lux < 20) {
                    torch(true)
                } else {
                    torch(false)
                }
            }
        }
    }

    public fun torch(isChecked: Boolean) {
        try {
            cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
            cameraid = cameraManager!!.cameraIdList[0]
            cameraManager!!.setTorchMode(cameraid, isChecked)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
}