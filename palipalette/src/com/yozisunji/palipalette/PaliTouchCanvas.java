package com.yozisunji.palipalette;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.samsung.android.sdk.look.airbutton.SlookAirButton;
import com.samsung.android.sdk.look.airbutton.SlookAirButton.ItemSelectListener;
import com.samsung.android.sdk.look.airbutton.SlookAirButtonAdapter;
import com.samsung.android.sdk.look.smartclip.SlookSmartClip;
import com.samsung.android.sdk.look.smartclip.SlookSmartClip.DataExtractionListener;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipCroppedArea;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipDataElement;

public class PaliTouchCanvas extends View {
	Path penPath = new Path();
	Path pencilPath = new Path();
	Path brushPath = new Path();
	float downX = 0, downY = 0, upX = 0, upY = 0, moveX = 0, moveY = 0,
			premoveX = 0, premoveY = 0;
	float minX = 0, minY = 0, maxX = 0, maxY = 0;
	float x = 0, y = 0, width = 0, height = 0;
	float left = 0, right = 0, top = 0, bottom = 0;
	float cx = 0, cy = 0, r = 0;
	float pressure, opacity;
	PaliPoint Point0, Point1;
	List<Float> brushX = new ArrayList<Float>();
	List<Float> brushY = new ArrayList<Float>();
	List<Float> brushP = new ArrayList<Float>();
	List<Float> movingX = new ArrayList<Float>();
	List<Float> movingY = new ArrayList<Float>();
	float brushR = 0;
	String html = "";
	String fillColor;
	String strokeColor;
	int strokeWidth;

	PaliObject tempObj;
	PaliObject tempObj2;
	public ArrayList<PaliObject> copyObject;
	float copyPosX = 0, copyPosY = 0;
	float pastePosX = 0, pastePosY = 0;
	public static float translateX = 0;
	public static float translateY = 0;

	PaliCanvas canvas;
	RectF rect;
	Bitmap bm;
	Canvas c;
	Paint p;

	PaliSelector selector = null;
	public boolean selected = false;
	boolean penActive = false;

	private boolean pinch = false;
	private boolean zoom = false;
	private boolean prepinch = false;
	float oldDist = 1f, newDist = 1f;
	public static PaliTouchCanvas parent;

	private int mScaledTouchSlope;
	long time;

	public static Context mContext;

	public int style;
	Handler handler = new Handler();

	int directingCnt;

	public PaliTouchCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		// TODO Auto-generated constructor stub
		tempObj = null;

		mScaledTouchSlope = ViewConfiguration.get(context).getScaledTouchSlop();

		Point0 = new PaliPoint();
		Point1 = new PaliPoint();

		copyObject = new ArrayList<PaliObject>();

		SlookAirButtonAdapter adp = new SlookAirButtonAdapter();
		SlookAirButton bt = new SlookAirButton(this, adp,
				SlookAirButton.UI_TYPE_MENU);
		bt.setItemSelectListener(airButtonListener);

