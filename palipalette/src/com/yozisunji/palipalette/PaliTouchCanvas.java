
package com.yozisunji.palipalette;

import java.util.ArrayList;
import java.util.List;

import com.samsung.android.example.helloaccessoryprovider.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class PaliTouchCanvas extends View {
	Path pencilPath = new Path();
	Path brushPath = new Path();
	float downX=0, downY=0, upX=0, upY=0, moveX=0, moveY=0, premoveX=0, premoveY=0;
	float minX=0, minY=0, maxX=0, maxY=0;
	float x=0, y=0, width=0, height=0;
	float cx=0, cy=0, r=0;
	float pressure, opacity;
	List<Float> brushX = new ArrayList<Float>();
    List<Float> brushY = new ArrayList<Float>();
    List<Float> brushP = new ArrayList<Float>();
    float brushR=0;
	String 			movement="";
	String 			html="";
	String fillColor;
	String strokeColor;
	int strokeWidth;	
	
	PaliObject tempObj;

	PaliCanvas canvas;	
	RectF rect;
	Bitmap bm;
	Canvas c;
	Paint p;
	
	PaliSelector selector=null;
	public boolean selected=false;
	
	private boolean zoom = false;
	float oldDist = 1f, newDist = 1f;
	public static PaliTouchCanvas parent;
	
	
	private int mScaledTouchSlope;
	long time;
	
	public static Context mContext;
	

	public PaliTouchCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context; 
		// TODO Auto-generated constructor stub
		tempObj = null;
		
		mScaledTouchSlope = ViewConfiguration.get( context ).getScaledTouchSlop();
		//parent = this;
		
		/*
		this.setOnHoverListener(new OnHoverListener() {			
			@Override
			public boolean onHover(View v, MotionEvent event) {
				Log.i("debug","hover"); // 호버기능.
				return false;
			}
		});
		*/
		
	}
	
	public void setCanvasAddr(PaliCanvas c, LinearLayout selectorll)
	{
		canvas = c;
		
		selector = new PaliSelector(mContext, new RectF(0, 0, 100, 100));
		selectorll.addView(selector);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)selector.getLayoutParams();
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;
        selector.setLayoutParams(params);
		selector.setVisibility(View.GONE); 
	}
	
	public void onDraw(Canvas cnvs)
	{
		cnvs.scale(PaliCanvas.zoom, PaliCanvas.zoom);
		if(tempObj!=null)
		{
			tempObj.setStrokeColor(Color.GREEN);
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
	
	public interface SPenHoverListener {
		boolean onHoverEvent(View v, MotionEvent e);
	}
	
	/*
	public boolean onHoverEvent(View v, MotionEvent e) {
		Log.i("debug","hover");
		return true;
		
	}*/
	
	public boolean onTouchEvent(MotionEvent e)
	 {
		
		 super.onTouchEvent(e);		 
		 switch(e.getAction() & MotionEvent.ACTION_MASK)
		 {
		 
		 case MotionEvent.ACTION_DOWN:	
			 fillColor = Integer.toHexString(PaliCanvas.fillColor).substring(2);
			 strokeColor = Integer.toHexString(PaliCanvas.strokeColor).substring(2);
			 strokeWidth = PaliCanvas.strokeWidth;
			 			 			 
			 downX = e.getX()/PaliCanvas.zoom;
			 downY = e.getY()/PaliCanvas.zoom;
			
			 switch(PaliCanvas.selectedTool)
			 {
			 case PaliCanvas.TOOL_PENCIL:
				 pencilPath = new Path();
				 minX=downX; minY=downY;
                 maxX=downX; maxY=downY;
                 pencilPath.moveTo(downX, downY);
				 tempObj = new PaliFreeDraw();
				 ((PaliFreeDraw)tempObj).getPath().moveTo(downX, downY);
				 break;
			 case PaliCanvas.TOOL_BRUSH:
				 html = "<g>";
				 				 
				 minX=downX; minY=downY;
                 maxX=downX; maxY=downY;
				 
				 brushX.add(downX);
                 brushY.add(downY);
                 pressure = e.getPressure();                   
                 brushP.add(pressure);
                 brushR = 30;
                 
                 tempObj = new PaliFreeDraw();
				 ((PaliFreeDraw)tempObj).getPath().moveTo(downX, downY);          
				 break;
				 
				 /*
				 brushPath = new Path();
				 minX=downX; minY=downY;
                 maxX=downX; maxY=downY;
                 brushPath.moveTo(downX, downY);
                 brushPath.addCircle(downX, downY, 30, Direction.CW);
				 tempObj = new PaliBrush();
				 ((PaliBrush)tempObj).getPath().moveTo(downX, downY);
				 break;
				 */
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
								 
					 brushX.add(moveX);
                     brushY.add(moveY);
                     pressure = e.getPressure();                   
                     brushP.add(pressure);
                     
                     html += "<circle cx=\""+moveX+"\" cy=\""+moveY+"\" r=\""+brushR+"\" fill=\"#"+fillColor+"\" fill-opacity=\""+pressure+"\" />";
                     ((PaliFreeDraw)tempObj).getPath().lineTo(moveX,moveY);
                     break;
					 /*
					 minX=min(minX,moveX);
	                 minY=min(minY,moveY);
	                 maxX=max(maxX,moveX);
	                 maxY=max(maxY,moveY);
					 movement = movement + " " + moveX + " " + moveY;
					 brushPath.lineTo(moveX, moveY);
					 brushPath.addCircle(moveX, moveY, 30, Direction.CW);
					 ((PaliBrush)tempObj).getPath().lineTo(moveX,moveY);				
					 break;
					 */
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
			 upX = e.getX()/PaliCanvas.zoom;
			 upY = e.getY()/PaliCanvas.zoom;
			 float left=999999, right=0, top=999999, bottom=0;
			 PaliObject temp;
			 
			 switch(PaliCanvas.selectedTool) {
			 case PaliCanvas.TOOL_PICKOBJECT:
				 if(!this.selected)
				 {
					 this.selected=false;
					 selector.selObjArr.clear();
					 
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
										 selector.selObjArr.add(new PaliPoint(i,j));
										 rect = temp.rect;
										 
										 this.selected = true;
										 break;
									 }
								 }
							 }
						 }
						 
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
										 selector.selObjArr.add(new PaliPoint(i,j));
										 left = min(left, temp.rect.left);
										 top = min(top, temp.rect.top);
										 right = max(right, temp.rect.right);
										 bottom = max(bottom, temp.rect.bottom);
										 this.selected = true;
									 }
								 }
							 }
						 }
					 }
					 
					 if(selected)
					 {
						 selector.setRect(rect);
						 LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)selector.getLayoutParams();
						 params.leftMargin = (int)rect.left;
						 params.topMargin = (int)rect.top;
				         params.width = (int)(rect.right-rect.left);
				         params.height = (int)(rect.bottom-rect.top);
				         selector.setLayoutParams(params);
						 selector.setVisibility(View.VISIBLE);
				         
					 }
					 return true;
				 }
				 else
				 {
					 if(!selector.getRect().contains(upX,upY))
					 {
						 this.selected=false;
						 selector.selObjArr.clear();
					 }
				 }
				 if(!selected)
				 {
					 selector.setVisibility(android.view.View.GONE);
				 }
				 return true;
			 case PaliCanvas.TOOL_PENCIL:
                 minX=min(minX,upX);
                 minY=min(minY,upY);
                 maxX=max(maxX,upX);
                 maxY=max(maxY,upY);
                 rect = new RectF(minX, minY, maxX, maxY);
                 opacity = PaliCanvas.alpha / 255.0f;
                 html = "<path fill=\"none\" stroke=\"#"+strokeColor+"\" stroke-width=\""+strokeWidth+"\" stroke-opacity=\""+opacity+"\" d=\"M"+downX+" "+downY+""+movement+"\" />";
                 SVGParser.Layers.get(canvas.currentLayer).objs.add(new PaliFreeDraw(html, pencilPath, rect));
                 tempObj=null;
                 pencilPath = null;
                 PaliCanvas.currentObject++;
                 PaliCanvas.drawMode = false;
                 canvas.DrawScreen();                 
                 break;
			 case PaliCanvas.TOOL_BRUSH:				 
				 html += "</g>";

				 minX=min(minX,upX);
                 minY=min(minY,upY);
                 maxX=max(maxX,upX);
                 maxY=max(maxY,upY);
                 rect = new RectF(minX-brushR, minY-brushR, maxX+brushR, maxY+brushR);
                 
                 bm = Bitmap.createBitmap((int)(rect.right), (int)(rect.bottom), Bitmap.Config.ARGB_8888);
				 c = new Canvas(bm);
				 p = new Paint();
				 p.setColor(PaliCanvas.fillColor);
				 p.setStyle(Paint.Style.FILL);
                 
                 for(int i=0; i<brushX.size(); i++) {               	 
                	 p.setAlpha((int)(PaliCanvas.alpha * brushP.get(i)));
                	 c.drawCircle(brushX.get(i)-rect.left,brushY.get(i)-rect.top,brushR,p);
                 }
                 
	             SVGParser.Layers.get(PaliCanvas.currentLayer).objs.add(new PaliBrush(html, bm, rect));
	             
	             tempObj=null;
	             bm=null;
	             c=null;
	             PaliCanvas.currentObject++;
	             PaliCanvas.drawMode = false;
	             canvas.DrawScreen();
	             
	             brushX.clear();
	             brushY.clear();
	             brushP.clear();
	             
	             //Log.i("debug", ""+html);
	             break;
	             
				 /*
				 minX=min(minX,upX);
                 minY=min(minY,upY);
                 maxX=max(maxX,upX);
                 maxY=max(maxY,upY);
                 rect = new RectF(minX, minY, maxX, maxY);
                 
                 html = "<path marker-mid=\"url(#marker_circle)\" fill=\"none\" stroke=\"#"+strokeColor+"\" stroke-width=\""+strokeWidth+"\" d=\"M"+downX+" "+downY+""+movement+"\" />";
                 SVGParser.Layers.get(canvas.currentLayer).objs.add(new PaliBrush(html, brushPath, rect));
                 tempObj=null;
                 brushPath = null;
                 PaliCanvas.currentObject++;
                 PaliCanvas.drawMode = false;
                 canvas.DrawScreen();
				 break;
				 */
			 case PaliCanvas.TOOL_CIRCLE:
				 r = (float)Math.sqrt((float)Math.pow(upX-downX, 2) + (float)Math.pow(upY-downY, 2));
				 cx = downX;
				 cy = downY;
				 opacity = PaliCanvas.alpha / 255.0f;
				 
				 html = "<circle cx=\""+cx+"\" cy=\""+cy+"\" r=\""+r+"\" stroke=\"#"+strokeColor+"\" stroke-width=\""+strokeWidth+"\" fill=\"#"+fillColor+"\" stroke-opacity=\""+opacity+"\" fill-opacity=\""+opacity+"\" />";
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
				 opacity = PaliCanvas.alpha / 255.0f;
				 
				 left = downX;
				 top = downY;
				 right = upX;
				 bottom = upY;				 
				 
				 html = "<ellipse cx=\""+cx+"\" cy=\""+cy+"\" rx=\""+width+"\" ry=\""+height+"\" stroke=\"#"+strokeColor+"\" stroke-width=\""+strokeWidth+"\" fill=\"#"+fillColor+"\" stroke-opacity=\""+opacity+"\" fill-opacity=\""+opacity+"\" />";
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
				 opacity = PaliCanvas.alpha / 255.0f;
				 
				 left = downX;
				 top = downY;
				 right = upX;
				 bottom = upY;
				 
				 html = "<rect x=\""+x+"\" y=\""+y+"\" width=\""+width+"\" height=\""+height+"\" stroke=\"#"+strokeColor+"\" stroke-width=\""+strokeWidth+"\" fill=\"#"+fillColor+"\" stroke-opacity=\""+opacity+"\" fill-opacity=\""+opacity+"\" />";
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
	
	
	 private float spacing(MotionEvent event) {
	        float x = (event.getX(0) - event.getX(1));
	        float y = (event.getY(0) - event.getY(1));
	        return FloatMath.sqrt(x * x + y * y);
	 
	    }
	 public void deleteObject()
	 {
		 for(int i=0;i<selector.selObjArr.size();i++)
		 {
			 SVGParser.Layers.get(selector.selObjArr.get(i).x).objs.remove(selector.selObjArr.get(i).y);
		 }
		 
		 selector.selObjArr.clear();
		 this.tempObj = null;
		 this.selected = false;
		 this.invalidate();
		 canvas.DrawScreen();
		 selector.mLongPressed = false;
		 selector.setVisibility(View.GONE);
	 }	
	 
}
