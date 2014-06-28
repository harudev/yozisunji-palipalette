package com.yozisunji.palipalette;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PaliTouchCanvas extends View{
	Path path = new Path();
	float downX=0, downY=0, upX=0, upY=0, moveX=0, moveY=0, premoveX=0, premoveY=0;
	String 			movement="";
	String 			html="";
	String fillColor;
	String strokeColor;
	
	
	PaliObject tempObj;
	Paint p;
	
	PaliCanvas canvas;

	public PaliTouchCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
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
			p.setColor(Color.BLUE);
			p.setStyle(Style.STROKE);
			p.setColor(Color.GREEN);
			p.setStyle(Style.FILL);
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
			 			 
			 switch(PaliCanvas.selectedTool)
			 {
			 case PaliCanvas.TOOL_PENCIL:
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
			 switch(PaliCanvas.selectedTool) {
			 case PaliCanvas.TOOL_PENCIL: // FreeDraw
				 html = "<path fill=\"none\" stroke=\"#"+strokeColor+"\" d=\"M"+downX+" "+downY+""+movement+"\" />";
				 SVGParser.Layers.get(canvas.currentLayer).objs.add(new PaliFreeDraw(html, path));
				 break;
			 case PaliCanvas.TOOL_CIRCLE: // Circle
				 float r = (float)Math.sqrt((float)Math.pow(upX-downX, 2) + (float)Math.pow(upY-downY, 2));
				 float cx = downX;
				 float cy = downY;
				 
				 html = "<circle cx=\""+cx+"\" cy=\""+cy+"\" r=\""+r+"\" stroke=\"#"+strokeColor+"\" stroke-width=\""+4+"\" fill=\"#"+fillColor+"\" />";
				 SVGParser.Layers.get(PaliCanvas.currentLayer).objs.add(new PaliCircle(html,cx,cy,r));
				 break;
			 case PaliCanvas.TOOL_RECTANGLE: // Rectangle
				 float x = Math.min(downX, upX);
				 float y = Math.min(downY, upY);
				 float width = (float)Math.sqrt((float)Math.pow(upX-downX, 2));
				 float height = (float)Math.sqrt((float)Math.pow(upY-downY, 2));
				 
				 float left = downX;
				 float top = downY;
				 float right = upX;
				 float bottom = upY;
				 
				 html = "<rect x=\""+x+"\" y=\""+y+"\" width=\""+width+"\" height=\""+height+"\" stroke=\"#"+strokeColor+"\" stroke-width=\""+4+"\" fill=\"#"+fillColor+"\" />";
				 SVGParser.Layers.get(canvas.currentLayer).objs.add(new PaliRectangle(html,left,top,right,bottom));				 
				 break;
			 }
			 Log.i("debug",""+html);
			 tempObj=null;
			 PaliCanvas.currentObject++;
			 PaliCanvas.drawMode = false;
			 //this.DrawScreen();
			 this.invalidate();
			 canvas.DrawScreen();
			 return true;
		 }
		 return false;
	 
	 }

}
