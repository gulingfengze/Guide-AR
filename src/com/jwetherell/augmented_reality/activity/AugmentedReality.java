package com.jwetherell.augmented_reality.activity;

import java.text.DecimalFormat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.jwetherell.augmented_reality.R;
import com.jwetherell.augmented_reality.camera.CameraSurface;
import com.jwetherell.augmented_reality.data.ARData;
import com.jwetherell.augmented_reality.ui.Marker;
import com.jwetherell.augmented_reality.widget.VerticalSeekBar;
import com.jwetherell.augmented_reality.widget.VerticalTextView;

/**
 * �̳�SensorsActivity �����AugmentedView�͵��ڸˡ�
 */
public class AugmentedReality extends SensorsActivity implements
		OnTouchListener {

	private static final String TAG = "AugmentedReality";
	private static final DecimalFormat FORMAT = new DecimalFormat("#.##");
	private static final int ZOOMBAR_BACKGROUND_COLOR = Color.argb(125, 55, 55,55);// ���ڸ���ɫ
			
	private static final String END_TEXT = FORMAT
			.format(AugmentedReality.MAX_ZOOM) + " km";
	private static final int END_TEXT_COLOR = Color.WHITE;

	protected static WakeLock wakeLock = null;
	protected static CameraSurface camScreen = null;
	protected static VerticalSeekBar myZoomBar = null;
	protected static VerticalTextView endLabel = null;
	protected static LinearLayout zoomLayout = null;
	protected static LinearLayout navigationLayout = null;
	protected static AugmentedView augmentedView = null;
	
	protected static Button valueC = null;

	
	/*public static final float MAX_ZOOM = 100; // in KM ���������Χ����λ��KM
	public static final float ONE_PERCENT = MAX_ZOOM / 100f;
	public static final float TEN_PERCENT = 10f * ONE_PERCENT;
	public static final float TWENTY_PERCENT = 2f * TEN_PERCENT;
	public static final float EIGHTY_PERCENTY = 4f * TWENTY_PERCENT;*/
	
	public static final float MAX_ZOOM = 10; // in KM ���������Χ��Ϊ10KM����λ��KM
	public static final float ONE_PERCENT = MAX_ZOOM / 10f;//1km
	
	public static boolean ui_portrait = false; // Defaulted to LANDSCAPE use
	public static boolean showRadar = true;
	public static boolean showZoomBar = true;
	public static boolean useRadarAutoOrientate = true;
	public static boolean useMarkerAutoRotate = true;
	public static boolean useDataSmoothing = true;
	public static boolean useCollisionDetection = false; // defaulted OFF
	public static boolean navigation = true;//�������
	private AlertDialog dialog;//���öԻ���

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// camScreen = new CameraSurface(this);
		// setContentView(camScreen);
		//
		// augmentedView = new AugmentedView(this);
		// augmentedView.setOnTouchListener(this);
		// LayoutParams augLayout = new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// addContentView(augmentedView, augLayout);
		//
		// zoomLayout = new LinearLayout(this);
		// zoomLayout.setVisibility((showZoomBar) ? LinearLayout.VISIBLE :
		// LinearLayout.GONE);
		// zoomLayout.setOrientation(LinearLayout.VERTICAL);
		// zoomLayout.setPadding(5, 5, 5, 5);
		// zoomLayout.setBackgroundColor(ZOOMBAR_BACKGROUND_COLOR);
		//
		// endLabel = new VerticalTextView(this);
		// endLabel.setText(END_TEXT);
		// endLabel.setTextColor(END_TEXT_COLOR);
		// LinearLayout.LayoutParams zoomTextParams = new
		// LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// zoomTextParams.gravity = Gravity.CENTER;
		// zoomLayout.addView(endLabel, zoomTextParams);
		//
		// myZoomBar = new VerticalSeekBar(this);
		// myZoomBar.setMax(100);//���������Χ
		// myZoomBar.setProgress(50);//Ĭ�Ͽ�ʼ��������Χ
		// // myZoomBar.setMax(50);//���������Χ
		// // myZoomBar.setProgress(25);//Ĭ�Ͽ�ʼ��������Χ
		// myZoomBar.setOnSeekBarChangeListener(myZoomBarOnSeekBarChangeListener);
		// LinearLayout.LayoutParams zoomBarParams = new
		// LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.FILL_PARENT);
		// zoomBarParams.gravity = Gravity.CENTER_HORIZONTAL;
		// zoomLayout.addView(myZoomBar, zoomBarParams);
		//
		// FrameLayout.LayoutParams frameLayoutParams = new
		// FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.FILL_PARENT, Gravity.RIGHT);
		// addContentView(zoomLayout, frameLayoutParams);
		//
		// updateDataOnZoom();
		//
		// PowerManager pm = (PowerManager)
		// getSystemService(Context.POWER_SERVICE);
		// wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK,
		// "DoNotDimScreen");
		// eginan allages kai mpike dikos mou
		// kwdikas(����ע�Ͳ��ֲ��߱�������xml�����ļ�����������һ��xml�����ļ��������һ����չӦ��)
		
		
		camScreen = new CameraSurface(this);
		setContentView(R.layout.home_screen);
		camScreen.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
                 
		FrameLayout liveLayout = (FrameLayout) findViewById(R.id.liveImage);
		liveLayout.addView(camScreen);
		
		augmentedView = new AugmentedView(this);
		augmentedView.setOnTouchListener(this);
		augmentedView.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		liveLayout.addView(augmentedView);
/*--------------------------------------start---------------------------------------------------------------------*/
                         /*����������Χ���ڸ˺ͷ�Χ˵����������*/
		zoomLayout = new LinearLayout(this);
		zoomLayout.setVisibility((showZoomBar) ? LinearLayout.VISIBLE
				: LinearLayout.GONE);
		zoomLayout.setOrientation(LinearLayout.VERTICAL);//����ֱ
		zoomLayout.setPadding(5, 5, 5, 5);
		zoomLayout.setBackgroundColor(ZOOMBAR_BACKGROUND_COLOR);
		                /*1.����������Χ���ڸ��ϱ߷�Χ��Ϣ�ı���������*/
		endLabel = new VerticalTextView(this);
		endLabel.setText(END_TEXT);
		endLabel.setTextColor(END_TEXT_COLOR);
		
		LinearLayout.LayoutParams zoomTextParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		zoomTextParams.gravity = Gravity.CENTER;
		zoomLayout.addView(endLabel, zoomTextParams);
		                /*2.����������Χ���ڸ��ϲ�������*/
		myZoomBar = new VerticalSeekBar(this);

		myZoomBar.setMax(100);//�����ڶ�Ϊ100
		myZoomBar.setProgress(50);//Ĭ�������50�����
		
		myZoomBar.setOnSeekBarChangeListener(myZoomBarOnSeekBarChangeListener);
		LinearLayout.LayoutParams zoomBarParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		zoomBarParams.gravity = Gravity.CENTER_HORIZONTAL;
		zoomLayout.addView(myZoomBar, zoomBarParams);
		FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
				Gravity.RIGHT);
