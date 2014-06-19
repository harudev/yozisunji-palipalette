package mem.yozisunji.palipalette;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;
import android.util.Log;

public abstract class PaliObject {
	Paint paint;
	String svgtag;
	boolean validation=false;
	
	PaliObject(){
		paint = new Paint();
        paint.setAntiAlias(true);
	}
	
	PaliObject(String tag, int scolor, int fcolor)
	{
		svgtag = tag;
		paint = new Paint();
        paint.setAntiAlias(true);
		paint.setColor(scolor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(fcolor);
		paint.setStyle(Paint.Style.FILL);
	}
	public void setValidation(boolean n)
	{
		validation=n;
	}
	public boolean getValidation()
	{
		return validation;
	}
	public void SetTag(String tag)
	{
		svgtag = tag;
	}
	
	public void setStrokeColor(int color)
	{
		paint.setColor(color);
		paint.setStyle(Paint.Style.STROKE);
	}
	
	public void setFillColor(int color)
	{
		paint.setColor(color);
		paint.setStyle(Paint.Style.FILL);
	}
	
	public abstract void drawObject(Canvas c);
	
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