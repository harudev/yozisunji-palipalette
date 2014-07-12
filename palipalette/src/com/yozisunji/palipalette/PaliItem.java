package com.yozisunji.palipalette;

import java.io.Serializable;

import android.R;
import android.graphics.drawable.Drawable;

public class PaliItem implements Serializable
{
	int x, y;
	int width, height;
	int funcNum;
	int itemNum;	
	Drawable image;
	
	public PaliItem()
	{
		
	}
	
	public PaliItem(int itemNum, int width, int height, Drawable image)
	{
		this.itemNum = itemNum;
		this.width = width;
		this.height = height;
		this.image = image;
	}
	
	public PaliItem(int f, int i, int x, int y)
	{
		PaliItem temp = CustomizingMainActivity.GearUIList.get(f).items.get(i);
		this.funcNum = f;
		this.itemNum = i;
		this.x = x;
		this.y = y;
		this.width = temp.width;
		this.height = temp.height;
		this.image = temp.image;
	}
}