package com.yozisunji.palipalette;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.provider.SyncStateContract.Constants;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class PaliCanvas extends View {
	private Canvas canvas;
	public int strokeColor;
	public int fillColor;
	private Picture picture;
	private Picture prepicture;
	int width, height;
	PictureDrawable pd;
	public static int selectedTool=1;
	String movement="";
	String html="";
	
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
	  
	 public void drawLayer(int layernum)
	 {
		 prepicture = new Picture(picture);
		 canvas = picture.beginRecording(width, height);
		 canvas.drawPicture(prepicture);
		 for(int i=0; i<SVGParser.objs.size();i++)
		 {
			 if(SVGParser.objs.get(i).getValidation()==true)
			 {
				 SVGParser.objs.get(i).setStrokeColor(strokeColor);
				 SVGParser.objs.get(i).setFillColor(fillColor);
				 SVGParser.objs.get(i).drawObject(canvas);
				 SVGParser.objs.get(i).setValidation(false);
			 }
		 }
		 picture.endRecording();
		 invalidate();
	 }
	 protected void onDraw(Canvas cnvs)
	 {
		 cnvs.drawPicture(prepicture);
		 cnvs.drawPicture(picture);
	 }
	 
	 public boolean onTouchEvent(MotionEvent e)
	 {
		 super.onTouchEvent(e);
		 
		 switch(e.getAction())
		 {
		 
		 case MotionEvent.ACTION_DOWN:
			 downX = e.getX();
			 downY = e.getY();
			 return true;
		 case MotionEvent.ACTION_MOVE:
			 moveX = e.getX();
			 moveY = e.getY();
			 movement = movement + " " + moveX + " " + moveY;
			 return true;
		 case MotionEvent.ACTION_UP:
			 upX = e.getX();
			 upY = e.getY();
			 
			 Log.i("debug",""+selectedTool);
			 
			 switch(selectedTool) {
			 case 0: // FreeDraw
				 html = "<path fill =\"none\" stroke=\"black\"/>";
				 break;
			 case 1: // Circle
				 float r = (float)Math.sqrt((float)Math.pow(upX-downX, 2) + (float)Math.pow(upY-downY, 2));
				 float cx = downX;
				 float cy = downY;
				 
				 html = "<circle cs="+ cx +" cy=" + cy + " r=" + r + " style=\"fill:yellow;stroke:purple;stroke-width:2\"/> ";
				 SVGParser.objs.add(new PaliCircle(html,cx,cy,r, true));
				 this.drawLayer(0);
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
				 SVGParser.objs.add(new PailRectangle(html,left,top,right,bottom,true));
				 this.drawLayer(0);
				 break;
			 }
			 return true;
		 }
		 return false;
	 
	 }
}
