
package com.yozisunji.palipalette;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PaliTouchCanvas extends View {
	Path path = new Path();
	float downX=0, downY=0, upX=0, upY=0, moveX=0, moveY=0, premoveX=0, premoveY=0;
	float minX=0, minY=0, maxX=0, maxY=0;
	String 			movement="";
	String 			html="";
	String fillColor;
	String strokeColor;
	
	
	PaliObject tempObj;
	Paint p;
	
	PaliCanvas canvas;
	
	RectF rect;
	
	boolean selected=false;
	long time;
	
	private Context mContext;
	

	public PaliTouchCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context; 
		// TODO Auto-generated constructor stub
		tempObj = null;
		p = new Paint();
	}
	
	public void setCanvasAddr(PaliCanvas c)
	{
		canvas = c;
	}
	
	public void onDraw(Canvas cnvs)
	{
		if(tempObj!=null)
		{
			tempObj.setStrokeColor(Color.BLUE);
			tempObj.setStyle(Style.STROKE);
			tempObj.setWidth(2);
			if(canvas.selectedTool!=3)
			{
				tempObj.setFillColor(Color.GREEN);
				tempObj.setStyle(Style.FILL);
			}
			tempObj.drawObject(cnvs);
			
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
			 time = e.getDownTime();
			 			 
			 switch(PaliCanvas.selectedTool)
			 {
			 case PaliCanvas.TOOL_PENCIL:
				 minX=downX; minY=downY;
                 maxX=downX; maxY=downY;
				 path.moveTo(downX, downY);
				 tempObj = new PaliFreeDraw();
				 ((PaliFreeDraw)tempObj).getPath().moveTo(downX, downY);
				 break;
			 }
			 return true;
		 case MotionEvent.ACTION_MOVE:
			 moveX = e.getX();
			 moveY = e.getY();
			 
			 switch(PaliCanvas.selectedTool)
			 {
			 case PaliCanvas.TOOL_PENCIL:
				 minX=min(minX,moveX);
                 minY=min(minY,moveY);
                 maxX=max(maxX,moveX);
                 maxY=max(maxY,moveY);
				 movement = movement + " " + moveX + " " + moveY;
				 path.lineTo(moveX, moveY);
				 ((PaliFreeDraw)tempObj).getPath().lineTo(moveX,moveY);
				 break;
			 case PaliCanvas.TOOL_CIRCLE:
				 tempObj = new PaliCircle(downX, downY, (float)Math.sqrt((float)Math.pow(moveX-downX, 2) + (float)Math.pow(moveY-downY, 2)));
				 break;
			 case PaliCanvas.TOOL_RECTANGLE:
				 tempObj = new PaliRectangle(downX, downY, moveX, moveY);
				 
			 }
			 //this.DrawScreen();
			 this.invalidate();
			 return true;
		 
		 case MotionEvent.ACTION_UP:
			 
			 fillColor = Integer.toHexString(PaliCanvas.fillColor).substring(2);
			 strokeColor = Integer.toHexString(PaliCanvas.strokeColor).substring(2);
			 
			 upX = e.getX();
			 upY = e.getY();
			 time = e.getEventTime() - time;
			 
			 switch(PaliCanvas.selectedTool) {
			 case PaliCanvas.TOOL_PICKOBJECT:
				 float left=999999, right=0, top=999999, bottom=0;
				 PaliObject temp;
				 if(time>1500 && this.selected)
				 {
					 ((MainActivity) mContext).popUpSubMenu();
					 /*
					 for(int i=0;i<PaliCanvas.selObjArr.size();i++)
					 {
						 SVGParser.Layers.get(PaliCanvas.selObjArr.get(i).x).objs.remove(PaliCanvas.selObjArr.get(i).y);
					 }
					 PaliCanvas.selObjArr.clear();
					 this.tempObj = null;
					 this.selected = false;
					 this.invalidate();
					 canvas.DrawScreen();
					 */
				 }
				 else
				 {
					 this.selected=false;
					 
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
						 if(this.selected)
						 {
							 this.invalidate();
						 }
						 else
						 {
							 PaliCanvas.selObjArr.clear();
							 this.tempObj=null;
							 this.invalidate();
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
				 }
				 return true;
			 case PaliCanvas.TOOL_PENCIL: // FreeDraw
                 minX=min(minX,upX);
                 minY=min(minY,upY);
                 maxX=max(maxX,upX);
                 maxY=max(maxY,upY);
                 rect = new RectF(minX, minY, maxX, maxY);
                 
                 html = "<path fill=\"none\" stroke=\"#"+strokeColor+"\" d=\"M"+downX+" "+downY+""+movement+"\" />";
                 SVGParser.Layers.get(canvas.currentLayer).objs.add(new PaliFreeDraw(html, path, rect));
                 tempObj=null;
                 PaliCanvas.currentObject++;
                 PaliCanvas.drawMode = false;
                 canvas.DrawScreen();
                 break;
			 case PaliCanvas.TOOL_CIRCLE: // Circle
				 float r = (float)Math.sqrt((float)Math.pow(upX-downX, 2) + (float)Math.pow(upY-downY, 2));
				 float cx = downX;
				 float cy = downY;
				 
				 html = "<circle cx=\""+cx+"\" cy=\""+cy+"\" r=\""+r+"\" stroke=\"#"+strokeColor+"\" stroke-width=\""+4+"\" fill=\"#"+fillColor+"\" />";
				 SVGParser.Layers.get(PaliCanvas.currentLayer).objs.add(new PaliCircle(html,cx,cy,r));
				 tempObj=null;
				 PaliCanvas.currentObject++;
				 PaliCanvas.drawMode = false;
				 canvas.DrawScreen();
				 break;
			 case PaliCanvas.TOOL_RECTANGLE: // Rectangle
				 float x = Math.min(downX, upX);
				 float y = Math.min(downY, upY);
				 float width = (float)Math.sqrt((float)Math.pow(upX-downX, 2));
				 float height = (float)Math.sqrt((float)Math.pow(upY-downY, 2));
				 
				 left = downX;
				 top = downY;
				 right = upX;
				 bottom = upY;
				 
				 html = "<rect x=\""+x+"\" y=\""+y+"\" width=\""+width+"\" height=\""+height+"\" stroke=\"#"+strokeColor+"\" stroke-width=\""+4+"\" fill=\"#"+fillColor+"\" />";
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
}
