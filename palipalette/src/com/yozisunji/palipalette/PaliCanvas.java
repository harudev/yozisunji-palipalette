
package com.yozisunji.palipalette;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PaliCanvas extends SurfaceView implements SurfaceHolder.Callback {
	public static final int TOOL_PICKOBJECT=0;
	public static final int TOOL_PEN=1;
	public static final int TOOL_PENCIL=2;
	public static final int TOOL_BRUSH=3;
	public static final int TOOL_CIRCLE=4;
	public static final int TOOL_ELLIPSE=5;
	public static final int TOOL_RECTANGLE=6;
	public static final int TOOL_STAR=7;
	public static final int TOOL_COLORPICKER=8;
	
	public static final int TOOL_FACTOR = 10;
	
	public static int strokeColor;
	public static int fillColor;
	public static int alpha;
	public static int strokeWidth;
	public static int width, height;
	public static int currentLayer;
	public static int currentObject;
	public static int selectedTool=0;
	
	public static boolean drawMode = true;
	public static float canvasX=0, canvasY=0;
	public static float zoom = 1;
	private Bitmap presaveBuffer, saveBuffer;
	public static int scale;
	
	public static ArrayList<PaliPoint> CopiedObjArr;
	
	public static PaliHistoryStack history;

		
	public PaliCanvas(Context c, AttributeSet attrs)
	{
		super(c, attrs);
		strokeColor = Color.RED;
		fillColor = Color.BLUE;
		alpha = 255;
		strokeWidth = 10;
		currentLayer=SVGParser.Layersize-1;
		currentObject=-1;
		history = new PaliHistoryStack();
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
					p.setColor(Color.DKGRAY);
					cnvs.drawPaint(p);
					
					cnvs.scale(zoom, zoom);
					cnvs.translate(canvasX/zoom, canvasY/zoom);
					
					RectF r = new RectF();
					r.left = 0;
					r.right = width;
					r.top = 0;
					r.bottom = height;
					
					p.setStyle(Style.FILL);
					p.setColor(Color.WHITE);
					cnvs.drawRect(r, p);
					
					for(int i = 0; i<SVGParser.Layers.size();i++)
					 {
						 if(SVGParser.Layers.get(i).visibility==true)
						 {
							 for(int j=0;j<SVGParser.Layers.get(i).objs.size();j++)
							 {
								 temp = SVGParser.Layers.get(i).objs.get(j);
								 if(PaliCanvas.selectedTool != PaliCanvas.TOOL_BRUSH) {
									 temp.setColor(temp.s_paint.getColor(), temp.f_paint.getColor());
								 }
								 temp.setWidth(temp.s_paint.getStrokeWidth());
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

