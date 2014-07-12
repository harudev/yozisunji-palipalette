package com.yozisunji.palipalette;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class PaliCircle extends PaliObject {
	float x, y;
	float r;

	PaliCircle(String tag, int scolor, int fcolor)
	{
		super(tag,scolor,fcolor);
	}
	PaliCircle(float x, float y, float r)
	{
		super();
		f_paint = new Paint();
		f_paint.setAntiAlias(true);
		f_paint.setColor(Color.GREEN);
		f_paint.setStyle(Paint.Style.FILL);
		this.x = x;
		this.y = y;
		this.r = r;
		
		this.rect = new RectF(x-r, y-r, x+r, y+r);
	}
	
	PaliCircle(String tag, float x, float y, float r)
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
		
		s_paint = new Paint();        
        s_paint.setAntiAlias(true);        
        s_paint.setStyle(Paint.Style.STROKE);
        s_paint.setColor(scolor);
        
        f_paint = new Paint();        
        f_paint.setAntiAlias(true);
        f_paint.setStyle(Paint.Style.FILL);
        f_paint.setColor(fcolor);
        
		this.rect = new RectF(x-r, y-r, x+r, y+r);
	}

	public void drawObject(Canvas c) {		
		c.drawCircle(x, y, r, s_paint);
		c.drawCircle(x, y, r, f_paint);
	}
	public void Move(float dx, float dy)
	{
		this.x += dx;
		this.y += dy;
		this.rect.left += dx; this.rect.right += dx; this.rect.top += dy; this.rect.bottom += dy;
	}
	public void Scale(float dx, float dy)
	{
		float s = (float) Math.sqrt(dx*dx + dy*dy);
		
		if(dx+dy > 0) {
			this.r += Math.sqrt(dx*dx + dy*dy);
			this.rect.left -= s; this.rect.right += s; this.rect.top -= s; this.rect.bottom += s;
			
		}
		else {
			this.r -= Math.sqrt(dx*dx + dy*dy);
			this.rect.left += s; this.rect.right -= s; this.rect.top += s; this.rect.bottom -= s;
		}
	}
}
