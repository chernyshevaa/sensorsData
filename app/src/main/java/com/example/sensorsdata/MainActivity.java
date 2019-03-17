package com.example.sensorsdata;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private Button mButton;
    private boolean start = false;
    private SensorManager sensorManager;
    private long begin = 0;

    StringBuilder stringBuilder;
    int delay = SensorManager.SENSOR_DELAY_NORMAL;

    Map<Sensor, SensorListener> sensorsListenersMap;
    List<Integer> sensorTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.textView);
        mButton = findViewById(R.id.button);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorTypes = new ArrayList<>();

        // Accelerometer
        sensorTypes.add(1);
        // Gravity
        sensorTypes.add(9);
        //Linear acceleration
        sensorTypes.add(10);
        // Acceleration Uncalibrated
        sensorTypes.add(35);

        sensorsListenersMap = new HashMap<>();


    }

    public void onClick(View view) {
        //start
        if (!start) {
            start = true;
            mTextView.setText("Started");
            mButton.setText("Stop");

            stringBuilder = new StringBuilder();

            for (int sensorType : sensorTypes) {
                // todo ask permission
                Sensor sensor = sensorManager.getDefaultSensor(sensorType);
                if (sensor != null) {
                    SensorListener listener = SensorListenerFactory.getSensorListener();
                    sensorsListenersMap.put(sensor, listener);
                    sensorManager.registerListener(listener, sensor, delay);
                }
            }


        }
        //stop
        else {
            start = false;
            mTextView.setText("Stopped");
            mButton.setText("Start");

            for (Map.Entry<Sensor, SensorListener> sensorListenerEntry : sensorsListenersMap.entrySet()) {
                sensorManager.unregisterListener(sensorListenerEntry.getValue(), sensorListenerEntry.getKey());
                List<SensorData> sensorEvents = sensorListenerEntry.getValue().getEvents();
                writeToFile(sensorEvents, sensorListenerEntry.getKey().getType());

            }


            stringBuilder = null;
            begin = 0;
        }
    }

    // todo move to FileHelper class
    private void writeToFile(List<SensorData> events, int sensorType) {
        try {
            // todo check storage is available
            File root = new File(Environment.getExternalStorageDirectory(), "sensorData");
            if (!root.exists()) {
                root.mkdir();
            }

            // todo one file for day
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH");
            String fileName = dateFormat.format(new Date()) + "_" + delay + ".csv";

            File file = new File(root, fileName);

            FileOutputStream fileOutputStream = new FileOutputStream(file, true);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            if (!file.exists()){
                outputStreamWriter.write("timestamp;type;accuracy;data\n");
            }
            System.out.println(events.size());
            for (SensorData event : events) {
                StringBuilder strEvent = new StringBuilder();

                // todo maybe time from start button click?
                strEvent.append(event.getTimestamp());
                strEvent.append(";");
                strEvent.append(SensorTypesHelper.getTypeString(event.getSensorType()));
                strEvent.append(";");
                strEvent.append(event.getAccuracy());
                strEvent.append(";");
                strEvent.append((Arrays.toString(event.getData())).substring(1, Arrays.toString(event.getData()).length() - 1));
                strEvent.append("\n");
                outputStreamWriter.write(strEvent.toString());
            }

            outputStreamWriter.write(stringBuilder.toString());

            outputStreamWriter.flush();
            outputStreamWriter.close();

            mTextView.setText("Data is successfully written to file.");

        } catch (IOException e) {
            System.out.println(e.getMessage());
            mTextView.setText("Error while writing data to file.");
        }
    }

    public void onShowSensors(View view) {
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder strSensors = new StringBuilder();

        for (Sensor sensor : deviceSensors) {
            strSensors.append(sensor.getName() + " | " + SensorTypesHelper.getTypeString(sensor.getType()) + "\n");
        }

        mTextView.setText(strSensors.toString());
    }

    public void onClickDelay(View view) {
        switch (view.getId()) {
            case R.id.rbNormal:
                delay = SensorManager.SENSOR_DELAY_NORMAL;
                break;
            case R.id.rbUI:
                delay = SensorManager.SENSOR_DELAY_UI;
                break;
            case R.id.rbGame:
                delay = SensorManager.SENSOR_DELAY_GAME;
                break;
            case R.id.rbFastest:
                delay = SensorManager.SENSOR_DELAY_FASTEST;
                break;
        }
    }
}
