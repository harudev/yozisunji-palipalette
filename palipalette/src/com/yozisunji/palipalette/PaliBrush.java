package com.yozisunji.palipalette;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class PaliBrush extends PaliObject {

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
	
	PaliBrush(String tag, Bitmap bitmap, RectF rect, float theta, Paint f_p) {
		f_paint = new Paint(f_p);
		svgtag = tag;
		this.bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		this.rect = new RectF(rect);
		this.theta = theta;
		this.Move(PaliTouchCanvas.translateX, PaliTouchCanvas.translateY);
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
		this.type = PaliCanvas.TOOL_BRUSH;
		c.save();
		c.rotate(theta, this.rect.centerX(), this.rect.centerY());
		c.drawBitmap(bitmap, rect.left, rect.top, f_paint);
		c.restore();
	}

	public void Move(float dx, float dy) {
		this.rect.left += dx; this.rect.right += dx; this.rect.top += dy; this.rect.bottom += dy;		
	}

	public void Scale(float dx, float dy) {
		float width = 1f+(dx/100.0f);
		float height = 1f+(dy/100.0f);		
		if(width < 0) width = 0.1f;
		if(height < 0) height = 0.1f;		
		
		this.bitmap = Bitmap.createScaledBitmap(this.bitmap, (int)(this.bitmap.getWidth()*width), (int)(this.bitmap.getHeight()*height), true);

		this.rect.right = this.rect.left + ((this.rect.right - this.rect.left) * width);
		this.rect.bottom = this.rect.top + ((this.rect.bottom - this.rect.top) * height);
	}
}
