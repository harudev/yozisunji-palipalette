package com.yozisunji.palipalette;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.samsung.android.example.helloaccessoryprovider.R;
//import com.samsung.android.example.helloaccessoryprovider.service.HelloAccessoryProviderService;
import com.samsung.android.example.helloaccessoryprovider.service.HelloAccessoryProviderService;

public class MainActivity extends Activity {

	public static final int STYLE_STROKECOLOR = 0;
	public static final int STYLE_FILLCOLOR = 1;
	public static final int STYLE_STROKEWIDTH = 2;

	static SVGParser svg;
	public PaliCanvas customview;
	PaliTouchCanvas touchview;
	private Context mContext;
	static SubMenuDialog dialog;
	public static HelloAccessoryProviderService hs;

	public static int screenHeight;
	public static int screenWidth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		setContentView(R.layout.main);
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		screenHeight = displaymetrics.heightPixels;
		screenWidth = displaymetrics.widthPixels;

		svg = new SVGParser();
		svg.addLayer();
		//svg.parse(getResources().openRawResource(R.drawable.test));
		customview = (PaliCanvas) findViewById(R.id.paliCanvas);
		customview.setBound(800, 600);
		touchview = (PaliTouchCanvas) findViewById(R.id.paliTouch);
		touchview.setCanvasAddr(customview,
				(LinearLayout) findViewById(R.id.selectLayout));

		hs = new HelloAccessoryProviderService(this);

		dialog = new SubMenuDialog();
		Button copyBtn = (Button) findViewById(R.id.copyBtn);
		Button pasteBtn = (Button) findViewById(R.id.pasteBtn);
		Button deletBtn = (Button) findViewById(R.id.deletBtn);

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				android.os.Process.killProcess(android.os.Process.myPid());
				return true;
			case KeyEvent.KEYCODE_MENU:
				// launchCustomizing();
				
				PaliCanvas.selectedTool++;
				if (PaliCanvas.selectedTool > 5)
					PaliCanvas.selectedTool = 0;

				changeTool();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	public static class SubMenuDialog extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder mBuilder = new AlertDialog.Builder(
					getActivity());
			LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
			mBuilder.setView(mLayoutInflater.inflate(R.layout.sub_menu, null));
			return mBuilder.create();
		}

		@Override
		public void onStop() {
			super.onStop();
		}
	}

	public void popUpSubMenu() {
		dialog.show(getFragmentManager(), "SubMenu");
	}

	public void OnClick(View v) {
		switch (v.getId()) {
		case R.id.copyBtn:
			touchview.copyObject();
			dialog.dismiss();
			break;
		case R.id.pasteBtn:
			touchview.pasteObject();
			dialog.dismiss();
			break;
		case R.id.deletBtn:
			dialog.dismiss();
			touchview.deleteObject();
			break;
		}
	}

	public void launchCustomizing() {
		Intent intent = new Intent(this, CustomizingMainActivity.class);
		startActivity(intent);
	}

	public void changeStyle(int style) {
		touchview.changeStyle(style);
	}

	public void changeTool() {
		touchview.selectedClear();
	}

	public void newActivity() {

		for (int i = 0; i < SVGParser.Layers.size(); i++) {
			SVGParser.Layers.get(i).objs.clear();
		}
		SVGParser.Layers.clear();
		svg.addLayer();
		SVGParser.Layersize = 1;
		PaliCanvas.currentLayer = SVGParser.Layersize - 1;
		PaliCanvas.currentObject = -1;

		customview.DrawScreen();
	}

	public void saveSVG() {
		String SVGTag = "<svg width=\""+screenWidth+"\" height=\""+screenHeight+"\">";
		PaliObject temp;
		for (int i = 0; i < SVGParser.Layers.size(); i++) {
			if (SVGParser.Layers.get(i).visibility == true) {
				for (int j = 0; j < SVGParser.Layers.get(i).objs.size(); j++) {
					temp = SVGParser.Layers.get(i).objs.get(j);
					SVGTag += temp.svgtag;
				}
			}
		}
		SVGTag += "</svg>";

		String path = "/mnt/sdcard/PaliPalette/";
		String fileName = "PaliSVG.svg";

		File file_path = new File(path);
		File file = new File(path + fileName);

		if (!file_path.exists()) {
			file_path.mkdirs();
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write((SVGTag).getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void openSVG() {
		newActivity();
		
		String path = "/mnt/sdcard/PaliPalette/";
		String fileName = "PaliSVG.svg";
		
		File file = new File(path+fileName);

		try {
			FileInputStream fis = new FileInputStream(file);  						
			svg.parse(fis);	
			fis.close();
            		
		} catch (Exception e) {
            e.printStackTrace();
        }		
		
		
		//svg.parse(getResources().openRawResource(R.drawable.test));
		customview.DrawScreen();
	}
	
	public void exportPNG() {
		Bitmap bm = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bm);
		
		PaliObject temp;
		for (int i = 0; i < SVGParser.Layers.size(); i++) {
			if (SVGParser.Layers.get(i).visibility == true) {
				for (int j = 0; j < SVGParser.Layers.get(i).objs.size(); j++) {
					temp = SVGParser.Layers.get(i).objs.get(j);
					temp.drawObject(c);
				}
			}
		}
		
		String path = "/mnt/sdcard/PaliPalette/";
		String fileName = "PaliPNG.png";			
		
		OutputStream outStream = null; 
		File file = new File(path, fileName); 
		File file_path = new File(path);

		if (!file_path.exists()) {
			file_path.mkdirs();
		}
		
		try {
			outStream = new FileOutputStream(file); 
			bm.compress(Bitmap.CompressFormat.PNG, 100, outStream); 
			outStream.flush(); 
			outStream.close(); 

		} catch (FileNotFoundException e) { 
			e.printStackTrace(); 

		} catch (IOException e) { 
			e.printStackTrace(); 
		} 			
	}
}
