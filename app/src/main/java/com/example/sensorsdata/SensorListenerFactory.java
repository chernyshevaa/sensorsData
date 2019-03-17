package com.example.sensorsdata;

/**
 * Factory which produces SensorListeners
 */
public class SensorListenerFactory {

    public static SensorListener getSensorListener() {

        return new SensorListener();
    }

}
