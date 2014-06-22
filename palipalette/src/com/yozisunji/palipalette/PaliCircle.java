package com.yozisunji.palipalette;

import android.graphics.Canvas;
import android.graphics.Paint;

public class PaliCircle extends PaliObject {
	float x, y;
	float r;
	/*
	PaliCircle()
	{
		super();
	}*/
	PaliCircle(String tag, int scolor, int fcolor)
	{
		super(tag,scolor,fcolor);
	}
	PaliCircle(String tag, float x, float y, float r, boolean v)
	{
		paint = new Paint();
        paint.setAntiAlias(true);
		svgtag=tag;
		this.x = x;
		this.y = y;
		this.r = r;
		validation = v;
	}
	PaliCircle(String tag, float x, float y, float r, int scolor, int fcolor)
	{
		svgtag=tag;
		this.x = x;
		this.y = y;
		this.r = r;
		paint = new Paint();
        paint.setAntiAlias(true);
		paint.setColor(scolor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(fcolor);
		paint.setStyle(Paint.Style.FILL);
	}
	public void drawObject(Canvas c) {
		c.drawCircle(x, y, r, paint);	
	}
}
