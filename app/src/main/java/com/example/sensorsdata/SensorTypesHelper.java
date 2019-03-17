package com.example.sensorsdata;

import android.hardware.Sensor;

/**
 * Helper class to get sensor types as strings
 */
public class SensorTypesHelper {
    static String getTypeString(int type) {
        String strType = "";

        if (Sensor.TYPE_ACCELEROMETER == type) {
            strType = "Accelerometer";
        } else if (Sensor.TYPE_GYROSCOPE == type) {
            strType = "Gyroscope";
        } else if (Sensor.TYPE_LIGHT == type) {
            strType = "Light";
        } else if (Sensor.TYPE_MAGNETIC_FIELD == type) {
            strType = "Magnetic";
        } else if (Sensor.TYPE_ORIENTATION == type) {
            strType = "Orientation";
        } else {
            strType = Integer.toString(type);
        }

        return strType;
    }
}
