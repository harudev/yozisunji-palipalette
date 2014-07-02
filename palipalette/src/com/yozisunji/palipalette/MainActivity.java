package com.yozisunji.palipalette;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.samsung.android.example.helloaccessoryprovider.R;
//import com.samsung.android.example.helloaccessoryprovider.service.HelloAccessoryProviderService;

public class MainActivity extends Activity {
	
	static SVGParser svg;
	PaliCanvas customview;
	PaliTouchCanvas touchview;
	private Context mContext;
	
	static SubMenuDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.main);	
		
		svg = new SVGParser();        
        svg.parse(getResources().openRawResource(R.drawable.test));
        customview = (PaliCanvas) findViewById(R.id.paliCanvas);
        customview.setBound(800,600);
        touchview = (PaliTouchCanvas) findViewById(R.id.paliTouch);
        touchview.setCanvasAddr(customview);
        
        //HelloAccessoryProviderService hs;  
        
        dialog = new SubMenuDialog();         
        Button copyBtn = (Button) findViewById(R.id.copyBtn);
        Button pasteBtn = (Button) findViewById(R.id.pasteBtn);
        Button deletBtn = (Button) findViewById(R.id.deletBtn);
        
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_DOWN) {
			switch(event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				android.os.Process.killProcess(android.os.Process.myPid()); 
				return true;
			case KeyEvent.KEYCODE_MENU:
				PaliCanvas.selectedTool++;
				if(PaliCanvas.selectedTool > 3)	PaliCanvas.selectedTool = 0;
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
    }
	
	public static class SubMenuDialog extends DialogFragment {
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
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
			dialog.dismiss();
			break;
		case R.id.pasteBtn:
			dialog.dismiss();
			break;
		case R.id.deletBtn:			
			dialog.dismiss();
			break;		
		}		
	}
}
