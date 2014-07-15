package com.yozisunji.palipalette;

import com.samsung.android.example.helloaccessoryprovider.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CustomizingActivity extends Activity {
	PaliScreen screen;
	GridLayout.LayoutParams layout;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		setContentView(R.layout.customizing);
		
		this.screen =(PaliScreen) findViewById(R.id.screen_edit);
		LinearLayout.LayoutParams lilayout = (LinearLayout.LayoutParams)this.screen.getLayoutParams();
		
		this.screen.copy(CustomizingMainActivity.screens.get(0),lilayout.width,lilayout.height);
		
		ExpandableListView listview = (ExpandableListView)this.findViewById(R.id.itemmainlistview);
		listview.setAdapter(new PaliExpandableAdapter(this,CustomizingMainActivity.GearUIList));
		listview.expandGroup(0);
		listview.expandGroup(1);
		listview.expandGroup(2);
		listview.expandGroup(3);
		listview.expandGroup(4);
	}
}
