package com.yozisunji.palipalette;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.GridLayout.Spec;

public class CustomizingActivity extends Activity {
	Context ctx = this;
	public PaliScreen screen;
	GridLayout.LayoutParams layout;
	
	public boolean mPressed = false;
	private PaliItem selectedItem=null;
	private ImageView dragItem;
	ExpandableListView listview;
	
	JSONArray screens;
	Integer selected;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		setContentView(R.layout.customizing);
		
		dragItem = (ImageView)findViewById(R.id.dragitemimage);
		
		this.screen =(PaliScreen) findViewById(R.id.screen_edit);
		LinearLayout.LayoutParams lilayout = (LinearLayout.LayoutParams)this.screen.getLayoutParams();
		
		Intent intent = getIntent();
		JSONObject json;
		try {
			json = new JSONObject(intent.getExtras().getString("json"));
			selected = intent.getExtras().getInt("selected");
			this.ParseJSON(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		listview = (ExpandableListView)this.findViewById(R.id.itemmainlistview);
		listview.setAdapter(new PaliExpandableAdapter(this,CustomizingMainActivity.GearUIList,vl));
		listview.expandGroup(0);
		listview.expandGroup(1);
		listview.expandGroup(2);
		listview.expandGroup(3);
		listview.expandGroup(4);
		listview.expandGroup(5);
		//listview.setOnChildClickListener(listClickListener);
		this.screen.setTouchable(true);
		this.screen.setSize(900, 900);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				//CustomizingMainActivity.screens.get(0).copy(this.screen, CustomizingMainActivity.screenSize, CustomizingMainActivity.screenSize);
				Intent intent = new Intent(this, CustomizingMainActivity.class);
				try {
					intent.putExtra("json_screens", this.PackJSON().toString());
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivity(intent);
				return true;
			case KeyEvent.KEYCODE_MENU:
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent e)
	{
		Rect r = new Rect();;
		screen.dispatchTouchEvent(e);
		listview.getGlobalVisibleRect(r);
		if(r.contains((int)e.getX(),(int)e.getY()))
			listview.dispatchTouchEvent(e);
		this.onTouchEvent(e);
		return true;
	}
	
	View.OnTouchListener vl= new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch(event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				selectedItem = (PaliItem)v.getTag();
				dragItem.setImageResource(selectedItem.imageid);
				LinearLayout.LayoutParams dl = (LinearLayout.LayoutParams) dragItem.getLayoutParams();
				dl.leftMargin = (int)event.getX()- dl.width/2;
				dl.topMargin = (int)event.getY() - dl.height/2;
				dragItem.setLayoutParams(dl);
				dragItem.setVisibility(View.VISIBLE);
				mPressed=true;
				return false;
			}
			return true;
		}
	};
	
	
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		super.onTouchEvent(e);		 
		 switch(e.getAction() & MotionEvent.ACTION_MASK)
		 {		 
		 case MotionEvent.ACTION_DOWN:
			 if(mPressed)
			 {	
				LinearLayout.LayoutParams lparam = (LinearLayout.LayoutParams) dragItem.getLayoutParams();
				lparam.leftMargin = (int)e.getX() - dragItem.getWidth()/2;
				lparam.topMargin = (int)e.getY() - dragItem.getHeight()/2;
				
				dragItem.setLayoutParams(lparam);
			 }
			 else
			 {
				 selectedItem=null;
			 }
			 return true;
		 case MotionEvent.ACTION_MOVE:
			 if(mPressed)
			 {	
				LinearLayout.LayoutParams lparam = (LinearLayout.LayoutParams) dragItem.getLayoutParams();
				lparam.leftMargin = (int)e.getX() - dragItem.getWidth()/2;
				lparam.topMargin = (int)e.getY() - dragItem.getHeight()/2;
				
				dragItem.setLayoutParams(lparam);
			 }
			 else
			 {
				 selectedItem=null;
			 }
			 return true;
		 case MotionEvent.ACTION_UP:
			 if(mPressed)
			 {
				 dragItem.setVisibility(View.GONE);
				 selectedItem=null;
				 mPressed = false;
			 }
			 else
			 {
				 selectedItem=null;
			 }
			 return true;
		 }
		 return true;
	}
	
	private void ParseJSON(JSONObject json)
	{
		try {
			screens = json.getJSONArray("customize");
			JSONArray screenjson = screens.getJSONArray(selected);
			screen.putJSON(screenjson);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private JSONObject PackJSON() throws JSONException
	{
		screens.put(selected,screen.getJSON());
		
		JSONObject json = new JSONObject();
		json.put("customize", screens);
		return json;
	}
}
