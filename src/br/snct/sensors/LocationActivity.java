package br.snct.sensors;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidsensors.R;

public class LocationActivity extends Activity {

	GpsService gps;
	
	TextView lat, lng, address;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		gps = new GpsService(LocationActivity.this);
		gps.getLocation();
		lat = (TextView) findViewById(R.id.textView2);
		lng = (TextView) findViewById(R.id.textView4);
		address = (TextView) findViewById(R.id.textView6);
		
		final Handler handler = new Handler();
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				if (!gps.isAvailable()) {
					Toast.makeText(getApplicationContext(), "Localização não disponível", Toast.LENGTH_SHORT).show();
					return;
				} else {
					double latitude = gps.getLatitude();
					lat.setText(String.valueOf(latitude));
					double longitude = gps.getLongitude();
					lng.setText(String.valueOf(longitude));
					String addr = gps.getLocationAddress();
					address.setText(addr);
					handler.postDelayed(this, 2000);
				}				
			}
		};
		handler.post(task);
	}
	
}
