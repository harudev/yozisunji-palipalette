package com.yozisunji.palipalette;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;
import android.widget.GridLayout;
import android.widget.ImageView;

public class PaliItemView extends ImageView {

	Context context;
	int x, y;
	Bitmap image;
	GridLayout.LayoutParams gl;
	boolean touchable;
	PaliItem iteminfo;
	
	public PaliItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public PaliItemView(int f, int i, int x, int y, Context context)
	{
		super(context);
		iteminfo = CustomizingMainActivity.GearUIList.get(f).items.get(i);
		this.x = x;
		this.y = y;
		this.context = context;
		gl = new GridLayout.LayoutParams(GridLayout.spec(y,iteminfo.height), GridLayout.spec(x,iteminfo.width));
		this.setImageResource(iteminfo.imageid);
	}
	
	public void setTouchable(boolean t)
	{
		this.touchable = t;
	}
	public void setSize(int w, int h)
	{
		gl = (GridLayout.LayoutParams)this.getLayoutParams();
		
		gl.width = w * iteminfo.width;
		gl.height = h * iteminfo.height;
		
		this.setLayoutParams(gl);
	}
}
