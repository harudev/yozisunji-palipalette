package com.yozisunji.palipalette;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.samsung.android.example.helloaccessoryprovider.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.GridLayout.Spec;

public class CustomizingMainActivity extends Activity {
	public static JSONObject json;
	private JSONArray jarr;
	public static ArrayList<PaliItemList> GearUIList;
	public static ArrayList<PaliScreen> screens;
	public static Integer selectedScreen=0;
	
	public static final int ICON_WIDTH = 1;
	public static final int ICON_HEIGHT = 1;
	
	Context mContext;

	public boolean mPressed = true;
	
	GridLayout grid;
	int indexX=0, indexY=0;
	int homeX, homeY;
	float preX, preY;
	
	Point activitySize = new Point();
	Point marginSize = new Point();
	int screenSize;
	
	public static int BackgroundColor;
	
	private int selected;
	Rect r;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		setContentView(R.layout.customizingmain);
		
		//ParseJSON();

		getWindowManager().getDefaultDisplay().getSize(this.activitySize);
		int tempX, tempY;
		tempY = (this.activitySize.y -100 - 80) / 2;
		tempX = (this.activitySize.x - 160 )/ 4;
		this.screenSize = (tempY<tempX)?tempY:tempX;
		if(screenSize==tempY)
		{
			this.marginSize.y = 20;
			this.marginSize.x = (this.activitySize.x - (this.screenSize*4))/8;
		}
		else
		{
			this.marginSize.x = 20;
			this.marginSize.y = (this.activitySize.y - 100 - (this.screenSize*2))/4;
		}
		
		BackgroundColor = Color.rgb(3,74,132);
		InitializeGearUIList();
		
		grid = (GridLayout) findViewById(R.id.screensLayout);
		screens = new ArrayList<PaliScreen>();
		
