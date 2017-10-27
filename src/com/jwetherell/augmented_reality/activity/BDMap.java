package com.jwetherell.augmented_reality.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.jwetherell.augmented_reality.R;

public class BDMap extends Activity implements OnClickListener{
	// ��λ���
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	MapView mMapView;
	BaiduMap mBaiduMap;
	private EditText et_search;
	private Button btn_search;
	private String content;//�û������Ҫ��ѯ������
	//�������������������õ�ͼͼ��
	private static boolean isWeiXing=true ;
	private static boolean isSimple=false ;
	// UI���
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true; // �Ƿ��״ζ�λ

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ�� �������������д����Ȼ�޷�Ӧ�õ�ͼ���ܶ�demoȴ���ã�
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.map);

//		requestLocButton = (Button) findViewById(R.id.button1);
//		mCurrentMode = LocationMode.NORMAL;// Ĭ�ϵ�ģʽ
//		requestLocButton.setText("��ͨ");
//		OnClickListener btnClickListener = new OnClickListener() {
//			public void onClick(View v) {
//				switch (mCurrentMode) {
//				case NORMAL:
//					requestLocButton.setText("����");
//					mCurrentMode = LocationMode.FOLLOWING;
//					mBaiduMap
//							.setMyLocationConfigeration(new MyLocationConfiguration(
//									mCurrentMode, true, mCurrentMarker));
//					break;
//				case COMPASS:
//					requestLocButton.setText("��ͨ");
//					mCurrentMode = LocationMode.NORMAL;
//					mBaiduMap
//							.setMyLocationConfigeration(new MyLocationConfiguration(
//									mCurrentMode, true, mCurrentMarker));
//					break;
//				case FOLLOWING:
//					requestLocButton.setText("����");
//					mCurrentMode = LocationMode.COMPASS;
//					mBaiduMap
//							.setMyLocationConfigeration(new MyLocationConfiguration(
//									mCurrentMode, true, mCurrentMarker));
//					break;
//				default:
//					break;
//				}
//			}
//		};
//		requestLocButton.setOnClickListener(btnClickListener);

		
		// ��ͼ��ʼ��
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// ������λͼ��
		mBaiduMap.setMyLocationEnabled(true);
		// ��λ��ʼ��
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		initView();

	}

	private void initView() {
		 //btn_search = (Button) findViewById(R.id.btn_search);
		 //btn_search.setOnClickListener(this);
		//et_search = (EditText) findViewById(R.id.et_search);
		//content = et_search.getText().toString();
		findViewById(R.id.model).setOnClickListener(this);
		findViewById(R.id.outdoor).setOnClickListener(this);
		//findViewById(R.id.rout).setOnClickListener(this);
		findViewById(R.id.indoor).setOnClickListener(this);	
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.model://Ĭ������µ�ͼ��ͨ��ʾ��
			if (isWeiXing) {//���ﵱ���model����ʱ�������ж�isWeixingΪtrue����false������isWeiXing=true�����Ե������ťʱ��ִ���л������ǵ�ͼģʽ
				isWeiXing = false;
				isSimple = true;//�����ٷֱ�ΪisWeingXing��isSimple����ʼֵ
				//���ǵ�ͼ  
				mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
//				mMapView.setSatellite(isWeiXing);
//				mMapView.setTraffic(isSimple);
			} else if (isSimple) {//�ٴε����ť֮ǰisSimple�Ѿ�����ֵΪtrue������ִ���л�����ͨ��ͼģʽ
				isSimple = false;
				isWeiXing = true;
				//��ͨ��ͼ  
				mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//				mMapView.setSatellite(isWeiXing);
//				mMapView.setTraffic(isSimple);
			}
			
			break;
		case R.id.outdoor:
			Intent i=new Intent(BDMap.this,AugmentedReality.class);
			startActivity(i);
			break;
		//case R.id.rout:
			
			//break;
		case R.id.indoor:
			
			break;
		//case R.id.btn_search:
			//content = et_search.getText().toString();
			//break;
		default:
			break;
		}
		
	}


	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);//���������˵�ͼ�����Ŵ󼶱�
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory
						.newMapStatus(builder.build()));
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	
}