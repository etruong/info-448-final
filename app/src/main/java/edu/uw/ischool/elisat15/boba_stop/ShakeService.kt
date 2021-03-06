package edu.uw.ischool.elisat15.boba_stop

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import android.support.v4.content.LocalBroadcastManager
import android.os.Bundle

class ShakeService : Service(), SensorEventListener {

    override fun onBind(intent: Intent?): IBinder? { return null }

    var xAccel: Float = 0.0F
    var yAccel: Float = 0.0F
    var zAccel: Float = 0.0F

    var xPrevAccel: Float = 0.0F
    var yPrevAccel: Float = 0.0F
    var zPrevAccel: Float = 0.0F

    var firstUpdate: Boolean = true
    var shakeInitiated: Boolean = false
    var shakeThreshold: Float = 2.5F // difference btwn acceleration

    lateinit var sensorManager: SensorManager
    lateinit var accelerator: Sensor

    override fun onCreate() {
        super.onCreate()
        Log.v("ShakeService", "created service")
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerator = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerator, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {

        updateAccelParameter(event!!.values[0], event!!.values[1], event!!.values[2])
        if (!shakeInitiated && isAccelerationChanged()) {
            shakeInitiated = true
        } else if (shakeInitiated && isAccelerationChanged()) {
            executeShakeAction()
        } else if (shakeInitiated && !isAccelerationChanged()) {
            shakeInitiated = false
        }

    }

    private fun isAccelerationChanged(): Boolean {

        // Detect if acceleration values are changed... If change is at least 2 axis, so can detect
        // shake motion

        val deltaX = Math.abs(xPrevAccel - xAccel)
        val deltaY = Math.abs(yPrevAccel - yAccel)
        val deltaZ = Math.abs(zPrevAccel - zAccel)

//        Log.v("service", "threshold: ${shakeThreshold} delta-x: ${deltaX} delta-y: ${deltaY} delta-z: ${deltaZ}")

        return (deltaX > shakeThreshold && deltaY > shakeThreshold) ||
                (deltaX > shakeThreshold && deltaZ > shakeThreshold) ||
                (deltaY > shakeThreshold && deltaZ > shakeThreshold)

    }

    private fun executeShakeAction() {
        Log.v("ShakeService", "executing shake action")
        sendMessageToActivity()
    }

    private fun sendMessageToActivity() {
        val intent = Intent("intentKey")
        val extras = Bundle()
        extras.putString("key", "hello")
        intent.putExtras(extras)
        this.sendBroadcast(intent)
    }

    private fun updateAccelParameter(xNewAccel: Float, yNewAccel: Float, zNewAccel: Float) {

        if (firstUpdate) {
            xPrevAccel = xNewAccel
            yPrevAccel = yNewAccel
            zPrevAccel = zNewAccel
            firstUpdate = false
        } else {
            xPrevAccel = xAccel
            yPrevAccel = yAccel
            zPrevAccel = zAccel
        }

        xAccel = xNewAccel
        yAccel = yNewAccel
        zAccel = zNewAccel

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("ShakeService", "destroyed service")
    }
}