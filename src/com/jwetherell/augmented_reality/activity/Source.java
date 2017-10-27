package com.jwetherell.augmented_reality.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Source extends ListActivity {
	private ArrayAdapter<ListCellData> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new ArrayAdapter<ListCellData>(this,
				android.R.layout.simple_list_item_1);
		setListAdapter(adapter);
		adapter.add(new ListCellData(this, "��Ϣ�����ѧԺ", new Intent(this,
				Xinkongyuan.class)));
		
		adapter.add(new ListCellData(this, "��������Ϣ����ѧԺ", new Intent(this,
				Dianxinyuan.class)));
		
		adapter.add(new ListCellData(this, "������ѧѧԺ", new Intent(this,
				Daqikexue.class)));
		
		adapter.add(new ListCellData(this, "��������ѧԺ", new Intent(this,
				Daqiwuli.class)));
		
		adapter.add(new ListCellData(this, "Ӧ������ѧԺ", new Intent(this,
				Yingyongqixiang.class)));
		
		adapter.add(new ListCellData(this, "ˮ������ѧԺ", new Intent(this,
				Shuiwenqixiang.class)));
		
		adapter.add(new ListCellData(this, "������ң��ѧԺ", new Intent(this,
				Diliyuyaogan.class)));
		
		adapter.add(new ListCellData(this, "��ý������ѧԺ", new Intent(this,
				Chuanmeiyuyishu.class)));
		
		adapter.add(new ListCellData(this, "������ѧ�빤��ѧԺ", new Intent(this,
				Huanke.class)));
		
		adapter.add(new ListCellData(this, "����������ѧԺ", new Intent(this,
				Jiruan.class)));
		
		adapter.add(new ListCellData(this, "��ѧ��ͳ��ѧԺ", new Intent(this,
				Shutong.class)));
		
		adapter.add(new ListCellData(this, "�������繤��ѧԺ", new Intent(this,
				Wudian.class)));
		
		adapter.add(new ListCellData(this, "��������ѧԺ", new Intent(this,
				Gongguan.class)));
		
		adapter.add(new ListCellData(this, "���ù���ѧԺ", new Intent(this,
				Jingguan.class)));
		
		adapter.add(new ListCellData(this, "�����Ļ�ѧԺ", new Intent(this,
				Yuyuan.class)));
		
		adapter.add(new ListCellData(this, "�����ѧѧԺ", new Intent(this,
				Haiyang.class)));
		
		adapter.add(new ListCellData(this, "�׶�ѧԺ", new Intent(this,
				Leiding.class)));
		
		adapter.add(new ListCellData(this, "���˼����ѧԺ", new Intent(this,
				Makesi.class)));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		ListCellData data = adapter.getItem(position);
		data.startActivity();
		super.onListItemClick(l, v, position, id);
	}

}
