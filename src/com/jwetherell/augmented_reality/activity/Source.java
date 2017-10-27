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
		adapter.add(new ListCellData(this, "信息与控制学院", new Intent(this,
				Xinkongyuan.class)));
		
		adapter.add(new ListCellData(this, "电子与信息工程学院", new Intent(this,
				Dianxinyuan.class)));
		
		adapter.add(new ListCellData(this, "大气科学学院", new Intent(this,
				Daqikexue.class)));
		
		adapter.add(new ListCellData(this, "大气物理学院", new Intent(this,
				Daqiwuli.class)));
		
		adapter.add(new ListCellData(this, "应用气象学院", new Intent(this,
				Yingyongqixiang.class)));
		
		adapter.add(new ListCellData(this, "水文气象学院", new Intent(this,
				Shuiwenqixiang.class)));
		
		adapter.add(new ListCellData(this, "地理与遥感学院", new Intent(this,
				Diliyuyaogan.class)));
		
		adapter.add(new ListCellData(this, "传媒与艺术学院", new Intent(this,
				Chuanmeiyuyishu.class)));
		
		adapter.add(new ListCellData(this, "环境科学与工程学院", new Intent(this,
				Huanke.class)));
		
		adapter.add(new ListCellData(this, "计算机与软件学院", new Intent(this,
				Jiruan.class)));
		
		adapter.add(new ListCellData(this, "数学与统计学院", new Intent(this,
				Shutong.class)));
		
		adapter.add(new ListCellData(this, "物理与光电工程学院", new Intent(this,
				Wudian.class)));
		
		adapter.add(new ListCellData(this, "公共管理学院", new Intent(this,
				Gongguan.class)));
		
		adapter.add(new ListCellData(this, "经济管理学院", new Intent(this,
				Jingguan.class)));
		
		adapter.add(new ListCellData(this, "语言文化学院", new Intent(this,
				Yuyuan.class)));
		
		adapter.add(new ListCellData(this, "海洋科学学院", new Intent(this,
				Haiyang.class)));
		
		adapter.add(new ListCellData(this, "雷丁学院", new Intent(this,
				Leiding.class)));
		
		adapter.add(new ListCellData(this, "马克思主义学院", new Intent(this,
				Makesi.class)));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		ListCellData data = adapter.getItem(position);
		data.startActivity();
		super.onListItemClick(l, v, position, id);
	}

}
