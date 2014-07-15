package com.yozisunji.palipalette;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.samsung.android.example.helloaccessoryprovider.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.GridLayout.Spec;

public class CustomizingMainActivity extends Activity implements OnLongClickListener {
	public static JSONObject json;
	private JSONArray jarr;
	public static ArrayList<PaliItemList> GearUIList;
	public static ArrayList<PaliScreen> screens;
	public static Integer selectedScreen=0;
	
	public static final int ICON_WIDTH = 1;
	public static final int ICON_HEIGHT = 1;
	
	GridLayout grid;
	int indexX=0, indexY=0;
	int homeX, homeY;
	
	public static int BackgroundColor;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		setContentView(R.layout.customizingmain);
		
		BackgroundColor = Color.rgb(3,74,132);
		InitializeGearUIList();
		
		grid = (GridLayout) findViewById(R.id.screensLayout);
		screens = new ArrayList<PaliScreen>();
		
		screens.add(new PaliScreen(this));
		
		screens.get(0).putItem(PaliCanvas.TOOL_PICKOBJECT, 0, 0, 0);
		screens.get(0).putItem(PaliCanvas.TOOL_PENCIL, 0, 0, 2);
		screens.get(0).putItem(PaliCanvas.TOOL_BRUSH, 0, 1, 1);
		
		
		//grid.addView(screens.get(0),0,0);
		Spec row = GridLayout.spec(0, 1); 
        Spec colspan = GridLayout.spec(0, 1);
        GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, colspan);
        //gridLayoutParam.width = 300;
        //gridLayoutParam.height = 300;
        

		grid.addView(screens.get(0),gridLayoutParam);
		screens.get(0).setSize(300,300);
		//ParseJSON();
	}
	
	private void ParseJSON()
	{
		try {
			JSONArray obj;
			JSONObject item;
			jarr = json.getJSONArray("customize");
			
			for( int i = 0 ; i<jarr.length() ; i++ )
			{
				screens.add(new PaliScreen(this));
				obj = jarr.getJSONArray(i);
				
				for( int j = 0; j < obj.length(); j++)
				{
					item = obj.getJSONObject(j);
					screens.get(i).putItem(item.getInt("Function"), item.getInt("Num"), item.getInt("PosX"), item.getInt("PosY"));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void InitializeGearUIList()
	{
		GearUIList = new ArrayList<PaliItemList>();

		GearUIList.add(new PaliItemList("Select Object"));
		GearUIList.add(new PaliItemList("Pencil"));
		GearUIList.add(new PaliItemList("Brush"));
		GearUIList.add(new PaliItemList("Circle"));
		GearUIList.add(new PaliItemList("Ellipse"));
		GearUIList.add(new PaliItemList("Rectangle"));
		
		GearUIList.get(0).putItem(PaliCanvas.TOOL_PICKOBJECT, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_pickobject_icon);
		GearUIList.get(1).putItem(PaliCanvas.TOOL_PENCIL, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_pencil_icon);
		GearUIList.get(2).putItem(PaliCanvas.TOOL_BRUSH, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_brush_icon);
		GearUIList.get(3).putItem(PaliCanvas.TOOL_CIRCLE, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_circle_icon);
		GearUIList.get(4).putItem(PaliCanvas.TOOL_ELLIPSE, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_ellipse_icon);
		GearUIList.get(5).putItem(PaliCanvas.TOOL_RECTANGLE, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_rect_icon);
		
		/*
		 *public static final int TOOL_PICKOBJECT=0;
		 *public static final int TOOL_PENCIL=1;
		 *public static final int TOOL_BRUSH=2;
		 *public static final int TOOL_CIRCLE=3;
		 *public static final int TOOL_ELLIPSE=4;
		 *public static final int TOOL_RECTANGLE=5;
		 */
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				android.os.Process.killProcess(android.os.Process.myPid());
				return true;
			case KeyEvent.KEYCODE_MENU:
				Intent intent = new Intent(this, CustomizingActivity.class);
				startActivity(intent);
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}
}
