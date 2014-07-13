package com.yozisunji.palipalette;


import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.drawable.Drawable;

public class PaliItemList implements Serializable {
	ArrayList<PaliItem> items;
	
	public PaliItemList()
	{
		items = new ArrayList<PaliItem>();
	}
	
	public void putItem(int item, int width, int height, int d)
	{
		items.add(new PaliItem(item, width, height,d));
	}
	/*
	public PaliItem getItem(int func, int item)
	{
		
	}*/
}