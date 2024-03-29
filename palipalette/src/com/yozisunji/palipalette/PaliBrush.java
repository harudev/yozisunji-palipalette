package com.yozisunji.palipalette;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
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

	PaliBrush(Bitmap bitmap, List<Float> brushX, List<Float> brushY, List<Float> brushP, float r, RectF rect) {
		f_paint = new Paint();
		f_paint.setAntiAlias(true);

		this.type = PaliCanvas.TOOL_BRUSH;
		this.bitmap = bitmap;
		this.brushX = new ArrayList<Float>(brushX);
		this.brushY = new ArrayList<Float>(brushY);
		this.brushP = new ArrayList<Float>(brushP);
		this.r = r;
		this.rect = rect;
		filter = new LightingColorFilter(PaliCanvas.fillColor, 1);
		tagSet();
	}
	
	PaliBrush(Bitmap bitmap, RectF rect, float theta, Paint f_p, ColorFilter filter) {
		f_paint = new Paint(f_p);		
		this.bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		this.rect = new RectF(rect);
		this.theta = theta;
		this.filter = filter;
		this.Move(PaliTouchCanvas.translateX, PaliTouchCanvas.translateY);
		tagSet();
	}

	public void drawObject(Canvas c) {
		this.type = PaliCanvas.TOOL_BRUSH;
		this.rotRect = rotateRect(this.rect, this.theta);
		c.save();
		c.rotate(theta, this.rect.centerX(), this.rect.centerY());
		f_paint.setColorFilter(filter);
		c.drawBitmap(bitmap, rect.left, rect.top, f_paint);
		c.restore();
	}

	public void Move(float dx, float dy) {
		this.rect.left += dx; this.rect.right += dx; this.rect.top += dy; this.rect.bottom += dy;		
		for(int i=0; i<brushX.size(); i++) {
			brushX.set(i, brushX.get(i)+dx);
			brushY.set(i, brushY.get(i)+dy);
		}
		tagSet();
	}

	public void Scale(float dx, float dy) {
		float width = 1f+(dx/this.bitmap.getWidth());
		float height = 1f+(dy/this.bitmap.getHeight());		
		if(width < 0) width = 0.001f;
		if(height < 0) height = 0.001f;		
		
		this.bitmap = Bitmap.createScaledBitmap(this.bitmap, (int)(this.bitmap.getWidth()*width), (int)(this.bitmap.getHeight()*height), true);

		this.rect.right = this.rect.left + ((this.rect.right - this.rect.left) * width);
		this.rect.bottom = this.rect.top + ((this.rect.bottom - this.rect.top) * height);
	}

	@Override
	public PaliObject copy() {
		PaliBrush brush = new PaliBrush(this.bitmap, this.rect, this.theta, this.f_paint, this.filter);
		
		brush.svgtag = this.svgtag;
		brush.type = this.type;
		brush.filter = this.filter;

		return brush;
	}
}
