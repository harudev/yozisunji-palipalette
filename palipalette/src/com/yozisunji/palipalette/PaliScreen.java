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
	
	private boolean touchable = false;
	
	public PaliScreen(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		items = new ArrayList<PaliItemView>();
		//this.setBackgroundColor(CustomizingMainActivity.BackgroundColor);
		this.setRowCount(3);
		this.setColumnCount(3);
		touchable = false;
		fillNullItem();
	}
	
	public PaliScreen(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		items = new ArrayList<PaliItemView>();
		//this.setBackgroundColor(CustomizingMainActivity.BackgroundColor);
		this.setRowCount(3);
		this.setColumnCount(3);
		touchable = false;
		fillNullItem();
		
		
	}
	
	public void fillNullItem()
	{
		PaliItemView temp = new PaliItemView(CustomizingMainActivity.Common, 0, 0, 0, mContext);
		this.items.add(temp);
		this.addView(temp , temp.gl);
		temp = new PaliItemView(CustomizingMainActivity.Common, 0, 1, 0, mContext);
		this.items.add(temp);
		this.addView(temp , temp.gl);
		temp = new PaliItemView(CustomizingMainActivity.Common, 0, 2, 0, mContext);
		this.items.add(temp);
		this.addView(temp , temp.gl);
		temp = new PaliItemView(CustomizingMainActivity.Common, 0, 0, 1, mContext);
		this.items.add(temp);
		this.addView(temp , temp.gl);
		temp = new PaliItemView(CustomizingMainActivity.Common, 0, 1, 1, mContext);
		this.items.add(temp);
		this.addView(temp , temp.gl);
		temp = new PaliItemView(CustomizingMainActivity.Common, 0, 2, 1, mContext);
		this.items.add(temp);
		this.addView(temp , temp.gl);
		temp = new PaliItemView(CustomizingMainActivity.Common, 0, 0, 2, mContext);
		this.items.add(temp);
		this.addView(temp , temp.gl);
		temp = new PaliItemView(CustomizingMainActivity.Common, 0, 1, 2, mContext);
		this.items.add(temp);
		this.addView(temp , temp.gl);
		temp = new PaliItemView(CustomizingMainActivity.Common, 0, 2, 2, mContext);
		this.items.add(temp);
		this.addView(temp , temp.gl);
	}
	public void putNullItem(int x, int y)
	{
		PaliItemView temp = new PaliItemView(PaliCanvas.TOOL_COMMON, 0, x, y, mContext);
		this.addView(temp , temp.gl);
	}
	
	public void putItem(int func, int item, int posX, int posY)
	{
		PaliItemView temp = this.items.get(posY*3+posX);
		
		temp.iteminfo = CustomizingMainActivity.GearUIList.get(func).items.get(item);
		temp.setImageResource(temp.iteminfo.imageid);
		
		temp.gl = (GridLayout.LayoutParams)temp.getLayoutParams();
		temp.gl.rowSpec = GridLayout.spec(posX,temp.iteminfo.height);
		temp.gl.columnSpec = GridLayout.spec(posY,temp.iteminfo.width);
		temp.setLayoutParams(temp.gl);
	}
	public void copy(PaliScreen p, int width, int height)
	{	
		PaliItemView temp;
		PaliItemView get;
		
		this.removeAllViews();
		this.items.clear();
				
		for(int i = 0; i<p.items.size();i++)
		{
			get = p.items.get(i);
			temp = new PaliItemView(get.iteminfo.funcNum, get.iteminfo.itemNum, get.x , get.y, mContext);
			this.items.add(i,temp);
			this.addView(temp , get.gl);
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
		touchable = t;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		//return gDetector.onTouchEvent(e);
		if(!touchable)
			return false;
		
		super.onTouchEvent(e);		 
		Rect r = new Rect();
		 switch(e.getAction() & MotionEvent.ACTION_MASK)
		 {		 
		 case MotionEvent.ACTION_DOWN:
			 
			for(int i=0;i<items.size();i++)
			{
				items.get(i).getGlobalVisibleRect(r);
				if(r.contains((int)e.getX(),(int)e.getY()))
				{
					
					selected=i;
					items.get(selected).setBackgroundColor(Color.argb(80, 255, 255, 255));
					mPressed=true;
					break;
				}
			}
			 return false;
		 case MotionEvent.ACTION_MOVE:
			 if(mPressed)
			 {	
				for(int i=0;i<items.size();i++)
				{
					 if(i!=selected)
					 {
							items.get(i).getGlobalVisibleRect(r);
							if(r.contains((int)e.getX(),(int)e.getY()))
							{	
								PaliItemView temp = items.get(selected);
						    	PaliItemView change = items.get(i);
						    	
						    	GridLayout.LayoutParams gridLayoutParam = (GridLayout.LayoutParams)temp.getLayoutParams();
						    	GridLayout.LayoutParams changeLayoutParam = (GridLayout.LayoutParams)change.getLayoutParams();
						    	temp.setLayoutParams(changeLayoutParam);
						    	change.setLayoutParams(gridLayoutParam);
						    	
						    	items.remove(temp);
						    	items.remove(change);
						    	
						    	if(selected<i)
						    	{
						    		items.add(selected,change);
							    	items.add(i,temp);
						    	}
						    	else
						    	{
						    		items.add(i,temp);
						    		items.add(selected,change);
						    	}
						    	
						    	
						    	selected = i;
						    	break;
						    
							}
					 }
				}
			 }
			 return true;
		 case MotionEvent.ACTION_UP:
			 if(mPressed)
			 {
				 
				 mPressed=false;
				 items.get(selected).setBackgroundColor(Color.argb(0, 255, 255, 255));
			 }
			 return true;
		 }
		 return true;
	}
}
