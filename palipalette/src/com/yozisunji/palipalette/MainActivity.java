package com.yozisunji.palipalette;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.samsung.android.example.helloaccessoryprovider.R;
import com.samsung.android.example.helloaccessoryprovider.service.HelloAccessoryProviderService;

public class MainActivity extends Activity {
	
	static SVGParser svg;
	PaliCanvas customview;

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
        customview = (PaliCanvas) findViewById(R.id.paliCanvas1);
        customview.setBound(800,600);
        //customview.DrawScreen();
        //test
        
        HelloAccessoryProviderService hs;

	}
}
