package com.sideslip;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.sideslip.util.CharacterParser;
import com.sideslip.util.MyAdapter;
import com.sideslip.util.PinyinComparator;
import com.sideslip.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends Activity implements OnClickListener {

	private SlidingLayer slidingLayer1, slidingLayer2;

	private ListView sortListView,listView1,listView2;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		slidingLayer1 = (SlidingLayer) findViewById(R.id.slidingLayer1);
		slidingLayer2 = (SlidingLayer) findViewById(R.id.slidingLayer2);

		initState();
		initListView();
	}

	private void initListView() {
		//实例化汉字转拼音类
				characterParser = CharacterParser.getInstance();
				
				pinyinComparator = new PinyinComparator();
				
				sideBar = (SideBar) findViewById(R.id.sidrbar);
				dialog = (TextView) findViewById(R.id.dialog);
				sideBar.setTextView(dialog);
				
				//设置右侧触摸监听
				sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
					
					@Override
					public void onTouchingLetterChanged(String s) {
						//该字母首次出现的位置
						int position = adapter.getPositionForSection(s.charAt(0));
						if(position != -1){
							sortListView.setSelection(position);
						}
						
					}
				});
				
				sortListView = (ListView) findViewById(R.id.country_lvcountry);
				sortListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						//这里要利用adapter.getItem(position)来获取当前position所对应的对象
						if (!slidingLayer1.isOpened()) {
							slidingLayer1.openLayer(true);
						}
					}
				});
				
				SourceDateList = filledData(getResources().getStringArray(R.array.date));
				
				// 根据a-z进行排序源数据
				Collections.sort(SourceDateList, pinyinComparator);
				adapter = new SortAdapter(this, SourceDateList);
				sortListView.setAdapter(adapter);
				
				listView1=(ListView) findViewById(R.id.list1);
				listView2=(ListView) findViewById(R.id.list2);
				
				final String[] strList=getResources().getStringArray(R.array.date);
				MyAdapter adapter=new MyAdapter(this, strList);
				listView1.setAdapter(adapter);
				listView2.setAdapter(adapter);
				
				listView1.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (!slidingLayer2.isOpened()) {
							slidingLayer2.openLayer(true);
						}
					}
				});
				listView2.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Toast.makeText(MainActivity.this, strList[arg2], Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void initState() {

		// Sticks container to right or left
		LayoutParams rlp1 = (LayoutParams) slidingLayer1.getLayoutParams();
		LayoutParams rlp2 = (LayoutParams) slidingLayer2.getLayoutParams();

		rlp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rlp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		slidingLayer1.setLayoutParams(rlp1);
		slidingLayer2.setLayoutParams(rlp2);

		slidingLayer1.setShadowWidth(0);
//		slidingLayer1.setShadowDrawable(R.drawable.sidebar_shadow);
		slidingLayer2.setShadowWidth(0);
//		slidingLayer2.setShadowDrawable(R.drawable.sidebar_shadow);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.btn:
//			if (!slidingLayer1.isOpened()) {
//				slidingLayer1.openLayer(true);
//			}
//			break;
//		case R.id.btn1:
//			if (!slidingLayer2.isOpened()) {
//				slidingLayer2.openLayer(true);
//			}
//			break;
//		case R.id.btn2:
//			Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
//			break;
		default:
			break;
		}
	}
	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
}