/*-----------------------------------------end------------------------------------------------------------------*/	
		
		/*-----------------------------------------start------------------------------------------------------------------*/			
		/*navigationLayout=new LinearLayout(this);
		navigationLayout.setVisibility((navigation)?LinearLayout.VISIBLE: LinearLayout.GONE);
		navigationLayout.setOrientation(LinearLayout.VERTICAL);//����ֱ
		navigationLayout.setPadding(5, 5, 5, 5);
		navigationLayout.setBackgroundColor(ZOOMBAR_BACKGROUND_COLOR);	
		iv = new ImageView(this);
		iv.setBackgroundResource(R.drawable.building);
		LinearLayout.LayoutParams navigationParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		navigationParams.gravity=Gravity.CENTER_VERTICAL;
		navigationLayout.addView(iv, navigationParams);*/
		/*FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				Gravity.CENTER_VERTICAL);*/
		//liveLayout.addView(navigationLayout, frameLayoutParams);
		/*-----------------------------------------end------------------------------------------------------------------*/	
		
		
		
		
		liveLayout.addView(zoomLayout, frameLayoutParams);//j��������Χ���ڸ˲��ּӵ���View��FrameLayout��������
		/*valueC = new Button(this);
        valueC.setText("NEW BUTTON");
        valueC.setId(8);
        valueC.setBackgroundColor(0x66444444);
		liveLayout.addView(valueC);*/
		

		updateDataOnZoom();

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm
				.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
		
		/*-----------------------------------������������-----------------------------------------*/
		/*findViewById(R.id.search).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new AlertDialog.Builder(AugmentedReality.this);
				        View view = View.inflate(getApplicationContext(), R.layout.dialog_search, null);
				        Button search = (Button) view.findViewById(R.id.search);
				        TextView poi=(TextView) view.findViewById(R.id.poi);
				        builder.setView(view );
				        
				        search.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								
								Toast.makeText(getApplicationContext(), "����POI��Ȥ�������", Toast.LENGTH_SHORT).show();
								//�رնԻ���
				                dialog.dismiss();
							}
						});
				        
				        dialog = builder.create();
				        dialog.show();
					}
				});*/
				

		/*-----------------------------------�б���ת-----------------------------------------*/ 
		findViewById(R.id.captureButton).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(AugmentedReality.this,Source.class);