		screens.add(new PaliScreen(this));
		screens.get(0).putItem(PaliCanvas.TOOL_PICKOBJECT, 0, 0, 0);
		screens.get(0).putItem(PaliCanvas.TOOL_PENCIL, 0, 1, 0);
		screens.get(0).putItem(PaliCanvas.TOOL_BRUSH, 0, 2, 0);
		Spec row = GridLayout.spec(0, 1); 
        Spec col = GridLayout.spec(0, 1);
        GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, col);
        gridLayoutParam.leftMargin=this.marginSize.x ;
        gridLayoutParam.rightMargin=this.marginSize.x ;
        gridLayoutParam.topMargin=this.marginSize.y;
        gridLayoutParam.bottomMargin=this.marginSize.y;
		grid.addView(screens.get(0),gridLayoutParam);
		screens.get(0).setSize(screenSize,screenSize);
		screens.get(0).setBackgroundColor(BackgroundColor);
		
		screens.add(new PaliScreen(this));
		screens.get(1).putItem(PaliCanvas.TOOL_PICKOBJECT, 0, 0, 0);
		screens.get(1).items.get(0).setVisibility(View.INVISIBLE);
		screens.get(1).putItem(PaliCanvas.TOOL_PICKOBJECT, 0, 0, 1);
		screens.get(1).putItem(PaliCanvas.TOOL_PENCIL, 0, 1, 1);
		screens.get(1).putItem(PaliCanvas.TOOL_BRUSH, 0, 2, 1);
		row = GridLayout.spec(0, 1); 
        col = GridLayout.spec(1, 1);
        gridLayoutParam = new GridLayout.LayoutParams(row, col);
        gridLayoutParam.leftMargin=this.marginSize.x ;
        gridLayoutParam.rightMargin=this.marginSize.x ;
        gridLayoutParam.topMargin=this.marginSize.y;
        gridLayoutParam.bottomMargin=this.marginSize.y;
		grid.addView(screens.get(1),gridLayoutParam);
		screens.get(1).setSize(screenSize,screenSize);
		screens.get(1).setBackgroundColor(BackgroundColor);
		
		screens.add(new PaliScreen(this));
		screens.get(2).putItem(PaliCanvas.TOOL_PICKOBJECT, 0, 0, 2);
		screens.get(2).putItem(PaliCanvas.TOOL_PENCIL, 0, 1, 2);
		screens.get(2).putItem(PaliCanvas.TOOL_BRUSH, 0, 2, 2);
		row = GridLayout.spec(0, 1); 
        col = GridLayout.spec(2, 1);
        gridLayoutParam = new GridLayout.LayoutParams(row, col);
        gridLayoutParam.leftMargin=this.marginSize.x ;
        gridLayoutParam.rightMargin=this.marginSize.x ;
        gridLayoutParam.topMargin=this.marginSize.y;
        gridLayoutParam.bottomMargin=this.marginSize.y;
		grid.addView(screens.get(2),gridLayoutParam);
		screens.get(2).setSize(screenSize,screenSize);
		screens.get(2).setBackgroundColor(BackgroundColor);
		
		screens.add(new PaliScreen(this));
		screens.get(3).putItem(PaliCanvas.TOOL_PICKOBJECT, 0, 0, 0);
		screens.get(3).putItem(PaliCanvas.TOOL_PENCIL, 0, 1, 1);
		screens.get(3).putItem(PaliCanvas.TOOL_BRUSH, 0, 2, 2);
		row = GridLayout.spec(0, 1); 
        col = GridLayout.spec(3, 1);
        gridLayoutParam = new GridLayout.LayoutParams(row, col);
        gridLayoutParam.leftMargin=this.marginSize.x ;
        gridLayoutParam.rightMargin=this.marginSize.x ;
        gridLayoutParam.topMargin=this.marginSize.y;
        gridLayoutParam.bottomMargin=this.marginSize.y;
		grid.addView(screens.get(3),gridLayoutParam);
		screens.get(3).setSize(screenSize,screenSize);
		screens.get(3).setBackgroundColor(BackgroundColor);
		
		screens.add(new PaliScreen(this));
		screens.get(4).putItem(PaliCanvas.TOOL_PICKOBJECT, 0, 0, 0);
		screens.get(4).putItem(PaliCanvas.TOOL_PENCIL, 0, 0, 1);
		screens.get(4).putItem(PaliCanvas.TOOL_BRUSH, 0, 0, 2);
		row = GridLayout.spec(1, 1); 
        col = GridLayout.spec(0, 1);
        gridLayoutParam = new GridLayout.LayoutParams(row, col);
        gridLayoutParam.leftMargin=this.marginSize.x ;
        gridLayoutParam.rightMargin=this.marginSize.x ;
        gridLayoutParam.topMargin=this.marginSize.y;
        gridLayoutParam.bottomMargin=this.marginSize.y;
		grid.addView(screens.get(4),gridLayoutParam);
		screens.get(4).setSize(screenSize,screenSize);
		screens.get(4).setBackgroundColor(BackgroundColor);
		
		screens.add(new PaliScreen(this));
		screens.get(5).putItem(PaliCanvas.TOOL_PICKOBJECT, 0, 0, 0);
		screens.get(5).putItem(PaliCanvas.TOOL_PENCIL, 0, 1, 1);
		screens.get(5).putItem(PaliCanvas.TOOL_BRUSH, 0, 1, 2);
		row = GridLayout.spec(1, 1); 
        col = GridLayout.spec(1, 1);
        gridLayoutParam = new GridLayout.LayoutParams(row, col);
        gridLayoutParam.leftMargin=this.marginSize.x ;
        gridLayoutParam.rightMargin=this.marginSize.x ;
        gridLayoutParam.topMargin=this.marginSize.y;
        gridLayoutParam.bottomMargin=this.marginSize.y;
		grid.addView(screens.get(5),gridLayoutParam);
		screens.get(5).setSize(screenSize,screenSize);
		screens.get(5).setBackgroundColor(BackgroundColor);
		
		
		LinearLayout.LayoutParams gridParm = (LinearLayout.LayoutParams) grid.getLayoutParams();
		gridParm.width = activitySize.x;
		gridParm.height = activitySize.y - 100;
		grid.setLayoutParams(gridParm);		
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
	public boolean onTouchEvent(MotionEvent e)
	{
		//return gDetector.onTouchEvent(e);
		
		super.onTouchEvent(e);		 
		 switch(e.getAction() & MotionEvent.ACTION_MASK)
		 {		 
		 case MotionEvent.ACTION_DOWN:
			 
			for(int i=0;i<screens.size();i++)
			{
				r= new Rect();
				screens.get(i).getGlobalVisibleRect(r);
				if(r.contains((int)e.getX(),(int)e.getY()))
				{
					
					selected=i;
					screens.get(selected).setBackgroundColor(Color.argb(60, 3, 74, 132));
					Log.w("LonPress","selected" + Integer.toString(i));
					mPressed=true;
					//startTimeout();
					break;
				}
				
			}
			 return false;
		 case MotionEvent.ACTION_MOVE:
			 if(mPressed)
			 {	
				indexX = 4 * (int)e.getX() / this.activitySize.x;
			    indexY = 2 * (int)e.getY() / this.activitySize.y;

		        if(screens.size()<5)
		        	indexY=0;
		        
			    int index = (indexY * 4) + indexX;
			    if((index < screens.size()) && index!=selected)
			    {
			    	GridLayout.LayoutParams gridLayoutParam = (GridLayout.LayoutParams)screens.get(index).getLayoutParams();
			    	PaliScreen temp = screens.get(index);
			    	screens.get(index).setLayoutParams(screens.get(selected).getLayoutParams());
			    	screens.get(selected).setLayoutParams(gridLayoutParam);
			    	screens.set(index,screens.get(selected));
			    	screens.set(selected, temp);
			    	
			    	selected = index;
			    }
			 }
			 return false;
		 case MotionEvent.ACTION_UP:
			 if(mPressed)
			 {
				 
				 mPressed=false;
				 screens.get(selected).setBackgroundColor(BackgroundColor);
			 }
			 return true;
		 }
		 return true;
	}
}
