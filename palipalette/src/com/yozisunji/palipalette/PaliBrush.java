package com.yozisunji.palipalette;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class PaliBrush extends PaliObject {
	Path path;

	PaliBrush(String tag, int scolor, int fcolor)
	{
		super(tag,scolor,fcolor);
	}
	PaliBrush(String tag, Path path)
	{
		paint = new Paint();
		paint.setAntiAlias(true);
		svgtag = tag;
		this.path = path;
		this.rect = new RectF(100, 100, 500, 500);
	}
	PaliBrush(String tag, Path path, RectF rect)
	{
		paint = new Paint();
		paint.setAntiAlias(true);
		svgtag = tag;
		this.path = path;
		this.rect = rect;
	}
	PaliBrush(String tag, Path path, RectF rect, int scolor, int fcolor)
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

	PaliBrush()
	{	
		this.path = new Path();
	}
	
	public Path getPath()
	{
		return path;
	}
	
	public void drawObject(Canvas c) {
		paint.setStyle(Paint.Style.FILL);
		c.drawPath(path, paint);
	}
}
