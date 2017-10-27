package com.jwetherell.augmented_reality.activity;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jwetherell.augmented_reality.R;
import com.jwetherell.augmented_reality.common.Navigation;
import com.jwetherell.augmented_reality.data.ARData;
import com.jwetherell.augmented_reality.data.BaiduPlacesDataSource;
import com.jwetherell.augmented_reality.data.GooglePlacesDataSource;
import com.jwetherell.augmented_reality.data.LocalDataSource;
import com.jwetherell.augmented_reality.data.NetworkDataSource;
import com.jwetherell.augmented_reality.data.WikipediaDataSource;
import com.jwetherell.augmented_reality.data.XiaoyuanDataSource;
import com.jwetherell.augmented_reality.ui.Marker;
import com.jwetherell.augmented_reality.widget.VerticalTextView;

/**
 * 
 * 此类扩展自AugmentedReality，作为一个如何去扩展自AugmentedReality类去显示多个数据源的数据信息。
 * 
 */
public class Demo extends AugmentedReality {
	/*private static final String LIBRARY = "南京信息工程大学图书馆";
	private static final String DIANXIN = "电子与信息工程学院";
	private static final String XINKONG = "信息与控制学院";
	private static final String HUANKE = "环境科学与工程学院";
	private static final String JIRUAN = "计算机与软件学院";
	private static final String DIYAO = "地理与遥感学院";
	private static final String LEIDING = "雷丁学院";*/
	
	
	
    private static final String TAG = "Demo";
//    private static final String locale = Locale.getDefault().getLanguage();//设置语言
    
   //测试,这里设置成中文
    private static final String locale ="zh-CN";
    
    
    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1);
    private static final ThreadPoolExecutor exeService = new ThreadPoolExecutor(1, 1, 20, TimeUnit.SECONDS, queue);
    private static final Map<String, NetworkDataSource> sources = new ConcurrentHashMap<String, NetworkDataSource>();

    private static Toast myToast = null;
    private static VerticalTextView text = null;
    
    private AlertDialog dialog;//设置对话框
	private TextView contens;


    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 创建 toast
        myToast = new Toast(getApplicationContext());
        myToast.setGravity(Gravity.CENTER, 0, 0);
        // 创建自定义text文本：Creating our custom text view, and setting text/rotation
        text = new VerticalTextView(getApplicationContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        text.setLayoutParams(params);
        text.setBackgroundResource(android.R.drawable.toast_frame);
        text.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_Small);
        text.setShadowLayer(2.75f, 0f, 0f, Color.parseColor("#BB000000"));
        myToast.setView(text);
        // 设置toast显示时间
        myToast.setDuration(Toast.LENGTH_SHORT);

        // 本地数据源
        LocalDataSource localData = new LocalDataSource(this.getResources());
        ARData.addMarkers(localData.getMarkers());
         //维基百科数据---访问不了
        NetworkDataSource wikipedia = new WikipediaDataSource(this.getResources());
        sources.put("wiki", wikipedia);
        //谷歌数据源---访问不了
        NetworkDataSource googlePlaces = new GooglePlacesDataSource(this.getResources());
        sources.put("googlePlaces", googlePlaces);
        //百度数据源---数据获取不到--按照文档应该没错，但在手机端读不到数据，暂且搁置。
        NetworkDataSource baiduPlaces = new BaiduPlacesDataSource(this.getResources());
        sources.put("baiduPlaces", baiduPlaces);


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart() {
        super.onStart();

        Location last = ARData.getCurrentLocation();
        updateData(last.getLatitude(), last.getLongitude(), last.getAltitude());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "onOptionsItemSelected() item=" + item);
        switch (item.getItemId()) {
            case R.id.showRadar:
                showRadar = !showRadar;
               
                item.setTitle(((showRadar) ? "隐藏" : "显示") + " 雷达");
                break;
            case R.id.showZoomBar:
                showZoomBar = !showZoomBar;
              
                item.setTitle(((showZoomBar) ? "隐藏" : "显示") + " 调节杆");
                zoomLayout.setVisibility((showZoomBar) ? LinearLayout.VISIBLE : LinearLayout.GONE);
                break;
            case R.id.exit:
                finish();
                break;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);

        updateData(location.getLatitude(), location.getLongitude(), location.getAltitude());
    }

    /**
     * 点击兴趣点图标时候弹出对话框（导航及详情）
     */
    @Override
    protected void markerTouched(Marker marker) {
/*//        text.setText(marker.getName());
//        myToast.show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_enter_password, null);
        Button detail = (Button) view.findViewById(R.id.detail);
        Button navigation = (Button) view.findViewById(R.id.navigation);
        contens = (TextView) view.findViewById(R.id.contents);
        contens.setText(marker.getName());//获取兴趣点名字并提供给TextView文本显示
       
        builder.setView(view );
        //兴趣点详情
        detail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(getApplicationContext(), "跳转到详情界面", Toast.LENGTH_SHORT).show();
				//关闭对话框
                dialog.dismiss();
			}
		});
        //兴趣 点导航
        navigation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 
				Toast.makeText(getApplicationContext(), "跳转到导航界面", Toast.LENGTH_SHORT).show();
				//关闭对话框
                dialog.dismiss();
                
			}
		});

        dialog = builder.create();
        dialog.show();*/
    	 String name_data=marker.getName();//获取兴趣点名字用于向下一个活动传递
        Intent i=new Intent(Demo.this,DetailsActivity.class);
		i.putExtra("name_data", name_data);
        startActivity(i);
        //Toast.makeText(getApplicationContext(), "skdjakldjkalkdjkal;sdjkaldjald", Toast.LENGTH_LONG).show();
    	 /*if(name_data.equals(LIBRARY)){
 			Intent i1=new Intent(Intent.ACTION_VIEW);
 			i1.setData(Uri.parse("http://www.baidu.com"));
 			
 		}else if (name_data.equals(DIANXIN)) {
 			
 		}else if (name_data.equals(XINKONG)) {
 			
 		}else if (name_data.equals(HUANKE)) {
 			
 		}else if (name_data.equals(JIRUAN)) {
 			Intent i1=new Intent(Intent.ACTION_VIEW);
 			i1.setData(Uri.parse("https://www.taobao.com"));
 		}else if (name_data.equals(DIYAO)) {
 			
 		}else if (name_data.equals(LEIDING)) {
 			
 		}*/
       
    }
    
   
    /**
     * 更新搜索数据
     */
    @Override
    protected void updateDataOnZoom() {
        super.updateDataOnZoom();
        Location last = ARData.getCurrentLocation();
        updateData(last.getLatitude(), last.getLongitude(), last.getAltitude());
    }

    private void updateData(final double lat, final double lon, final double alt) {
        try {
            exeService.execute(new Runnable() {
                @Override
                public void run() {
                    for (NetworkDataSource source : sources.values())
                        download(source, lat, lon, alt);
                }
            });
        } catch (RejectedExecutionException rej) {
            Log.w(TAG, "Not running new download Runnable, queue is full.");
        } catch (Exception e) {
            Log.e(TAG, "Exception running download Runnable.", e);
        }
    }

    private static boolean download(NetworkDataSource source, double lat, double lon, double alt) {
        if (source == null) return false;

        String url = null;
        try {
            url = source.createRequestURL(lat, lon, alt, ARData.getRadius(), locale);
        } catch (NullPointerException e) {
            return false;
        }

        List<Marker> markers = null;
        try {
            markers = source.parse(url);
        } catch (NullPointerException e) {
            return false;
        }

        ARData.addMarkers(markers);
        return true;
    }
}