		SlookSmartClip ssc = new SlookSmartClip(this);
		ssc.setDataExtractionListener(smartClipListener);
	}

	private ItemSelectListener airButtonListener = new ItemSelectListener() {

		@Override
		public void onItemSelected(View arg0, int arg1, Object arg2) {

		}
	};

	private DataExtractionListener smartClipListener = new DataExtractionListener() {

		@Override
		public int onExtractSmartClipData(View arg0,
				SlookSmartClipDataElement arg1, SlookSmartClipCroppedArea arg2) {

			return 0;
		}
	};

	public void setCanvasAddr(PaliCanvas c, LinearLayout selectorll) {
		canvas = c;

		selector = new PaliSelector(mContext, new RectF(0, 0, 100, 100), c,
				this);
		selectorll.addView(selector);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) selector
				.getLayoutParams();
		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.MATCH_PARENT;
		selector.setLayoutParams(params);
		selector.setVisibility(View.GONE);
	}

	public void onDraw(Canvas cnvs) {
		cnvs.scale(PaliCanvas.zoom, PaliCanvas.zoom);
		cnvs.translate(PaliCanvas.canvasX / PaliCanvas.zoom, PaliCanvas.canvasY
				/ PaliCanvas.zoom);
		if (tempObj != null) {
			tempObj.setStrokeColor(Color.GREEN);
			tempObj.setStyle(Style.STROKE);
			tempObj.setWidth(2);
			if (PaliCanvas.selectedTool == PaliCanvas.TOOL_PICKOBJECT) {
				tempObj.setFillColor(Color.GREEN);
				tempObj.setAlpha(30);
			} else {
				tempObj.setFillColor(Color.GREEN);
				tempObj.setStyle(Style.FILL);
			}
			tempObj.drawObject(cnvs);
		}
		if (tempObj2 != null) {
			tempObj2.setStrokeColor(Color.GREEN);
			tempObj2.setStyle(Style.STROKE);
			tempObj2.setWidth(2);
			if (PaliCanvas.selectedTool == PaliCanvas.TOOL_PICKOBJECT) {
				tempObj2.setFillColor(Color.GREEN);
				tempObj2.setAlpha(30);
			} else {
				tempObj2.setFillColor(Color.GREEN);
				tempObj2.setStyle(Style.FILL);
			}
			tempObj2.drawObject(cnvs);
		}
		super.onDraw(cnvs);
	}

	public interface SPenHoverListener {
		boolean onHoverEvent(View v, MotionEvent e);
	}

	public boolean onTouchEvent(MotionEvent e) {

		super.onTouchEvent(e);
		switch (e.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			directingCnt = 0;

			fillColor = Integer.toHexString(PaliCanvas.fillColor).substring(2);
			strokeColor = Integer.toHexString(PaliCanvas.strokeColor)
					.substring(2);
			strokeWidth = PaliCanvas.strokeWidth;

			downX = (e.getX() - PaliCanvas.canvasX) / PaliCanvas.zoom;
			downY = (e.getY() - PaliCanvas.canvasY) / PaliCanvas.zoom;

			switch (PaliCanvas.selectedTool) {
			case PaliCanvas.TOOL_PENCIL:
				pencilPath = new Path();
				minX = downX;
				minY = downY;
				maxX = downX;
				maxY = downY;
				pencilPath.moveTo(downX, downY);
				movingX.add(downX);
				movingY.add(downY);
				tempObj = new PaliFreeDraw();
				((PaliFreeDraw) tempObj).getPath().moveTo(downX, downY);
				break;
			case PaliCanvas.TOOL_BRUSH:
				html = "<g>";

				minX = downX;
				minY = downY;
				maxX = downX;
				maxY = downY;

				brushX.add(downX);
				brushY.add(downY);
				pressure = e.getPressure();
				brushP.add(pressure);
				brushR = 30;

				tempObj = new PaliFreeDraw();
				((PaliFreeDraw) tempObj).getPath().moveTo(downX, downY);
				break;

			/*
			 * brushPath = new Path(); minX=downX; minY=downY; maxX=downX;
			 * maxY=downY; brushPath.moveTo(downX, downY);
			 * brushPath.addCircle(downX, downY, 30, Direction.CW); tempObj =
			 * new PaliBrush(); ((PaliBrush)tempObj).getPath().moveTo(downX,
			 * downY); break;
			 */
			case PaliCanvas.TOOL_PICKOBJECT:
				selector.startTimeout();
				pastePosX = downX;
				pastePosY = downY;
				break;
			}
			return true;
		case MotionEvent.ACTION_POINTER_DOWN:
			Point0.fx = e.getX(0);
			Point0.fy = e.getY(0);
			Point1.fx = e.getX(1);
			Point1.fy = e.getY(1);

			newDist = spacing(e);
			oldDist = spacing(e);
			this.pinch = true;
			return true;
		case MotionEvent.ACTION_POINTER_UP:

			this.pinch = false;
			// this.zoom = false;
			// this.prepinch = false;

			return true;
		case MotionEvent.ACTION_MOVE:
			if (pinch) {
				selector.stopTimeout();
				if (!prepinch) {
					if (directingCnt > 3) {
						if (directing(e, Point0, Point1))
							zoom = false;
						else
							zoom = true;

						prepinch = true;
					}
					directingCnt++;

				}

				if (!zoom) {
					float dx = e.getX(0) - Point0.fx;
					float dy = e.getY(0) - Point0.fy;

					PaliCanvas.canvasX += dx;
					PaliCanvas.canvasY += dy;

					if (selected) {
						rect = selector.getRect();
						LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) selector
								.getLayoutParams();

						params.leftMargin = (int) ((rect.left + PaliCanvas.canvasX) * PaliCanvas.zoom);
						params.topMargin = (int) ((rect.top + PaliCanvas.canvasY) * PaliCanvas.zoom);

						selector.setLayoutParams(params);
						selector.invalidate();
					}
					canvas.DrawScreen();

					Point0.fx = e.getX(0);
					Point0.fy = e.getY(0);
					Point1.fx = e.getX(1);
					Point1.fy = e.getY(1);
				} else {
					newDist = spacing(e);
					zoom = true;

					if (newDist - oldDist > 20) { // zoom in
						oldDist = newDist;
						if (PaliCanvas.zoom < 300)
							PaliCanvas.zoom *= 1.05;
					} else if (oldDist - newDist > 20) { // zoom out
						oldDist = newDist;
						if (PaliCanvas.zoom > 0.5)
							PaliCanvas.zoom /= 1.05;
					}
					if (selected) {
						rect = selector.getRect();
						LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) selector
								.getLayoutParams();
						params.leftMargin = (int) ((rect.left + PaliCanvas.canvasX) * PaliCanvas.zoom);
						params.topMargin = (int) ((rect.top + PaliCanvas.canvasY) * PaliCanvas.zoom);
						params.width = (int) ((rect.right - rect.left) * PaliCanvas.zoom);
						params.height = (int) ((rect.bottom - rect.top) * PaliCanvas.zoom);
						selector.setLayoutParams(params);
						selector.invalidate();
					}
					canvas.DrawScreen();
				}
			} else if (prepinch == false) {
				moveX = (e.getX() - PaliCanvas.canvasX) / PaliCanvas.zoom;
				moveY = (e.getY() - PaliCanvas.canvasY) / PaliCanvas.zoom;

				if ((Math.abs(moveX - downX) > 30)
						|| (Math.abs(moveY - downY) > 30))
					selector.stopTimeout();

				switch (PaliCanvas.selectedTool) {
				case PaliCanvas.TOOL_PENCIL:
					minX = min(minX, moveX);
					minY = min(minY, moveY);
					maxX = max(maxX, moveX);
					maxY = max(maxY, moveY);
					movingX.add(moveX);
					movingY.add(moveY);
					pencilPath.lineTo(moveX, moveY);
					((PaliFreeDraw) tempObj).getPath().lineTo(moveX, moveY);
					break;
				case PaliCanvas.TOOL_BRUSH:
					minX = min(minX, moveX);
					minY = min(minY, moveY);
					maxX = max(maxX, moveX);
					maxY = max(maxY, moveY);

					brushX.add(moveX);
					brushY.add(moveY);
					pressure = e.getPressure();
					brushP.add(pressure);

					((PaliFreeDraw) tempObj).getPath().lineTo(moveX, moveY);
					break;
				case PaliCanvas.TOOL_CIRCLE:
					tempObj = new PaliCircle(downX, downY,
							(float) Math.sqrt((float) Math
									.pow(moveX - downX, 2)
									+ (float) Math.pow(moveY - downY, 2)));
					break;
				case PaliCanvas.TOOL_ELLIPSE:
					left = Math.min(downX, moveX);
					top = Math.min(downY, moveY);
					right = Math.max(downX, moveX);
					bottom = Math.max(downY, moveY);
					tempObj = new PaliEllipse(left, top, right, bottom);
					break;
				case PaliCanvas.TOOL_RECTANGLE:
				case PaliCanvas.TOOL_PICKOBJECT:
					left = Math.min(downX, moveX);
					top = Math.min(downY, moveY);
					right = Math.max(downX, moveX);
					bottom = Math.max(downY, moveY);
					tempObj = new PaliRectangle(left, top, right, bottom);
					break;
				case PaliCanvas.TOOL_STAR:
					tempObj = new PaliStar(downX, downY,
							(float) Math.sqrt((float) Math
									.pow(moveX - downX, 2)
									+ (float) Math.pow(moveY - downY, 2)));
				}
			}

			this.invalidate();
			return true;

		case MotionEvent.ACTION_UP:
			if (PaliCanvas.selectedTool != PaliCanvas.TOOL_PEN) {
				tempObj = null;
			}
			this.invalidate();

			if (prepinch) {
				this.zoom = false;
				this.prepinch = false;
				return true;
			} else {
				upX = (e.getX() - PaliCanvas.canvasX) / PaliCanvas.zoom;
				upY = (e.getY() - PaliCanvas.canvasY) / PaliCanvas.zoom;
				float rect_left = 999999, rect_right = 0, rect_top = 999999, rect_bottom = 0;
				PaliObject temp;

				switch (PaliCanvas.selectedTool) {
				case PaliCanvas.TOOL_PICKOBJECT:

					selector.stopTimeout();
					if (this.selected) {
						if (selector.getRect().contains(upX, upY)) {
							return true;
						} else {
							this.selected = false;
							selector.selObjArr.clear();
							selector.setVisibility(View.GONE);
						}
					}
					this.selected = false;
					selector.selObjArr.clear();

					if (upX == downX && upY == downY) {
						for (int i = SVGParser.Layers.size() - 1; i >= 0; i--) {
							if (SVGParser.Layers.get(i).visibility == true) {
								for (int j = SVGParser.Layers.get(i).objs
										.size() - 1; j >= 0; j--) {
									temp = SVGParser.Layers.get(i).objs.get(j);
									if (temp.rect.contains(upX, upY)) {
										selector.selObjArr.add(new PaliPoint(i,
												j));
										rect = temp.rotRect;

										this.selected = true;
										break;
									}
								}
							}
						}

					} else {
						this.rect = new RectF(min(downX, upX), min(downY, upY),
								max(downX, upX), max(downY, upY));

						for (int i = SVGParser.Layers.size() - 1; i >= 0; i--) {
							if (SVGParser.Layers.get(i).visibility == true) {
								for (int j = SVGParser.Layers.get(i).objs
										.size() - 1; j >= 0; j--) {
									temp = SVGParser.Layers.get(i).objs.get(j);
									if (this.rect.contains(temp.rect)) {
										selector.selObjArr.add(new PaliPoint(i,
												j));
										rect_left = min(rect_left,
												temp.rotRect.left);
										rect_top = min(rect_top,
												temp.rotRect.top);
										rect_right = max(rect_right,
												temp.rotRect.right);
										rect_bottom = max(rect_bottom,
												temp.rotRect.bottom);
										this.selected = true;
									}
								}
								rect.left = rect_left;
								rect.top = rect_top;
								rect.right = rect_right;
								rect.bottom = rect_bottom;
							}
						}
					}

					if (selected) {
						selector.setRect(rect);
						LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) selector
								.getLayoutParams();
						params.leftMargin = (int) ((rect.left + PaliCanvas.canvasX) * PaliCanvas.zoom);
						params.topMargin = (int) ((rect.top + PaliCanvas.canvasY) * PaliCanvas.zoom);
						params.width = (int) ((rect.right - rect.left) * PaliCanvas.zoom);
						params.height = (int) ((rect.bottom - rect.top) * PaliCanvas.zoom);
						selector.setLayoutParams(params);
						selector.setVisibility(View.VISIBLE);
					}
					if (!selected) {
						selector.setVisibility(android.view.View.GONE);
					}
					return true;
				case PaliCanvas.TOOL_PEN:
					PaliCanvas.history.Do(SVGParser.Layers);
					if (!penActive) {
						minX = upX;
						minY = upY;
						maxX = upX;
						maxY = upY;

						penPath = new Path();
						penPath.moveTo(upX, upY);

						tempObj = new PaliFreeDraw();
						((PaliFreeDraw) tempObj).getPath().moveTo(downX, downY);
						movingX.add(upX);
						movingY.add(upY);

						tempObj2 = new PaliCircle(upX, upY, 15);
						penActive = true;
					} else {

						if ((tempObj2.x - tempObj2.r) <= upX
								&& (tempObj2.x + tempObj2.r) >= upX
								&& (tempObj2.y - tempObj2.r) <= upY
								&& (tempObj2.y + tempObj2.r) >= upY) {
							minX = min(minX, tempObj2.x);
							minY = min(minY, tempObj2.y);
							maxX = max(maxX, tempObj2.x);
							maxY = max(maxY, tempObj2.y);
							rect = new RectF(minX, minY, maxX, maxY);

							penPath.lineTo(tempObj2.x, tempObj2.y);
							((PaliFreeDraw) tempObj).getPath().lineTo(
									tempObj2.x, tempObj2.y);
							movingX.add(tempObj2.x);
							movingY.add(tempObj2.y);

							SVGParser.Layers.get(canvas.currentLayer).objs
									.add(new PaliPen(penPath, movingX, movingY,
											rect));

							PaliCanvas.currentObject++;
							movingX.clear();
							movingY.clear();
							tempObj = null;
							tempObj2 = null;
							penPath = null;
							penActive = false;
						} else {
							minX = min(minX, upX);
							minY = min(minY, upY);
							maxX = max(maxX, upX);
							maxY = max(maxY, upY);

							penPath.lineTo(upX, upY);
							((PaliFreeDraw) tempObj).getPath().lineTo(upX, upY);
							movingX.add(upX);
							movingY.add(upY);
						}
					}

					canvas.DrawScreen();
					break;
				case PaliCanvas.TOOL_PENCIL:
					PaliCanvas.history.Do(SVGParser.Layers);

					minX = min(minX, upX);
					minY = min(minY, upY);
					maxX = max(maxX, upX);
					maxY = max(maxY, upY);
					rect = new RectF(minX, minY, maxX, maxY);
					opacity = PaliCanvas.alpha / 255.0f;

					SVGParser.Layers.get(canvas.currentLayer).objs
							.add(new PaliPencil(pencilPath, movingX, movingY,
									rect));
					pencilPath = null;
					PaliCanvas.currentObject++;
					PaliCanvas.drawMode = false;
					canvas.DrawScreen();

					movingX.clear();
					movingY.clear();
					break;
				case PaliCanvas.TOOL_BRUSH:
					PaliCanvas.history.Do(SVGParser.Layers);

					html += "</g>";

					minX = min(minX, upX);
					minY = min(minY, upY);
					maxX = max(maxX, upX);
					maxY = max(maxY, upY);
					rect = new RectF(minX - brushR, minY - brushR, maxX
							+ brushR, maxY + brushR);

					bm = Bitmap.createBitmap((int) (rect.right),
							(int) (rect.bottom), Bitmap.Config.ARGB_8888);
					c = new Canvas(bm);
					p = new Paint();
					p.setColor(Color.WHITE);
					p.setStyle(Paint.Style.FILL);

					for (int i = 0; i < brushX.size(); i++) {
						p.setAlpha((int) (PaliCanvas.alpha * brushP.get(i)));
						c.drawCircle(brushX.get(i) - rect.left, brushY.get(i)
								- rect.top, brushR, p);
					}

					SVGParser.Layers.get(PaliCanvas.currentLayer).objs
							.add(new PaliBrush(bm, brushX, brushY, brushP, rect));

					bm = null;
					c = null;
					PaliCanvas.currentObject++;
					PaliCanvas.drawMode = false;
					canvas.DrawScreen();

					brushX.clear();
					brushY.clear();
					brushP.clear();

					break;

				case PaliCanvas.TOOL_CIRCLE:
					PaliCanvas.history.Do(SVGParser.Layers);

					r = (float) Math.sqrt((float) Math.pow(upX - downX, 2)
							+ (float) Math.pow(upY - downY, 2));
					cx = downX;
					cy = downY;

					SVGParser.Layers.get(PaliCanvas.currentLayer).objs
							.add(new PaliCircle(cx, cy, r));
					PaliCanvas.currentObject++;
					PaliCanvas.drawMode = false;
					canvas.DrawScreen();
					break;
				case PaliCanvas.TOOL_ELLIPSE:
					PaliCanvas.history.Do(SVGParser.Layers);

					left = Math.min(downX, upX);
					top = Math.min(downY, upY);
					right = Math.max(downX, upX);
					bottom = Math.max(downY, upY);

					SVGParser.Layers.get(canvas.currentLayer).objs
							.add(new PaliEllipse(left, top, right, bottom));
					PaliCanvas.currentObject++;
					PaliCanvas.drawMode = false;
					canvas.DrawScreen();
					break;
				case PaliCanvas.TOOL_RECTANGLE:
					PaliCanvas.history.Do(SVGParser.Layers);

					left = Math.min(downX, upX);
					top = Math.min(downY, upY);
					right = Math.max(downX, upX);
					bottom = Math.max(downY, upY);

					SVGParser.Layers.get(canvas.currentLayer).objs
							.add(new PaliRectangle(left, top, right, bottom));
					PaliCanvas.currentObject++;
					PaliCanvas.drawMode = false;
					canvas.DrawScreen();
					break;
				case PaliCanvas.TOOL_STAR:
					PaliCanvas.history.Do(SVGParser.Layers);

					r = (float) Math.sqrt((float) Math.pow(upX - downX, 2)
							+ (float) Math.pow(upY - downY, 2));
					cx = downX;
					cy = downY;

					SVGParser.Layers.get(PaliCanvas.currentLayer).objs
							.add(new PaliStar(cx, cy, r));
					PaliCanvas.currentObject++;
					PaliCanvas.drawMode = false;
					canvas.DrawScreen();
					break;
				}
				this.invalidate();
				return true;
			}
		}
		return false;
	}

	float min(float a, float b) {
		return (a < b) ? a : b;
	}

	float max(float a, float b) {
		return (a > b) ? a : b;
	}

	private float spacing(MotionEvent event) {
		float x = (event.getX(0) - event.getX(1));
		float y = (event.getY(0) - event.getY(1));
		return FloatMath.sqrt(x * x + y * y);
	}

	private boolean directing(MotionEvent e, PaliPoint p0, PaliPoint p1) {
		float b = (p1.fx - e.getX(1));
		float a = (p0.fx - e.getX(0));
		if (a * b > 0)
			return true;
		else
			return false;
	}

	public void deleteObject() {
		PaliCanvas.history.Do(SVGParser.Layers);

		for (int i = 0; i < selector.selObjArr.size(); i++) {
			SVGParser.Layers.get(selector.selObjArr.get(i).x).objs
					.remove(selector.selObjArr.get(i).y);
		}

		selector.selObjArr.clear();
		this.tempObj = null;
		this.selected = false;
		this.invalidate();
		canvas.DrawScreen();
		selector.mLongPressed = false;
		selector.setVisibility(View.GONE);
	}

	public void copyObject() {
		copyObject.clear();
		for (int i = 0; i < selector.selObjArr.size(); i++) {
			copyObject
					.add(SVGParser.Layers.get(selector.selObjArr.get(i).x).objs
							.get(selector.selObjArr.get(i).y));
		}
		copyPosX = rect.centerX();
		copyPosY = rect.centerY();
	}

	public void pasteObject() {
		PaliCanvas.history.Do(SVGParser.Layers);

		translateX = pastePosX - copyPosX;
		translateY = pastePosY - copyPosY;
		for (int i = 0; i < copyObject.size(); i++) {
			PaliObject obj = copyObject.get(i);
			switch (obj.type) {
			case PaliCanvas.TOOL_PEN:
				SVGParser.Layers.get(PaliCanvas.currentLayer).objs
						.add(new PaliPen(obj.path, obj.rect, obj.theta,
								obj.s_paint));
				break;
			case PaliCanvas.TOOL_PENCIL:
				SVGParser.Layers.get(PaliCanvas.currentLayer).objs
						.add(new PaliPencil(obj.path, obj.rect, obj.theta,
								obj.s_paint));
				break;
			case PaliCanvas.TOOL_BRUSH:
				SVGParser.Layers.get(PaliCanvas.currentLayer).objs
						.add(new PaliBrush(obj.bitmap, obj.rect, obj.theta,
								obj.f_paint, obj.filter));
				break;
			case PaliCanvas.TOOL_CIRCLE:
				SVGParser.Layers.get(PaliCanvas.currentLayer).objs
						.add(new PaliCircle(obj.x + translateX, obj.y
								+ translateY, obj.r, obj.theta, obj.s_paint,
								obj.f_paint));
				break;
			case PaliCanvas.TOOL_ELLIPSE:
				SVGParser.Layers.get(PaliCanvas.currentLayer).objs
						.add(new PaliEllipse(obj.left + translateX, obj.top
								+ translateY, obj.right + translateX,
								obj.bottom + translateY, obj.theta,
								obj.s_paint, obj.f_paint));
				break;
			case PaliCanvas.TOOL_RECTANGLE:
				SVGParser.Layers.get(PaliCanvas.currentLayer).objs
						.add(new PaliRectangle(obj.left + translateX, obj.top
								+ translateY, obj.right + translateX,
								obj.bottom + translateY, obj.theta,
								obj.s_paint, obj.f_paint));
				break;
			case PaliCanvas.TOOL_STAR:
				SVGParser.Layers.get(PaliCanvas.currentLayer).objs
						.add(new PaliStar(obj.x + translateX, obj.y
								+ translateY, obj.r, obj.theta, obj.s_paint,
								obj.f_paint));
			}
		}
		PaliCanvas.currentObject++;
		canvas.DrawScreen();
	}

	public void selectedClear() {
		this.selected = false;
		selector.selObjArr.clear();
		handler.post(hideViewThread);

	}

	Runnable hideViewThread = new Runnable() {

		@Override
		public void run() {
			selector.setVisibility(View.GONE);
		}
	};

	public void changeStyle(int style) {
		this.style = style;
		handler.post(changeStyleThread);
	}

	Runnable changeStyleThread = new Runnable() {

		@Override
		public void run() {
			if (selector.selObjArr.size() > 0) {
				PaliCanvas.history.Do(SVGParser.Layers);

				for (int i = 0; i < selector.selObjArr.size(); i++) {
					switch (style) {
					case MainActivity.STYLE_STROKECOLOR:
						SVGParser.Layers.get(selector.selObjArr.get(i).x).objs
								.get(selector.selObjArr.get(i).y).s_paint
								.setColor(PaliCanvas.strokeColor);
						break;
					case MainActivity.STYLE_FILLCOLOR:
						SVGParser.Layers.get(selector.selObjArr.get(i).x).objs
								.get(selector.selObjArr.get(i).y).f_paint
								.setColor(PaliCanvas.fillColor);
						SVGParser.Layers.get(selector.selObjArr.get(i).x).objs
								.get(selector.selObjArr.get(i).y).filter = new LightingColorFilter(
								PaliCanvas.fillColor, 1);
						break;
					case MainActivity.STYLE_STROKEWIDTH:
						SVGParser.Layers.get(selector.selObjArr.get(i).x).objs
								.get(selector.selObjArr.get(i).y).s_paint
								.setStrokeWidth(PaliCanvas.strokeWidth);
						break;
					}
				}
			}
			canvas.DrawScreen();
		}
	};
}