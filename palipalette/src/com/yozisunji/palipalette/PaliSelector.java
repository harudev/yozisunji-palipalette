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
import android.view.MotionEvent;
import android.view.View;

public class PaliSelector extends View {
	RectF rect;
	Rect recti;
	int width, height;
	public ArrayList<PaliPoint> selObjArr;
	
	Drawable moveButton;
	Drawable scaleButton;
	Drawable rotateButton;
	
	public PaliSelector(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		moveButton = getResources().getDrawable(R.drawable.select_icon_move);
		scaleButton = getResources().getDrawable(R.drawable.select_icon_scale);
		rotateButton = getResources().getDrawable(R.drawable.select_icon_rotate);
	}
	
	public PaliSelector(Context context, RectF r) {
		super(context);
		// TODO Auto-generated constructor stub
		
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
	public boolean onTouchEvent(MotionEvent e)
	{
		return super.onTouchEvent(e);
	}
}
