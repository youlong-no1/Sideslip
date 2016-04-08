package com.sideslip.util;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sideslip.R;

public class MyAdapter extends BaseAdapter{
	
	private Context context;
	private String[] list;
	public MyAdapter(Context context,String[] list){
		this.context=context;
		this.list=list;
	}
	@Override
	public int getCount() {
		return list.length;
	}

	@Override
	public Object getItem(int arg0) {
		return list[arg0];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=LayoutInflater.from(context).inflate(R.layout.item1, null);
		TextView txt=(TextView) view.findViewById(R.id.txt);
		txt.setText(list[position]);
		return view;
	}

}
