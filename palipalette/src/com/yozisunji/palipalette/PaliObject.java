package com.yozisunji.palipalette;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public abstract class PaliObject {
	Paint paint;
	String svgtag;
	
	public boolean selected;
	
	RectF rect;
	PaliObject(){
		paint = new Paint();
        paint.setAntiAlias(true);
	}
	
	PaliObject(String tag, int scolor, int fcolor)
	{
		svgtag = tag;
		paint = new Paint();
        paint.setAntiAlias(true);
		paint.setColor(scolor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(fcolor);
		paint.setStyle(Paint.Style.FILL);
	}
	public void SetTag(String tag)
	{
		svgtag = tag;
	}
	
	public void setStrokeColor(int color)
	{
		paint.setColor(color);
		paint.setStyle(Paint.Style.STROKE);
	}
	
	public void setFillColor(int color)
	{
		paint.setColor(color);
		paint.setStyle(Paint.Style.FILL);
	}
	
	public void setWidth(float w)
	{
		paint.setStrokeWidth(w);
	}
	
	public void setColor(int scolor, int fcolor)
	{
		paint.setColor(scolor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(fcolor);
		paint.setStyle(Paint.Style.FILL);
	}
	
	public void setStyle(Paint.Style s)
	{
		paint.setStyle(s);
	}
	
	public abstract void drawObject(Canvas c);
	
}



/*
 * type
 * 0 : 
 * 1 :
 * 2 :
 * 3 :
 * 4 :
 * 5 :
 * 6 :
 * 7 :
 * 8 : 
 * 9 : 
 * 10 : 
 */