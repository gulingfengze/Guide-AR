package com.jwetherell.augmented_reality.activity;

import com.jwetherell.augmented_reality.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Jiruan extends Activity {
	private WebView webView;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.jiruan);
	webView=(WebView) findViewById(R.id.web_view);
	webView.getSettings().setJavaScriptEnabled(true);
	webView.setWebViewClient(new WebViewClient(){
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	});
	webView.loadUrl("https://cs.nuist.edu.cn/index.action");
}
}
