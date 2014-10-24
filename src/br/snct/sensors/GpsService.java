package br.snct.sensors;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GpsService extends Service implements LocationListener {

	private Context context;
	private LocationManager locationManager;
	private double latitude, longitude;
	private boolean enabled, available;
	
	public GpsService(Context context) {
		this.context = context;
		this.locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
		this.enabled = false;
		this.available = false;
	}
	
	public Location getLocation() {
		try {
			enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			
			if (enabled) {
				long intervalo = 1000*10; // 10 segundo
				float distancia = 10; // 10 metros
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, intervalo, distancia, this);
				if (locationManager != null) {
					Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (location != null) {
						latitude = location.getLatitude();
						longitude = location.getLongitude();
						System.out.println(latitude);
						System.out.println(longitude);
						Log.v("LAT:", String.valueOf(latitude));
						Log.v("LNG:", String.valueOf(longitude));
						available = true;
						return location;
					}
				}
			}
			
			if (!enabled) {
				ask();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		available = false;
		return null;
	}
	
	public String getLocationAddress() {
		 
        if (available) {
 
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e1) {
                e1.printStackTrace();
                return ("IO Exception trying to get address:" + e1);
            } catch (IllegalArgumentException e2) {
                String errorString = "Illegal arguments "
                        + Double.toString(latitude) + " , "
                        + Double.toString(longitude)
                        + " passed to address service";
                e2.printStackTrace();
                return errorString;
            }

            if (addresses != null && addresses.size() > 0) {

            	Address address = addresses.get(0);

            	String addressText = String.format(
                        "%s, %s, %s",

                        address.getMaxAddressLineIndex() > 0 ? address
                                .getAddressLine(0) : "",
                        address.getLocality(),
                        address.getCountryName());

            	System.out.println(addressText);
            	return addressText;
            } else {
                return "Nenhum endereço encontrado pelo serviço.";
            }
        } else {
            return "Localização não disponível";
        }
 
    }
	
	public void ask() {
		
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(context);
 
        mAlertDialog.setTitle("Permitir o uso do GPS?")
        .setMessage("Ativar o GPS para receber localizações?")
        .setPositiveButton("Abrir Configurações", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
                }
            })
            .setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    }
                }).show();
    }
	
	public boolean isAvailable() {
		return available;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}
	
	public void close() {
		if (locationManager != null) {
			locationManager.removeUpdates(GpsService.this);
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
	}

	@Override
	public void onProviderEnabled(String arg0) {
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
