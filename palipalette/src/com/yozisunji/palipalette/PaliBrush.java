package com.yozisunji.palipalette;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class PaliBrush extends PaliObject {
	Bitmap bitmap;

	PaliBrush(String tag, int scolor, int fcolor) {
		super(tag, scolor, fcolor);
	}

	PaliBrush(String tag, Bitmap bitmap) {
		f_paint = new Paint();
		f_paint.setAntiAlias(true);
		svgtag = tag;
		this.bitmap = bitmap;
		this.rect = new RectF(100, 100, 500, 500);
	}

	PaliBrush(String tag, Bitmap bitmap, RectF rect) {
		f_paint = new Paint();
		f_paint.setAntiAlias(true);
		svgtag = tag;
		this.bitmap = bitmap;
		this.rect = rect;
	}

	PaliBrush(String tag, Bitmap bitmap, RectF rect, int scolor, int fcolor) {
		svgtag = tag;
		this.bitmap = bitmap;
		s_paint = new Paint();
		s_paint.setAntiAlias(true);
		s_paint.setStyle(Paint.Style.STROKE);
		s_paint.setColor(scolor);

		f_paint = new Paint();
		f_paint.setAntiAlias(true);
		f_paint.setStyle(Paint.Style.FILL);
		f_paint.setColor(fcolor);
		this.rect = rect;
	}

	public void drawObject(Canvas c) {
		c.drawBitmap(bitmap, rect.left, rect.top, f_paint);
	}

	public void Move(float dx, float dy) {

	}

	public void Scale(float dx, float dy) {

	}
}
