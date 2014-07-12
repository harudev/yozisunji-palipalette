package com.yozisunji.palipalette;


import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Selection;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.samsung.android.example.helloaccessoryprovider.R;

public class PaliSelector extends View {
	RectF rect;
	Rect recti;
	int width, height;	
	
	boolean mMovePressed = false;
	boolean mScalePressed = false;
	boolean mRotatePressed = false;
	PaliCanvas canvas;
	public boolean mLongPressed = false;
	private Handler mHandler = new Handler();
	private LongPressCheckRunnable mLongPressCheckRunnable = new LongPressCheckRunnable();
	
	private int mLongPressTimeout;
	
	public ArrayList<PaliPoint> selObjArr;
	
	float downX=0, downY=0, upX=0, upY=0, moveX=0, moveY=0, premoveX=0, premoveY=0;
	
	Drawable moveButton;
	Drawable scaleButton;
	Drawable rotateButton;
	
	public PaliSelector(Context context, RectF r, PaliCanvas c) {
		super(context);
		// TODO Auto-generated constructor stub

		selObjArr = new ArrayList<PaliPoint>();
		
		this.rect = r;
		recti = new Rect((int)r.left, (int)r.top, (int)r.right, (int)r.bottom);
		
		width = recti.right-recti.left;
		height = recti.bottom-recti.top;
		
		moveButton = getResources().getDrawable(R.drawable.select_icon_move);
		scaleButton = getResources().getDrawable(R.drawable.select_icon_scale);
		rotateButton = getResources().getDrawable(R.drawable.select_icon_rotate);
		mLongPressTimeout = ViewConfiguration.getLongPressTimeout();
		
		moveButton.setBounds( 0, 0, 50, 50 );
		rotateButton.setBounds( width-50, 0, width, 50 );
		scaleButton.setBounds( width-50, height-50, width, height );
		
		canvas = c;
	}
	
	public void setRect(RectF r)
	{
		this.rect = r;
		recti = new Rect((int)r.left, (int)r.top, (int)r.right, (int)r.bottom);
		
		width = recti.right-recti.left;
		height = recti.bottom-recti.top;
		
		moveButton = getResources().getDrawable(R.drawable.select_icon_move);
		scaleButton = getResources().getDrawable(R.drawable.select_icon_scale);
		rotateButton = getResources().getDrawable(R.drawable.select_icon_rotate);
		
		moveButton.setBounds( 0, 0, 50, 50 );
		rotateButton.setBounds( width-50, 0, width, 50 );
		scaleButton.setBounds( width-50, height-50, width, height );
	}
	public void onDraw(Canvas cnvs)
	{
		cnvs.scale(PaliCanvas.zoom, PaliCanvas.zoom);
		//cnvs.translate(PaliCanvas.canvasX, PaliCanvas.canvasY);
		moveButton.draw(cnvs);
		scaleButton.draw(cnvs);
		rotateButton.draw(cnvs);
		
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(3);
		paint.setStyle(Style.STROKE);
		cnvs.drawRect(0,0,width,height, paint);
	}
	public RectF getRect()
	{
		return this.rect;
	}
	public boolean onTouchEvent(MotionEvent e)
	{
		 super.onTouchEvent(e);
		 Rect temp;
		 switch(e.getAction() & MotionEvent.ACTION_MASK)
		 {
		 case MotionEvent.ACTION_DOWN:
			 startTimeout();
			 
			 downX = e.getX();
			 downY = e.getY();
			 
			 if(moveButton.getBounds().contains((int)downX, (int)downY))
			 {
				 mMovePressed = true;
			 }
			 if(rotateButton.getBounds().contains((int)downX, (int)downY))
			 {
				 mRotatePressed = true;
			 }
			 if(scaleButton.getBounds().contains((int)downX, (int)downY))
			 {
				 mScalePressed = true;
			 }

			 //SVGParser.Layers.get(selObjArr.get(0).x).objs.get(selObjArr.get(0).y).Move(100, 100);
			 //canvas.DrawScreen();
			 
			 
			 break;
		 case MotionEvent.ACTION_MOVE:
			 moveX = e.getX();
			 moveY = e.getY();			
			 
			 if((Math.abs(moveX-downX)>10) || (Math.abs(moveY-downY)>10))
				 stopTimeout();
			 break;
		 case MotionEvent.ACTION_UP:
			 stopTimeout();
			 
			 upX = e.getX();
			 upY = e.getY();	
			 
			 if(mMovePressed) {
				 for(int i=selObjArr.size()-1 ; i>=0; i--) {
					 SVGParser.Layers.get(selObjArr.get(i).x).objs.get(selObjArr.get(i).y).Move(upX-downX, upY-downY);
				 }
			 }
			 else if(mRotatePressed) {
				 for(int i=selObjArr.size()-1 ; i>=0; i--) {
					 SVGParser.Layers.get(selObjArr.get(i).x).objs.get(selObjArr.get(i).y).Rotate((upX-downX)+(upY-downY));
				 }
			 }
			 else if(mScalePressed) {
				 for(int i=selObjArr.size()-1 ; i>=0; i--) {
					 SVGParser.Layers.get(selObjArr.get(i).x).objs.get(selObjArr.get(i).y).Scale((upX-downX)/2, (upY-downY)/2);
				 }
			 }
			 
			 this.invalidate();
			 canvas.DrawScreen();			 
			 
			 mMovePressed = false;
			 mRotatePressed = false;
			 mScalePressed = false;
			 break;
		 }
		return true;
	}
	
	public void startTimeout(){
		mLongPressed = false;
		mHandler.postDelayed( mLongPressCheckRunnable, 700 );
	}
	
	public void stopTimeout(){
		if ( !mLongPressed )
			mHandler.removeCallbacks( mLongPressCheckRunnable );
	}
	 public void finishLongPressed()
	 {
		 this.mLongPressed=false;
	 }
	 
	private class LongPressCheckRunnable implements Runnable{
		@Override
		public void run() {
			mLongPressed = true;
			performHapticFeedback( HapticFeedbackConstants.LONG_PRESS );
			((MainActivity) PaliTouchCanvas.mContext).popUpSubMenu();
		}
	}
	
}
