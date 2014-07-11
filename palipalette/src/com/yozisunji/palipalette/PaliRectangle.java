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
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.GREEN);
		paint.setStyle(Paint.Style.FILL);
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		
		this.rect = new RectF(left, top, right, bottom);
	}
	
	PaliRectangle(String tag, float left, float top, float right, float bottom)
	{
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(PaliCanvas.fillColor);
		paint.setStyle(Paint.Style.FILL);
		paint.setAlpha(PaliCanvas.alpha);
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
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(scolor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(fcolor);
		paint.setStyle(Paint.Style.FILL);
		
		this.rect = new RectF(left, top, right, bottom);
	}
	
	public void drawObject(Canvas c) {
		c.drawRect(left, top, right, bottom, paint);
	}
}
