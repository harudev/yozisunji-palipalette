package com.yozisunji.palipalette;

import java.util.ArrayList;

import android.content.Context;
import android.widget.GridLayout;

public class PaliScreen extends GridLayout {
	PaliItemList itemList;
	
	public PaliScreen(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		itemList = new PaliItemList();
	}
	
	public PaliScreen(Context context, PaliScreen p) {
		super(context);
		// TODO Auto-generated constructor stub
		
		itemList = p.itemList;
	}
	
	public void putItem(int func, int item, int posX, int posY)
	{
		this.itemList.items.add(new PaliItem());
	}

}
