package com.yozisunji.palipalette;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.provider.SyncStateContract.Constants;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class PaliCanvas extends SurfaceView implements SurfaceHolder.Callback {
	SurfaceHolder	sHolder;
	Context			sContext;
	private Canvas 	canvas;
	public int		strokeColor;
	public int		fillColor;
	private Picture picture;
	private Picture prepicture;
	int				width, height;
	PictureDrawable pd;
	String 			movement="";
	String 			html="";
	int 			currentLayer;
	int 			currentObject;
	static int 		drawCase;
	public static int selectedTool=0;
	
	
	Path path = new Path();
	float downX=0, downY=0, upX=0, upY=0, moveX=0, moveY=0;
	
	public PaliCanvas(Context c)
	{
		super(c);
		canvas = new Canvas();
		canvas.drawColor(Color.WHITE);
		picture = new Picture();
		pd = new PictureDrawable(picture);
		strokeColor = Color.BLACK;
		fillColor = Color.RED;
		
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		sHolder = holder;
		sContext = c;
	}
	
	public PaliCanvas(Context c, AttributeSet attrs)
	{
		super(c, attrs);
		canvas = new Canvas();
		canvas.drawColor(Color.WHITE);
		prepicture = new Picture();
		picture = new Picture();
		pd = new PictureDrawable(picture);
		strokeColor = Color.BLACK;
		fillColor = Color.RED;
		currentLayer=SVGParser.Layersize-1;
		if(this.isHardwareAccelerated())
			Log.w("Catch","View Accelerated");
		else
			Log.w("Catch","View Not Accelerated");
		this.drawCase = 0;
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		sHolder = holder;
		sContext = c;
	}
	public PaliCanvas(Context c, AttributeSet attrs,int defStyle)
	{
		super(c, attrs, defStyle);
		canvas = new Canvas();
		canvas.drawColor(Color.WHITE);
		pd = new PictureDrawable(picture);
		picture = new Picture();
		strokeColor = Color.BLACK;
		fillColor = Color.RED;
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		sHolder = holder;
		sContext = c;
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
	  

	 protected void DrawScreen() 
	 {
		android.graphics.Canvas cnvs = null;
		PaliObject temp;	
		try
		{
			cnvs = getHolder().lockCanvas(null);
			
			synchronized(getHolder())
			{
				if(drawCase == 0)
				 {
					Paint p = new Paint();
					p.setColor(Color.WHITE);
					cnvs.drawPaint(p);
					 
					 Log.w("drawCase","0");
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
				 else if(drawCase == 2)
				 {
					 Log.w("drawCase","2");
					 cnvs = canvas;
					 temp = SVGParser.Layers.get(currentLayer).objs.get(SVGParser.Layers.get(currentLayer).objs.size()-1);
					 temp.setStrokeColor(strokeColor);
					 temp.setFillColor(fillColor);
					 temp.drawObject(cnvs);
					 canvas = cnvs;
				 }
			}
		}
		finally
		{
			if(cnvs != null) getHolder().unlockCanvasAndPost(cnvs);
		}
			
		 
		 
	 }
	 
	 public boolean onTouchEvent(MotionEvent e)
	 {
		 super.onTouchEvent(e);
		 
		 switch(e.getAction())
		 {
		 
		 case MotionEvent.ACTION_DOWN:
			 downX = e.getX();
			 downY = e.getY();
			 
			 if(selectedTool==0) {
				 path.moveTo(downX, downY);
			 }		
			 
			 return true;
		 case MotionEvent.ACTION_MOVE:
			 moveX = e.getX();
			 moveY = e.getY();
			 
			 if(selectedTool==0) {
				 movement = movement + " " + moveX + " " + moveY;
				 path.lineTo(moveX, moveY);
			 }
			 
			 return true;
		 case MotionEvent.ACTION_UP:
			 upX = e.getX();
			 upY = e.getY();			 			 
			 switch(selectedTool) {
			 case 0: // FreeDraw
				 html = "<path fill=\"none\" stroke=\"black\" d=\"M "+downX+" "+downY+""+movement+"\" />";
				 SVGParser.Layers.get(currentLayer).objs.add(new PaliFreeDraw(html, path));
				 PaliCanvas.drawCase = 2;
				 this.DrawScreen();
				 break;
			 case 1: // Circle
				 float r = (float)Math.sqrt((float)Math.pow(upX-downX, 2) + (float)Math.pow(upY-downY, 2));
				 float cx = downX;
				 float cy = downY;
				 
				 html = "<circle cs="+ cx +" cy=" + cy + " r=" + r + " style=\"fill:yellow;stroke:purple;stroke-width:2\"/> ";
				 SVGParser.Layers.get(currentLayer).objs.add(new PaliCircle(html,cx,cy,r));
				 PaliCanvas.drawCase = 2;
				 this.DrawScreen();
				 break;
			 case 2: // Rectangle
				 float x = Math.min(downX, upX);
				 float y = Math.min(downY, upY);
				 float width = (float)Math.sqrt((float)Math.pow(upX-downX, 2));
				 float height = (float)Math.sqrt((float)Math.pow(upY-downY, 2));
				 
				 float left = downX;
				 float top = downY;
				 float right = upX;
				 float bottom = upY;
				 
				 html = "<rect x="+x+" y="+y+" width="+width+" height="+height+" style=\"fill:rgb(0,0,255);stroke-width:3;stroke:rgb(0,0,0)\" /> ";
				 SVGParser.Layers.get(currentLayer).objs.add(new PailRectangle(html,left,top,right,bottom));
				 PaliCanvas.drawCase = 2;
				 this.DrawScreen();
				 break;
			 }
			 return true;
		 }
		 return false;
	 
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

class CanvasThread extends Thread
{
	SurfaceHolder sHolder;
	Context sContext;
	public CanvasThread(SurfaceHolder holder, Context context)
	{
		sHolder = holder;
		sContext = context;
	}
	
	public void run()
	{
	}
	
}
