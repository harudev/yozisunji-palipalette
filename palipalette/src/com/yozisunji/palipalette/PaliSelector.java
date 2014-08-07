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
import android.widget.LinearLayout;

public class PaliSelector extends View {
	RectF rect;
	Rect recti;
	int width, height;

	public boolean mMovePressed = false;
	public boolean mScalePressed = false;
	public boolean mRotatePressed = false;
	PaliCanvas canvas;
	PaliTouchCanvas touchCanvas;
	public boolean mLongPressed = false;
	private Handler mHandler = new Handler();
	private LongPressCheckRunnable mLongPressCheckRunnable = new LongPressCheckRunnable();
	private int mLongPressTimeout=700;

	public ArrayList<PaliPoint> selObjArr;

	float downX = 0, downY = 0, upX = 0, upY = 0, moveX = 0, moveY = 0,
			premoveX = 0, premoveY = 0;
	float movingX = 0, movingY = 0;
	float rotate = 0;
	float scaleX = 0, scaleY = 0;

	Drawable moveButton;
	Drawable scaleButton;
	Drawable rotateButton;

	int screenWidth = 0, screenHeight = 0;

	public PaliSelector(Context context, RectF r, PaliCanvas c,
			PaliTouchCanvas tc) {
		super(context);
		// TODO Auto-generated constructor stub

		selObjArr = new ArrayList<PaliPoint>();

		this.rect = r;
		recti = new Rect((int) r.left, (int) r.top, (int) r.right,
				(int) r.bottom);

		width = recti.right - recti.left;
		height = recti.bottom - recti.top;

		moveButton = getResources().getDrawable(R.drawable.select_icon_move);
		scaleButton = getResources().getDrawable(R.drawable.select_icon_scale);
		rotateButton = getResources()
				.getDrawable(R.drawable.select_icon_rotate);
		mLongPressTimeout = ViewConfiguration.getLongPressTimeout();

		moveButton.setBounds(0, 0, 50, 50);
		rotateButton.setBounds(width - 50, 0, width, 50);
		scaleButton.setBounds(width - 50, height - 50, width, height);

		canvas = c;
		touchCanvas = tc;

		screenWidth = MainActivity.screenWidth;
		screenHeight = MainActivity.screenHeight;
	}

	public void setRect(RectF r) {
		this.rect = r;
		recti = new Rect((int) r.left, (int) r.top, (int) r.right,
				(int) r.bottom);

		width = recti.right - recti.left;
		height = recti.bottom - recti.top;

		moveButton = getResources().getDrawable(R.drawable.select_icon_move);
		scaleButton = getResources().getDrawable(R.drawable.select_icon_scale);
		rotateButton = getResources()
				.getDrawable(R.drawable.select_icon_rotate);

		moveButton.setBounds(0, 0, 50, 50);
		rotateButton.setBounds(width - 50, 0, width, 50);
		scaleButton.setBounds(width - 50, height - 50, width, height);
	}

	public void onDraw(Canvas cnvs) {
		cnvs.translate(movingX, movingY);
		cnvs.scale((1 + (scaleX / cnvs.getWidth())) * PaliCanvas.zoom,
				(1 + (scaleY / cnvs.getHeight())) * PaliCanvas.zoom);
		cnvs.save();
		cnvs.rotate(rotate, width / 2, height / 2);
		moveButton.draw(cnvs);
		scaleButton.draw(cnvs);
		rotateButton.draw(cnvs);
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(3);
		paint.setStyle(Style.STROKE);

		cnvs.drawRect(0, 0, width, height, paint);
		cnvs.restore();
	}

	public RectF getRect() {
		return this.rect;
	}

