package com.yozisunji.palipalette;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.GridLayout.Spec;

public class CustomizingMainActivity extends Activity {
	public static ArrayList<PaliItemList> GearUIList;
	public static ArrayList<PaliItemList> GearUIViewList;
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
	
	public static Point activitySize = new Point();
	Point marginSize = new Point();
	public static int screenSize;
	
	public static int BackgroundColor;
	
	private int selected;
	Rect r;
	
	Dialog deleteDialog;
	public boolean mLongPressed = false;
	private Handler mHandler = new Handler();
	private LongPressCheckRunnable mLongPressCheckRunnable = new LongPressCheckRunnable();
	private int mLongPressTimeout=700;
	
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
		
		mLongPressTimeout = ViewConfiguration.getLongPressTimeout();

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
	}
	
	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		
		if(null!= intent)
		{
			setIntent(intent);
		}
	}
	@Override
	public void onResume()
	{
		InitializeGearUIViewList();
		this.grid.removeAllViews();
		this.screens.clear();
		
		Intent intent = getIntent();
		if(intent.getExtras().containsKey("json_screens"))
		{
			try {
				JSONObject json = new JSONObject(intent.getExtras().getString("json_screens"));
				this.ParseJSON(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		if(screens.size()<8)
		{
			addScreen = new PaliScreen(this);
			addScreen.setBackgroundResource(R.drawable.screen_background);
			addScreen.putItem(Common, 0, 0, 0);
			addScreen.putItem(Common, 1, 1, 1);
			addX = screens.size()%4;
			addY = screens.size()/4;
			Spec row = GridLayout.spec(addY, 1); 
	        Spec col = GridLayout.spec(addX, 1);
	        GridLayout.LayoutParams gridLayoutParam;
	        gridLayoutParam = new GridLayout.LayoutParams(row, col);
	        gridLayoutParam.leftMargin=this.marginSize.x ;
	        gridLayoutParam.rightMargin=this.marginSize.x ;
	        gridLayoutParam.topMargin=this.marginSize.y;
	        gridLayoutParam.bottomMargin=this.marginSize.y;
			grid.addView(addScreen,gridLayoutParam);
			addScreen.setSize(screenSize,screenSize);
			addScreen.sWidth=screenSize;
			addScreen.sHeight=screenSize;
			Log.w("screensizetest",String.valueOf(100));
			Log.w("screensize",String.valueOf(screenSize));
		}
		
		LinearLayout.LayoutParams gridParm = (LinearLayout.LayoutParams) grid.getLayoutParams();
		gridParm.width = activitySize.x;
		gridParm.height = activitySize.y - 100;
		grid.setLayoutParams(gridParm);		
		
		super.onResume();
	}
	
	private void ParseJSON(JSONObject json)
	{
		try {
			JSONArray jarr, screen;
			jarr = json.getJSONArray("customize");
			
			for( int i = 0 ; i<jarr.length() ; i++ )
			{
				screens.add(new PaliScreen(mContext));
				screen = jarr.getJSONArray(i);
				screens.get(i).putJSON(screen);
				
				Spec row = GridLayout.spec(i/4, 1); 
		        Spec col = GridLayout.spec(i%4, 1);
				GridLayout.LayoutParams gridLayoutParam;
		        gridLayoutParam = new GridLayout.LayoutParams(row, col);
		        gridLayoutParam.leftMargin=marginSize.x ;
		        gridLayoutParam.rightMargin=marginSize.x ;
		        gridLayoutParam.topMargin=marginSize.y;
		        gridLayoutParam.bottomMargin=marginSize.y;
				grid.addView(screens.get(i),gridLayoutParam);
				screens.get(i).setSize(screenSize,screenSize);
				screens.get(i).sWidth=screenSize;
				screens.get(i).sHeight=screenSize;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private JSONObject PackJSON() throws JSONException
	{
		JSONObject json = new JSONObject();
		JSONArray jarr = new JSONArray();
		for(int i=0 ; i<screens.size(); i++)
		{
			jarr.put(screens.get(i).getJSON());
		}
		json.put("customize", jarr);
		return json;
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
		// Color Picker
		GearUIList.get(Select).putItem(Select, 2, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_colorpick_icon,"Color Picker");
				
		// Pencil Icon
		GearUIList.get(Drawing).putItem(Drawing, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_pencil_icon,"Pencil");
		// Brush Icon
		GearUIList.get(Drawing).putItem(Drawing, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_brush_icon,"Brush");
		// Pen Icon
		GearUIList.get(Drawing).putItem(Drawing, 2, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_pen_icon,"Pen");
		
		// Rectangle Icon
		GearUIList.get(Shape).putItem(Shape, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_rect_icon,"Rectangle");
		// Circle Icon
		GearUIList.get(Shape).putItem(Shape, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_circle_icon,"Circle");
		// Ellipse Icon
		GearUIList.get(Shape).putItem(Shape, 2, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_ellipse_icon,"Ellipse");
		// Star Icon
		GearUIList.get(Shape).putItem(Shape, 3, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_star_icon,"Star");
		// Shape Widget
		GearUIList.get(Shape).putItem(Shape, 4, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_star_icon,"Shape");
				
	
		// Color Widget
		GearUIList.get(Style).putItem(Style, 0, PaliItem.TYPE_WIDGET, 3, 3, R.drawable.tool_color_3x3_widget, "Color");
		// Stroke Widget
		GearUIList.get(Style).putItem(Style, 1, PaliItem.TYPE_WIDGET, 1, 3, R.drawable.tool_stroke_1x3_widget, "Stroke Width");
		
		
		// New File Icon
		GearUIList.get(File).putItem(File, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_new_icon, "New File");
		// Open File Icon
		GearUIList.get(File).putItem(File, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_open_icon, "Open File");
		// Save File Icon
		GearUIList.get(File).putItem(File, 2, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_save_icon, "Save File");
		// Export File Icon
		GearUIList.get(File).putItem(File, 3, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_export_icon, "Export File");
		// File Icon
		GearUIList.get(File).putItem(File, 4, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_file_icon, "File");
		
		
		// Undo Icon
		GearUIList.get(History).putItem(History, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_undo_icon, "Undo");
		// Redo Icon
		GearUIList.get(History).putItem(History, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_redo_icon, "Redo");
		
		// Allmenu Icon
		GearUIList.get(Config).putItem(Config, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_allmenu_icon, "All Menu");
		// Customize Icon
		GearUIList.get(Config).putItem(Config, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_customize_icon, "Customizing");
		// Help Icon
		GearUIList.get(Config).putItem(Config, 2, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_help_icon, "Help");
		
		// Null Icon
		GearUIList.get(Common).putItem(Common, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.null_item);
		// Screen Add Icon
		GearUIList.get(Common).putItem(Common, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.custom_screen_add);
	}

	
	private void InitializeGearUIViewList()
	{
		GearUIViewList = new ArrayList<PaliItemList>();
		
		GearUIViewList.add(new PaliItemList("Select Object"));
		GearUIViewList.add(new PaliItemList("Drawing Tool"));
		GearUIViewList.add(new PaliItemList("Shape"));
		GearUIViewList.add(new PaliItemList("Style"));
		GearUIViewList.add(new PaliItemList("History"));
		GearUIViewList.add(new PaliItemList("File"));
		GearUIViewList.add(new PaliItemList("Config"));
		
		// Pick Object Icon
		GearUIViewList.get(Select).putItem(Select, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_pickobject_icon, "Pick Object");
		// Layer Icon
		GearUIViewList.get(Select).putItem(Select, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_layer_icon,"Layer");
		// Color Picker
		GearUIViewList.get(Select).putItem(Select, 2, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_colorpick_icon,"Color Picker");
				
		// Pencil Icon
		GearUIViewList.get(Drawing).putItem(Drawing, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_pencil_icon,"Pencil");
		// Brush Icon
		GearUIViewList.get(Drawing).putItem(Drawing, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_brush_icon,"Brush");
		// Pen Icon
		GearUIViewList.get(Drawing).putItem(Drawing, 2, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_pen_icon,"Pen");
		
		// Rectangle Icon
		GearUIViewList.get(Shape).putItem(Shape, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_rect_icon,"Rectangle");
		// Circle Icon
		GearUIViewList.get(Shape).putItem(Shape, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_circle_icon,"Circle");
		// Ellipse Icon
		GearUIViewList.get(Shape).putItem(Shape, 2, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_ellipse_icon,"Ellipse");
		// Star Icon
		GearUIViewList.get(Shape).putItem(Shape, 3, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_star_icon,"Star");
		// Shape Widget
		GearUIViewList.get(Shape).putItem(Shape, 4, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_star_icon,"Shape");
				
		
				// Color Widget
		GearUIViewList.get(Style).putItem(Style, 0, PaliItem.TYPE_WIDGET, 3, 3, R.drawable.tool_color_3x3_widget, "Color");
		// Stroke Widget
		GearUIViewList.get(Style).putItem(Style, 1, PaliItem.TYPE_WIDGET, 1, 3, R.drawable.tool_stroke_1x3_widget, "Stroke Width");
		
		
		// New File Icon
		GearUIViewList.get(File).putItem(File, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_new_icon, "New File");
		// Open File Icon
		GearUIViewList.get(File).putItem(File, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_open_icon, "Open File");
		// Save File Icon
		GearUIViewList.get(File).putItem(File, 2, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_save_icon, "Save File");
		// Export File Icon
		GearUIViewList.get(File).putItem(File, 3, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_export_icon, "Export File");
		// File Icon
		GearUIViewList.get(File).putItem(File, 4, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_file_icon, "File");
		
		// Undo Icon
		GearUIViewList.get(History).putItem(History, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_undo_icon, "Undo");
		// Redo Icon
		GearUIViewList.get(History).putItem(History, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_redo_icon, "Redo");
		
		// Allmenu Icon
		GearUIViewList.get(Config).putItem(Config, 0, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_allmenu_icon, "All Menu");
		// Customize Icon
		GearUIViewList.get(Config).putItem(Config, 1, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_customize_icon, "Customizing");
		// Help Icon
		GearUIViewList.get(Config).putItem(Config, 2, PaliItem.TYPE_ICON, ICON_WIDTH, ICON_HEIGHT, R.drawable.tool_help_icon, "Help");
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
			screens.get(screenN).sWidth=screenSize;
			screens.get(screenN).sHeight=screenSize;
			screens.get(screenN).setSize(screenSize, screenSize);
			screens.get(screenN).setBackgroundResource(R.drawable.screen_background);
			
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
			screens.add(new PaliScreen(mContext));
		       
	        Spec row = GridLayout.spec(addY, 1); 
	        Spec col = GridLayout.spec(addX, 1);
	        GridLayout.LayoutParams  gridLayoutParam = new GridLayout.LayoutParams(row, col);
	        gridLayoutParam.leftMargin=this.marginSize.x ;
	        gridLayoutParam.rightMargin=this.marginSize.x ;
	        gridLayoutParam.topMargin=this.marginSize.y;
	        gridLayoutParam.bottomMargin=this.marginSize.y;
			grid.addView(screens.get(screenN),gridLayoutParam);
			screens.get(screenN).sWidth=screenSize;
			screens.get(screenN).sHeight=screenSize;
			screens.get(screenN).setSize(screenSize, screenSize);
			screens.get(screenN).setBackgroundResource(R.drawable.screen_background);
			
			addScreen.setVisibility(View.GONE);
			
		}
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent intent;
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				intent = new Intent(this, MainActivity.class);
				try {
					intent.putExtra("json_screen", this.PackJSON().toString());
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivity(intent);
				//android.os.Process.killProcess(android.os.Process.myPid());
				return super.dispatchKeyEvent(event);
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
			
			for(int i=0;i<screens.size();i++)
			{
				
				screens.get(i).getGlobalVisibleRect(r);
				if(r.contains((int)e.getX(),(int)e.getY()))
				{
					
					selected=i;
					screens.get(selected).setBackgroundResource(R.drawable.screen_background_pressed);
					mPressed=true;
					startTimeout();
					break;
				}
				
			}
			preX = e.getX();
			preY = e.getY();
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
			    	stopTimeout();
			    	
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
				    		temp.setLayoutParams(tempLayoutParam);
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
			 if(mLongPressed)
			 {
					return true;
			 }
			 else{
				 if(mPressed)
				 {
					 stopTimeout();
					 mPressed=false;
					 screens.get(selected).setBackgroundResource(R.drawable.screen_background);
					 
					 if(Math.abs(preX-e.getX())<5 && Math.abs(preY-e.getY())<5){
						 indexX = 4 * (int)e.getX() / this.activitySize.x;
						 indexY = 2 * (int)e.getY() / this.activitySize.y;
						 
						 Intent intent = new Intent(this, CustomizingActivity.class);
						 try {
							intent.putExtra("json", this.PackJSON().toString());
							intent.putExtra("selected", indexY*4+indexX);
						 } catch (JSONException error) {
							// TODO Auto-generated catch block
								error.printStackTrace();
						 }
						 startActivity(intent);
					 }
				 }
				 else
				 {
					addScreen.getGlobalVisibleRect(r);
					if(r.contains((int)e.getX(),(int)e.getY())) 
					{
						this.addScreen();
					}
					return true;
				 }
			 }
			 return true;
		 }
		 return true;
	}
	public void startTimeout() {
		mLongPressed = false;
		mHandler.postDelayed(mLongPressCheckRunnable, mLongPressTimeout);
	}

	public void stopTimeout() {
		if (!mLongPressed)
			mHandler.removeCallbacks(mLongPressCheckRunnable);
	}

	public void finishLongPressed() {
		this.mLongPressed = false;
	}
	
	private void createDeleteDialog(boolean t) {
		final View innerView = getLayoutInflater().inflate(R.layout.deletedialog,
				null);
		deleteDialog = new Dialog(this);
		deleteDialog.setContentView(innerView);
		
		if(t)
		{
			deleteDialog.setTitle("Delete Confirmation");
			deleteDialog.setCancelable(false);
			deleteDialog.setCanceledOnTouchOutside(true);
	
			Button delete_okBtn = (Button) deleteDialog.findViewById(R.id.delete_okBtn);
			Button delete_cancelBtn = (Button) deleteDialog.findViewById(R.id.delete_cancelBtn);
			TextView deleteText = (TextView) deleteDialog.findViewById(R.id.deleteText);
			deleteText.setText("Are you sure to delete the screen?");
			
			delete_okBtn.setOnClickListener(
					new View.OnClickListener() 
					{
						@Override
						public void onClick(View v) {
							mLongPressed = true;
							mPressed = false;
							PaliScreen selScreen = screens.get(selected);
					    	GridLayout.LayoutParams gridLayoutParam = (GridLayout.LayoutParams)selScreen.getLayoutParams();
					    	PaliScreen temp;
					    	GridLayout.LayoutParams tempLayoutParam;
					    	
					    	int dx, dy;
					    	int i;
					    	
							for(i=selected+1;i<screens.size();i++) {
					    		temp=screens.get(i);
					    		tempLayoutParam = (GridLayout.LayoutParams) screens.get(i).getLayoutParams();
								if(i>4)
					    		{
					    			dx=(i-1)%4;
					    			dy=1;
					    		}
					    		else if(i==4)
					    		{
					    			dx = 3;
					    			dy = 0;
					    		}
					    		else
					    		{
					    			dx = (i-1)%4;
					    			dy=0;
					    		}
					    		tempLayoutParam.rowSpec = GridLayout.spec(dy,1);
					    		tempLayoutParam.columnSpec = GridLayout.spec(dx,1);
					    		temp.setLayoutParams(tempLayoutParam);
					    	}
							
							temp=addScreen;
				    		tempLayoutParam = (GridLayout.LayoutParams) addScreen.getLayoutParams();
							if(i>4)
				    		{
				    			dx=(i-1)%4;
				    			dy=1;
				    		}
				    		else if(i==4)
				    		{
				    			dx = 3;
				    			dy = 0;
				    		}
				    		else
				    		{
				    			dx = (i-1)%4;
				    			dy=0;
				    		}
				    		tempLayoutParam.rowSpec = GridLayout.spec(dy,1);
				    		tempLayoutParam.columnSpec = GridLayout.spec(dx,1);
				    		temp.setLayoutParams(tempLayoutParam);
				    		
				    		if(addX>0)
								addX--;
							else if(addY>0)
							{
								addY--;
								addX=3;
							}
				    		
				    		screens.remove(selScreen);
							selScreen.setVisibility(View.GONE);
							
							
							deleteDialog.cancel();
							finishLongPressed();
						}
					});
			delete_cancelBtn.setOnClickListener(
					new View.OnClickListener() 
					{
						@Override
						public void onClick(View v) {
							deleteDialog.cancel();
							finishLongPressed();
						}
					});
		}
		else
		{
			deleteDialog.setTitle("Delete Failed");
			deleteDialog.setCancelable(false);
			deleteDialog.setCanceledOnTouchOutside(false);
	
			Button delete_okBtn = (Button) deleteDialog.findViewById(R.id.delete_okBtn);
			Button delete_cancelBtn = (Button) deleteDialog.findViewById(R.id.delete_cancelBtn);
			delete_cancelBtn.setVisibility(View.GONE);
			TextView deleteText = (TextView) deleteDialog.findViewById(R.id.deleteText);
			deleteText.setText("You can't delete the screen that include \"Customize\" Icon!");
			delete_okBtn.setOnClickListener(
					new View.OnClickListener() 
					{
						@Override
						public void onClick(View v) {
							deleteDialog.cancel();
							finishLongPressed();
						}
					});
		}


		
	}
	
	public void popUpDeleteDialog() {
		deleteDialog.show();
	}
	private class LongPressCheckRunnable implements Runnable {
		@Override
		public void run() {
	    	createDeleteDialog(!screens.get(selected).hasCustomize());
	    	popUpDeleteDialog();
	    	
		}
	}
}
