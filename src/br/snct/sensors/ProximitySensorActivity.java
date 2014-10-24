package br.snct.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ToggleButton;

import com.example.androidsensors.R;

public class ProximitySensorActivity  extends Activity implements SensorEventListener {

	private SensorManager 	sensorManager;
	private Sensor 			sensor;	// Objeto Sensor que será utilizado para medir Proximidade
	private ToggleButton 	sensorValue; // Componente UI para Exibição do Nível de Proximidade 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_proximity_sensor);
		
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		sensorValue = (ToggleButton) findViewById(R.id.sensorValue);
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
		float proximity = event.values[0];
		Log.v("H", String.valueOf(proximity));
		sensorValue.setChecked(proximity == 0);
	}
}

