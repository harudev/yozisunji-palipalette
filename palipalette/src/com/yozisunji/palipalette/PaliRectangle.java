package com.yozisunji.palipalette;

import android.graphics.Canvas;
import android.graphics.Paint;

public class PaliRectangle extends PaliObject {
	float left, top, right, bottom;

	PaliRectangle(String tag, int scolor, int fcolor)
	{
		super(tag,scolor,fcolor);
	}
	PaliRectangle(String tag, float left, float top, float right, float bottom)
	{
		paint = new Paint();
		paint.setAntiAlias(true);
		svgtag = tag;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	PaliRectangle(String tag, float left, float top, float right, float bottom, int scolor, int fcolor)
	{
		svgtag = tag;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(scolor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(fcolor);
		paint.setStyle(Paint.Style.FILL);
	}
	
	public void drawObject(Canvas c) {
		c.drawRect(left, top, right, bottom, paint);
	}
}
