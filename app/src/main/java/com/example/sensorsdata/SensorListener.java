package com.example.sensorsdata;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Sensor Listener which stores events
 */
public class SensorListener implements SensorEventListener {

    float b;

    List<SensorEvent> events = new ArrayList<>();
    List<Long> tList = new ArrayList<>();
    List<SensorData> sensorData = new ArrayList<>();

    @Override
    public void onSensorChanged(SensorEvent event) {

        // todo maybe events.add(event.clone()) will work
        SensorData data = new SensorData(event.timestamp, event.accuracy, event.sensor.getType(), event.values);
        sensorData.add(data);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public List<SensorData> getEvents() {

        return sensorData;
    }
}
