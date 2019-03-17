package com.example.sensorsdata;

/**
 * Represents sensor event
 */
public class SensorData {
    // todo may be toCSV() method
    Long timestamp;
    int accuracy;
    int sensorType;
    float[] data;

    public SensorData(Long timestamp, int accuracy, int sensorType, float[] data) {
        this.timestamp = timestamp / 1000000;
        this.accuracy = accuracy;
        this.sensorType = sensorType;
        this.data = data.clone();
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getSensorType() {
        return sensorType;
    }

    public float[] getData() {
        return data;
    }
}
