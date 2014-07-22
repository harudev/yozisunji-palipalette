package com.yozisunji.palipalette;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;

public abstract class PaliObject {
	Paint f_paint;
	Paint s_paint;
	String svgtag;
	
	RectF rect;
	float theta = 0;
	
	int type;
	float x,y;
	float r;
	float left, top, right, bottom;
	Path path;
	Bitmap bitmap;
	
	ColorFilter filter;
	
	boolean rotFlag = false;
	RectF rotRect;
	
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

	public void Rotate(float theta)
	{
		if(rotFlag == false) {
			rotFlag = true;
		}		
		this.theta += theta;
		this.theta %= 360;
		
		tagSet();
	}
	public void tagSet() 
	{
		float width = 0;
		float height = 0;
		
		String fillColor = Integer.toHexString(f_paint.getColor()).substring(2);
		String strokeColor = Integer.toHexString(s_paint.getColor()).substring(2);
		Log.i("debug","fill: "+fillColor);
		Log.i("debug","stroke: "+strokeColor);
		
		switch(this.type) {
		case PaliCanvas.TOOL_CIRCLE:
			svgtag = "<circle cx=\"" + x + "\" cy=\"" + y + "\" r=\""
					+ r + "\" stroke=\"#" + strokeColor
					+ "\" stroke-width=\"" + s_paint.getStrokeWidth()
					+ "\" fill=\"#" + fillColor
					+ "\" stroke-opacity=\"" + s_paint.getAlpha()
					+ "\" fill-opacity=\"" + f_paint.getAlpha() + "\" />";
			break;
		case PaliCanvas.TOOL_ELLIPSE:				
			width = this.right - this.left;
			height = this.bottom - this.top;
			x = left + width/2;
			y = top + height/2;
			svgtag = "<ellipse cx=\"" + x + "\" cy=\"" + y + "\" rx=\""
					+ width/2 + "\" ry=\"" + height/2 + "\" stroke=\"#"
					+ strokeColor + "\" stroke-width=\"" + s_paint.getStrokeWidth()
					+ "\" fill=\"#" + fillColor
					+ "\" stroke-opacity=\"" + s_paint.getAlpha()
					+ "\" fill-opacity=\"" + f_paint.getAlpha()
					+ "\" transform=\"rotate("+theta+","+x+","+y+")\" />";
			break;
		case PaliCanvas.TOOL_RECTANGLE:
			width = this.right - this.left;
			height = this.bottom - this.top;
			x = left;
			y = top;
			svgtag = "<rect x=\"" + x + "\" y=\"" + y + "\" width=\""
					+ width + "\" height=\"" + height + "\" stroke=\"#"
					+ strokeColor + "\" stroke-width=\"" + s_paint.getStrokeWidth()
					+ "\" fill=\"#" + fillColor
					+ "\" stroke-opacity=\"" + s_paint.getAlpha()
					+ "\" fill-opacity=\"" + f_paint.getAlpha()
					+ "\" transform=\"rotate("+theta+","+(x+width/2)+","+(y+height/2)+")\" />";
			break;			
		}
	}
	public abstract void drawObject(Canvas c);
	public abstract void Move(float dx, float dy);
	public abstract void Scale(float dx, float dy);	
	//public abstract void copyInfo();
	
	RectF rotateRect(RectF r, float theta) {
		double radian = ((Math.PI * theta) / 180);
		double sin = Math.sin(radian);
		double cos = Math.cos(radian);

		float lt_x = (float) ((r.centerX() - r.left) * cos - ((r.centerY() - r.top) * sin));
		float lt_y = (float) ((r.centerX() - r.left) * sin - ((r.centerY() - r.top) * cos));

		float rt_x = (float) ((r.centerX() - r.right) * cos - ((r.centerY() - r.top) * sin));
		float rt_y = (float) ((r.centerX() - r.right) * sin - ((r.centerY() - r.top) * cos));

		float lb_x = (float) ((r.centerX() - r.left) * cos - ((r.centerY() - r.bottom) * sin));
		float lb_y = (float) ((r.centerX() - r.left) * sin - ((r.centerY() - r.bottom) * cos));

		float rb_x = (float) ((r.centerX() - r.right) * cos - ((r.centerY() - r.bottom) * sin));
		float rb_y = (float) ((r.centerX() - r.right) * sin - ((r.centerY() - r.bottom) * cos));

		float r_left = Math.min(lt_x, rt_x);
		r_left = Math.min(r_left, lb_x);
		r_left = Math.min(r_left, rb_x);	
		
		float r_top = Math.min(lt_y, rt_y);
		r_top = Math.min(r_top, lb_y);
		r_top = Math.min(r_top, rb_y);
		
		float r_right = Math.max(lt_x, rt_x);
		r_right = Math.max(r_right, lb_x);
		r_right = Math.max(r_right, rb_x);	
		
		float r_bottom = Math.max(lt_y, rt_y);
		r_bottom = Math.max(r_bottom, lb_y);
		r_bottom = Math.max(r_bottom, rb_y);

		RectF r_rect = new RectF(r_left+r.centerX(), r_top+r.centerY(), r_right+r.centerX(), r_bottom+r.centerY());
		return r_rect;
	}
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