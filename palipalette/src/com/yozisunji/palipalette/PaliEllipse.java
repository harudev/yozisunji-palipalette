package com.yozisunji.palipalette;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class PaliEllipse extends PaliObject {
	
	float left, top, right, bottom;

	PaliEllipse (RectF r)
	{
		this.rect = new RectF(r);
		this.left = rect.left;
		this.top = rect.top;
		this.right = rect.right;
		this.bottom = rect.bottom;
	}
	PaliEllipse(String tag, int scolor, int fcolor)
	{
		super(tag,scolor,fcolor);
	}
	PaliEllipse(String tag, float left, float top, float right, float bottom)
	{
		paint = new Paint();
		paint.setAntiAlias(true);
		svgtag = tag;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		
		this.rect = new RectF(left, top, right, bottom);
	}
	PaliEllipse(float left, float top, float right, float bottom)
	{
		paint = new Paint();
		paint.setAntiAlias(true);
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		
		this.rect = new RectF(left, top, right, bottom);
	}
	PaliEllipse(String tag, float left, float top, float right, float bottom, int scolor, int fcolor)
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
		c.drawOval(new RectF(left, top, right, bottom), paint);
	}
}