package com.yozisunji.palipalette;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
	public boolean connectFlag = false;

	static SVGParser svg;
	public PaliCanvas customview;
	PaliTouchCanvas touchview;
	

	public PaliConnector hs;

	public static int screenHeight;
	public static int screenWidth;

	ImageView intro;
	ImageView noConnImg;
	private Handler mHandler = new Handler();

	private Dialog subDialog = null;
	private Dialog saveDialog = null;
	private Dialog exportDialog = null;
	private Dialog openDialog = null;
	private Dialog helpDialog = null;
	private Dialog helpImgDialog = null;
	EditText save_name;
	EditText export_name;
	ListView open_list;
	ListView help_list;
	ImageView help_img;
	int imgCnt;

	ImageButton copyBtn;
	ImageButton pasteBtn;
	ImageButton deletBtn;	

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
		customview.setBound(screenWidth, screenHeight);
		touchview = (PaliTouchCanvas) findViewById(R.id.paliTouch);
		touchview.setCanvasAddr(customview,
				(LinearLayout) findViewById(R.id.selectLayout));

		hs = new PaliConnector();

		createSubDialog();
		createSaveDialog();
		createExportDialog();
		createOpenDialog();
		createHelpDialog();
		createHelpImgDialog();

		// test
		connectSuccess();
		//
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		if (null != intent) {
			setIntent(intent);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		hs.setActivity(this);

		if (this.getIntent().getExtras() == null)
			return;

		Intent intent = getIntent();
		if (intent.getExtras().containsKey("json_screen")) {
			try {
				// PaliConnector.getInstance().send(new
				// JSONObject(intent.getExtras().getString("json_screen")));
				hs.send(new JSONObject(intent.getExtras().getString(
						"json_screen")));
			} catch (JSONException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				android.os.Process.killProcess(android.os.Process.myPid());
				return true;
			case KeyEvent.KEYCODE_MENU:
				//popUpHelpMenu();
				
				PaliCanvas.selectedTool++;
				if(PaliCanvas.selectedTool >= PaliCanvas.TOOL_COMMON) {
					PaliCanvas.selectedTool = 0;
				}
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
		subDialog.getWindow()
				.setBackgroundDrawable(new ColorDrawable(0x000000));

		copyBtn = (ImageButton) subDialog.findViewById(R.id.copyBtn);
		pasteBtn = (ImageButton) subDialog.findViewById(R.id.pasteBtn);
		deletBtn = (ImageButton) subDialog.findViewById(R.id.deletBtn);
	}

	private void createSaveDialog() {
		final View innerView = getLayoutInflater().inflate(R.layout.save_menu,
				null);

		saveDialog = new Dialog(this);
		saveDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		saveDialog.setContentView(innerView);

		saveDialog.setCancelable(true);
		saveDialog.setCanceledOnTouchOutside(true);

		Button save_okBtn = (Button) saveDialog.findViewById(R.id.save_okBtn);
		Button save_cancleBtn = (Button) saveDialog
				.findViewById(R.id.save_cancelBtn);
		save_name = (EditText) saveDialog.findViewById(R.id.save_editText);
	}

	private void createExportDialog() {
		final View innerView = getLayoutInflater().inflate(
				R.layout.export_menu, null);

		exportDialog = new Dialog(this);
		exportDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		exportDialog.setContentView(innerView);

		exportDialog.setCancelable(true);
		exportDialog.setCanceledOnTouchOutside(true);

		Button export_okBtn = (Button) exportDialog
				.findViewById(R.id.export_okBtn);
		Button export_cancleBtn = (Button) exportDialog
				.findViewById(R.id.export_cancelBtn);
		export_name = (EditText) exportDialog
				.findViewById(R.id.export_editText);
	}

	private void createOpenDialog() {
		final View innerView = getLayoutInflater().inflate(R.layout.open_menu,
				null);

		openDialog = new Dialog(this);
		openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		openDialog.setContentView(innerView);

		openDialog.setCancelable(true);
		openDialog.setCanceledOnTouchOutside(true);

		open_list = (ListView) openDialog.findViewById(R.id.open_list);
	}

	private void createHelpDialog() {
		final View innerView = getLayoutInflater().inflate(R.layout.help_menu,
				null);

		helpDialog = new Dialog(this);
		helpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		helpDialog.setContentView(innerView);

		helpDialog.setCancelable(true);
		helpDialog.setCanceledOnTouchOutside(true);

		help_list = (ListView) helpDialog.findViewById(R.id.help_list);
	}

	private void createHelpImgDialog() {
		final View innerView = getLayoutInflater().inflate(R.layout.help_image,
				null);

		helpImgDialog = new Dialog(this);
		helpImgDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		helpImgDialog.setContentView(innerView);

		helpImgDialog.setCancelable(true);
		helpImgDialog.setCanceledOnTouchOutside(true);

		help_img = (ImageView) helpImgDialog.findViewById(R.id.help_image);
	}

	public void popUpSubMenu() {
		copyBtn.setVisibility(View.VISIBLE);
		pasteBtn.setVisibility(View.VISIBLE);
		deletBtn.setVisibility(View.VISIBLE);

		if (touchview.copyObject.size() == 0) {
			pasteBtn.setVisibility(View.GONE);
		}
		if (touchview.selector.selObjArr.size() == 0) {
			copyBtn.setVisibility(View.GONE);
			deletBtn.setVisibility(View.GONE);
		}

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

	public void popUpHelpMenu() {
		ArrayList<String> helpName = new ArrayList<String>();

		final ArrayAdapter<String> helpList = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, helpName);

		helpName.add("Drawing");
		helpName.add("Gear Icon");
		helpName.add("Custommizing");
		helpName.add("Screen Control");

		help_list.setAdapter(helpList);
		helpDialog.show();

		help_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int listNum, long arg3) {
				popUpHelpImg(listNum);
				helpDialog.cancel();
			}
		});
	}

	public void popUpHelpImg(final int listNum) {
		helpImgDialog.show();

		if (listNum == 0) {
			help_img.setImageResource(R.drawable.help_drawing_1);
		} else if (listNum == 1) {
			help_img.setImageResource(R.drawable.help_gear_icon_1);
		} else if (listNum == 2) {
			help_img.setImageResource(R.drawable.help_custom_1);
			imgCnt = 3;
		} else if (listNum == 3) {
			help_img.setImageResource(R.drawable.help_screen_control_1);
			imgCnt = 2;
		}

		help_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listNum == 0 || listNum == 1) {
					helpImgDialog.dismiss();
					helpDialog.show();
				} else if (listNum == 2) {
					imgCnt--;
					if (imgCnt == 2)
						help_img.setImageResource(R.drawable.help_custom_2);
					else if (imgCnt == 1)
						help_img.setImageResource(R.drawable.help_custom_3);
					else {
						helpImgDialog.dismiss();
						helpDialog.show();
					}
				} else if (listNum == 3) {
					imgCnt--;
					if(imgCnt== 1)
						help_img.setImageResource(R.drawable.help_screen_control_2);
					else {
						helpImgDialog.dismiss();
						helpDialog.show();
					}
				}
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
		case R.id.save_cancelBtn:
			saveDialog.cancel();
			break;
		case R.id.export_okBtn:
			s = export_name.getText().toString();
			exportPNG(s);
			exportDialog.cancel();
			break;
		case R.id.export_cancelBtn:
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

	public void saveSVG(final String name) {
		final ProgressDialog saveDialog = ProgressDialog.show(
				MainActivity.this, "Save", "saving...", true);

		final Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				saveDialog.dismiss();

				if (msg.what == 0) {
					Toast.makeText(ctx, "save success", Toast.LENGTH_SHORT)
							.show();
				} else {
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
		final ProgressDialog openDialog = ProgressDialog.show(
				MainActivity.this, "Open", "opening...", true);

		final Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				openDialog.dismiss();

				if (msg.what == 0) {
					Toast.makeText(ctx, "open success", Toast.LENGTH_SHORT)
							.show();
				} else {
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
		final ProgressDialog exportDialog = ProgressDialog.show(
				MainActivity.this, "Export", "exporting...", true);

		final Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				exportDialog.dismiss();

				if (msg.what == 0) {
					Toast.makeText(ctx, "export success", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(ctx, "export fail", Toast.LENGTH_SHORT)
							.show();
				}

			}
		};

		new Thread() {
			public void run() {
				super.run();
				Bitmap bm = Bitmap.createBitmap(1080, 1920,
						Bitmap.Config.ARGB_8888);
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

	public void connectSuccess() {
		connectFlag = true;
		mHandler.postDelayed(new IntroRunnable(), 100);
	}

	private class IntroRunnable implements Runnable {
		@Override
		public void run() {
			if (!connectFlag) {
				intro.setImageResource(R.drawable.no_conn);
			} else {
				intro.setVisibility(View.GONE);
				
				try {
					initGear();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	
	public void initGear() throws JSONException {
		
		JSONObject json = new JSONObject();
		json.put("layerCnt", SVGParser.Layers.size());
		json.put("currentLayer", PaliCanvas.currentLayer);
		JSONArray jsonArr = new JSONArray();		
		for(int i=0; i<SVGParser.Layers.size(); i ++) {
			if(SVGParser.Layers.get(i).visibility) {
				jsonArr.put(i);
			}
		}
		json.put("checkedLayer", jsonArr);
		hs.send(json);
		
		Log.i("debug", ""+json);
	}
}
