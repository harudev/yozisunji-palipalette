
package com.yozisunji.palipalette;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.Paint.Style;
import android.provider.SyncStateContract.Constants;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PaliCanvas extends SurfaceView implements SurfaceHolder.Callback {
	public static final int TOOL_PENCIL=0;
	public static final int TOOL_CIRCLE=1;
	public static final int TOOL_RECTANGLE=2;
	
	public static int strokeColor;
	public static int fillColor;
	public static int width, height;
	public static int currentLayer;
	public static int currentObject;
	public static int selectedTool=0;

		
	public PaliCanvas(Context c, AttributeSet attrs)
	{
		super(c, attrs);
		strokeColor = Color.BLACK;
		fillColor = Color.RED;
		currentLayer=SVGParser.Layersize-1;
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
	}
	public void setBound(int w, int h)
	{
		width = w;
		height = h;
	}
	
	 protected void onFinishInflate() {
	        setClickable(true);
	        Log.w(Constants.DATA,"onFinishInflate()");
	 }
	  

	 public void DrawScreen() 
	 {
		Canvas cnvs = null;
		PaliObject temp;	
		try
		{
			cnvs = getHolder().lockCanvas(null);
			
			synchronized(getHolder())
			{
				 Paint p = new Paint();
                 p.setColor(Color.WHITE);
                 cnvs.drawPaint(p);
				for(int i = 0; i<SVGParser.Layers.size();i++)
				 {
					 if(SVGParser.Layers.get(i).visibility==100)
					 {
						 for(int j=0;j<SVGParser.Layers.get(i).objs.size();j++)
						 {
							 temp = SVGParser.Layers.get(i).objs.get(j);
							 temp.setStrokeColor(strokeColor);
							 temp.setFillColor(fillColor);
							 temp.drawObject(cnvs);
						 }
					 }
				 }
			}
		}
		finally
		{
			if(cnvs != null) getHolder().unlockCanvasAndPost(cnvs);
		}
			
		 
		 
	 }
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
			DrawScreen();
		    
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
			DrawScreen();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
}
