package com.yozisunji.palipalette;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
//import com.samsung.android.example.helloaccessoryprovider.service.HelloAccessoryProviderService;
//import com.samsung.android.example.helloaccessoryprovider.service.HelloAccessoryProviderService;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final int STYLE_STROKECOLOR = 0;
	public static final int STYLE_FILLCOLOR = 1;
	public static final int STYLE_STROKEWIDTH = 2;
	private Context ctx = this;

	static SVGParser svg;
	public PaliCanvas customview;
	PaliTouchCanvas touchview;

	public static HelloAccessoryProviderService hs;

	public static int screenHeight;
	public static int screenWidth;

	ImageView intro;
	private Handler mHandler = new Handler();

	private Dialog subDialog = null;
	private Dialog saveDialog = null;
	private Dialog exportDialog = null;
	private Dialog openDialog = null;
	EditText save_name;
	EditText export_name;
	ListView open_list;

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

		intro = (ImageView) findViewById(R.id.introImage);
		mHandler.postDelayed(new IntroRunnable(), 2400);

		svg = new SVGParser();
		svg.addLayer();
		// svg.parse(getResources().openRawResource(R.drawable.test));

		customview = (PaliCanvas) findViewById(R.id.paliCanvas);
		customview.setBound(800, 600);
		touchview = (PaliTouchCanvas) findViewById(R.id.paliTouch);
		touchview.setCanvasAddr(customview,
				(LinearLayout) findViewById(R.id.selectLayout));

		hs = new HelloAccessoryProviderService();
		/*
		 * Intent intent = getIntent();
		 * if(intent.getExtras().containsKey("json_screen")) { try { hs.send(new
		 * JSONObject(intent.getExtras().getString("json_screen"))); } catch
		 * (JSONException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } }
		 */
		createSubDialog();
		createSaveDialog();
		createExportDialog();
		createOpenDialog();
	}

	@Override
	public void onResume() {
		super.onResume();
		/*
		 * Intent intent = getIntent();
		 * if(intent.getExtras().containsKey("json_screen")) { try { hs.send(new
		 * JSONObject(intent.getExtras().getString("json_screen"))); } catch
		 * (JSONException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } }
		 */
		HelloAccessoryProviderService.setActivity(this);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				android.os.Process.killProcess(android.os.Process.myPid());
				return true;
			case KeyEvent.KEYCODE_MENU:				
				PaliCanvas.selectedTool ++; if(PaliCanvas.selectedTool >
				PaliCanvas.TOOL_COMMON) { PaliCanvas.selectedTool = 0; }
				return true;				
			}
		}
		return super.dispatchKeyEvent(event);
	}

	private void createSubDialog() {
		final View innerView = getLayoutInflater().inflate(R.layout.sub_menu,
				null);

		subDialog = new Dialog(this);
		subDialog.setContentView(innerView);

		subDialog.setCancelable(true);
		subDialog.setCanceledOnTouchOutside(true);

		ImageButton copyBtn = (ImageButton) subDialog
				.findViewById(R.id.copyBtn);
		ImageButton pasteBtn = (ImageButton) subDialog
				.findViewById(R.id.pasteBtn);
		ImageButton deletBtn = (ImageButton) subDialog
				.findViewById(R.id.deletBtn);
	}

	private void createSaveDialog() {
		final View innerView = getLayoutInflater().inflate(R.layout.save_menu,
				null);

		saveDialog = new Dialog(this);
		saveDialog.setContentView(innerView);

		saveDialog.setTitle("Save SVG");
		saveDialog.setCancelable(true);
		saveDialog.setCanceledOnTouchOutside(true);

		Button save_okBtn = (Button) saveDialog.findViewById(R.id.save_okBtn);
		Button save_cancleBtn = (Button) saveDialog
				.findViewById(R.id.save_cancleBtn);
		save_name = (EditText) saveDialog.findViewById(R.id.save_editText);
	}

	private void createExportDialog() {
		final View innerView = getLayoutInflater().inflate(
				R.layout.export_menu, null);

		exportDialog = new Dialog(this);
		exportDialog.setContentView(innerView);

		exportDialog.setTitle("Save PNG");
		exportDialog.setCancelable(true);
		exportDialog.setCanceledOnTouchOutside(true);

		Button export_okBtn = (Button) exportDialog
				.findViewById(R.id.export_okBtn);
		Button export_cancleBtn = (Button) exportDialog
				.findViewById(R.id.export_cancleBtn);
		export_name = (EditText) exportDialog
				.findViewById(R.id.export_editText);
	}

	private void createOpenDialog() {
		final View innerView = getLayoutInflater().inflate(R.layout.open_menu,
				null);

		openDialog = new Dialog(this);
		openDialog.setContentView(innerView);

		openDialog.setTitle("Open SVG");
		openDialog.setCancelable(true);
		openDialog.setCanceledOnTouchOutside(true);

		open_list = (ListView) openDialog.findViewById(R.id.open_list);
	}

	public void popUpSubMenu() {
		subDialog.show();
	}

	public void popUpSaveMenu() {
		saveDialog.show();
	}

	public void popUpExportMenu() {
		exportDialog.show();
	}

	public void popUpOpenMenu() {
		ArrayList<String> openFileName = new ArrayList<String>();
		String path = "/mnt/sdcard/PaliPalette/";
		File files = new File(path);
		final ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, openFileName);
		if (files.listFiles().length > 0) {
			for (File file : files.listFiles()) {
				if (file.getName().toLowerCase().endsWith(".svg")) {
					openFileName.add(file.getName());
				}
			}
		}
		files = null;
		open_list.setAdapter(fileList);
		openDialog.show();

		open_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String s = fileList.getItem(arg2);
				openSVG(s);
				openDialog.cancel();
			}
		});
	}

	public void OnClick(View v) {
		String s;

		switch (v.getId()) {
		case R.id.copyBtn:
			touchview.copyObject();
			subDialog.cancel();
			break;
		case R.id.pasteBtn:
			touchview.pasteObject();
			subDialog.cancel();
			break;
		case R.id.deletBtn:
			subDialog.cancel();
			touchview.deleteObject();
			break;
		case R.id.save_okBtn:
			s = save_name.getText().toString();
			saveSVG(s);
			saveDialog.cancel();
			break;
		case R.id.save_cancleBtn:
			saveDialog.cancel();
			break;
		case R.id.export_okBtn:
			s = export_name.getText().toString();
			exportPNG(s);
			exportDialog.cancel();
			break;
		case R.id.export_cancleBtn:
			exportDialog.cancel();
			break;
		}
	}

	public void launchCustomizing(JSONObject json) {
		Intent intent = new Intent(this, CustomizingMainActivity.class);
		intent.putExtra("json_screens", json.toString());
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

	public void saveSVG(final String name)
	{
		final ProgressDialog saveDialog = ProgressDialog.show(MainActivity.this, "Save", "saving...", true);

		final Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				saveDialog.dismiss();
				
				if(msg.what == 0) {
					Toast.makeText(ctx, "save success", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(ctx, "save fail", Toast.LENGTH_SHORT).show();
				}
				
			}
		};
		
		new Thread() {
			public void run() {
				super.run();

				String SVGTag = "<svg width=\"" + screenWidth + "\" height=\""
						+ screenHeight + "\">";
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
				String fileName = name + ".svg";

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
					mHandler.sendEmptyMessage(1);
				}

				mHandler.sendEmptyMessage(0);
			}
		}.start();
	}

	public void openSVG(final String name) {
		final ProgressDialog openDialog = ProgressDialog.show(MainActivity.this, "Open", "opening...", true);

		final Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				openDialog.dismiss();
				
				if(msg.what == 0) {
					Toast.makeText(ctx, "open success", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(ctx, "open fail", Toast.LENGTH_SHORT).show();
				}
				
			}
		};
		
		new Thread() {
			public void run() {
				super.run();
				newActivity();

				String path = "/mnt/sdcard/PaliPalette/";
				String fileName = name;

				File file = new File(path + fileName);

				try {
					FileInputStream fis = new FileInputStream(file);
					svg.parse(fis);
					fis.close();

				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(1);
				}

				customview.DrawScreen();
				mHandler.sendEmptyMessage(0);
			}
		}.start();
		
	}

	public void exportPNG(final String name) {
		final ProgressDialog exportDialog = ProgressDialog.show(MainActivity.this, "Export", "exporting...", true);
		
		final Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				exportDialog.dismiss();
				
				if(msg.what == 0) {
					Toast.makeText(ctx, "export success", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(ctx, "export fail", Toast.LENGTH_SHORT).show();
				}
				
			}
		};
		
		new Thread() {
			public void run() {
				super.run();
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
				String fileName = name + ".png";

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
					mHandler.sendEmptyMessage(1);

				} catch (IOException e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(1);
				}
				mHandler.sendEmptyMessage(0);
			}
		}.start();
		
	}

	private class IntroRunnable implements Runnable {
		@Override
		public void run() {
			intro.setVisibility(View.GONE);
		}
	}
}
