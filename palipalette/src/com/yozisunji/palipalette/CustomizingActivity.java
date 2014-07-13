package com.yozisunji.palipalette;

import com.samsung.android.example.helloaccessoryprovider.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class CustomizingActivity extends Activity {
	PaliScreen screen;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		setContentView(R.layout.customizing);
		
		this.screen =(PaliScreen) findViewById(R.id.screen_edit);
		
		this.screen.copy(CustomizingMainActivity.screens.get(0));
	}
}
