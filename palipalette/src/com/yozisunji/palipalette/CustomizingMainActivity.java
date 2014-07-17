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
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.GridLayout.Spec;

public class CustomizingMainActivity extends Activity {
	public static JSONObject json;
	private JSONArray jarr;
	public static ArrayList<PaliItemList> GearUIList;
	public static ArrayList<PaliScreen> screens;
	public static Integer selectedScreen=0;
	public PaliScreen addScreen;
	
	public static final int ICON_WIDTH = 1;
	public static final int ICON_HEIGHT = 1;
	
	Context mContext;

	public boolean mPressed = true;
	
	GridLayout grid;
	int addX = 0, addY = 0;
	int indexX=0, indexY=0;
	int homeX, homeY;
	float preX, preY;
	
	Point activitySize = new Point();
	Point marginSize = new Point();
	public static int screenSize;
	
	public static int BackgroundColor;
	
	private int selected;
	Rect r;
	
	public static final int Select 	= 0;
	public static final int Drawing	= 1;
	public static final int Shape	= 2;
	public static final int Style	= 3;
	public static final int History	= 4;
	public static final int File	= 5;
	public static final int Config	= 6;
	public static final int Common	= 7;
	
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
		Log.w("LonPress",Integer.toString(this.activitySize.x)+" : "+ Integer.toString(this.activitySize.y));
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
		screens.get(0).putItem(Drawing, 0, 0, 0);
		screens.get(0).putItem(Drawing, 1, 0, 1);
		screens.get(0).putItem(Select, 1, 1, 0);
		screens.get(0).putItem(Select, 0, 1, 1);
		screens.get(0).putItem(Shape, 3, 2, 0);
		screens.get(0).putItem(Style, 1, 0, 2);
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
		screens.get(1).putItem(Style, 0, 0, 0);
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
		screens.get(2).putItem(History, 0, 0, 0);
		screens.get(2).putItem(History, 1, 0, 1);
		screens.get(2).putItem(Config, 1, 0, 2);
		screens.get(2).putItem(File, 2, 1, 0);
		screens.get(2).putItem(Config, 0, 1, 1);
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
			
		
		if(screens.size()<8)
		{
			addScreen = new PaliScreen(this);
			addScreen.setBackgroundColor(Color.argb(90, 3, 74, 132));
			//addScreen.setAlpha(Color.alpha(70));
			addScreen.putItem(Common, 0, 0, 0);
			addScreen.putItem(Common,1,1,1);
			addX = 3;
			addY = 0;
			row = GridLayout.spec(addY, 1); 
	        col = GridLayout.spec(addX, 1);
	        gridLayoutParam = new GridLayout.LayoutParams(row, col);
	        gridLayoutParam.leftMargin=this.marginSize.x ;
	        gridLayoutParam.rightMargin=this.marginSize.x ;
	        gridLayoutParam.topMargin=this.marginSize.y;
	        gridLayoutParam.bottomMargin=this.marginSize.y;
			grid.addView(addScreen,gridLayoutParam);
			addScreen.setSize(screenSize, screenSize);
		}
		
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
				screens.add(new PaliScreen(mContext));
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
		GearUIList.add(new PaliItemList("Drawing Tool"));
		GearUIList.add(new PaliItemList("Shape"));
		GearUIList.add(new PaliItemList("Style"));
		GearUIList.add(new PaliItemList("History"));
		GearUIList.add(new PaliItemList("File"));
		GearUIList.add(new PaliItemList("Config"));
		GearUIList.add(new PaliItemList("Common"));
		
		// Pick Object Icon
		GearUIList.get(Select).putItem(Select, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_pickobject_icon, "Pick Object");
		// Layer Icon
		GearUIList.get(Select).putItem(Select, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_layer_icon,"Layer");
		
		// Pencil Icon
		GearUIList.get(Drawing).putItem(Drawing, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_pencil_icon,"Pencil");
		// Brush Icon
		GearUIList.get(Drawing).putItem(Drawing, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_brush_icon,"Brush");
		
		// Circle Icon
		GearUIList.get(Shape).putItem(Shape, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_circle_icon,"Circle");
		// Ellipse Icon
		GearUIList.get(Shape).putItem(Shape, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_ellipse_icon,"Ellipse");
		// Rectangle Icon
		GearUIList.get(Shape).putItem(Shape, 2, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_rect_icon,"Rectangle");
		// Change Icon
		GearUIList.get(Shape).putItem(Shape, 3, PaliItem.TYPE_WIDGET, 2, 1, R.drawable.tool_shape_2x1_widget,"Shape Change");
		
		// Stroke Color Icon
		GearUIList.get(Style).putItem(Style, 0, PaliItem.TYPE_WIDGET, 3, 3, R.drawable.tool_color_3x3_widget, "Color");
		// Fill Color Icon
		GearUIList.get(Style).putItem(Style, 1, PaliItem.TYPE_WIDGET, 1, 3, R.drawable.tool_stroke_1x3_widget, "Stroke Width");
		
		
		// New File Icon
		GearUIList.get(File).putItem(File, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_new_icon, "New File");
		// Open File Icon
		GearUIList.get(File).putItem(File, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_open_icon, "Open File");
		// Save File Icon
		GearUIList.get(File).putItem(File, 2, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_save_icon, "Save File");
		// Export File Icon
		GearUIList.get(File).putItem(File, 3, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_export_icon, "Export File");
		
		
		// Undo Icon
		GearUIList.get(History).putItem(History, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_undo_icon, "Undo");
		// Redo Icon
		GearUIList.get(History).putItem(History, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_redo_icon, "Redo");
		
		
		GearUIList.get(Config).putItem(Config, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_allmenu_icon, "All Menu");
		GearUIList.get(Config).putItem(Config, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_config_icon, "Configuration");
		
