package com.yozisunji.palipalette;


import com.samsung.android.example.helloaccessoryprovider.R;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class PaliSelector extends View {
	RectF rect;
	Rect recti;
	int width, height;
	
	public boolean mLongPressed = false;
	private Handler mHandler = new Handler();
	private LongPressCheckRunnable mLongPressCheckRunnable = new LongPressCheckRunnable();
	
	private int mLongPressTimeout;
	
	public ArrayList<PaliPoint> selObjArr;
	
	Drawable moveButton;
	Drawable scaleButton;
	Drawable rotateButton;
	
	public PaliSelector(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

		selObjArr = new ArrayList<PaliPoint>();
		
		moveButton = getResources().getDrawable(R.drawable.select_icon_move);
		scaleButton = getResources().getDrawable(R.drawable.select_icon_scale);
		rotateButton = getResources().getDrawable(R.drawable.select_icon_rotate);
		
		mLongPressTimeout = ViewConfiguration.getLongPressTimeout();
	}
	
	public PaliSelector(Context context, RectF r) {
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
		moveButton.draw(cnvs);
		scaleButton.draw(cnvs);
		rotateButton.draw(cnvs);
		
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(2);
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
		 switch(e.getAction() & MotionEvent.ACTION_MASK)
		 {
		 
		 case MotionEvent.ACTION_DOWN:
			 startTimeout();
			 break;
		 case MotionEvent.ACTION_MOVE:
			 stopTimeout();
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
