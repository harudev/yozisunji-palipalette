
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
	public int		strokeColor;
	public int		fillColor;
	int				width, height;
	public int 			currentLayer;
	public int 			currentObject;
	public static int selectedTool=2;
	
	Path path = new Path();
	float downX=0, downY=0, upX=0, upY=0, moveX=0, moveY=0, premoveX=0, premoveY=0;
	String 			movement="";
	String 			html="";
	
	PaliObject tempObj;
	Paint p;
	
	
		
	public PaliCanvas(Context c, AttributeSet attrs)
	{
		super(c, attrs);
		strokeColor = Color.BLACK;
		fillColor = Color.RED;
		currentLayer=SVGParser.Layersize-1;
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		tempObj=null;
		
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
				/*
				if(tempObj!=null)
				{
					p.setColor(Color.BLUE);
					p.setStyle(Style.STROKE);
					p.setColor(Color.GREEN);
					p.setStyle(Style.FILL);
					tempObj.drawObject(cnvs);
					
				}
				*/	
			}
		}
		finally
		{
			if(cnvs != null) getHolder().unlockCanvasAndPost(cnvs);
		}
			
		 
		 
	 }
	/*
	 public boolean onTouchEvent(MotionEvent e)
	 {
		 super.onTouchEvent(e);
		 
		 switch(e.getAction())
		 {
		 
		 case MotionEvent.ACTION_DOWN:
			 downX = e.getX();
			 downY = e.getY();
			 			 
			 switch(selectedTool)
			 {
			 case 0:
				 path.moveTo(downX, downY);
				 tempObj = new PaliFreeDraw();
				 ((PaliFreeDraw)tempObj).getPath().moveTo(downX, downY);
				 break;
			 }
			 return true;
		 case MotionEvent.ACTION_MOVE:
			 moveX = e.getX();
			 moveY = e.getY();
			 
			 switch(selectedTool)
			 {
			 case 0:
				 movement = movement + " " + moveX + " " + moveY;
				 path.lineTo(moveX, moveY);
				 ((PaliFreeDraw)tempObj).getPath().lineTo(moveX,moveY);
				 break;
			 case 1:
				 tempObj = new PaliCircle(downX, downY, (float)Math.sqrt((float)Math.pow(moveX-downX, 2) + (float)Math.pow(moveY-downY, 2)));
				 break;
			 case 2:
				 tempObj = new PaliRectangle(downX, downY, moveX, moveY);
				 
			 }
			 this.DrawScreen();
			 //this.invalidate();
			 return true;
		 case MotionEvent.ACTION_UP:
			 upX = e.getX();
			 upY = e.getY();	
			 switch(selectedTool) {
			 case 0: // FreeDraw
				 html = "<path fill=\"none\" stroke=\"black\" d=\"M "+downX+" "+downY+""+movement+"\" />";
				 SVGParser.Layers.get(currentLayer).objs.add(new PaliFreeDraw(html, path));
				 break;
			 case 1: // Circle
				 float r = (float)Math.sqrt((float)Math.pow(upX-downX, 2) + (float)Math.pow(upY-downY, 2));
				 float cx = downX;
				 float cy = downY;
				 
				 html = "<circle cs="+ cx +" cy=" + cy + " r=" + r + " style=\"fill:yellow;stroke:purple;stroke-width:2\"/> ";
				 SVGParser.Layers.get(currentLayer).objs.add(new PaliCircle(html,cx,cy,r));
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
				 SVGParser.Layers.get(currentLayer).objs.add(new PaliRectangle(html,left,top,right,bottom));
				 break;
			 }
			 tempObj=null;
			 this.DrawScreen();
			 //this.invalidate();
			 //canvas.DrawScreen();
			 return true;
		 }
		 return false;
	 
	 }
	 */
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
