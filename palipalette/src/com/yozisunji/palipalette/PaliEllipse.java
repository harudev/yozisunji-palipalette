package com.yozisunji.palipalette;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class PaliEllipse extends PaliObject {
	
	PaliEllipse (RectF r)
	{
		super();
		this.rect = new RectF(r);
		this.left = rect.left;
		this.top = rect.top;
		this.right = rect.right;
		this.bottom = rect.bottom;
	}
		
	PaliEllipse(float left, float top, float right, float bottom)
	{
		super();
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
        
        this.type = PaliCanvas.TOOL_ELLIPSE;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		
		this.rect = new RectF(left, top, right, bottom);
		tagSet();
	}
	
	PaliEllipse(float left, float top, float right, float bottom, int strokeColor, int fillColor, float strokeWidth, float strokeOpacity, float fillOpacity, float theta)
	{
		s_paint = new Paint();        
        s_paint.setAntiAlias(true);        
        s_paint.setStyle(Paint.Style.STROKE);
        s_paint.setColor(strokeColor);
        s_paint.setAlpha((int)strokeOpacity);
        s_paint.setStrokeWidth(strokeWidth);
        
        f_paint = new Paint();        
        f_paint.setAntiAlias(true);
        f_paint.setStyle(Paint.Style.FILL);
        f_paint.setColor(fillColor);
        f_paint.setAlpha((int)fillOpacity); 
        
        this.type = PaliCanvas.TOOL_ELLIPSE;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.theta = theta;
		this.rect = new RectF(left, top, right, bottom);
		tagSet();
	}
	
	PaliEllipse(float left, float top, float right, float bottom, float theta, Paint s_p, Paint f_p)
	{
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.theta = theta;		
		this.rect = new RectF(left, top, right, bottom);
		s_paint = new Paint(s_p);       
        f_paint = new Paint(f_p);
        tagSet();
	}
		
	public void drawObject(Canvas c) {
		this.type = PaliCanvas.TOOL_ELLIPSE;
		this.rotRect = rotateRect(this.rect, this.theta);
		c.save();
		c.rotate(theta, this.rect.centerX(), this.rect.centerY());
		c.drawOval(new RectF(left, top, right, bottom), s_paint);
		c.drawOval(new RectF(left, top, right, bottom), f_paint);
		c.restore();		
	}
	public void Move(float dx, float dy)
	{
		this.left += dx;
		this.right += dx;
		this.top += dy;
		this.bottom += dy;
		this.rect.left += dx; this.rect.right += dx; this.rect.top += dy; this.rect.bottom += dy;	
		
		tagSet();
	}
	public void Scale(float dx, float dy)
	{
		if(dx>0) {
			this.right += dx/2;			
			this.rect.right += dx/2; 
		}
		else {
			this.right += dx*2;			
			this.rect.right += dx*2; 
		}
		
		if(dy>0) {
			this.bottom += dy/2;
			this.rect.bottom += dy/2;
		}
		else {
			this.bottom += dy*2;
			this.rect.bottom += dy*2;
		}	
		
		tagSet();
	}

	@Override
	public PaliObject copy() {
		PaliEllipse ellipse = new PaliEllipse(this.left, this.top, this.right, this.bottom, this.theta, this.s_paint, this.f_paint);
		
		ellipse.svgtag = this.svgtag;
		ellipse.type = this.type;
		ellipse.filter = this.filter;

		return ellipse;
	}
}