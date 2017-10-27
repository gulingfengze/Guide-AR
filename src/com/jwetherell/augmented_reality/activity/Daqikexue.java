package com.jwetherell.augmented_reality.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jwetherell.augmented_reality.R;

public class Daqikexue extends Activity {
	private WebView webView;
         @Override
        protected void onCreate(Bundle savedInstanceState) {
        	// TODO Auto-generated method stub
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.daqikexue);
        	webView=(WebView) findViewById(R.id.web_view);
        	webView.getSettings().setJavaScriptEnabled(true);
        	webView.setWebViewClient(new WebViewClient(){
        		@Override
        		public boolean shouldOverrideUrlLoading(WebView view, String url) {
        			view.loadUrl(url);
        			return true;
        		}
        	});
        	webView.loadUrl("http://cas.nuist.edu.cn/");
        }
}