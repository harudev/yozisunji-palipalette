package com.yozisunji.palipalette;

import java.util.ArrayList;


public class PaliItemList {
	String FuncName;
	ArrayList<PaliItem> items;
	
	public PaliItemList()
	{
		items = new ArrayList<PaliItem>();
	}
	public PaliItemList(String f)
	{
		items = new ArrayList<PaliItem>();
		this.FuncName = f;
	}
	
	public void putItem(int func, int item, int itemType, int width, int height, int d)
	{
		items.add(new PaliItem(func, item, itemType, width, height,d,FuncName));
	}
	
	public void putItem(int func, int item, int itemType, int width, int height, int d, String f)
	{
		items.add(new PaliItem(func, item, itemType, width, height,d,f));
	}
	/*
	public PaliItem getItem(int func, int item)
	{
		
	}*/
}