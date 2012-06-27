package com.ketan.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ListOfFrame extends Activity {

	private ListView lv;
	static final String[] FrameList = new String[] {
			"antique look hand carved mirror frame", "back and gold frame",
			"balloons frame", "blue photo frame", "flowers frame",
			"carton frame", "photo barbie frame", "photo frame",
			"fss fall frame", "gold frame", "gold rose frame",
			"heart wedding flower frame", "love picture frame", "mickey frame",
			"miss you frame", "mysticmorning frame", "photo frame",
			"pink yellow scrapbook frame", "wc toocute frame", "wedding frame",
			"wedding photo frame","yellow dotted frame" };
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_of_frame);

		lv = (ListView) findViewById(R.id.list_of_frame_listView);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, FrameList);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				final Intent intent = new Intent();
				// Intent intent = new Intent(ListOfFrame.this,
				// TransparentImgcropActivity.class);
				intent.putExtra("pos", arg2);
				// startActivity(intent);
				setResult(RESULT_OK, intent);
				finish();
			}

		});
	}
}
