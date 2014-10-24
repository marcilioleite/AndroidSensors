package br.snct.sensors;
import java.util.Arrays;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class AccelerometerSensorActivity extends Activity implements SensorEventListener {

	private SensorManager 	sensorManager;
	private Sensor 			sensor; 
	
	private long xValue;
	private XYSeries xSeries, ySeries, zSeries;
	private XYMultipleSeriesDataset dataset;
	private XYSeriesRenderer xRenderer, yRenderer, zRenderer;
	private XYMultipleSeriesRenderer renderer;
	private GraphicalView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		
		xSeries = new XYSeries("x");
		ySeries = new XYSeries("y");
		zSeries = new XYSeries("z");
		
		dataset = new XYMultipleSeriesDataset();
		dataset.addAllSeries(Arrays.asList(new XYSeries[] {
			xSeries, 
			ySeries, 
			zSeries
		}));
		
		xRenderer = new XYSeriesRenderer();
		xRenderer.setColor(Color.YELLOW);
		yRenderer = new XYSeriesRenderer();
		yRenderer.setColor(Color.MAGENTA);
		zRenderer = new XYSeriesRenderer();
		zRenderer.setColor(Color.CYAN);
		
		renderer = new XYMultipleSeriesRenderer();
		renderer.addSeriesRenderer(xRenderer);
		renderer.addSeriesRenderer(yRenderer);
		renderer.addSeriesRenderer(zRenderer);
		renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.BLACK);
	    renderer.setAxisTitleTextSize(16);
	    renderer.setChartTitleTextSize(20);
	    renderer.setLabelsTextSize(15);
	    renderer.setLegendTextSize(15);
	    renderer.setMargins(new int[] { 20, 30, 15, 0 });
	    renderer.setZoomButtonsVisible(true);
	    renderer.setPointSize(5);
	    renderer.setXAxisMin(Math.max(0, xValue-20));
		renderer.setXAxisMax(xValue+30);
	    
	    view = ChartFactory.getLineChartView(AccelerometerSensorActivity.this, dataset, renderer);
	    
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(view);
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
		float accelX = event.values[SensorManager.DATA_X];
		float accelY = event.values[SensorManager.DATA_Y];
		float accelZ = event.values[SensorManager.DATA_Z];
		
		xSeries.add(xValue, accelX);
		ySeries.add(xValue, accelY);
		zSeries.add(xValue, accelZ);
		
		renderer.setXAxisMin(Math.max(0, xValue-20));
		renderer.setXAxisMax(xValue+30);
		
		view.repaint();
		xValue++;
	}
}
