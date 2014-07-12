package com.yozisunji.palipalette;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;

public class PaliFreeDraw extends PaliObject {
	Path path;

	PaliFreeDraw(String tag, int scolor, int fcolor)
	{
		super(tag,scolor,fcolor);
	}
	PaliFreeDraw(String tag, Path path)
	{
		s_paint = new Paint();
		s_paint.setAntiAlias(true);
		s_paint.setColor(PaliCanvas.strokeColor);
		svgtag = tag;
		this.path = path;
		this.rect = new RectF(100, 100, 500, 500);
	}
	PaliFreeDraw(String tag, Path path, RectF rect)
	{
		s_paint = new Paint();
		s_paint.setAntiAlias(true);
		s_paint.setColor(PaliCanvas.strokeColor);
		s_paint.setStyle(Paint.Style.STROKE);
		s_paint.setAlpha(PaliCanvas.alpha);
		s_paint.setStrokeWidth(PaliCanvas.strokeWidth);
		svgtag = tag;
		this.path = path;
		this.rect = rect;
	}
	PaliFreeDraw(String tag, Path path, RectF rect, int scolor)
	{
		svgtag = tag;		
		this.path = path;		
		s_paint = new Paint();        
        s_paint.setAntiAlias(true);        
        s_paint.setStyle(Paint.Style.STROKE);
        s_paint.setColor(PaliCanvas.strokeColor);
        s_paint.setAlpha(PaliCanvas.alpha);
        s_paint.setStrokeWidth(PaliCanvas.strokeWidth);
		this.rect = rect;
	}

	PaliFreeDraw()
	{	
		this.path = new Path();
	}
	
	public Path getPath()
	{
		return path;
	}
	
	public void drawObject(Canvas c) {
		s_paint.setStyle(Paint.Style.STROKE);
		c.drawPath(path, s_paint);
	}
	public void Move(float dx, float dy)
	{
		Matrix moveMatrix = new Matrix();
		moveMatrix.setTranslate(dx,dy);
		this.path.transform(moveMatrix); 
		this.rect.left += dx; this.rect.right += dx; this.rect.top += dy; this.rect.bottom += dy;
	
	}
	public void Scale(float dx, float dy)
	{
		float width = 1f+(dx/100.0f);
		float height = 1f+(dy/100.0f);		
		if(width < 0) width = 0.1f;
		if(height < 0) height = 0.1f;		

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(width, height, this.rect.left, this.rect.top);
		this.path.transform(scaleMatrix);
		
		this.rect.right = this.rect.left + ((this.rect.right - this.rect.left) * width);
		this.rect.bottom = this.rect.top + ((this.rect.bottom - this.rect.top) * height);

	}
}
