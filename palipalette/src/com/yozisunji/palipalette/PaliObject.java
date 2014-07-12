package com.yozisunji.palipalette;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public abstract class PaliObject {
	Paint f_paint;
	Paint s_paint;
	String svgtag;
	
	RectF rect;
	PaliObject(){        
        f_paint = new Paint();
        f_paint.setAntiAlias(true);
        s_paint = new Paint();
        s_paint.setAntiAlias(true);
	}
	
	PaliObject(String tag, int scolor, int fcolor)
	{
		svgtag = tag;
		s_paint = new Paint();        
        s_paint.setAntiAlias(true);        
        s_paint.setStyle(Paint.Style.STROKE);
        s_paint.setColor(scolor);
        
        f_paint = new Paint();        
        f_paint.setAntiAlias(true);
        f_paint.setStyle(Paint.Style.FILL);
        f_paint.setColor(fcolor);
	}
	public void SetTag(String tag)
	{
		svgtag = tag;
	}
	
	public void setStrokeColor(int color)
	{
		s_paint.setColor(color);
		s_paint.setStyle(Paint.Style.STROKE);
	}
	
	public void setFillColor(int color)
	{
		f_paint.setColor(color);
		f_paint.setStyle(Paint.Style.FILL);
	}
	
	public void setWidth(float w)
	{
		s_paint.setStrokeWidth(w);
	}
	
	public void setColor(int scolor, int fcolor)
	{
		s_paint.setColor(scolor);
		s_paint.setStyle(Paint.Style.STROKE);
		f_paint.setColor(fcolor);
		f_paint.setStyle(Paint.Style.FILL);
	}
	
	public void setStyle(Paint.Style s)
	{
		s_paint.setStyle(s);
	}
	
	public RectF getRect()
	{
		return this.rect;
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