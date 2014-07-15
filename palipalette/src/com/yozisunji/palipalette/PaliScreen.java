package com.yozisunji.palipalette;

import java.util.ArrayList;
import com.samsung.android.example.helloaccessoryprovider.R;
import android.content.Context;
import android.widget.GridLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class PaliScreen extends GridLayout{
	ArrayList<PaliItemView> items;
	Context mContext;
	
	public PaliScreen(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		items = new ArrayList<PaliItemView>();
		this.setBackgroundResource(R.drawable.screen_background);
		this.setRowCount(3);
		this.setColumnCount(3);
	}
	
	public PaliScreen(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		items = new ArrayList<PaliItemView>();
		this.setBackgroundResource(R.drawable.screen_background);
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
	public boolean onTouchEvent(MotionEvent e)
	{
		switch(e.getAction())
		{
		
		}
		return super.onTouchEvent(e);
	}	
}