//						 i.setComponent(new ComponentName(
//						 "com.jwetherell.augmented_reality.activity",
//						 "com.jwetherell.augmented_reality.activity.Source"));
						startActivity(i);
					}
				});
		/*-----------------------------------��ͼ��ת-----------------------------------------*/
				findViewById(R.id.mapButton).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent i = new Intent(AugmentedReality.this,BDMap.class);							
								startActivity(i);
							}
						});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResume() {
		super.onResume();

		wakeLock.acquire();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPause() {
		
		
		super.onPause();

		wakeLock.release();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSensorChanged(SensorEvent evt) {
		super.onSensorChanged(evt);

		if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER
				|| evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			augmentedView.postInvalidate();
		}
	}

	private OnSeekBarChangeListener myZoomBarOnSeekBarChangeListener = new OnSeekBarChangeListener() {

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			updateDataOnZoom();
			camScreen.invalidate();
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
		
		}

		public void onStopTrackingTouch(SeekBar seekBar) {
			updateDataOnZoom();
			camScreen.invalidate();
		}
	};
	private SurfaceHolder sh;
	private ImageView iv;

	private static float calcZoomLevel() {
		int myZoomLevel = myZoomBar.getProgress();
		float myout = 0;
		float percent = 0;
		
		
		/*--------start ---------*/
		 
		  if (myZoomLevel <= 100) {//��100�����ȷ�֮��
			percent = myZoomLevel / 10f;//ÿ10����Ϊһ������
		
			myout = ONE_PERCENT * percent;
		  }
		/*--------end---------*/
		  
		  
		  
		  
			 
		/*
		if (myZoomLevel <= 25) {
			percent = myZoomLevel / 25f;
			
			myout = ONE_PERCENT * percent;
		} else if (myZoomLevel > 25 && myZoomLevel <= 50) {
			percent = (myZoomLevel - 25f) / 25f;
			myout = ONE_PERCENT + (TEN_PERCENT * percent);
		} else if (myZoomLevel > 50 && myZoomLevel <= 75) {
			percent = (myZoomLevel - 50f) / 25f;
			myout = TEN_PERCENT + (TWENTY_PERCENT * percent);
		} else {
			percent = (myZoomLevel - 75f) / 25f;
			myout = TWENTY_PERCENT + (EIGHTY_PERCENTY * percent);
		}*/

		return myout;
	}

	/**
	 * ���ڸ˸ı�ʱ����
	 */
	protected void updateDataOnZoom() {
		float zoomLevel = calcZoomLevel();
		ARData.setRadius(zoomLevel);
		ARData.setZoomLevel(FORMAT.format(zoomLevel));
		ARData.setZoomProgress(myZoomBar.getProgress());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onTouch(View view, MotionEvent me) {
		if (me.getAction() != MotionEvent.ACTION_DOWN)
			return false;

		
		for (Marker marker : ARData.getMarkers()) {
			if (marker.handleClick(me.getX(), me.getY())) {
				markerTouched(marker);
				return true;
			}
		}

		return super.onTouchEvent(me);
	}

	protected void markerTouched(Marker marker) {
		Log.w(TAG,
				"markerTouched() not implemented. marker=" + marker.getName());

	}
	
	
}
