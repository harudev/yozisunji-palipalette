package com.yozisunji.palipalette;

import java.util.ArrayList;

import com.samsung.android.example.helloaccessoryprovider.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.widget.GridLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class PaliScreen extends GridLayout{
	ArrayList<PaliItemView> items;
	Context mContext;
	
	private boolean mPressed = false;
	private int selected = 0;
	private int indexX, indexY;
	
	public PaliScreen(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		items = new ArrayList<PaliItemView>();
		this.setBackgroundColor(CustomizingMainActivity.BackgroundColor);
		this.setRowCount(3);
		this.setColumnCount(3);
	}
	
	public PaliScreen(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		items = new ArrayList<PaliItemView>();
		this.setBackgroundColor(CustomizingMainActivity.BackgroundColor);
		this.setRowCount(3);
		this.setColumnCount(3);
	}
	
	public void putItem(int func, int item, int posX, int posY)
	{
		PaliItemView temp = new PaliItemView(func, item, posX, posY,mContext);
		this.items.add(temp);
		this.addView(temp , temp.gl);
	}
	
	public void copy(PaliScreen p, int width, int height)
	{		
		PaliItemView temp;
		
		this.removeAllViews();
		
		for(int i = 0; i<p.items.size();i++)
		{
			temp = p.items.get(i);
						
			this.items.add(new PaliItemView(temp.iteminfo.funcNum, temp.iteminfo.itemNum, temp.x, temp.y, temp.context));
			this.addView(this.items.get(i) , temp.gl);
		}
		
		this.setSize(width, height);
		this.invalidate();
	}
	public void setSize(int width, int height)
	{
		this.getLayoutParams().width = width;
		this.getLayoutParams().height = height;
		
		for(int i = 0; i<items.size() ; i++)
		{
			items.get(i).setSize(width/3, height/3);
		}
	}
	
	public void setTouchable(boolean t)
	{
		
	}
	/*
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		//return gDetector.onTouchEvent(e);
		
		super.onTouchEvent(e);		 
		 switch(e.getAction() & MotionEvent.ACTION_MASK)
		 {		 
		 case MotionEvent.ACTION_DOWN:
			 
			for(int i=0;i<items.size();i++)
			{
				Rect r= new Rect();
				items.get(i).getGlobalVisibleRect(r);
				if(r.contains((int)e.getX(),(int)e.getY()))
				{
					
					selected=i;
					items.get(selected).setBackgroundColor(Color.argb(60, 255, 200, 200));
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
				//indexX = 4 * (int)e.getX() / this.activitySize.x;
			    //indexY = 2 * (int)e.getY() / this.activitySize.y;

		        if(items.size()<5)
		        	indexY=0;
		        
			    int index = (indexY * 4) + indexX;
			    if((index < items.size()) && index!=selected)
			    {
			    	GridLayout.LayoutParams gridLayoutParam = (GridLayout.LayoutParams)items.get(index).getLayoutParams();
			    	PaliItemView temp = items.get(index);
			    	items.get(index).setLayoutParams(items.get(selected).getLayoutParams());
			    	items.get(selected).setLayoutParams(gridLayoutParam);
			    	items.set(index,items.get(selected));
			    	items.set(selected, temp);
			    	
			    	selected = index;
			    }
			 }
			 return false;
		 case MotionEvent.ACTION_UP:
			 if(mPressed)
			 {
				 
				 mPressed=false;
				 items.get(selected).setBackgroundColor(Color.argb(70, 255, 255, 255));
			 }
			 return true;
		 }
		 return true;
	}*/
}
