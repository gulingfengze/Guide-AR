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
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	MapView mMapView;
	BaiduMap mBaiduMap;
	private EditText et_search;
	private Button btn_search;
	private String content;//用户输入的要查询的内容
	//下面两个变量用于设置地图图层
	private static boolean isWeiXing=true ;
	private static boolean isSimple=false ;
	// UI相关
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true; // 是否首次定位

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现 ，这个方法必须写，不然无法应用地图（很多demo却不用）
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.map);

//		requestLocButton = (Button) findViewById(R.id.button1);
//		mCurrentMode = LocationMode.NORMAL;// 默认的模式
//		requestLocButton.setText("普通");
//		OnClickListener btnClickListener = new OnClickListener() {
//			public void onClick(View v) {
//				switch (mCurrentMode) {
//				case NORMAL:
//					requestLocButton.setText("跟随");
//					mCurrentMode = LocationMode.FOLLOWING;
//					mBaiduMap
//							.setMyLocationConfigeration(new MyLocationConfiguration(
//									mCurrentMode, true, mCurrentMarker));
//					break;
//				case COMPASS:
//					requestLocButton.setText("普通");
//					mCurrentMode = LocationMode.NORMAL;
//					mBaiduMap
//							.setMyLocationConfigeration(new MyLocationConfiguration(
//									mCurrentMode, true, mCurrentMarker));
//					break;
//				case FOLLOWING:
//					requestLocButton.setText("罗盘");
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

		
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
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
		case R.id.model://默认情况下地图普通显示，
			if (isWeiXing) {//这里当点击model按键时候首先判断isWeixing为true还是false，这里isWeiXing=true，所以当点击按钮时候执行切换到卫星地图模式
				isWeiXing = false;
				isSimple = true;//这里再分别为isWeingXing和isSimple赋初始值
				//卫星地图  
				mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
//				mMapView.setSatellite(isWeiXing);
//				mMapView.setTraffic(isSimple);
			} else if (isSimple) {//再次点击按钮之前isSimple已经被赋值为true，所以执行切换带普通地图模式
				isSimple = false;
				isWeiXing = true;
				//普通地图  
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
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);//这里设置了地图的最大放大级别
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
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	
}