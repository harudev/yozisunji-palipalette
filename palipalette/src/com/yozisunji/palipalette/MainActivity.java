package com.yozisunji.palipalette;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.samsung.android.example.helloaccessoryprovider.R;
//import com.samsung.android.example.helloaccessoryprovider.service.HelloAccessoryProviderService;

public class MainActivity extends Activity {
	
	static SVGParser svg;
	PaliCanvas customview;
	PaliTouchCanvas touchview;

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
}
