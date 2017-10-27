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
	private static final String LIBRARY = "�Ͼ���Ϣ���̴�ѧͼ���";
	private static final String DIANXIN = "��������Ϣ����ѧԺ";
	private static final String XINKONG = "��Ϣ�����ѧԺ";
	private static final String HUANKE = "������ѧ�빤��ѧԺ";
	private static final String JIRUAN = "����������ѧԺ";
	private static final String DIYAO = "������ң��ѧԺ";
	private static final String LEIDING = "�׶�ѧԺ";
	/*-----------------------------------------------------------*/
	private static final String SPORT = "������";
	private static final String WENDELOU = "�ĵ�¥";
	private static final String QIXIANGLOU = "����¥";
	private static final String MINGDELOU = "����¥";
	private static final String SHANGXIANLOU = "����¥";
	private static final String YIFULOU = "�ݷ�¥";
	private static final String YINGYUJIAO = "Ӣ���";
	private static final String CHANGWANGTA = "������";
	private static final String FANZHONGYAN = "��������";
	private static final String XUCHANGWANG = "�쳤����";

	private TextView tv_base_content_title;
	private WebView wv;
	private ImageButton ib_base_content_back;
	private ImageButton ib_base_content_share;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_activity);
		/* ���ܴ��ݹ�����nameֵ */
		Intent i = getIntent();
		String data = i.getStringExtra("name_data");
		tv_base_content_title = (TextView) findViewById(R.id.tv_base_content_title);
		tv_base_content_title.setText(data);

		/* ���ؼ� */
		ib_base_content_back = (ImageButton) findViewById(R.id.ib_base_content_back);
		ib_base_content_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		/* ����� */
		ib_base_content_share = (ImageButton) findViewById(R.id.ib_base_content_share);
		ib_base_content_share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedApp.showShare(getApplicationContext());

			}
		});

		/* ��վ��ת */
		/*
		 * wv = (WebView) findViewById(R.id.wv);
		 * wv.getSettings().setJavaScriptEnabled(true); wv.setWebViewClient(new
		 * WebViewClient(){
		 * 
		 * @Override public boolean shouldOverrideUrlLoading(WebView view,
		 * String url) { view.loadUrl(url); return true; } });
		 */
		/* ���ݴ��ݵ�nameֵ�ж���Ե���Ȥ�㲢��������ҳ */
		if (data.equals(LIBRARY)) {// ͼ���

			// wv.loadUrl("http://lib.nuist.edu.cn/list.php?fid=11?fid=11&aid=82");

		} else if (data.equals(DIANXIN)) {// ����Ժ

		} else if (data.equals(XINKONG)) {// �ſ�Ժ

		} else if (data.equals(HUANKE)) {// ����Ժ

		} else if (data.equals(JIRUAN)) {// ����Ժ

			// wv.loadUrl("https://www.taobao.com");
		} else if (data.equals(DIYAO)) {// ��ңԺ

		} else if (data.equals(LEIDING)) {// �׶�ѧԺ

		} else if (data.equals(SPORT)) {// ������

		} else if (data.equals(WENDELOU)) {// �ĵ�¥

		} else if (data.equals(QIXIANGLOU)) {// ����¥

		} else if (data.equals(MINGDELOU)) {// ����¥

		} else if (data.equals(SHANGXIANLOU)) {// ����¥

		} else if (data.equals(YIFULOU)) {// �ݷ�¥

		} else if (data.equals(YINGYUJIAO)) {// Ӣ���

		} else if (data.equals(CHANGWANGTA)) {// ������

		} else if (data.equals(FANZHONGYAN)) {// ��������

		} else if (data.equals(XUCHANGWANG)) {// �쳤����

		}

	}

}
