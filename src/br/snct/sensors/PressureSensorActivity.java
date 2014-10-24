package br.snct.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.androidsensors.R;

public class PressureSensorActivity  extends Activity implements SensorEventListener {

	private SensorManager 	sensorManager;
	private Sensor 			sensor;	// Objeto Sensor que ser� utilizado para medir Press�o Atmosf�rica 
	private TextView 		sensorValue; // Componente UI para Exibi��o do N�vel de Press�o Atmosf�rica 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pressure_sensor);
		
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
		sensorValue = (TextView) findViewById(R.id.sensorValue);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float pressure = event.values[0];
		Log.v("PRESSAO ATMOSF�RICA", String.valueOf(pressure));
		float height = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure);
		Log.v("ALTURA", String.valueOf(height));
		sensorValue.setText(String.valueOf(height));
	}
}