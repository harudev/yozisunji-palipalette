
package com.yozisunji.palipalette;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PaliCanvas extends SurfaceView implements SurfaceHolder.Callback {
	public static final int TOOL_PENCIL=0;
	public static final int TOOL_BRUSH=1;
	public static final int TOOL_CIRCLE=2;
	public static final int TOOL_ELLIPSE=3;
	public static final int TOOL_RECTANGLE=4;
	public static final int TOOL_PICKOBJECT=5;
	public static int strokeColor;
	public static int fillColor;
	public static int alpha;
	public static int strokeWidth;
	public static int width, height;
	public static int currentLayer;
	public static int currentObject;
	public static int selectedTool=5;
	
	public static boolean drawMode = true;
	public static float zoom = 1;
	private Bitmap presaveBuffer, saveBuffer;
	public static int scale;
	
	public static ArrayList<PaliPoint> CopiedObjArr;

		
	public PaliCanvas(Context c, AttributeSet attrs)
	{
		super(c, attrs);
		strokeColor = Color.BLUE;
		fillColor = Color.YELLOW;
		alpha = 255;
		strokeWidth = 3;
		currentLayer=SVGParser.Layersize-1;
		currentObject=-1;
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
	}
	public void setBound(int w, int h)
	{
		width = w;
		height = h;
		
		saveBuffer = Bitmap.createBitmap(width,height,Config.ARGB_8888);
	}
	
	 protected void onFinishInflate() {
	        setClickable(true);
	 }
	  

	 public void DrawScreen() 
	 {
		Canvas cnvs = null;
		PaliObject temp;	
		drawMode = true;
		
		try
		{
			
			cnvs = new Canvas(saveBuffer);
			
			cnvs = getHolder().lockCanvas(null);			
			
			if(drawMode)
			{		
				synchronized(getHolder())
				{
					Paint p = new Paint();
					p.setColor(Color.WHITE);
					cnvs.drawPaint(p);
					cnvs.scale(zoom, zoom);

                    
					for(int i = 0; i<SVGParser.Layers.size();i++)
					 {
						 if(SVGParser.Layers.get(i).visibility==100)
						 {
							 for(int j=0;j<SVGParser.Layers.get(i).objs.size();j++)
							 {
								 temp = SVGParser.Layers.get(i).objs.get(j);
								 if(PaliCanvas.selectedTool != PaliCanvas.TOOL_BRUSH) {
									 temp.setColor(temp.paint.getColor(), temp.paint.getColor());
								 }
								 temp.setWidth(temp.paint.getStrokeWidth());
								 temp.drawObject(cnvs);
							 }
						 }
					 }
					
				}
				//presaveBuffer = saveBuffer.copy(Bitmap.Config.ARGB_8888, false);
				//saveBuffer.recycle();
			}
			else
			{
				
				synchronized(getHolder())
				{
					
					cnvs.drawBitmap(saveBuffer,0,0,new Paint());
					Log.w("presave Width,height", Integer.toString(saveBuffer.getWidth())+" : "+Integer.toString(saveBuffer.getHeight()));
					temp = SVGParser.Layers.get(this.currentLayer).objs.get(this.currentObject);
					temp.setStrokeColor(strokeColor);
					temp.setFillColor(fillColor);
					temp.drawObject(cnvs);
					
				}
				
				//presaveBuffer.recycle();
				//presaveBuffer = saveBuffer.copy(Bitmap.Config.ARGB_8888, false);
				//saveBuffer.recycle();
				drawMode = true;
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

