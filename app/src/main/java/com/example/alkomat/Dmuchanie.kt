package com.example.alkomat

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Color.*
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*


class Dmuchanie : AppCompatActivity() {
    private val REQUEST_RECORD_AUDIO = 1
    private lateinit var microphoneLevelTextView: TextView
    private lateinit var meanTextView: TextView
    private lateinit var startButton: Button
    private var volMean = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dmuchanko)

        microphoneLevelTextView = findViewById(R.id.MicrophoneLevelText)
        meanTextView = findViewById(R.id.TextViewWynik)
        startButton = findViewById(R.id.StartButton)
        startButton.setOnClickListener {
            checkPermissions()
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO
            )
        } else {
            startRecording()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_RECORD_AUDIO -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startRecording()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startRecording() {
        volMean = 0;
        val audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            44100, AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            AudioRecord.getMinBufferSize(
                44100, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
        )

        val buffer = ShortArray(320)
        audioRecord.startRecording()

        object : CountDownTimer(3000, 100) {

            override fun onTick(millisUntilFinished: Long) {
                audioRecord.read(buffer, 0, buffer.size)
                var max = 0
                for (i in buffer) {
                    if (Math.abs(i.toInt()) > max) {
                        max = Math.abs(i.toInt())
                    }
                }
                val volume = (max / 32767.0 * 100).toInt()
                volMean += volume
                runOnUiThread {
                    microphoneLevelTextView.text = "Moc: $volume"
                }
            }

            override fun onFinish() {
                meanTextView.text = "Srednia: ${volMean / 100.0}‰"
                if (volMean < 100) {
                    startButton.setBackgroundColor(parseColor("#00FF00"))
                    microphoneLevelTextView.setText(getResources().getString(R.string.dmuchanieGreen))
                } else if (volMean > 300) {
                    startButton.setBackgroundColor(parseColor("#FF0000"))
                    microphoneLevelTextView.setText(getResources().getString(R.string.dmuchanieRed))
                } else {
                    startButton.setBackgroundColor(parseColor("#FFFF00"))
                    microphoneLevelTextView.setText(getResources().getString(R.string.dmuchanieYellow))
                }
                startButton.setText("Ponów")
                audioRecord.stop();
            }
        }.start()
    }
}