		// Null Icon
		GearUIList.get(Common).putItem(Common, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.null_item);
		// Screen Add Icon
		GearUIList.get(Common).putItem(Common, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.custom_screen_add);
		
		/*
		 public static final int Select 	= 0;
	public static final int Drawing	= 1;
	public static final int Shape	= 2;
	public static final int Style	= 3;
	public static final int History	= 4;
	public static final int File	= 5;
	public static final int Config	= 6;
	public static final int Common	= 7;
		 */
	}

	
	private void addScreen()
	{
		int screenN = screens.size();
		if(screenN<7)
		{
			screens.add(new PaliScreen(mContext));
		       
	        Spec row = GridLayout.spec(addY, 1); 
	        Spec col = GridLayout.spec(addX, 1);
	        GridLayout.LayoutParams  gridLayoutParam = new GridLayout.LayoutParams(row, col);
	        gridLayoutParam.leftMargin=this.marginSize.x ;
	        gridLayoutParam.rightMargin=this.marginSize.x ;
	        gridLayoutParam.topMargin=this.marginSize.y;
	        gridLayoutParam.bottomMargin=this.marginSize.y;
			grid.addView(screens.get(screenN),gridLayoutParam);
			screens.get(screenN).setSize(screenSize,screenSize);
			screens.get(screenN).setBackgroundColor(BackgroundColor);
			
			if(addX<3)
				addX++;
			else
			{
				addY++;
				addX=0;
			}
			
			row = GridLayout.spec(addY, 1); 
			col = GridLayout.spec(addX, 1);
			gridLayoutParam = (GridLayout.LayoutParams) addScreen.getLayoutParams();
	        gridLayoutParam.leftMargin=this.marginSize.x ;
	        gridLayoutParam.rightMargin=this.marginSize.x ;
	        gridLayoutParam.topMargin=this.marginSize.y;
	        gridLayoutParam.bottomMargin=this.marginSize.y;
	        gridLayoutParam.rowSpec = row;
	        gridLayoutParam.columnSpec = col;
	        addScreen.setLayoutParams(gridLayoutParam);
	        
	        
		}
		else if(screenN==7)
		{
				
			
		}
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				
				//android.os.Process.killProcess(android.os.Process.myPid());
				return super.dispatchKeyEvent(event);
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
			r= new Rect();
			addScreen.getGlobalVisibleRect(r);
			if(r.contains((int)e.getX(),(int)e.getY())) 
			{
				this.addScreen();
				break;
			}
			for(int i=0;i<screens.size();i++)
			{
				
				screens.get(i).getGlobalVisibleRect(r);
				if(r.contains((int)e.getX(),(int)e.getY()))
				{
					
					selected=i;
					screens.get(selected).setBackgroundColor(Color.argb(60, 3, 74, 132));
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
			    	PaliScreen selScreen = screens.get(selected);
			    	GridLayout.LayoutParams gridLayoutParam = (GridLayout.LayoutParams)selScreen.getLayoutParams();
			    	screens.remove(selScreen);
			    	PaliScreen temp;
			    	GridLayout.LayoutParams tempLayoutParam;
			    	
			    	int dx, dy;
			    	
				    if(index<selected)
				    {
				    	
				    	
				    	for(int i=index;i<selected;i++) {
				    		temp=screens.get(i);
				    		tempLayoutParam = (GridLayout.LayoutParams) screens.get(i).getLayoutParams();
			    			
				    		if(i>3)
				    		{
				    			dx=i%4 + 1;
				    			dy=1;
				    		}
				    		else if(i==3)
				    		{
				    			dx = 0;
				    			dy = 1;
				    		}
				    		else
				    		{
				    			dx=i%4+1;
				    			dy=0;
				    		}
				    		tempLayoutParam.rowSpec = GridLayout.spec(dy,1);
				    		tempLayoutParam.columnSpec = GridLayout.spec(dx,1);
				    		temp.setLayoutParams(tempLayoutParam);
				    	}
				    }
				    else // index>selected
				    {
				    	for(int i=selected;i<index;i++) {
				    		temp=screens.get(i);
				    		tempLayoutParam = (GridLayout.LayoutParams) screens.get(i).getLayoutParams();
			    			if(i>3)
				    		{
				    			dx=i%4;
				    			dy=1;
				    		}
				    		else if(i==3)
				    		{
				    			dx = 3;
				    			dy = 0;
				    		}
				    		else
				    		{
				    			dx = i%4;
				    			dy=0;
				    		}
				    		tempLayoutParam.rowSpec = GridLayout.spec(dy,1);
				    		tempLayoutParam.columnSpec = GridLayout.spec(dx,1);
				    		temp.setLayoutParams(tempLayoutParam);;
				    	}
				    }
				    gridLayoutParam.rowSpec = GridLayout.spec(indexY,1);
			    	gridLayoutParam.columnSpec = GridLayout.spec(indexX,1);
			    	selScreen.setLayoutParams(gridLayoutParam);
			    	screens.add(index,selScreen);
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
