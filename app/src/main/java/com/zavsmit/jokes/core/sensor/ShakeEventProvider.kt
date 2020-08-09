package com.zavsmit.jokes.core.sensor

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.zavsmit.jokes.core.SingleLiveEvent


class ShakeEventProvider(application: Application) : SingleLiveEvent<Boolean>() {
    companion object {
        private const val SHAKE_THRESHOLD = 800
    }

    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private var lastZ: Float = 0f
    private var lastUpdate: Long = 0

    private var sensorManager: SensorManager = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val listener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            //do nothing here
        }

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val curTime = System.currentTimeMillis()
                if (curTime - lastUpdate > 100) {
                    val diffTime: Long = curTime - lastUpdate
                    lastUpdate = curTime
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    val speed: Float = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000
                    if (speed > SHAKE_THRESHOLD) {
                        postValue(true)
                    }
                    lastX = x
                    lastY = y
                    lastZ = z
                }
            }
        }
    }

    fun subscribe() {
        sensorManager.apply {
            registerListener(
                    listener,
                    getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun unsubscribe() {
        sensorManager.unregisterListener(listener)
    }
}