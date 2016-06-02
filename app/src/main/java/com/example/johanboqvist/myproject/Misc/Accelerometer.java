package com.example.johanboqvist.myproject.Misc;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Accelerometer handles the accelereometer sensor
 */
public class Accelerometer implements SensorEventListener {

    private SensorManager   mSensorManager;
    private Sensor          mAccelerometer;
    private Context         context;
    private float           x = 0.f, y = 0.f, z = 0.f;
    private float           grav = 9.81f;

    public Accelerometer(Context c){
        // Instantiate SensorManager
        this.context = c;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        // Get Accelerometer sensor
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener( this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.8f;

        x = event.values[0];
        y = event.values[1];

        /** z is never really used **/
        grav = alpha * grav + (1 - alpha) * event.values[2];
        z = event.values[2] - grav;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public float getX() { return x; }
    public float getY() {
        return y;
    }
    public float getZ() {
        return z;
    }
}
