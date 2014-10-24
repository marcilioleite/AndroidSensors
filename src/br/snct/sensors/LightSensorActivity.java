package br.snct.sensors;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.androidsensors.R;

public class LightSensorActivity extends Activity implements SensorEventListener {

	private SensorManager 	sensorManager;
	private Sensor 			sensor;	// Objeto Sensor que será utilizado para medir Iluminação 
	private ProgressBar 	sensorValue; // Componente UI para Exibição do Nível de Iluminação 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_light_sensor);
		
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		sensorValue = (ProgressBar) findViewById(R.id.sensorValue);
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
		float light = event.values[0]; // Valor de Iluminação Capturado
		Log.v("ILUMINAÇÃO DO AMBIENTE:", String.valueOf(light));
		sensorValue.setProgress((int) light); // Componente UI Atualizado com o valor
	}
}
