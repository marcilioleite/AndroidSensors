package br.snct.sensors;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class GyroSensorActivity extends Activity {

	private GyroView ui;
    private SensorManager sensorManager;
    private Sensor giroscopio, acelerometro, campoMagnetico;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ui = new GyroView(this);
        ui.setBackgroundColor(getResources().getColor(android.R.color.black));

        setContentView(ui);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        giroscopio = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        campoMagnetico = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    private SensorEventListener giroscopioHandler = new SensorEventListener() {

        private static final float MIN_TIME_STEP = (1f / 40f);
        private long lastTime = System.currentTimeMillis();
        private float rotationX, rotationY, rotationZ;

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            float angularVelocity = z * 0.96f;

            long now = System.currentTimeMillis();
            float timeDiff = (now - lastTime) / 1000f;
            lastTime = now;
            if (timeDiff > 1) {
                timeDiff = MIN_TIME_STEP;
            }

            rotationX += x * timeDiff;
            if (rotationX > 0.5f)
                rotationX = 0.5f;
            else if (rotationX < -0.5f)
                rotationX = -0.5f;

            rotationY += y * timeDiff;
            if (rotationY > 0.5f)
                rotationY = 0.5f;
            else if (rotationY < -0.5f)
                rotationY = -0.5f;

            rotationZ += angularVelocity * timeDiff;

            ui.setGyroRotation(rotationX, rotationY, rotationZ);
        }
    };

    private SensorEventListener acelerometroHandler = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            float x = values[0];
            float y = values[1];

            ui.setAcceleration(-x, y);
        }
    };

    private SensorEventListener campoMagneticoHandler = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            float x = values[0];
            float y = values[1];

            ui.setMagneticField(x, -y);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(giroscopioHandler, giroscopio, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(acelerometroHandler, acelerometro, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(campoMagneticoHandler, campoMagnetico, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(giroscopioHandler, giroscopio);
        sensorManager.unregisterListener(acelerometroHandler, giroscopio);
        sensorManager.unregisterListener(campoMagneticoHandler, campoMagnetico);
    }
}