	public boolean onTouchEvent(MotionEvent e) {
		super.onTouchEvent(e);
		switch (e.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			startTimeout();

			downX = e.getX();
			downY = e.getY();

			if (moveButton.getBounds().contains((int) downX, (int) downY)) {
				mMovePressed = true;
			} else if (rotateButton.getBounds().contains((int) downX,
					(int) downY)) {
				mRotatePressed = true;
			} else if (scaleButton.getBounds().contains((int) downX,
					(int) downY)) {
				mScalePressed = true;
			}

			break;
		case MotionEvent.ACTION_MOVE:
			moveX = e.getX();
			moveY = e.getY();

			if ((Math.abs(moveX - downX) > 10)
					|| (Math.abs(moveY - downY) > 10))
				stopTimeout();

			if (mMovePressed) {
				movingX = moveX;
				movingY = moveY;

				rect = this.getRect();
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this
						.getLayoutParams();
				params.leftMargin = (int) ((rect.left + PaliCanvas.canvasX - screenWidth) * PaliCanvas.zoom);
				params.topMargin = (int) ((rect.top + PaliCanvas.canvasY - screenHeight) * PaliCanvas.zoom);
				params.width = (int) ((rect.right - rect.left + movingX) * PaliCanvas.zoom);
				params.height = (int) ((rect.bottom - rect.top + movingY) * PaliCanvas.zoom);
				this.setLayoutParams(params);

				this.invalidate();
			} else if (mRotatePressed) {
				rotate = (moveX - downX) + (moveY - downY);

				float w = rect.right - rect.left;
				float h = rect.bottom - rect.top;

				rect = this.getRect();
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this
						.getLayoutParams();
				params.leftMargin = (int) ((rect.left + PaliCanvas.canvasX) * PaliCanvas.zoom);
				params.topMargin = (int) ((rect.top + PaliCanvas.canvasY) * PaliCanvas.zoom);
				params.width = (int) ((rect.right - rect.left + movingX + h) * PaliCanvas.zoom);
				params.height = (int) ((rect.bottom - rect.top + movingY + w) * PaliCanvas.zoom);
				this.setLayoutParams(params);

				this.invalidate();
			} else if (mScalePressed) {
				scaleX = (moveX - downX) / 2;
				scaleY = (moveY - downY) / 2;

				rect = this.getRect();
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this
						.getLayoutParams();
				params.leftMargin = (int) ((rect.left + PaliCanvas.canvasX) * PaliCanvas.zoom);
				params.topMargin = (int) ((rect.top + PaliCanvas.canvasY) * PaliCanvas.zoom);
				params.width = (int) ((rect.right - rect.left + movingX + scaleX) * PaliCanvas.zoom);
				params.height = (int) ((rect.bottom - rect.top + movingY + scaleY) * PaliCanvas.zoom);
				this.setLayoutParams(params);

				this.invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
			stopTimeout();

			upX = e.getX();
			upY = e.getY();

			if (mMovePressed) {
				for (int i = selObjArr.size() - 1; i >= 0; i--) {
					SVGParser.Layers.get(selObjArr.get(i).x).objs.get(
							selObjArr.get(i).y).Move(upX - downX - screenWidth,
							upY - downY - screenHeight);
				}
				touchCanvas.selectedClear();
			} else if (mRotatePressed) {
				for (int i = selObjArr.size() - 1; i >= 0; i--) {
					float theta = (upX - downX) + (upY - downY);
					SVGParser.Layers.get(selObjArr.get(i).x).objs.get(
							selObjArr.get(i).y).Rotate(theta);

				}
				touchCanvas.selectedClear();
			} else if (mScalePressed) {
				for (int i = selObjArr.size() - 1; i >= 0; i--) {
					SVGParser.Layers.get(selObjArr.get(i).x).objs.get(
							selObjArr.get(i).y).Scale((upX - downX) / 2,
							(upY - downY) / 2);
				}
				touchCanvas.selectedClear();
			}

			canvas.DrawScreen();
			this.invalidate();

			mMovePressed = false;
			mRotatePressed = false;
			mScalePressed = false;

			movingX = 0;
			movingY = 0;
			rotate = 0;
			scaleX = 0;
			scaleY = 0;
			break;
		}
		return true;
	}

	public void startTimeout() {
		mLongPressed = false;
		mHandler.postDelayed(mLongPressCheckRunnable, mLongPressTimeout);
	}

	public void stopTimeout() {
		if (!mLongPressed)
			mHandler.removeCallbacks(mLongPressCheckRunnable);
	}

	public void finishLongPressed() {
		this.mLongPressed = false;
	}

	private class LongPressCheckRunnable implements Runnable {
		@Override
		public void run() {
			mLongPressed = true;
			performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
			((MainActivity) PaliTouchCanvas.mContext).popUpSubMenu();
		}
	}

}
