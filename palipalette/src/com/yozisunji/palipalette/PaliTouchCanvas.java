
package com.yozisunji.palipalette;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class PaliTouchCanvas extends View {
	Path pencilPath = new Path();
	Path brushPath = new Path();
	float downX=0, downY=0, upX=0, upY=0, moveX=0, moveY=0, premoveX=0, premoveY=0;
	float minX=0, minY=0, maxX=0, maxY=0;
	float x=0, y=0, width=0, height=0;
	float cx=0, cy=0, r=0;
	String 			movement="";
	String 			html="";
	String fillColor;
	String strokeColor;
	int strokeWidth;	
	
	PaliObject tempObj;
	Paint p;
	
	PaliCanvas canvas;
	
	RectF rect;
	
	public boolean selected=false;
	
	private boolean zoom = false;
	float oldDist = 1f, newDist = 1f;
	private PaliTouchCanvas parent;
	
	private boolean mLongPressed = false;
	private Handler mHandler = new Handler();
	private LongPressCheckRunnable mLongPressCheckRunnable = new LongPressCheckRunnable();
	
	private int mLongPressTimeout;
	private int mScaledTouchSlope;
	long time;
	
	private Context mContext;
	

	public PaliTouchCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context; 
		// TODO Auto-generated constructor stub
		tempObj = null;
		p = new Paint();
		mLongPressTimeout = ViewConfiguration.getLongPressTimeout();
		mScaledTouchSlope = ViewConfiguration.get( context ).getScaledTouchSlop();
		parent = this;
	}
	
	public void setCanvasAddr(PaliCanvas c)
	{
		canvas = c;
	}
	
	public void onDraw(Canvas cnvs)
	{
		cnvs.scale(PaliCanvas.zoom, PaliCanvas.zoom);
		if(tempObj!=null)
		{
			tempObj.setStrokeColor(Color.BLUE);
			tempObj.setStyle(Style.STROKE);
			tempObj.setWidth(2);
			if(canvas.selectedTool!=PaliCanvas.TOOL_PICKOBJECT)
			{
				tempObj.setFillColor(Color.GREEN);
				tempObj.setStyle(Style.FILL);
			}
			tempObj.drawObject(cnvs);			
		}		
		super.onDraw(cnvs);
	}
	
	
	public boolean onTouchEvent(MotionEvent e)
	 {
		
		 super.onTouchEvent(e);		 
		 switch(e.getAction() & MotionEvent.ACTION_MASK)
		 {
		 
		 case MotionEvent.ACTION_DOWN:	
			 
			 downX = e.getX()/PaliCanvas.zoom;
			 downY = e.getY()/PaliCanvas.zoom;
			
			 switch(PaliCanvas.selectedTool)
			 {
			 case PaliCanvas.TOOL_PICKOBJECT:
				 if(this.selected && tempObj.getRect().contains(downX, downY))
				 {
					 startTimeout();
				 }
				 break;
			 case PaliCanvas.TOOL_PENCIL:
				 pencilPath.reset();
				 minX=downX; minY=downY;
                 maxX=downX; maxY=downY;
                 pencilPath.moveTo(downX, downY);
				 tempObj = new PaliFreeDraw();
				 ((PaliFreeDraw)tempObj).getPath().moveTo(downX, downY);
				 break;
			 case PaliCanvas.TOOL_BRUSH:
				 brushPath.reset();
				 minX=downX; minY=downY;
                 maxX=downX; maxY=downY;
                 brushPath.moveTo(downX, downY);
                 brushPath.addCircle(downX, downY, 30, Direction.CW);
				 tempObj = new PaliBrush();
				 ((PaliBrush)tempObj).getPath().moveTo(downX, downY);
				 break;
			 }
			 return true;
		 case MotionEvent.ACTION_POINTER_DOWN:
			 newDist = spacing(e);
			 oldDist = spacing(e);
			 this.zoom = true;
			 return true;
		 case MotionEvent.ACTION_POINTER_UP:
			 this.zoom = false;
			 return true;
		 case MotionEvent.ACTION_MOVE:
			 if(zoom==true)
			 {
				 newDist = spacing(e);
				 
				 if (newDist - oldDist > 20) { // zoom in
	                    oldDist = newDist;
	                    if(PaliCanvas.zoom < 1000)
	                    	PaliCanvas.zoom *= 1.1;
	                } else if(oldDist - newDist > 20) { // zoom out
	                    oldDist = newDist;
	                    if(PaliCanvas.zoom > 1)
	                    	PaliCanvas.zoom /= 1.1;
	                }
				 this.invalidate();
				 canvas.DrawScreen();
			 }
			 else
			 {
				 moveX = e.getX()/PaliCanvas.zoom;
				 moveY = e.getY()/PaliCanvas.zoom;
				 
				 switch(PaliCanvas.selectedTool)
				 {
				 case PaliCanvas.TOOL_PICKOBJECT:
					 stopTimeout();
					 break;
				 case PaliCanvas.TOOL_PENCIL:
					 minX=min(minX,moveX);
	                 minY=min(minY,moveY);
	                 maxX=max(maxX,moveX);
	                 maxY=max(maxY,moveY);
					 movement = movement + " " + moveX + " " + moveY;
					 pencilPath.lineTo(moveX, moveY);
					 ((PaliFreeDraw)tempObj).getPath().lineTo(moveX,moveY);
					 break;
				 case PaliCanvas.TOOL_BRUSH:
					 minX=min(minX,moveX);
	                 minY=min(minY,moveY);
	                 maxX=max(maxX,moveX);
	                 maxY=max(maxY,moveY);
					 movement = movement + " " + moveX + " " + moveY;
					 brushPath.lineTo(moveX, moveY);
					 brushPath.addCircle(moveX, moveY, 30, Direction.CW);
					 ((PaliBrush)tempObj).getPath().lineTo(moveX,moveY);				
					 break;
				 case PaliCanvas.TOOL_CIRCLE:
					 tempObj = new PaliCircle(downX, downY, (float)Math.sqrt((float)Math.pow(moveX-downX, 2) + (float)Math.pow(moveY-downY, 2)));
					 break;
				 case PaliCanvas.TOOL_ELLIPSE:
					 tempObj = new PaliEllipse(downX, downY, moveX, moveY);
					 break;
				 case PaliCanvas.TOOL_RECTANGLE:
					 tempObj = new PaliRectangle(downX, downY, moveX, moveY);
					 
				 }
			 }
			
			 //this.DrawScreen();
			 this.invalidate();
			 return true;
		 
		 case MotionEvent.ACTION_UP:
			 fillColor = Integer.toHexString(PaliCanvas.fillColor).substring(2);
			 strokeColor = Integer.toHexString(PaliCanvas.strokeColor).substring(2);
			 strokeWidth = PaliCanvas.strokeWidth;
			 
			 upX = e.getX()/PaliCanvas.zoom;
			 upY = e.getY()/PaliCanvas.zoom;
			 
			 switch(PaliCanvas.selectedTool) {
			 case PaliCanvas.TOOL_PICKOBJECT:
				 float left=999999, right=0, top=999999, bottom=0;
				 PaliObject temp;
				 
				 stopTimeout();
				 this.selected=false;
				 PaliCanvas.selObjArr.clear();
				 this.tempObj=null;
				 
				 if(upX==downX && upY==downY)
				 {
					 for(int i = SVGParser.Layers.size()-1; i>=0;i--)
					 {
						 if(SVGParser.Layers.get(i).visibility==100)
						 {
							 for(int j=SVGParser.Layers.get(i).objs.size()-1;j>=0;j--)
							 {
								 temp = SVGParser.Layers.get(i).objs.get(j);
								 if(temp.rect.contains(upX,upY))
								 {
									 tempObj=new PaliRectangle(temp.rect);
									 PaliCanvas.selObjArr.add(new PaliPoint(i,j));
									 this.selected = true;
									 break;
								 }
							 }
						 }
					 }

					 this.invalidate();
				 }
				 else
				 {
					 this.rect = new RectF(min(downX,upX), min(downY,upY), max(downX,upX), max(downY,upY));

					 for(int i = SVGParser.Layers.size()-1; i>=0;i--)
					 {
						 if(SVGParser.Layers.get(i).visibility==100)
						 {
							 for(int j=SVGParser.Layers.get(i).objs.size()-1;j>=0;j--)
							 {
								 temp = SVGParser.Layers.get(i).objs.get(j);
								 if(this.rect.contains(temp.rect))
								 {
									 PaliCanvas.selObjArr.add(new PaliPoint(i,j));
									 left = min(left, temp.rect.left);
									 top = min(top, temp.rect.top);
									 right = max(right, temp.rect.right);
									 bottom = max(bottom, temp.rect.bottom);
									 this.selected = true;
								 }
							 }
						 }
					 }
					 
					 if(selected)
					 {
						 tempObj = new PaliRectangle(left, top, right, bottom);
						 this.invalidate();
					 }
					 else
					 {
						 PaliCanvas.selObjArr.clear();
						 this.tempObj=null;
						 this.invalidate();
					 }
				 }
				 
				 return true;
			 case PaliCanvas.TOOL_PENCIL:
                 minX=min(minX,upX);
                 minY=min(minY,upY);
                 maxX=max(maxX,upX);
                 maxY=max(maxY,upY);
                 rect = new RectF(minX, minY, maxX, maxY);
                 
                 html = "<path fill=\"none\" stroke=\"#"+strokeColor+"\" stroke-width=\""+strokeWidth+"\" d=\"M"+downX+" "+downY+""+movement+"\" />";
                 SVGParser.Layers.get(canvas.currentLayer).objs.add(new PaliFreeDraw(html, pencilPath, rect));
                 tempObj=null;
                 PaliCanvas.currentObject++;
                 PaliCanvas.drawMode = false;
                 canvas.DrawScreen();
                 break;
			 case PaliCanvas.TOOL_BRUSH:
				 minX=min(minX,upX);
                 minY=min(minY,upY);
                 maxX=max(maxX,upX);
                 maxY=max(maxY,upY);
                 rect = new RectF(minX, minY, maxX, maxY);
                 
                 html = "<path marker-mid=\"url(#marker_circle)\" fill=\"none\" stroke=\"#"+strokeColor+"\" stroke-width=\""+strokeWidth+"\" d=\"M"+downX+" "+downY+""+movement+"\" />";
                 SVGParser.Layers.get(canvas.currentLayer).objs.add(new PaliBrush(html, brushPath, rect));
                 tempObj=null;
                 PaliCanvas.currentObject++;
                 PaliCanvas.drawMode = false;
                 canvas.DrawScreen();
				 break;
			 case PaliCanvas.TOOL_CIRCLE:
				 r = (float)Math.sqrt((float)Math.pow(upX-downX, 2) + (float)Math.pow(upY-downY, 2));
				 cx = downX;
				 cy = downY;
				 
				 html = "<circle cx=\""+cx+"\" cy=\""+cy+"\" r=\""+r+"\" stroke=\"#"+strokeColor+"\" stroke-width=\""+strokeWidth+"\" fill=\"#"+fillColor+"\" />";
				 SVGParser.Layers.get(PaliCanvas.currentLayer).objs.add(new PaliCircle(html,cx,cy,r));
				 tempObj=null;
				 PaliCanvas.currentObject++;
				 PaliCanvas.drawMode = false;
				 canvas.DrawScreen();
				 break;
			 case PaliCanvas.TOOL_ELLIPSE: 
				 x = Math.min(downX, upX);
				 y = Math.min(downY, upY);
				 width = (float)Math.sqrt((float)Math.pow(upX-downX, 2));
				 height = (float)Math.sqrt((float)Math.pow(upY-downY, 2));
				 
				 cx=x+(width/2);
				 cy=y+(height/2);
				 
				 left = downX;
				 top = downY;
				 right = upX;
				 bottom = upY;
				 
				 html = "<ellipse cx=\""+cx+"\" cy=\""+cy+"\" rx=\""+width+"\" ry=\""+height+"\" stroke=\"#"+strokeColor+"\" stroke-width=\""+strokeWidth+"\" fill=\"#"+fillColor+"\" />";
				 SVGParser.Layers.get(canvas.currentLayer).objs.add(new PaliEllipse(html,left,top,right,bottom));	
				 tempObj=null;
				 PaliCanvas.currentObject++;
				 PaliCanvas.drawMode = false;
				 canvas.DrawScreen();
				 
				
				 
				 break;				 
			 case PaliCanvas.TOOL_RECTANGLE:
				 x = Math.min(downX, upX);
				 y = Math.min(downY, upY);
				 width = (float)Math.sqrt((float)Math.pow(upX-downX, 2));
				 height = (float)Math.sqrt((float)Math.pow(upY-downY, 2));
				 
				 left = downX;
				 top = downY;
				 right = upX;
				 bottom = upY;
				 
				 html = "<rect x=\""+x+"\" y=\""+y+"\" width=\""+width+"\" height=\""+height+"\" stroke=\"#"+strokeColor+"\" stroke-width=\""+strokeWidth+"\" fill=\"#"+fillColor+"\" />";
				 SVGParser.Layers.get(canvas.currentLayer).objs.add(new PaliRectangle(html,left,top,right,bottom));	
				 tempObj=null;
				 PaliCanvas.currentObject++;
				 PaliCanvas.drawMode = false;
				 canvas.DrawScreen();
				 			 
				 break;
			 }
			 this.invalidate();
			 
			 return true;
		 }
		 return false;	 
	 }
	
	float min(float a, float b)
	{
		return (a<b)?a:b;
	}

	float max(float a, float b)
	{
		return (a>b)?a:b;
	}
	public void startTimeout(){
		mLongPressed = false;
		mHandler.postDelayed( mLongPressCheckRunnable, 700 );
	}
	
	public void stopTimeout(){
		if ( !mLongPressed )
			mHandler.removeCallbacks( mLongPressCheckRunnable );
	}
	
	 private float spacing(MotionEvent event) {
	        float x = (event.getX(0) - event.getX(1))/PaliCanvas.zoom;
	        float y = (event.getY(0) - event.getY(1))/PaliCanvas.zoom;
	        return FloatMath.sqrt(x * x + y * y);
	 
	    }
	
	private class LongPressCheckRunnable implements Runnable{
		@Override
		public void run() {
			mLongPressed = true;
			parent.performHapticFeedback( HapticFeedbackConstants.LONG_PRESS );
			((MainActivity) mContext).popUpSubMenu();
		}
	}
}
