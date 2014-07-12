package com.yozisunji.palipalette;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.samsung.android.example.helloaccessoryprovider.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class CustomizingMainActivity extends Activity {
	public static JSONObject json;
	private JSONArray jarr;
	public static ArrayList<PaliItemList> GearUIList;
	public static ArrayList<PaliScreen> screens;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		setContentView(R.layout.customizingmain);
		screens = new ArrayList<PaliScreen>();
		InitializeGearUIList();
		ParseJSON();
	}
	
	private void ParseJSON()
	{
		try {
			JSONArray obj;
			JSONObject item;
			jarr = json.getJSONArray("customize");
			
			for( int i = 0 ; i<jarr.length() ; i++ )
			{
				screens.add(new PaliScreen(this));
				obj = jarr.getJSONArray(i);
				
				for( int j = 0; j < obj.length(); j++)
				{
					item = obj.getJSONObject(j);
					screens.get(i).putItem(item.getInt("Function"), item.getInt("Num"), item.getInt("PosX"), item.getInt("PosY"));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void InitializeGearUIList()
	{
		GearUIList = new ArrayList<PaliItemList>();

		GearUIList.add(new PaliItemList());
		GearUIList.add(new PaliItemList());
		GearUIList.add(new PaliItemList());
		GearUIList.add(new PaliItemList());
		GearUIList.add(new PaliItemList());
		GearUIList.add(new PaliItemList());
		
		GearUIList.get(0).putItem(0, 2, 2, getResources().getDrawable(R.drawable.icon_copy));
		GearUIList.get(1).putItem(1, 2, 2, getResources().getDrawable(R.drawable.icon_copy));
		GearUIList.get(2).putItem(2, 2, 2, getResources().getDrawable(R.drawable.icon_copy));
		GearUIList.get(3).putItem(3, 2, 2, getResources().getDrawable(R.drawable.icon_copy));
		GearUIList.get(4).putItem(4, 2, 2, getResources().getDrawable(R.drawable.icon_copy));
		GearUIList.get(5).putItem(5, 2, 2, getResources().getDrawable(R.drawable.icon_copy));
	}
}
