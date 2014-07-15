package com.yozisunji.palipalette;

import java.io.Serializable;
import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.GridLayout;
import android.widget.ImageView;

public class PaliItem 
{
	public static final int TYPE_ICON = 0;
	public static final int TYPE_WIDGET = 1;
	
	int width, height;
	int funcNum;
	int itemNum;
	int imageid;
	int itemType;
	
	String funcName;
	String itemTypeName;
		
	public PaliItem()
	{
	}
	
	public PaliItem(int funcNum, int itemNum, int itemType, int width, int height, int imageid, String fName)
	{
		this.funcNum = funcNum;
		this.itemNum = itemNum;
		this.itemType = itemType;
		this.width = width;
		this.height = height;
		this.imageid = imageid;
		this.funcName = fName;
		
		switch(itemType)
		{
		case TYPE_ICON:
			itemTypeName="Icon";
			break;
		case TYPE_WIDGET:
			itemTypeName="Widget";
		}
	}
}