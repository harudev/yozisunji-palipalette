package com.yozisunji.palipalette;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

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
	PaliCircle(float x, float y, float r)
	{
		super();
		paint = new Paint();
        paint.setAntiAlias(true);
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.GREEN);
		paint.setStyle(Paint.Style.FILL);
		this.x = x;
		this.y = y;
		this.r = r;
		
		this.rect = new RectF(x-r, y-r, x+r, y+r);
	}

	PaliCircle(String tag, float x, float y, float r)
	{
		paint = new Paint();
        paint.setAntiAlias(true);
		svgtag=tag;
		this.x = x;
		this.y = y;
		this.r = r;
		this.rect = new RectF(x-r, y-r, x+r, y+r);
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
		this.rect = new RectF(x-r, y-r, x+r, y+r);
	}
	public void drawObject(Canvas c) {
		c.drawCircle(x, y, r, paint);
	}
}
