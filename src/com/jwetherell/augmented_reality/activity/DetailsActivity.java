package com.jwetherell.augmented_reality.activity;

import com.jwetherell.augmented_reality.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

public class DetailsActivity extends Activity {
	private static final String LIBRARY = "南京信息工程大学图书馆";
	private static final String DIANXIN = "电子与信息工程学院";
	private static final String XINKONG = "信息与控制学院";
	private static final String HUANKE = "环境科学与工程学院";
	private static final String JIRUAN = "计算机与软件学院";
	private static final String DIYAO = "地理与遥感学院";
	private static final String LEIDING = "雷丁学院";
	/*-----------------------------------------------------------*/
	private static final String SPORT = "体育馆";
	private static final String WENDELOU = "文德楼";
	private static final String QIXIANGLOU = "气象楼";
	private static final String MINGDELOU = "明德楼";
	private static final String SHANGXIANLOU = "尚贤楼";
	private static final String YIFULOU = "逸夫楼";
	private static final String YINGYUJIAO = "英语角";
	private static final String CHANGWANGTA = "长望塔";
	private static final String FANZHONGYAN = "范仲淹像";
	private static final String XUCHANGWANG = "徐长望像";

	private TextView tv_base_content_title;
	private WebView wv;
	private ImageButton ib_base_content_back;
	private ImageButton ib_base_content_share;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_activity);
		/* 接受传递过来的name值 */
		Intent i = getIntent();
		String data = i.getStringExtra("name_data");
		tv_base_content_title = (TextView) findViewById(R.id.tv_base_content_title);
		tv_base_content_title.setText(data);

		/* 返回键 */
		ib_base_content_back = (ImageButton) findViewById(R.id.ib_base_content_back);
		ib_base_content_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		/* 分享键 */
		ib_base_content_share = (ImageButton) findViewById(R.id.ib_base_content_share);
		ib_base_content_share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedApp.showShare(getApplicationContext());

			}
		});

		/* 网站跳转 */
		/*
		 * wv = (WebView) findViewById(R.id.wv);
		 * wv.getSettings().setJavaScriptEnabled(true); wv.setWebViewClient(new
		 * WebViewClient(){
		 * 
		 * @Override public boolean shouldOverrideUrlLoading(WebView view,
		 * String url) { view.loadUrl(url); return true; } });
		 */
		/* 根据传递的name值判断相对的兴趣点并进入其主页 */
		if (data.equals(LIBRARY)) {// 图书馆

			// wv.loadUrl("http://lib.nuist.edu.cn/list.php?fid=11?fid=11&aid=82");

		} else if (data.equals(DIANXIN)) {// 电信院

		} else if (data.equals(XINKONG)) {// 信控院

		} else if (data.equals(HUANKE)) {// 环科院

		} else if (data.equals(JIRUAN)) {// 计软院

			// wv.loadUrl("https://www.taobao.com");
		} else if (data.equals(DIYAO)) {// 地遥院

		} else if (data.equals(LEIDING)) {// 雷丁学院

		} else if (data.equals(SPORT)) {// 体育馆

		} else if (data.equals(WENDELOU)) {// 文德楼

		} else if (data.equals(QIXIANGLOU)) {// 气象楼

		} else if (data.equals(MINGDELOU)) {// 明德楼

		} else if (data.equals(SHANGXIANLOU)) {// 尚贤楼

		} else if (data.equals(YIFULOU)) {// 逸夫楼

		} else if (data.equals(YINGYUJIAO)) {// 英语角

		} else if (data.equals(CHANGWANGTA)) {// 长望塔

		} else if (data.equals(FANZHONGYAN)) {// 范仲淹像

		} else if (data.equals(XUCHANGWANG)) {// 徐长望像

		}

	}

}
