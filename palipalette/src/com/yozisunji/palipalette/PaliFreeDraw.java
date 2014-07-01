package com.yozisunji.palipalette;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class PaliFreeDraw extends PaliObject {
	Path path;

	PaliFreeDraw(String tag, int scolor, int fcolor)
	{
		super(tag,scolor,fcolor);
	}
	PaliFreeDraw(String tag, Path path)
	{
		paint = new Paint();
		paint.setAntiAlias(true);
		svgtag = tag;
		this.path = path;
		this.rect = new RectF(100, 100, 500, 500);
	}
	PaliFreeDraw(String tag, Path path, RectF rect)
	{
		paint = new Paint();
		paint.setAntiAlias(true);
		svgtag = tag;
		this.path = path;
		this.rect = rect;
	}
	PaliFreeDraw(String tag, Path path, RectF rect, int scolor, int fcolor)
	{
		svgtag = tag;		
		this.path = path;		
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(scolor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(fcolor);
		paint.setStyle(Paint.Style.FILL);
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
		paint.setStyle(Paint.Style.STROKE);
		c.drawPath(path, paint);
	}
}
