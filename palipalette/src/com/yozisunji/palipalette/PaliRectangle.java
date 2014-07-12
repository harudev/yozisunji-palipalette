package com.yozisunji.palipalette;

import android.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class PaliRectangle extends PaliObject {
	float left, top, right, bottom;

	PaliRectangle (RectF r)
	{
		this.rect = new RectF(r);
		this.left = rect.left;
		this.top = rect.top;
		this.right = rect.right;
		this.bottom = rect.bottom;
	}
	PaliRectangle(String tag, int scolor, int fcolor)
	{
		super(tag,scolor,fcolor);
	}
	
	PaliRectangle(float left, float top, float right, float bottom)
	{
		f_paint = new Paint();
		f_paint.setAntiAlias(true);
		f_paint.setColor(Color.GREEN);
		f_paint.setStyle(Paint.Style.FILL);
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		
		this.rect = new RectF(left, top, right, bottom);
	}
	
	PaliRectangle(String tag, float left, float top, float right, float bottom)
	{
		s_paint = new Paint();        
        s_paint.setAntiAlias(true);        
        s_paint.setStyle(Paint.Style.STROKE);
        s_paint.setColor(PaliCanvas.strokeColor);
        s_paint.setAlpha(PaliCanvas.alpha);
        s_paint.setStrokeWidth(PaliCanvas.strokeWidth);
        
        f_paint = new Paint();        
        f_paint.setAntiAlias(true);
        f_paint.setStyle(Paint.Style.FILL);
        f_paint.setColor(PaliCanvas.fillColor);
        f_paint.setAlpha(PaliCanvas.alpha);  
        
		svgtag = tag;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		
		this.rect = new RectF(left, top, right, bottom);
	}
	
	PaliRectangle(String tag, float left, float top, float right, float bottom, int scolor, int fcolor)
	{
		svgtag = tag;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		
		s_paint = new Paint();        
        s_paint.setAntiAlias(true);        
        s_paint.setStyle(Paint.Style.STROKE);
        s_paint.setColor(scolor);
        
        f_paint = new Paint();        
        f_paint.setAntiAlias(true);
        f_paint.setStyle(Paint.Style.FILL);
        f_paint.setColor(fcolor);
		
		this.rect = new RectF(left, top, right, bottom);
	}
	
	public void drawObject(Canvas c) {
		c.drawRect(left, top, right, bottom, s_paint);
		c.drawRect(left, top, right, bottom, f_paint);
	}
	public void Move(float dx, float dy)
	{
	
	}
	public void Scale(float dx, float dy)
	{
		
	}
}
