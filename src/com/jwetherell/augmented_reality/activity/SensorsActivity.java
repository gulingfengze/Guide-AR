package com.jwetherell.augmented_reality.activity;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;

import com.jwetherell.augmented_reality.common.LowPassFilter;
import com.jwetherell.augmented_reality.common.Matrix;
import com.jwetherell.augmented_reality.common.Navigation;
import com.jwetherell.augmented_reality.common.Orientation;
import com.jwetherell.augmented_reality.data.ARData;

/**
 * �����������ݺ�λ������
 */
public class SensorsActivity extends Activity implements SensorEventListener,
		LocationListener {

	private static final String TAG = "SensorsActivity";
	private static final AtomicBoolean computing = new AtomicBoolean(false);

	private static final int MIN_TIME = 30 * 1000;
	private static final int MIN_DISTANCE = 10;

	private static final float temp[] = new float[9]; // Temporary rotation
														// matrix in Android
														// format
	private static final float rotation[] = new float[9]; // Final rotation
															// matrix in Android
															// format
	private static final float grav[] = new float[3]; // Gravity (a.k.a
														// accelerometer data)
	private static final float mag[] = new float[3]; // Magnetic

	/*
	 * �������㡣 private static final float apr[] = new float[3]; //Azimuth, pitch, roll
	 */

	private static final Matrix worldCoord = new Matrix();
	private static final Matrix magneticCompensatedCoord = new Matrix();
	private static final Matrix xAxisRotation = new Matrix();
	private static final Matrix yAxisRotation = new Matrix();
	private static final Matrix mageticNorthCompensation = new Matrix();

	private static GeomagneticField gmf = null;
	private static float smooth[] = new float[3];
	private static SensorManager sensorMgr = null;
	private static List<Sensor> sensors = null;
	private static Sensor sensorGrav = null;
	private static Sensor sensorMag = null;
	private static LocationManager locationMgr = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStart() {
		super.onStart();

		float neg90rads = (float) Math.toRadians(-90);

	
		xAxisRotation.set(1f, 0f, 0f, 0f, FloatMath.cos(neg90rads),
				-FloatMath.sin(neg90rads), 0f, FloatMath.sin(neg90rads),
				FloatMath.cos(neg90rads));

	
		yAxisRotation.set(FloatMath.cos(neg90rads), 0f,
				FloatMath.sin(neg90rads), 0f, 1f, 0f,
				-FloatMath.sin(neg90rads), 0f, FloatMath.cos(neg90rads));

		try {
			sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

			sensors = sensorMgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
			if (sensors.size() > 0)
				sensorGrav = sensors.get(0);

			sensors = sensorMgr.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
			if (sensors.size() > 0)
				sensorMag = sensors.get(0);

			sensorMgr.registerListener(this, sensorGrav,
					SensorManager.SENSOR_DELAY_UI);
			sensorMgr.registerListener(this, sensorMag,
					SensorManager.SENSOR_DELAY_UI);
//----------------------------------------------------------------------------------------------------------
			locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					MIN_TIME, MIN_DISTANCE, this);

			try {

				try {
					Location gps = locationMgr
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					Location network = locationMgr
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (gps != null)
						onLocationChanged(gps);
					else if (network != null)
						onLocationChanged(network);
					else
						onLocationChanged(ARData.hardFix);
				} catch (Exception ex2) {
					onLocationChanged(ARData.hardFix);
				}

				gmf = new GeomagneticField((float) ARData.getCurrentLocation()
						.getLatitude(), (float) ARData.getCurrentLocation()
						.getLongitude(), (float) ARData.getCurrentLocation()
						.getAltitude(), System.currentTimeMillis());

				float dec = (float) Math.toRadians(-gmf.getDeclination());

				synchronized (mageticNorthCompensation) {
					// ��λ����
					// [ 1, 0, 0 ]
					// [ 0, 1, 0 ]
					// [ 0, 0, 1 ]
					mageticNorthCompensation.toIdentity();

					
					mageticNorthCompensation.set(FloatMath.cos(dec), 0f,
							FloatMath.sin(dec), 0f, 1f, 0f,
							-FloatMath.sin(dec), 0f, FloatMath.cos(dec));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex1) {
			try {
				if (sensorMgr != null) {
					sensorMgr.unregisterListener(this, sensorGrav);
					sensorMgr.unregisterListener(this, sensorMag);
					sensorMgr = null;
				}
				if (locationMgr != null) {
					locationMgr.removeUpdates(this);
					locationMgr = null;
				}
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onStop() {
		super.onStop();

		try {
			try {
				sensorMgr.unregisterListener(this, sensorGrav);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				sensorMgr.unregisterListener(this, sensorMag);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			sensorMgr = null;

			try {
				locationMgr.removeUpdates(this);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			locationMgr = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSensorChanged(SensorEvent evt) {
		if (!computing.compareAndSet(false, true))
			return;

		if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			if (AugmentedReality.useDataSmoothing) {
				smooth = LowPassFilter.filter(0.5f, 1.0f, evt.values, grav);
				grav[0] = smooth[0];
				grav[1] = smooth[1];
				grav[2] = smooth[2];
			} else {
				grav[0] = evt.values[0];
				grav[1] = evt.values[1];
				grav[2] = evt.values[2];
			}
			Orientation.calcOrientation(grav);
			ARData.setDeviceOrientation(Orientation.getDeviceOrientation());
			ARData.setDeviceOrientationAngle(Orientation.getDeviceAngle());
		} else if (evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			if (AugmentedReality.useDataSmoothing) {
				smooth = LowPassFilter.filter(2.0f, 4.0f, evt.values, mag);
				mag[0] = smooth[0];
				mag[1] = smooth[1];
				mag[2] = smooth[2];
			} else {
				mag[0] = evt.values[0];
				mag[1] = evt.values[1];
				mag[2] = evt.values[2];
			}
		}

		// Find real world position relative to phone location����������ֻ�λ�õ���ʵ����λ��
		// Get rotation matrix given the gravity and geomagnetic matrices���������ʹž���õ���ת����
		SensorManager.getRotationMatrix(temp, null, grav, mag);

		SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_Y,
				SensorManager.AXIS_MINUS_Z, rotation);


		// ��float 9ת��Ϊ����
		worldCoord
				.set(rotation[0], rotation[1], rotation[2], rotation[3],
						rotation[4], rotation[5], rotation[6], rotation[7],
						rotation[8]);

		// // �ҵ�����ڴű�����λ�� ////

		magneticCompensatedCoord.toIdentity();

		synchronized (mageticNorthCompensation) {
			//  ����˻���ű������ľ���
			magneticCompensatedCoord.prod(mageticNorthCompensation);
		}


		// ���̼�����Ļ���ƽ����Ļָ����գ���ת����
		
		
		magneticCompensatedCoord.prod(xAxisRotation);


		//    ���������꽻��˻��õ�һ��mag����������
		magneticCompensatedCoord.prod(worldCoord);

		// Y axis
		magneticCompensatedCoord.prod(yAxisRotation);


		// ��ת������Ϊ���º������ں���ģʽ�����෴��
		magneticCompensatedCoord.invert();

		// ������ת�������ڽ����ж����lat / lonת��Ϊx / y / z
		ARData.setRotationMatrix(magneticCompensatedCoord);

		// ʹ���ֻ�����ת�������Ǻͷ�λ
		Navigation.calcPitchBearing(magneticCompensatedCoord);
		ARData.setAzimuth(Navigation.getAzimuth());

		computing.set(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProviderDisabled(String provider) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProviderEnabled(String provider) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLocationChanged(Location location) {
		ARData.setCurrentLocation(location);
		gmf = new GeomagneticField((float) ARData.getCurrentLocation()
				.getLatitude(), (float) ARData.getCurrentLocation()
				.getLongitude(), (float) ARData.getCurrentLocation()
				.getAltitude(), System.currentTimeMillis());

		float dec = (float) Math.toRadians(-gmf.getDeclination());

		synchronized (mageticNorthCompensation) {
			mageticNorthCompensation.toIdentity();

			mageticNorthCompensation.set(FloatMath.cos(dec), 0f,
					FloatMath.sin(dec), 0f, 1f, 0f, -FloatMath.sin(dec), 0f,
					FloatMath.cos(dec));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		if (sensor == null)
			throw new NullPointerException();

		if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
				&& accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
			Log.e(TAG, "Compass data unreliable");
		}
	}
	
	

}
