package com.yozisunji.palipalette;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class PaliBrush extends PaliObject {
	Bitmap bitmap;

	PaliBrush(String tag, int scolor, int fcolor)
	{
		super(tag,scolor,fcolor);
	}
	PaliBrush(String tag, Bitmap bitmap)
	{
		paint = new Paint();
		paint.setAntiAlias(true);
		svgtag = tag;
		this.bitmap = bitmap;
		this.rect = new RectF(100, 100, 500, 500);
	}
	PaliBrush(String tag, Bitmap bitmap, RectF rect)
	{
		paint = new Paint();
		paint.setAntiAlias(true);
		svgtag = tag;
		this.bitmap = bitmap;
		this.rect = rect;
	}
	PaliBrush(String tag, Bitmap bitmap, RectF rect, int scolor, int fcolor)
	{
		svgtag = tag;		
		this.bitmap = bitmap;		
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(scolor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(fcolor);
		paint.setStyle(Paint.Style.FILL);
		this.rect = rect;
	}
	
	public void drawObject(Canvas c) {
		c.drawBitmap(bitmap, rect.left, rect.top, paint);
		
		/*
		Bitmap bm = Bitmap.createBitmap(1200, 1200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);

        canvas.drawCircle(1000, 1000, 30, paint);
        canvas.drawCircle(80, 50, 30, paint);

        c.drawBitmap(bm, 0, 0, paint);
        */
	}
}
